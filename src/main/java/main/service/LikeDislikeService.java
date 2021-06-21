package main.service;

import java.sql.Timestamp;
import java.util.Optional;
import main.api.request.LikeDislikeRequest;
import main.api.response.chekResponse.Result;
import main.model.repositories.PostVotesRepository;
import main.model.repositories.PostsRepository;
import main.model.tables.PostVotes;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeDislikeService {
  private final AuthenticationUser authenticationUser;
  private final PostVotesRepository postVotesRepository;
  private final PostsRepository postsRepository;

  @Autowired
  public LikeDislikeService(AuthenticationUser authenticationUser,
      PostVotesRepository postVotesRepository,
      PostsRepository postsRepository) {
    this.authenticationUser = authenticationUser;
    this.postVotesRepository = postVotesRepository;
    this.postsRepository = postsRepository;
  }

  public Result likeDislike(LikeDislikeRequest postId, int value) {
    Users currentUser = authenticationUser.getUser();
    if (currentUser == null) {
      return new Result(false);
    }

    Optional<PostVotes> vote = postVotesRepository.findVotes(currentUser.getId(), postId.getPostId());

    if (!vote.isPresent()) {
      PostVotes postVotes = new PostVotes();
      postVotes.setPost(postsRepository.findById(postId.getPostId()).get());
      postVotes.setUser(currentUser);
      postVotes.setTime(new Timestamp(System.currentTimeMillis()));
      postVotes.setValue((short) value);
      postVotesRepository.save(postVotes);
      return new Result(true);
    }

    PostVotes postVotes = vote.get();

    if (postVotes.getValue() == value) {
      return new Result(false);
    }

    postVotes.setValue((short) value);
    postVotes.setTime(new Timestamp(System.currentTimeMillis()));
    postVotesRepository.save(postVotes);
    return new Result(true);
  }

}
