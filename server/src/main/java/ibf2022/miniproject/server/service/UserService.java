package ibf2022.miniproject.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.miniproject.server.model.Role;
import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.repository.RoleRepository;
import ibf2022.miniproject.server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    public void initRolesAndUser() {
        Role admin = new Role();
        admin.setRoleName("Admin");
        admin.setRoleDescription("Admin role");
        roleRepository.save(admin);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for new accounts");
        roleRepository.save(userRole);

        User adminUser = new User();
        adminUser.setUsername("admin1");
        adminUser.setPassword("admin1");
        adminUser.setRole(admin);
        userRepository.save(adminUser);

        User user = new User();
        user.setUsername("user1");
        user.setPassword("user1");
        user.setRole(userRole);
        userRepository.save(user);
    }
}
