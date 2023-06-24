package ibf2022.miniproject.server.model;

import jakarta.validation.constraints.NotBlank;

public class Login {
    
    @NotBlank
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
    
    public Login() {
    }
    
    public Login(@NotBlank String email, @NotBlank String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login [email=" + email + ", password=" + password + "]";
    }
}
