package com.kanetah.planH.service;

import com.kanetah.planH.entity.relationship.Authority;
import com.kanetah.planH.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.kanetah.planH.entity.node.User;
import com.kanetah.planH.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserDetailsServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Authority authority = authorityRepository.findByUser_UserName(username);
        User user = authority.getUser();
        user.addRoleAuthorities(authority);

        return user;
    }
}
