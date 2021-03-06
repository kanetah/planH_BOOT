package top.kanetah.planH.repository;

import top.kanetah.planH.entity.node.UserRoot;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRootRepository
        extends PagingAndSortingRepository<UserRoot, Long> {

    @Query("match (node:UserRoot) return node")
    UserRoot find();
}
