package github.srcmaxim.filesharingsystem.repository;

import github.srcmaxim.filesharingsystem.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Override
    public abstract List<Resource> findAll();

    @Override
    public abstract Resource findOne(Long aLong);

    @Override
    public abstract Resource save(Resource entity);

    @Override
    public abstract void delete(Long aLong);

    @Override
    public abstract void delete(Resource entity);

}
