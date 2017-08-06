package com.kanetah.planH.service;

import com.kanetah.planH.entity.node.Role;
import com.kanetah.planH.entity.node.User;
import com.kanetah.planH.entity.relationship.Authority;
import com.kanetah.planH.repository.AuthorityRepository;
import com.kanetah.planH.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AdminService(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    public void addUser(User user) {
        Role role = roleRepository.findUserRole();
        Authority authority = new Authority(user, role);
        authorityRepository.save(authority);
    }
}
