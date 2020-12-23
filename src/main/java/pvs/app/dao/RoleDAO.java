package pvs.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pvs.app.entity.Role;

import java.util.List;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {
//    List<Role> findAllByUserId(Long userId);
}
