package planH.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import planH.entity.node.Authority;
import planH.entity.node.User;
import planH.entity.relationship.Role;
import planH.repository.AuthorityRepository;
import planH.repository.RoleRepository;
import planH.repository.UserRepository;

import java.util.Collection;

import static planH.entity.node.Authority.ROLE_ADMIN;
import static planH.entity.node.Authority.ROLE_USER;

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

//        User user = new User(214, "poi");
//        Role role = new Role(user, Authority.ROLE_USER);
//        user.addRoleAuthorities(role);
//        System.out.println("start");
//        authorityRepository.save(Authority.ROLE_ADMIN);
//        System.out.println(1);
//        authorityRepository.save(Authority.ROLE_USER);
//        System.out.println(2);
//        repository.save(user);
//        System.out.println(3);
//        roleRepository.save(role);
//        System.out.println("end");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Collection authorities = userDetails.getAuthorities();
        if (authorities.contains(ROLE_USER))
            return "forward:user/" + userDetails.getUsername();
        else if (authorities.contains(ROLE_ADMIN))
            return "admin";
        throw new UserRoleException();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "无访问权限，请确认帐号或联系管理员")
    private class UserRoleException extends RuntimeException {
    }
}
