package kanetah.planH.repository;

import kanetah.planH.entity.relationship.SubordinateUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubordinateUserRepository
        extends PagingAndSortingRepository<SubordinateUser, Long> {
}
