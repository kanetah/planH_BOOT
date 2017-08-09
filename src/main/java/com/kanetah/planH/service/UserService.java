package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.Task;
import com.kanetah.planH.entity.relationship.Submit;
import com.kanetah.planH.info.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserService {

    private final RepositoryService repositoryService;

    @Autowired
    public UserService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    //fixme
    public List<TaskInfo> getTask(long form, long to, String userCode) {

        System.out.println("f: " + form + "t: " + to + "u: " + userCode);

        List<TaskInfo> taskInfos = new ArrayList<>();
        Iterator<Task> iterator = repositoryService.taskRepository.findAll().iterator();
        iterator.forEachRemaining(e ->
                //fixme
                taskInfos.add(new TaskInfo(e, null)));
        return taskInfos;
    }
}
