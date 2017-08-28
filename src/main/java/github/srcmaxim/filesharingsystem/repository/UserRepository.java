package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    List<User> findAll();

    @Override
    User findOne(Long aLong);

    User findByLogin(String login);

    @Override
    User save(User entity);

    @Override
    void delete(Long aLong);

    @Override
    void delete(User entity);

    @Query(value = "select " +
            "case when count(1)>0 then 'true' else 'false' end " +
            "from file_sharing_system.user where login = :login", nativeQuery = true)
    boolean existsByLogin(@Param("login") String login);

    @Query(value = "select " +
            "case when count(1)>0 then 'true' else 'false' end " +
            "from file_sharing_system.user where email=:email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);

    @Query(value = "select " +
            "case when count(1)>0 then 'true' else 'false' end " +
            "from file_sharing_system.user where phone=:phone", nativeQuery = true)
    boolean existsByPhone(@Param("phone") String phone);

}
