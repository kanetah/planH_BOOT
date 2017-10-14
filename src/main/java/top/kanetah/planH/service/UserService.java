package top.kanetah.planH.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.entity.relationship.Submit;
import top.kanetah.planH.format.FormatSaveProcessor;
import top.kanetah.planH.format.FormatType;
import top.kanetah.planH.info.Info;
import top.kanetah.planH.info.InfoImpl;
import top.kanetah.planH.tools.FormatSaveProcessorTool;
import top.kanetah.planH.tools.InterfaceTool;

import java.io.IOException;
import java.util.*;

@Service
public class UserService extends ApplicationObjectSupport {

    private final RepositoryService repositoryService;
    private final Info info;
    private final FormatSaveProcessorTool formatSaveProcessorTool;
    private Map<Long, List<TaskSubmit>> buff = new HashMap<>();
    private List<Class<Object>> processorInterfaces = InterfaceTool.getAllClassByInterface(
            FormatSaveProcessor.class
    );

    @Autowired
    public UserService(
            RepositoryService repositoryService,
            InfoImpl info,
            FormatSaveProcessorTool formatSaveProcessorTool
    ) {
        this.repositoryService = repositoryService;
        this.info = info;
        this.formatSaveProcessorTool = formatSaveProcessorTool;
    }

    public String getUserName(long userCode) {
        return repositoryService.userRepository.findByUserCode(userCode).getUserName();
    }

    private class TaskSubmit {
        Task task;
        Submit submit;

        TaskSubmit(Task task, Submit submit) {
            this.task = task;
            this.submit = submit;
        }

        Object[] getTaskAndSubmit() {
            return new Object[]{task, submit};
        }
    }

    private void putBuffList(long userCode) {
        List<TaskSubmit> taskSubmitList = new ArrayList<>();
        repositoryService.taskRepository.findAll().iterator().forEachRemaining(task -> {
            final Submit[] lastSubmit = {Submit.emptySubmit()};
            repositoryService.submitRepository.findAllByUser_UserCode(userCode).forEach(submit -> {
                if (submit.getTask().getId().equals(task.getId())
                        && (lastSubmit[0] == null || lastSubmit[0].equals(Submit.emptySubmit())
                        || lastSubmit[0].getSubmitDate().before(submit.getSubmitDate())))
                    lastSubmit[0] = submit;
            });
            taskSubmitList.add(new TaskSubmit(task, lastSubmit[0]));
        });
        final Date now = new Date();
        taskSubmitList.sort((o1, o2) -> {
            boolean b1 = o1.submit.equals(Submit.emptySubmit()) && o1.task.getDeadlineOnJVM().after(now);
            boolean b2 = o2.submit.equals(Submit.emptySubmit()) && o2.task.getDeadlineOnJVM().after(now);
            return !b1 && b2 ? 1 : b1 && !b2 ? -1 :
                    o2.task.getDeadlineOnJVM().compareTo(o1.task.getDeadlineOnJVM());
        });
        buff.put(userCode, taskSubmitList);
    }

    public List<Map<String, Object>> getTask_Compare(int from, int to, long userCode) {
        if (from == 0)
            putBuffList(userCode);
        List<Map<String, Object>> ajaxList = new ArrayList<>();
        for (int i = from; i < to; ++i)
            try {
                Object task = info.byOrigin(buff.get(userCode).get(i).getTaskAndSubmit());
                HashMap<String, Object> infoMap = new HashMap<>();
                info.getEnumList(task.getClass()).forEach(attribute ->
                        infoMap.put(attribute.getValue(), attribute.invokeMargetMethod(task)));
                ajaxList.add(infoMap);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        return ajaxList;
    }

    public void submitTask(long userCode, Long taskId, MultipartFile file) throws IOException {
        Optional<Task> optional = repositoryService.taskRepository.findById(taskId);
        assert optional.isPresent();
        Task task = optional.get();
        User user = repositoryService.userRepository.findByUserCode(userCode);
        repositoryService.submitRepository.save(
                new Submit(user, task, file.getOriginalFilename(), new Date(),
                        formatSaveProcessorTool.findProcessorByTask(task).saveFile(user, task, file)));
    }

    public Object[] getProcessorValues() {
        Object[] values = new Object[processorInterfaces.size()];
        for (int i = 0; i < values.length; i++)
            values[i] = processorInterfaces.get(i).getAnnotation(FormatType.class).value();
        return values;
    }
}
