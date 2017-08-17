package kanetah.planH.repository;

import kanetah.planH.entity.relationship.Submit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitRepository
        extends PagingAndSortingRepository<Submit, Long> {
    Submit findByUser_UserCodeAndTask_TaskId(long userCode, long taskId);

    Iterable<Submit> findAllByUser_UserCode(long userCode);
}
