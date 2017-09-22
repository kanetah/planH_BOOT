package top.kanetah.planH.repository;

import top.kanetah.planH.entity.relationship.Submit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitRepository
        extends PagingAndSortingRepository<Submit, Long> {

    Iterable<Submit> findAllByUser_UserCode(long userCode);

    Iterable<Submit> findAllByTask_Id(Long id);
}
