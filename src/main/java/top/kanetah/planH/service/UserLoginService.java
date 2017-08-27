package top.kanetah.planH.service;

import top.kanetah.planH.entity.node.User;
import top.kanetah.planH.entity.relationship.Authority;
import top.kanetah.planH.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService implements UserDetailsService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserLoginService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Authority authority = authorityRepository.findByUser_UserName(username);
        User user = authority.getUser();
        user.addRoleAuthorities(authority);

        return user;
    }
}
