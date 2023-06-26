package ibf2022.miniproject.server.repository;

import ibf2022.miniproject.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);

    public User findByEmail(String email);

    public User findByResetPasswordToken(String token);

}
