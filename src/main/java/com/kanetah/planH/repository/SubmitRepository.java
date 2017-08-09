package com.kanetah.planH.repository;

import com.kanetah.planH.entity.relationship.Submit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitRepository
        extends PagingAndSortingRepository<Submit, Long> {
}
