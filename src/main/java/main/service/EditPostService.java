package main.service;

import java.sql.Timestamp;
import java.util.Optional;
import main.api.request.PostRequest;
import main.api.response.postIdResponse.PostErrorResponse;
import main.model.repositories.GlobalSettingsRepository;
import main.model.repositories.PostsRepository;
import main.model.repositories.Tag2postRepository;
import main.model.repositories.TagsRepository;
import main.model.repositories.UsersRepository;
import main.model.tables.ModerationStatus;
import main.model.tables.Posts;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditPostService extends AddPostService {

  private final PostsRepository postsRepository;
  private final UsersRepository usersRepository;
  private final AuthenticationUser authenticationUser;
  private final GlobalSettingsRepository globalSettingsRepository;
  private final TagsRepository tagsRepository;
  private final Tag2postRepository tag2postRepository;

  @Autowired
  public EditPostService(PostsRepository postsRepository,
      UsersRepository usersRepository, AuthenticationUser authenticationUser,
      GlobalSettingsRepository globalSettingsRepository,
      TagsRepository tagsRepository, Tag2postRepository tag2postRepository) {
    super( postsRepository, usersRepository,  authenticationUser, globalSettingsRepository,
         tagsRepository,  tag2postRepository);
    this.postsRepository = postsRepository;
    this.usersRepository = usersRepository;
    this.authenticationUser = authenticationUser;
    this.globalSettingsRepository = globalSettingsRepository;
    this.tagsRepository = tagsRepository;
    this.tag2postRepository = tag2postRepository;
  }


  public PostErrorResponse editPost (PostRequest postRequest, int id) {
    PostErrorResponse postErrorResponse = checkRequest(postRequest);
    Optional<Posts> foundPost = postsRepository.findById(id);
    Posts post = foundPost.get();

    if (postErrorResponse != null) {
      return postErrorResponse;
    }

    post.setTitle(postRequest.getTitle());
    post.setText(postRequest.getText());
    post.setActive(postRequest.isActive());

    Users currentUser = authenticationUser.getUser();

    if (currentUser.getId() == post.getUser().getId() && globalSettingsRepository.findByCode("POST_PREMODERATION").get().getValue().equals("yes")) {
      post.setModerationStatus(ModerationStatus.NEW);
    }

    if (postRequest.getTimestamp().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
      post.setTime(postRequest.getTimestamp());
    } else {
      post.setTime(new Timestamp(System.currentTimeMillis()));
    }

    post.setTags(getTags(postRequest.getTags()));

    postsRepository.save((post));
    tagDependence(postRequest);

    return null;
  }
}
