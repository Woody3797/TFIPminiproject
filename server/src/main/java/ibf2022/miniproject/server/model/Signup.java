package ibf2022.miniproject.server.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Signup {
    
    @NotBlank
    @Size(min = 1)
    private String email;
    
    @NotBlank
    private String password;

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
        return "Signup [email=" + email + ", password=" + password + "]";
    }
}
