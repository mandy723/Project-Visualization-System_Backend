package pvs.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryDAO extends CrudRepository<pvs.app.entity.Repository, Long> {
    pvs.app.entity.Repository findByUrl(String url);
}