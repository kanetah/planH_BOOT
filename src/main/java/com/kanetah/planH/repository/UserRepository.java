package com.kanetah.planH.repository;

import com.kanetah.planH.entity.node.User;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends GraphRepository<User> {

//    @PostFilter("hasRole('ROLE_ADMIN') || " +
//            "userName == principal.username")
    long findUserCodeByUserName(String userName);

//    @PostFilter("hasRole('ROLE_ADMIN') || " +
//            "userName == principal.username")
    User findByUserName(String userName);
}
