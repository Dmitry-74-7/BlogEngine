package main.service;

import java.util.Optional;
import main.api.response.StatisticsMyResponse;
import main.model.repositories.GlobalSettingsRepository;
import main.model.repositories.PostsRepository;
import main.model.tables.GlobalSettings;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

  private final PostsRepository postsRepository;
  private final AuthenticationUser authenticationUser;
  private final GlobalSettingsRepository globalSettingsRepository;

  @Autowired
  public StatisticsService(PostsRepository postsRepository,
      AuthenticationUser authenticationUser,
      GlobalSettingsRepository globalSettingsRepository) {
    this.postsRepository = postsRepository;
    this.authenticationUser = authenticationUser;
    this.globalSettingsRepository = globalSettingsRepository;
  }

  public StatisticsMyResponse statisticsMy() {
    StatisticsMyResponse statistics = new StatisticsMyResponse();
    Users currentUser = authenticationUser.getUser();

    statistics.setPostsCount(ifNullElse(postsRepository.myPostCount(currentUser.getId())));

    if (statistics.getPostsCount() == 0) {
      return statistics;
    }
    statistics.setLikesCount(ifNullElse(postsRepository.myLikeDislikeCount(currentUser.getId(), (short) 1)));
    statistics.setDislikesCount(ifNullElse(postsRepository.myLikeDislikeCount(currentUser.getId(), (short) -1)));
    statistics.setViewsCount(ifNullElse(postsRepository.myViewCount(currentUser.getId())));
    statistics.setFirstPublication(postsRepository
        .findMyActionPosts(PageRequest.of(0, 1, Sort.by("time")), "ACCEPTED", currentUser.getId())
        .get(0).getTime()
        .getTime()/1000);
    return statistics;
  }

  public StatisticsMyResponse statisticsAll() {
    StatisticsMyResponse statistics = new StatisticsMyResponse();
    Users currentUser = authenticationUser.getUser();
    Optional<GlobalSettings> globalSettings = globalSettingsRepository.findByCode("STATISTICS_IS_PUBLIC");

    if (globalSettings.get().getValue().equals("no") && !currentUser.isModerator()) {
      return null;
    }

    statistics.setPostsCount(postsRepository.PostCount());
    statistics.setLikesCount(ifNullElse(postsRepository.LikeDislikeCount((short) 1)));
    statistics.setDislikesCount(ifNullElse(postsRepository.LikeDislikeCount((short) -1)));
    statistics.setViewsCount(ifNullElse(postsRepository.ViewCount()));
    statistics.setFirstPublication(postsRepository
        .findPosts(PageRequest.of(0,1, Sort.by("time")))
        .get(0).getTime()
        .getTime()/1000);
    return statistics;
  }

  private Integer ifNullElse(Integer integer) {
    if (integer == null) {
      return 0;
    }
    return integer;
  }
}
