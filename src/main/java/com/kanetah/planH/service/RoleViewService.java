package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.Role;
import com.kanetah.planH.entity.node.TaskRoot;
import com.kanetah.planH.entity.node.User;
import com.kanetah.planH.entity.node.UserRoot;
import com.kanetah.planH.entity.relationship.Authority;
import com.kanetah.planH.entity.relationship.SubordinateUser;
import com.kanetah.planH.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleViewService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final TaskRootRepository taskRootRepository;
    private final UserRootRepository userRootRepository;
    private final SubordinateTaskRepository subordinateTaskRepository;
    private final SubordinateUserRepository subordinateUserRepository;

    @Autowired
    public RoleViewService(UserRepository userRepository, RoleRepository roleRepository, AuthorityRepository authorityRepository, TaskRootRepository taskRootRepository, UserRootRepository userRootRepository, SubordinateTaskRepository subordinateTaskRepository, SubordinateUserRepository subordinateUserRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.taskRootRepository = taskRootRepository;
        this.userRootRepository = userRootRepository;
        this.subordinateTaskRepository = subordinateTaskRepository;
        this.subordinateUserRepository = subordinateUserRepository;
    }

    private void before() {
    }

    public String getViewByRole() {

        before();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        List<String> roles = new ArrayList<>();
        userDetails.getAuthorities().forEach(e ->
                roles.add(e.getAuthority()));
        if (roles.contains(Role.ROLE_USER))
            return "forward:user/" + userDetails.getUsername();
        else if (roles.contains(Role.ROLE_ADMIN))
            return "forward:admin";
        throw new UserRoleException();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "role not found")
    private class UserRoleException extends RuntimeException {
    }
}
