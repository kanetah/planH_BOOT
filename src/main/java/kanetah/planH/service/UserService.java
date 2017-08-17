package kanetah.planH.service;

import kanetah.planH.entity.node.Task;
import kanetah.planH.entity.node.User;
import kanetah.planH.entity.relationship.Submit;
import kanetah.planH.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    private final RepositoryService repositoryService;
    @Value(value = "${kanetah.planH.userPatchFileStorePath}")
    private String storePath;
    @Autowired
    private Info info;
    private Map<Long, List<Task>> BuffMap = new HashMap<>();

    @Autowired
    public UserService(
            RepositoryService repositoryService,
            Info info
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
                .findAllByUser_UserCode(userCode).forEach(submit ->
                submitMap.put(
                        submit.getTask().getTaskId(),
                        submit
                ));

        if (from == 0) {
            Stack<Task> taskStack = new Stack<>();
            List<Task> submitted = new ArrayList<>();
            repositoryService.taskRepository
                    .findAll().iterator()
                    .forEachRemaining(task -> {
                        if (submitMap.get(task.getTaskId()) == null)
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
        for (int i = from; i < to; ++i) {
            try {
                Task task = tasks.get(i);
                Submit submit = submitMap.get(task.getTaskId());
                if (submit == null) submit = new Submit();
                taskInfoList.add(
                        info.byOrigin(
                                task,
                                submit
                        )
                );
            } catch (IndexOutOfBoundsException e) {
                break;
            }
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

    public Map<String, Object> submitTask(long userCode, Long taskId, MultipartFile file)
            throws IOException {

        Task task = repositoryService.taskRepository.findOne(taskId);
        User user = repositoryService.userRepository.findByUserCode(userCode);

        String originalFilename = file.getOriginalFilename();
        String path = storePath + "/" + task.getSubject();
        File targetFile = new File(path);
        if (!targetFile.exists())
            if (!targetFile.mkdirs())
                throw new CreateFileException();
        targetFile = new File(
                path,
                task.getTitle() + " " + userCode +
                        originalFilename.substring(originalFilename.lastIndexOf('.'))
        );
        if (!targetFile.exists())
            if (!targetFile.createNewFile())
                throw new CreateFileException();
        file.transferTo(targetFile);

        Submit submit = new Submit(user, task, originalFilename, new Date());
        repositoryService.submitRepository.save(submit);

        Map<String, Object> map = new HashMap<>();
        map.put("status", "patched");
        return map;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "can not create new file")
    private class CreateFileException extends RuntimeException {
    }
}
