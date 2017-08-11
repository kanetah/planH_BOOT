package kanetah.planH.service;

import kanetah.planH.entity.relationship.SubordinateUser;
import kanetah.planH.entity.node.*;
import kanetah.planH.entity.relationship.Authority;
import kanetah.planH.entity.relationship.SubordinateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final RepositoryService repositoryService;

    @Autowired
    public AdminService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void createTask(Task task) {

        TaskRoot taskRoot = repositoryService.taskRootRepository.find();
        SubordinateTask subordinateTask = new SubordinateTask(taskRoot, task);
        repositoryService.subordinateTaskRepository.save(subordinateTask);
    }

    public void createUser(User user) {

        UserRoot userRoot = repositoryService.userRootRepository.find();
        SubordinateUser subordinateUser = new SubordinateUser(userRoot, user);
        repositoryService.subordinateUserRepository.save(subordinateUser);

        Role role = repositoryService.roleRepository.findUserRole();
        user = repositoryService.userRepository.findByUserName(user.getUserName());
        Authority authority = new Authority(user, role);
        repositoryService.authorityRepository.save(authority);
    }
}
