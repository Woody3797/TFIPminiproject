package ibf2022.miniproject.server.model;

public class User {
    
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String credentials;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCredentials() {
        return credentials;
    }
    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
    
    public User() {
    }
    
    public User(String username, String password, String email, String credentials) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
                + ", credentials=" + credentials + "]";
    }



}
