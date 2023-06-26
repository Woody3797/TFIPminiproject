package ibf2022.miniproject.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException(email + " not found");
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public boolean signupNewUser(String email, String password) {
        String jwtToken = jwtService.generateTokenFromEmail(email);
        System.out.println(jwtToken);
        return userRepository.signupNewUser(email, password);
    }
    
}
