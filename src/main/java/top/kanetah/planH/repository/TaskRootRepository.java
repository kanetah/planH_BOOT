package top.kanetah.planH.repository;

import top.kanetah.planH.entity.node.TaskRoot;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRootRepository
        extends PagingAndSortingRepository<TaskRoot, Long> {

    @Query("match (node:TaskRoot) return node")
    TaskRoot find();
}
