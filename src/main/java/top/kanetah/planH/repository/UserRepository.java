package top.kanetah.planH.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import top.kanetah.planH.entity.node.User;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository
        extends PagingAndSortingRepository<User, Long> {

    long findUserCodeByUserName(String userName);

    User findByUserName(String userName);

    User findByUserCode(long userCode);
}
