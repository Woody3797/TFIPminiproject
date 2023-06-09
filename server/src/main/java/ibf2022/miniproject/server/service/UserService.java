package ibf2022.miniproject.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public User createNewUser(User user) {
        return userRepository.save(user);
    }

    public void initRolesAndUser() {
       
        User adminUser = new User();
        adminUser.setUsername("admin1");
        adminUser.setPassword("admin1");
        userRepository.save(adminUser);

        User user = new User();
        user.setUsername("user1");
        user.setPassword("user1");
        userRepository.save(user);
    }
}
