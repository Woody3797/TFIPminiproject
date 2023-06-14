package ibf2022.miniproject.server.model;

public enum UserRole {
  ADMIN {
    @Override
    public String defaultDescription() {
      return "Admin description";
    }

  },
  USER {
    @Override
    public String defaultDescription() {
      return "USER description";
    }
  };

  public abstract String defaultDescription();
  }
