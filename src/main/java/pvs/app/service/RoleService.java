package pvs.app.service;

import org.springframework.stereotype.Service;
import pvs.app.dao.RoleDAO;
import pvs.app.dto.RoleDTO;
import pvs.app.entity.Role;

@Service
public class RoleService {

    private final RoleDAO roleDAO;

    RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void save(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());

        roleDAO.save(role);
    }

    public Role getByName(String name) {
        return roleDAO.findByName(name);
    }
}
