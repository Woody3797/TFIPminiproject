package ibf2022.miniproject.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.miniproject.server.model.Role;
import ibf2022.miniproject.server.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    public Role createNewRole(Role role) {
        return roleRepository.save(role);
    }
}
