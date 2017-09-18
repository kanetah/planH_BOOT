package top.kanetah.planH.aspect;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.kanetah.planH.repository.TaskRepository;

import java.util.Date;

@Component
public aspect SendMailAspect implements InitializingBean {

    @Autowired
    private TaskRepository taskRepository;

    public SendMailAspect() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        taskRepository.findAll().forEach(
                task ->
                        setTimer(task.getDeadline())
        );
    }

    pointcut changeTaskDeadline(Date deadline):
            set(Date top.kanetah.planH.entity.node.Task.deadline) && args(deadline);

    after(Date deadline): changeTaskDeadline(deadline) {
        setTimer(deadline);
    }

    private void setTimer(Date date) {

    }
}
