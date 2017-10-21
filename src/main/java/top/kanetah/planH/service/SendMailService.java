package top.kanetah.planH.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import top.kanetah.planH.entity.node.Role;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.pojo.Subject;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.tools.CompactTool;
import top.kanetah.planH.tools.FormatSaveProcessorTool;
import top.kanetah.planH.tools.RegexTool;
import top.kanetah.planH.pojo.TreeMultiValueMap;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class SendMailService implements InitializingBean {

    private static final int A_HOUR = 60 * 60 * 1000;
    private static TreeMultiValueMap<Date, Long> dateMap = new TreeMultiValueMap<>();
    private final RepositoryService repositoryService;
    private final AdminService adminService;
    private final JavaMailSenderImpl mailSender;
    private final FormatSaveProcessorTool formatSaveProcessorTool;
    private final SpringTemplateEngine thymeleaf;
    @Value(value = "${kanetah.planH.subject.infoPath}")
    private String subjectInfoPath;
    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;
    @Value(value = "${spring.mail.username}")
    private String mailUsername;
    private Map<String, Subject> subjectMap = new HashMap<>();

    @Autowired
    public SendMailService(
            RepositoryService repositoryService,
            AdminService adminService,
            JavaMailSenderImpl mailSender,
            FormatSaveProcessorTool formatSaveProcessorTool,
            SpringTemplateEngine thymeleaf
    ) {
        this.repositoryService = repositoryService;
        this.adminService = adminService;
        this.mailSender = mailSender;
        this.formatSaveProcessorTool = formatSaveProcessorTool;
        this.thymeleaf = thymeleaf;
    }

    private static void setTimer(Task task) {
        if (new Date().before(task.getDeadlineOnJVM()))
            dateMap.add(task.getDeadlineOnJVM(), task.getId());
    }

    static void setTimer(Task task, Long removeId) {
        dateMap.removeValue(removeId);
        setTimer(task);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        repositoryService.taskRepository.findAll().forEach(SendMailService::setTimer);

        new Thread(() -> {
            Exception exception;
            while (true) {
                try {
                    if (!dateMap.isEmpty()) {
                        Date nextDate = dateMap.firstKey();
                        long sleepTime = nextDate.getTime() - new Date().getTime();
                        Thread.sleep((sleepTime < 0 ? 0 : sleepTime) + A_HOUR * 3);
                        dateMap.getValues(nextDate).forEach(SendMailService.this::sendMail);
                        dateMap.remove(nextDate);
                    } else
                        Thread.sleep(5 * A_HOUR);
                } catch (Exception e) {
                    exception = e;
                    break;
                }
            }
            throw new RuntimeException(exception);
        }).start();

//        dateMap.getValues(dateMap.firstKey()).forEach(SendMailService.this::sendMail);
    }

    @SuppressWarnings("unchecked")
    private void beforeSendMail() throws IOException {
        for (Map map : new ObjectMapper().readValue(new File(subjectInfoPath), Map[].class)) {
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
        }
    }

    private void sendMail(Long taskId) {
        try {
            beforeSendMail();

            Optional<Task> optional = repositoryService.taskRepository.findById(taskId);
            assert optional.isPresent();
            Task task = optional.get();
            Subject subject = subjectMap.get(task.getSubject());
            File submitZip = createTaskZipFile(task);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailUsername);
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
        return new CompactTool(srcPath + ".zip").zip(srcPath);
    }

    private String emailText(Task task) {
        Context context = new Context();
        context.setVariable("teacherName", subjectMap.get(task.getSubject()).getTeacher());
        context.setVariable("taskTitle", task.getTitle());
        File submitDir = new File(storePath + "/" + task.getSubject() + "/" + task.getTitle());
        String[] fileNames = submitDir.list();
        assert fileNames != null;
        context.setVariable("submitSize", fileNames.length);
        Map<Long, User> users = new HashMap<>();
        repositoryService.authorityRepository.findAll().forEach(authority -> {
            if (authority.getRole().getRoleName().equals(Role.ROLE_USER))
                users.put(authority.getUser().getUserCode(), authority.getUser());
        });
        context.setVariable("userCount", users.size());
        List<User> submitted = new ArrayList<>();
        if (formatSaveProcessorTool.findProcessorByTask(task).fileUserWhenSendMail())
            for (String fileName : fileNames)
                submitted.add(repositoryService.userRepository.findByUserCode(Long.valueOf(
                        "2" + RegexTool.lastRegex(
                                fileName.replace(task.getTitle(), ""), "\\d{2}"))));
        int flagSize = 20;
        context.setVariable("flagSize", flagSize);
        context.setVariable("codePrefix", adminService.getUserCodePrefix());
        submitted.forEach(u ->
                users.remove(u.getUserCode()));
        users.forEach((k, v) -> System.out.println(v));
        List<Cell> tableCells = new ArrayList<>();
        if (submitted.size() == 0) {
            context.setVariable("t1", "提交文件");
            context.setVariable("t2", "大小");
            File[] submitFiles = submitDir.listFiles();
            assert submitFiles != null;
            for (File elem : submitFiles)
                tableCells.add(new Cell(
                        elem.getName(), elem.getTotalSpace() + "B"
                ));
        } else {
            context.setVariable("t1", "学号");
            context.setVariable("t2", "姓名");
            (fileNames.length >= flagSize ? users.values() : submitted).forEach(user ->
                    tableCells.add(new Cell(
                            String.valueOf(user.getUserCode()),
                            user.getUserName()
                    )));
        }
        context.setVariable("table", tableCells);
        return thymeleaf.process("email.html", context);
    }

    public Object[] getSubjectNames() throws IOException {
        List<Object> subjectNames = new ArrayList<>();
        for (Map map : new ObjectMapper().readValue(
                new File(subjectInfoPath),
                Map[].class
        ))
            subjectNames.add(map.get("subject"));
        return subjectNames.toArray();
    }

    public Object[] getTimers() {
        return dateMap.keySet().toArray();
    }

    class Cell {
        public String c1, c2;

        Cell(String c1, String c2) {
            this.c1 = c1;
            this.c2 = c2;
        }
    }
}
