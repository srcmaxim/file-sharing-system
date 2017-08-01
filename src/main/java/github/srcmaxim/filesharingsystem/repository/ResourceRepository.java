package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
