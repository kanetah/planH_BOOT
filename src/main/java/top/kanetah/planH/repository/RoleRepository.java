package top.kanetah.planH.repository;

import top.kanetah.planH.entity.node.Role;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository
        extends PagingAndSortingRepository<Role, Long> {

    @Query("match(node:Role) where node.roleName = 'USER' return node")
    Role findUserRole();

    @Query("match(node:Role) where node.roleName = 'ADMIN' return node")
    Role findAdminRole();
}
