package ibf2022.miniproject.server.service;

import ibf2022.miniproject.server.exception.UserNotFoundException;
import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.repository.UserRepository;
import java.util.NoSuchElementException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public void login(String username, String password) {
    return;
  }


  public User get(Integer id) throws UserNotFoundException {
    try {
      return (User) userRepository.findById(id).get();
    } catch (NoSuchElementException ex) {
      throw new UserNotFoundException("Could not find any user with ID " + id);
    }
  }

  private boolean checkUnique(String username) {
    User userDB = userRepository.findByUsername(username);
    return userDB == null;
  }

  public String updateResetPasswordToken(String email) throws UserNotFoundException {
    User user = userRepository.findByEmail(email);
    System.out.println(user.getEmail() + "test");
    if (user != null) {
      String token = RandomString.make(64);
      user.setResetPasswordToken(token);
      userRepository.save(user);

      return token;
    } else {
      throw new UserNotFoundException("Email not found" + email);
    }
  }

  public boolean resetPassword(String code, String newPassword) throws UserNotFoundException {
    User user = userRepository.findByResetPasswordToken(code);
    if (user == null) {
      throw new UserNotFoundException("User not found");
    }
    user.setPassword(newPassword);
    user.setResetPasswordToken(null);
    userRepository.save(user);
    return true;
  }
}
