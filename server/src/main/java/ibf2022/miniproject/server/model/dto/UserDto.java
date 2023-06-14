package ibf2022.miniproject.server.model.dto;

import ibf2022.miniproject.server.model.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UserDto {

  private String username;
  @Enumerated(EnumType.STRING)
  private UserRole userRole;


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }
}
