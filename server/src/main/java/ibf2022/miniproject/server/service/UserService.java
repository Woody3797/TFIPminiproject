package ibf2022.miniproject.server.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public boolean resetPassword(String email, String password) {
        return userRepository.resetPassword(email, password);
    }

    public String editProfileDetails(String email, String password, MultipartFile profileImage) throws IOException {
        if (password != null) {
            String encodedPassword = jwtService.passwordEncoder().encode(password);
            userRepository.resetPassword(email, encodedPassword);
        }
        if (profileImage != null) {
            String url = userRepository.editProfileImage(email, profileImage);
            return url;
        } else {
            return userRepository.deleteProfilePic(email);
        }
    }

    public String getProfilePic(String email) {
        return userRepository.getProfilePic(email);
    }
    
}
