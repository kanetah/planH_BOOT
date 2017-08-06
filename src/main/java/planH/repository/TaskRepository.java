package planH.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import planH.entity.node.Task;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
}
