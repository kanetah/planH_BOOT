package com.kanetah.planH.service;

import com.kanetah.planH.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RepositoryService {

    final AuthorityRepository authorityRepository;
    final RoleRepository roleRepository;
    final SubmitRepository submitRepository;
    final SubordinateTaskRepository subordinateTaskRepository;
    final SubordinateUserRepository subordinateUserRepository;
    final TaskRepository taskRepository;
    final TaskRootRepository taskRootRepository;
    final UserRepository userRepository;
    final UserRootRepository userRootRepository;

    @Autowired
    public RepositoryService(
            AuthorityRepository authorityRepository,
            RoleRepository roleRepository,
            SubmitRepository submitRepository,
            SubordinateTaskRepository subordinateTaskRepository,
            SubordinateUserRepository subordinateUserRepository,
            TaskRepository taskRepository,
            TaskRootRepository taskRootRepository,
            UserRepository userRepository,
            UserRootRepository userRootRepository
    ) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.submitRepository = submitRepository;
        this.subordinateTaskRepository = subordinateTaskRepository;
        this.subordinateUserRepository = subordinateUserRepository;
        this.taskRepository = taskRepository;
        this.taskRootRepository = taskRootRepository;
        this.userRepository = userRepository;
        this.userRootRepository = userRootRepository;
    }
}
