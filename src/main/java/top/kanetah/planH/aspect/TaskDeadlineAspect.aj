package top.kanetah.planH.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.repository.TaskRepository;
import top.kanetah.planH.service.SendMailService;

@Component
public aspect TaskDeadlineAspect implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(TaskDeadlineAspect.class);
    @Autowired
    private TaskRepository taskRepository;

    public TaskDeadlineAspect() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        taskRepository.findAll().forEach(
                SendMailService::setTimer
        );
    }

    pointcut changeTaskDeadline():
            set(java.util.Date top.kanetah.planH.entity.node.Task.deadline);

    after(): changeTaskDeadline() {
        Task task = (Task) thisJoinPoint.getTarget();
        logger.info(
                "Task[id=" + task.getId() + "] deadline was changed, " +
                        "set a new timer on " + task.getDeadlineOnJVM() + " for send mail.");
        SendMailService.setTimer(task, task.getId());
    }
}
