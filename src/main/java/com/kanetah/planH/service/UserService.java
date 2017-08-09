package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.info.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final RepositoryService repositoryService;

    @Autowired
    public UserService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    //fixme
    public List<Map<String, Object>> getTask(long form, long to, String userCode) {

        List<TaskInfo> taskInfos = new ArrayList<>();
        Iterator<Task> iterator = repositoryService.taskRepository.findAll().iterator();
        iterator.forEachRemaining(e ->
                taskInfos.add(new TaskInfo(e, null)));

        List<Map<String, Object>> ajaxList = new ArrayList<>();
        TaskInfo taskInfo;
        int i;
        for (i = 0; i < taskInfos.size(); ++i) {
            taskInfo = taskInfos.get(i);
            HashMap<String, Object> infoMap = new HashMap<>();
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
}
