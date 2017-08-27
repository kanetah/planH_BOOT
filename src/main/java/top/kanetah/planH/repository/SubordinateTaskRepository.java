package top.kanetah.planH.repository;

import top.kanetah.planH.entity.relationship.SubordinateTask;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubordinateTaskRepository
        extends PagingAndSortingRepository<SubordinateTask, Long> {
}
