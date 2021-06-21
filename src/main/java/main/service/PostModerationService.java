package main.service;

import java.util.Optional;
import main.api.request.PostModerationRequest;
import main.api.response.chekResponse.Result;
import main.model.repositories.PostsRepository;
import main.model.tables.ModerationStatus;
import main.model.tables.Posts;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostModerationService {

  private final AuthenticationUser authenticationUser;
  private final PostsRepository postsRepository;

  @Autowired
  public PostModerationService(
      AuthenticationUser authenticationUser, PostsRepository postsRepository) {
    this.authenticationUser = authenticationUser;
    this.postsRepository = postsRepository;
  }


  public Result postModerationResponse(PostModerationRequest moderationRequest) {

    Users currentUser = authenticationUser.getUser();

    if (!currentUser.isModerator()) {
      return new Result(false);
    }

    Optional<Posts> post = postsRepository.findById(moderationRequest.getId());

    if (!post.isPresent()) {
       return new Result(false);
    }

    Posts currentPost = post.get();

    switch (moderationRequest.getDecision().toLowerCase()) {
        case "accept":
          currentPost.setModerationStatus(ModerationStatus.ACCEPTED);
          break;
        case "decline":
          currentPost.setModerationStatus(ModerationStatus.DECLINED);
          break;
        default:
          return new Result(false);
      }
    currentPost.setModeratorId(currentUser.getId());
    postsRepository.save(currentPost);

    return new Result(true);


  }
}
