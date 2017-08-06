package com.kanetah.planH.repository;

import com.kanetah.planH.entity.relationship.SubordinateUser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubordinateUserRepository extends PagingAndSortingRepository<SubordinateUser, Long> {
}
