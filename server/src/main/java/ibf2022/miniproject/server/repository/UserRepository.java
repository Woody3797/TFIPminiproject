package ibf2022.miniproject.server.repository;

import static ibf2022.miniproject.server.repository.SQLQueries.*;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import ibf2022.miniproject.server.model.Signup;
import ibf2022.miniproject.server.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findByUsername(String username) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_USER_BY_USERNAME, username);
        if (rs.next()) {
            User user = User.createFromRowSet(rs);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            user.setAuthorities(authorities);
            return user;
        } else {
            throw new UsernameNotFoundException(username + " not found.");
        }
    }

    public boolean signupNewUser(Signup signupRequest) {
        return jdbcTemplate.update(SIGNUP_NEW_USER, signupRequest.getUsername(), signupRequest.getPassword(), signupRequest.getEmail()) > 0;
    }

    public boolean existsByEmail(String email) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_USER_BY_EMAIL, email);
        return rs.next();
    }
    
}
