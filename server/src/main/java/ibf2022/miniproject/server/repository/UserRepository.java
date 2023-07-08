package ibf2022.miniproject.server.repository;

import static ibf2022.miniproject.server.repository.SQLQueries.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import ibf2022.miniproject.server.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AmazonS3 amazonS3;

    public User findByEmail(String email) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_USER_BY_EMAIL, email);
        if (rs.next()) {
            User user = User.createFromRowSet(rs);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            user.setAuthorities(authorities);
            return user;
        } else {
            throw new UsernameNotFoundException(email + " not found.");
        }
    }

    public boolean signupNewUser(String email, String password) {
        return jdbcTemplate.update(SIGNUP_NEW_USER, email, password) > 0;
    }

    public boolean existsByEmail(String email) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_USER_BY_EMAIL, email);
        return rs.next();
    }

    public boolean resetPassword(String email, String password) {
        if (existsByEmail(email)) {
            return jdbcTemplate.update(CHANGE_CURRENT_USER_PASSWORD, password, email) > 0;
        }
        return false;
    }

    @SuppressWarnings("null")
    public String editProfileImage(String email, MultipartFile profileImage) throws IOException {
        if (existsByEmail(email)) {
            // Add custom metadata
            Map<String, String> userData = new HashMap<>();
            userData.put("filename", profileImage.getOriginalFilename());
            userData.put("upload-date", LocalDateTime.now().toString());
            // Add object's metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(profileImage.getContentType());
            metadata.setContentLength(profileImage.getSize());
            metadata.setUserMetadata(userData);
            // woodybucket - bucket name
            // key - imagefile path
            // file.getInputStream() - actual bytes
            // metadata
            String key = email + "/profileImage";
            PutObjectRequest putReq = new PutObjectRequest("woodybucket", key, profileImage.getInputStream(), metadata);
            // Make the file publically accessible
            putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putReq);
    
            return amazonS3.getUrl("woodybucket", key).toString();
        }
        return null;
    }

    public String getProfilePic(String email) {
        if (existsByEmail(email)) {
            String key = email + "/profileImage";
            if (amazonS3.doesObjectExist("woodybucket", key)) {
                return amazonS3.getUrl("woodybucket", key).toString();
            }
        }
        return null;
    }

    public String deleteProfilePic(String email) {
        if (existsByEmail(email)) {
            String key = email + "/profileImage";
            amazonS3.deleteObject("woodybucket", key);
            return "picture deleted";
        }
        return null;
    }
    
}
