package ibf2022.miniproject.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ibf2022.miniproject.server.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    
}
