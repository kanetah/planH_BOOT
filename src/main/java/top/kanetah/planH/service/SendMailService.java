package top.kanetah.planH.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.tools.TreeMultiValueMap;

import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class SendMailService implements InitializingBean {

    private static TreeMultiValueMap<Date, Long> dateMap = new TreeMultiValueMap<>();
    private final RepositoryService repositoryService;
    private final JavaMailSenderImpl mailSender;

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

    @Override
    public void afterPropertiesSet() throws Exception {
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
            System.out.println("id: " + taskId);
            Optional<Task> optional = repositoryService.taskRepository.findById(taskId);
            assert optional.isPresent();
            Task task = optional.get();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("2205485120@qq.com");
            helper.setSubject("test2");
            helper.setText("logger test");
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
