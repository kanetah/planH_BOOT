package top.kanetah.planH.service;

import org.springframework.stereotype.Service;
import top.kanetah.planH.entity.node.Task;

@Service
public class SendMailService {

    public static void setTimer(Task task) {
        System.out.println("set date: " + task.getDeadlineOnJVM() + ", id: " + task.getId());
    }
}
