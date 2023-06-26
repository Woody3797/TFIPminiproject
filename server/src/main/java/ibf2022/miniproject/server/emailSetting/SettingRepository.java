package ibf2022.miniproject.server.emailSetting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, String> {
  public Setting findByKey(String key);
}
