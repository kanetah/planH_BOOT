package planH.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostFilter;
import planH.entity.node.User;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends GraphRepository<User> {

    @PostFilter("hasRole('ROLE_ADMIN') || " +
            "userName == principal.username")
    long findUserCodeByUserName(@Param("userName") String userName);

    @PostFilter("hasRole('ROLE_ADMIN') || " +
            "userName == principal.username")
    User findUserByUserName(@Param("userName") String userName);
}
