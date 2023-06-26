package ibf2022.miniproject.server.emailSetting;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {

  @Autowired
  private SettingRepository repo;




  public EmailSettingBag getEmailSettings() {
    List<Setting> settings = repo.findAll();
    return new EmailSettingBag(settings);
  }
}
