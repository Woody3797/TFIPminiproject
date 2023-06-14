package ibf2022.miniproject.server.security;

import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ShopUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if(user!=null){
      return new ShopUserDetails(user);
    }
    throw  new UsernameNotFoundException("Could not find user with username: " + username);
  }



}
