package kanetah.planH.repository;

import kanetah.planH.entity.relationship.Authority;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository
        extends PagingAndSortingRepository<Authority, Long> {

    Authority findByUser_UserName(String userName);
}
