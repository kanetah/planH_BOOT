package top.kanetah.planH.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.entity.relationship.Submit;
import top.kanetah.planH.pojo.Subject;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.tools.CompactAlgorithm;
import top.kanetah.planH.tools.FileTool;
import top.kanetah.planH.tools.TreeMultiValueMap;


import javax.mail.internet.MimeMessage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class SendMailService implements InitializingBean {

    private static final int A_HOUR = 60 * 60 * 1000;
    private static TreeMultiValueMap<Date, Long> dateMap = new TreeMultiValueMap<>();
    private final RepositoryService repositoryService;
    private final JavaMailSenderImpl mailSender;
    private final SpringTemplateEngine thymeleaf;
    @Value(value = "${kanetah.planH.subject.info}")
    private Resource subjectInfoResource;
    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;
    private Map<String, Subject> subjectMap = new HashMap<>();
    private List<String> subjectNames = new ArrayList<>();

    @Autowired
    public SendMailService(
            RepositoryService repositoryService,
            JavaMailSenderImpl mailSender,
            SpringTemplateEngine thymeleaf
    ) {
        this.repositoryService = repositoryService;
        this.mailSender = mailSender;
        this.thymeleaf = thymeleaf;
    }

    public static void setTimer(Task task) {
        if (new Date().before(task.getDeadlineOnJVM()))
            dateMap.add(task.getDeadlineOnJVM(), task.getId());
    }

    public static void setTimer(Task task, Long removeId) {
        dateMap.removeValue(removeId);
        setTimer(task);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        for (Map map : new ObjectMapper().readValue(
                FileTool.inputStreamToFile(
                        subjectInfoResource.getInputStream()
                ),
                Map[].class
        )) {
            Subject subject = new Subject();
            map.forEach((k, v) -> {
                try {
                    Field field = Subject.class.getDeclaredField(k.toString());
                    field.setAccessible(true);
                    field.set(subject, v);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            subjectMap.put(subject.getSubject(), subject);
            subjectNames.add(subject.getSubject());
        }

        new Thread(() -> {
            Exception exception;
            while (true) {
                try {
                    if (!dateMap.isEmpty()) {
                        Date nextDate = dateMap.firstKey();
                        long sleepTime = nextDate.getTime() - new Date().getTime();
                        Thread.sleep((sleepTime < 0 ? 0 : sleepTime) + A_HOUR);
                        dateMap.getValues(nextDate).forEach(
                                SendMailService.this::sendMail
                        );
                        dateMap.remove(nextDate);
                    } else
                        Thread.sleep(3 * A_HOUR);
                } catch (Exception e) {
                    exception = e;
                    break;
                }
            }
            throw new RuntimeException(exception);
        }).start();

//        System.out.println(emailText(
//                repositoryService.taskRepository.findById(36L).get()
//        ));
    }

    private void sendMail(Long taskId) {
        try {
            Optional<Task> optional = repositoryService.taskRepository.findById(taskId);
            assert optional.isPresent();
            Task task = optional.get();
            Subject subject = subjectMap.get(task.getSubject());
            File submitZip = createTaskZipFile(task);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(subject.getTarget());
            helper.setSubject("作业提交：" + task.getTitle() + "_15移春2班");
            helper.addAttachment(submitZip.getName(), submitZip);
            helper.setText(emailText(task), true);
            mailSender.send(message);
            if (!submitZip.delete())
                throw new Exception("Can not delete zip file: '" + submitZip.getName() + "'.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File createTaskZipFile(Task task) {
        String srcPath = storePath + "/" + task.getSubject() + "/" + task.getTitle();
        return new CompactAlgorithm(
                srcPath + ".zip"
        ).zipFiles(srcPath);
    }

    private String emailText(Task task) {
        Context context = new Context();
        context.setVariable("teacherName", subjectMap.get(task.getSubject()).getTeacher());
        context.setVariable("taskTitle", task.getTitle());
        List<Submit> submits = new LinkedList<>();
        repositoryService.submitRepository.findAllByTask_Id(task.getId()).forEach(submits::add);
        List<User> Submitted = new LinkedList<>();
        submits.forEach(submit -> Submitted.add(submit.getUser()));
        int flagSize = 20;
        context.setVariable("flagSize", flagSize);
        if (submits.size() >= flagSize) {
            List<User> notSubmitted = new LinkedList<>();
            repositoryService.userRepository.findAll().forEach(notSubmitted::add);
            notSubmitted.removeAll(Submitted);
            context.setVariable("users", notSubmitted);
        } else
            context.setVariable("users", Submitted);
        context.setVariable("submitSize", submits.size());
        context.setVariable("userCount", repositoryService.userRepository.count());
        return thymeleaf.process("email.html", context);
    }

    public Object[] getSubjectNames() {
        return subjectNames.toArray();
    }
}
