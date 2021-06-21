package main.service;

import java.util.Optional;
import main.api.request.SettingsRequest;
import main.model.repositories.GlobalSettingsRepository;
import main.model.tables.GlobalSettings;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalSettingsService {

  private final GlobalSettingsRepository globalSettingsRepository;
  private final AuthenticationUser authenticationUser;

  @Autowired
  public GlobalSettingsService(
      GlobalSettingsRepository globalSettingsRepository,
      AuthenticationUser authenticationUser) {
    this.globalSettingsRepository = globalSettingsRepository;
    this.authenticationUser = authenticationUser;
  }

  public void globalSettings(SettingsRequest settingsRequest) {

    Users currentUser = authenticationUser.getUser();

    if (currentUser == null || !currentUser.isModerator()) {
      return;
    }

    Optional<GlobalSettings> multiUserSettings = globalSettingsRepository.findByCode("MULTIUSER_MODE");
    GlobalSettings multiUser = multiUserSettings.get();

    if (settingsRequest.isMultiUserMode()) {
      multiUser.setValue("yes");
    } else {
      multiUser.setValue("no");
    }
    globalSettingsRepository.save(multiUser);

    Optional<GlobalSettings> postPremoderationSettings = globalSettingsRepository.findByCode("POST_PREMODERATION");
    GlobalSettings postPremoderation = postPremoderationSettings.get();

    if (settingsRequest.isPostPremoderation()) {
      postPremoderation.setValue("yes");
    } else {
      postPremoderation.setValue("no");
    }
    globalSettingsRepository.save(postPremoderation);

    Optional<GlobalSettings> statisticIsPublicSettings = globalSettingsRepository.findByCode("STATISTICS_IS_PUBLIC");
    GlobalSettings statisticIsPublic = statisticIsPublicSettings.get();

    if (settingsRequest.isStatisticsIsPublic()) {
      statisticIsPublic.setValue("yes");
    } else {
      statisticIsPublic.setValue("no");
    }
    globalSettingsRepository.save(statisticIsPublic);

  }
}
