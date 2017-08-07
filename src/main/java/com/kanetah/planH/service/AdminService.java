package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.*;
import com.kanetah.planH.entity.relationship.Authority;
import com.kanetah.planH.entity.relationship.SubordinateTask;
import com.kanetah.planH.entity.relationship.SubordinateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final RepositoryService repositoryService;

    @Autowired
    public AdminService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void addTask(Task task) {

        TaskRoot taskRoot = repositoryService.taskRootRepository.find();
        SubordinateTask subordinateTask = new SubordinateTask(taskRoot, task);
        repositoryService.subordinateTaskRepository.save(subordinateTask);
    }

    public void addUser(User user) {

        UserRoot userRoot = repositoryService.userRootRepository.find();
        SubordinateUser subordinateUser = new SubordinateUser(userRoot, user);
        repositoryService.subordinateUserRepository.save(subordinateUser);

        Role role = repositoryService.roleRepository.findUserRole();
        user = repositoryService.userRepository.findByUserName(user.getUserName());
        Authority authority = new Authority(user, role);
        repositoryService.authorityRepository.save(authority);
    }
}
