package ibf2022.miniproject.server.service;

import ibf2022.miniproject.server.exception.UserNotFoundException;
import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.model.UserRole;
import ibf2022.miniproject.server.repository.UserRepository;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void login(String username, String password){
        return;
    }

    public void signup(String username, String password){
        if(checkUnique(username)){
            User user = new User(username,password, UserRole.USER);
            encodePassword(user);
            User savedUser = userRepository.save(user);
            System.out.println(savedUser.getId()+": "+savedUser.getPassword());
        } else
        {
            System.out.println(username+"is not unique "+password);
        }
    }



    public User get(Integer id) throws UserNotFoundException {
        try {
            return (User) userRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    private boolean checkUnique(String username){
        User userDB = userRepository.findByUsername(username);
        return userDB == null;
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

}
