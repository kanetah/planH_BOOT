package kanetah.planH.repository;

import kanetah.planH.entity.relationship.SubordinateTask;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubordinateTaskRepository
        extends PagingAndSortingRepository<SubordinateTask, Long> {
}
