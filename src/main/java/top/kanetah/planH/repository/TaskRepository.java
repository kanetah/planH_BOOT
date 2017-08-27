package top.kanetah.planH.repository;

import top.kanetah.planH.entity.node.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository
        extends PagingAndSortingRepository<Task, Long> {
}
