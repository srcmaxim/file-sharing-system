package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    List<User> findAll();

    @Override
    User findOne(Long aLong);

    @Override
    User save(User entity);

    @Override
    void delete(Long aLong);

    @Override
    void delete(User entity);

}
