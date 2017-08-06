package planH.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import planH.entity.relationship.Authority;

@Repository
public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long> {

    Authority findByUser_UserName(String userName);
}
