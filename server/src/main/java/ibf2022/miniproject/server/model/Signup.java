package ibf2022.miniproject.server.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Signup {
    
    @NotBlank
    private String username;
    
    @NotBlank
    @Size(min = 1)
    private String email;
    
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Signup [username=" + username + ", email=" + email + ", password=" + password + "]";
    }
}
