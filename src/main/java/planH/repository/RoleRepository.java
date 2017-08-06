package planH.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import planH.entity.relationship.Role;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
}
