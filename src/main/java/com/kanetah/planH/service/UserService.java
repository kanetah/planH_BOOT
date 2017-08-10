package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.info.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public List<Map<String, Object>> getTask(long form, long to, String userCode) {

        List<TaskInfo> taskInfos = new ArrayList<>();
        //fixme
        Iterator<Task> iterator = repositoryService.taskRepository.findAll().iterator();
        iterator.forEachRemaining(e ->
                taskInfos.add(new TaskInfo(e, null)));

        List<Map<String, Object>> ajaxList = new ArrayList<>();
        TaskInfo taskInfo;
        int i;
        for (i = 0; i < taskInfos.size(); ++i) {
            taskInfo = taskInfos.get(i);
            HashMap<String, Object> infoMap = new HashMap<>();
            infoMap.put("taskId", taskInfo.getTaskId());
            infoMap.put("subject", taskInfo.getSubject());
            infoMap.put("title", taskInfo.getTitle());
            infoMap.put("content", taskInfo.getContent());
            infoMap.put("deadline", taskInfo.getDeadline());
            infoMap.put("submit", taskInfo.getSubmitDate());
            infoMap.put("path", taskInfo.getSubmitFilePath());
            ajaxList.add(infoMap);
        }

        return ajaxList;
    }

    public Map<String, Object> submitTask(long userCode, Long taskId, MultipartFile file)
            throws IOException {
        System.out.println(userCode);
        System.out.println(taskId);
        System.out.println(file.getSize());

        System.out.println("开始");
        String path = "D:/temp/aaa";
        String fileName = file.getOriginalFilename();
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if(!targetFile.exists())
            if(!targetFile.createNewFile())
                throw new CreateFileException();
        file.transferTo(targetFile);

        Map<String, Object> map = new HashMap<>();
        map.put("status", "patched");
        return map;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "can not create new file")
    private class CreateFileException extends RuntimeException {
    }
}
