package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.Role;
import com.kanetah.planH.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.kanetah.planH.repository.AuthorityRepository;
import com.kanetah.planH.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleViewService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public RoleViewService(UserRepository repository, RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    public String getViewByRole() {

//        User user = new User(213, "kane");
//        Role role = roleRepository.findUserRole();
//        System.out.println("NICO");
//        Authority authority = new Authority(user,role);
//        System.out.println("AOWU");
//        System.out.println("POI:: " + role.getRoleName());
//        authorityRepository.save(authority);

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
