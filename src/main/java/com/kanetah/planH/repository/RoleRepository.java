package com.kanetah.planH.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.kanetah.planH.entity.node.Role;

@Repository
public interface RoleRepository
        extends PagingAndSortingRepository<Role, Long> {

    @Query("match(node:Role) where node.roleName = 'USER' return node")
    Role findUserRole();
}
