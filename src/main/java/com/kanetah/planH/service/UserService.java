package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.entity.node.User;
import com.kanetah.planH.entity.relationship.Submit;
import com.kanetah.planH.info.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.kanetah.planH.info.TaskInfoAttribute.AllTaskInfoAttribute;

@Service
public class UserService {

    private final RepositoryService repositoryService;
    @Value(value = "${planH.attribute.userPatchFileStorePath}")
    private String storePath;

    @Autowired
    public UserService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public List<Map<String, Object>> getTask(long form, long to, long userCode) {

        List<TaskInfo> taskInfos = new ArrayList<>();
        //fixme 推荐算法
        Iterator<Task> iterator = repositoryService.taskRepository.findAll().iterator();
        iterator.forEachRemaining(e ->
                taskInfos.add(new TaskInfo(e, null)));

        List<Map<String, Object>> ajaxList = new ArrayList<>();
        TaskInfo[] taskInfo = new TaskInfo[1];
        taskInfos.forEach(info -> {
            taskInfo[0] = info;
            HashMap<String, Object> infoMap = new HashMap<>();
            AllTaskInfoAttribute.forEach(attribute ->
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

        Submit submit = new Submit(user, task, originalFilename);
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
