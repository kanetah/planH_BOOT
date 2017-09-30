package top.kanetah.planH.service;

import org.springframework.context.support.ApplicationObjectSupport;
import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.entity.relationship.Submit;
import top.kanetah.planH.format.FormatSaveProcessor;
import top.kanetah.planH.format.FormatType;
import top.kanetah.planH.info.Info;
import top.kanetah.planH.info.InfoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.tools.FormatSaveProcessorTool;
import top.kanetah.planH.tools.InterfaceTool;

import java.io.IOException;
import java.util.*;

@Service
public class UserService extends ApplicationObjectSupport {

    private final RepositoryService repositoryService;
    private final Info info;
    private Map<Long, List<Task>> BuffMap = new HashMap<>();
    private List<Class<Object>> processorInterfaces = InterfaceTool.getAllClassByInterface(
            FormatSaveProcessor.class
    );

    @Autowired
    public UserService(
            RepositoryService repositoryService,
            InfoImpl info
    ) {
        this.repositoryService = repositoryService;
        this.info = info;
    }

    public String getUserName(long userCode) {
        return
                repositoryService.userRepository
                        .findByUserCode(userCode).getUserName();
    }

    public List<Map<String, Object>> getTask(int from, int to, long userCode) {
        List<Object> taskInfoList = new ArrayList<>();
        Map<Long, Submit> submitMap = new HashMap<>();
        repositoryService.submitRepository
                .findAllByUser_UserCode(userCode).forEach(submit -> {
            Submit oldSubmit = submitMap.get(submit.getTask().getId());
            if (oldSubmit != null
                    && oldSubmit.getSubmitDate().compareTo(submit.getSubmitDate()) > 0)
                return;
            submitMap.put(
                    submit.getTask().getId(),
                    submit
            );
        });

        if (from == 0) {
            Stack<Task> taskStack = new Stack<>();
            List<Task> submitted = new ArrayList<>();
            repositoryService.taskRepository
                    .findAll().iterator()
                    .forEachRemaining(task -> {
                        if (submitMap.get(task.getId()) == null)
                            taskStack.push(task);
                        else
                            submitted.add(task);
                    });
            List<Task> taskList = new ArrayList<>();
            while (!taskStack.empty())
                taskList.add(taskStack.pop());
            taskList.addAll(submitted);
            BuffMap.put(userCode, taskList);
        }

        List<Task> tasks = BuffMap.get(userCode);
        for (int i = from; i < to; ++i)
            try {
                Task task = tasks.get(i);
                Submit submit = submitMap.get(task.getId());
                if (submit == null) submit = Submit.emptySubmit();
                taskInfoList.add(
                        info.byOrigin(
                                task,
                                submit
                        )
                );
            } catch (IndexOutOfBoundsException e) {
                break;
            }

        List<Map<String, Object>> ajaxList = new ArrayList<>();
        Object[] taskInfo = new Object[1];
        taskInfoList.forEach(task -> {
            taskInfo[0] = task;
            HashMap<String, Object> infoMap = new HashMap<>();
            info.getEnumList(task.getClass()).forEach(attribute ->
                    infoMap.put(
                            attribute.getValue(),
                            attribute.invokeMargetMethod(taskInfo[0])
                    ));
            ajaxList.add(infoMap);
        });

        return ajaxList;
    }

    public void submitTask(
            long userCode, Long taskId, MultipartFile file
    ) throws IOException {
        Optional<Task> optional = repositoryService.taskRepository.findById(taskId);
        assert optional.isPresent();
        Task task = optional.get();
        User user = repositoryService.userRepository.findByUserCode(userCode);
        String saveFileName = new FormatSaveProcessorTool().findProcessorByTask(task)
                .saveFile(user, task, file);
        Submit submit = new Submit(user, task, file.getOriginalFilename(), saveFileName, new Date());
        repositoryService.submitRepository.save(submit);
    }

    public Object[] getProcessorValues() {
        Object[] values = new Object[processorInterfaces.size()];
        for (int i = 0; i < values.length; i++)
            values[i] = processorInterfaces.get(i).
                    getAnnotation(FormatType.class).value();
        return values;
    }
}
