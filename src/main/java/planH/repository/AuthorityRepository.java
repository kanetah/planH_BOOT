package planH.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import planH.entity.node.Authority;

@Repository
public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long> {
}
