package ibf2022.miniproject.server.repository;

import org.springframework.data.repository.CrudRepository;

import ibf2022.miniproject.server.model.Role;

public interface RoleRepository extends CrudRepository<Role, String> {
    
}
