package kanetah.planH.repository;

import kanetah.planH.entity.node.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository
        extends PagingAndSortingRepository<Task, Long> {
}
