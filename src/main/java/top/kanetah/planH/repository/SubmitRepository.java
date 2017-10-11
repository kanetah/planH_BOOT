package top.kanetah.planH.repository;

import top.kanetah.planH.entity.node.Task;
import top.kanetah.planH.entity.relationship.Submit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SubmitRepository
        extends PagingAndSortingRepository<Submit, Long> {

    Iterable<Submit> findAllByUser_UserCode(long userCode);

    Iterable<Submit> findAllByTask(Task task);
}
