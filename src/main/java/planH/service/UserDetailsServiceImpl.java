package planH.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import planH.entity.node.User;
import planH.entity.relationship.Authority;
import planH.repository.AuthorityRepository;
import planH.repository.UserRepository;

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
