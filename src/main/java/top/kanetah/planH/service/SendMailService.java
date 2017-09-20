package top.kanetah.planH.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
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

    private static TreeMultiValueMap<Date, Long> dateMap = new TreeMultiValueMap<>();
    private final RepositoryService repositoryService;
    private final JavaMailSenderImpl mailSender;
    @Value(value = "${kanetah.planH.subject.info}")
    private Resource subjectInfoResource;
    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;
    private Map<String, Subject> subjectMap = new HashMap<>();

    @Autowired
    public SendMailService(
            RepositoryService repositoryService,
            JavaMailSenderImpl mailSender
    ) {
        this.repositoryService = repositoryService;
        this.mailSender = mailSender;
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
        Class<? extends Subject> clazz = Subject.class;
        for (Map map : new ObjectMapper().readValue(
                FileTool.inputStreamToFile(
                        subjectInfoResource.getInputStream()
                ),
                Map[].class
        )) {
            Subject subject = new Subject();
            map.forEach((k, v) -> {
                try {
                    Field field = clazz.getDeclaredField(k.toString());
                    field.setAccessible(true);
                    field.set(subject, v);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            subjectMap.put(subject.getSubject(), subject);
        }

        new Thread(() -> {
            Exception exception;
            while (true) {
                try {
                    if (dateMap.size() > 0) {
                        Date nextDate = dateMap.firstKey();
                        long sleepTime = nextDate.getTime() - new Date().getTime();
                        if (sleepTime < 0)
                            sleepTime = 0;
                        Thread.sleep(sleepTime + 60 * 60 * 1000);
                        dateMap.getValues(nextDate).forEach(
                                SendMailService.this::sendMail
                        );
                        dateMap.remove(nextDate);
                    } else
                        Thread.sleep(3 * 60 * 60 * 1000);
                } catch (Exception e) {
                    exception = e;
                    break;
                }
            }
            throw new RuntimeException(exception);
        }).start();
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
            helper.setText("请见附件");
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

    public Object[] getSubjectNames() {
        return subjectMap.keySet().toArray();
    }
}
