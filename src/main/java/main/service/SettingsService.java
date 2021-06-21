package main.service;

import java.util.List;
import main.api.response.SettingsResponse;
import main.model.tables.GlobalSettings;
import main.model.repositories.GlobalSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

  private final GlobalSettingsRepository globalSettingsRepository;

  @Autowired
  public SettingsService(GlobalSettingsRepository globalSettingsRepository) {
    this.globalSettingsRepository = globalSettingsRepository;
  }

  public SettingsResponse settingsResponse() {
    SettingsResponse settingsResponse = new SettingsResponse();
    List<GlobalSettings> globalSettings = globalSettingsRepository.findAll();

    globalSettings.forEach(e -> {
      switch (e.getCode()) {
        case "MULTIUSER_MODE":
          if (e.getValue().toLowerCase().equals("yes")) {
            settingsResponse.setMultiuserMode(true);
          }
          break;
        case "POST_PREMODERATION":
          if (e.getValue().toLowerCase().equals("yes")) {
            settingsResponse.setPostPremoderation(true);
          }
          break;
        case "STATISTICS_IS_PUBLIC":
              if (e.getValue().toLowerCase().equals("yes")) {
                settingsResponse.setStatisticsIsPublic(true);
              }
              break;
      }
    });

    return settingsResponse;
  }
}
