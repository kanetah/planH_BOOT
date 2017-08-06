package planH.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import planH.entity.node.Role;
import planH.entity.node.User;
import planH.entity.relationship.Authority;
import planH.repository.AuthorityRepository;
import planH.repository.RoleRepository;
import planH.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static planH.entity.node.Role.ROLE_ADMIN;
import static planH.entity.node.Role.ROLE_USER;

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
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = new ArrayList<>();
        authorities.forEach(e ->
                roles.add(e.getAuthority()));
        if (roles.contains(ROLE_USER))
            return "forward:user/" + userDetails.getUsername();
        else if (roles.contains(ROLE_ADMIN))
            return "admin";
        throw new UserRoleException();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "role not found")
    private class UserRoleException extends RuntimeException {
    }
}
