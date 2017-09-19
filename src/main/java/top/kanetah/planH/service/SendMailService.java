package top.kanetah.planH.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.tools.TreeMultiValueMap;

import java.util.*;

@Service
public class SendMailService implements InitializingBean {

    private static TreeMultiValueMap<Date, Long> dateMap = new TreeMultiValueMap<>();

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
        System.out.println("id: " + taskId);
    }
}
