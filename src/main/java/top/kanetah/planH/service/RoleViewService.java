package top.kanetah.planH.service;

import org.springframework.beans.factory.annotation.Autowired;
import top.kanetah.planH.entity.node.Role;
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

    private final RepositoryService repositoryService;

    @Autowired
    public RoleViewService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    private UserDetails userDetails() {

        return (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public String getViewByRole() {

        repositoryService.userRepository.deleteById(24L);

        List<String> roles = getRole();
        if (roles.contains(Role.ROLE_ADMIN))
            return "forward:admin/";
        else if (roles.contains(Role.ROLE_USER))
            return "forward:user/" + userDetails().getPassword();
        throw new UserRoleException();
    }

    public List<String> getRole() {

        List<String> roles = new ArrayList<>();
        userDetails().getAuthorities().forEach(e ->
                roles.add(e.getAuthority()));
        return roles;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "role not found")
    private class UserRoleException extends RuntimeException {
    }
}
