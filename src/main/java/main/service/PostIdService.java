package main.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import main.api.response.postIdResponse.PostIdResponse;
import main.api.response.postIdResponse.CommentsStruct;
import main.api.response.postIdResponse.PostRespUserStructId;
import main.api.response.postResponse.PostRespUserStruct;
import main.model.repositories.PostsRepository;
import main.model.tables.Posts;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import main.service.classes.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostIdService extends PostService{

  private final PostsRepository postsRepository;
  private final AuthenticationUser authenticationUser;

  @Autowired
  public PostIdService(PostsRepository postsRepository,
      AuthenticationUser authenticationUser) {
    super(postsRepository);
    this.postsRepository = postsRepository;
    this.authenticationUser = authenticationUser;
  }

  public PostIdResponse postIdResponse (int id) {
    List<Posts> post = postsRepository.findPostsId(id);
    if (post.size() != 0) {
        return getPostIdResponse(post.get(0), id);
    }
    return null;
  }

  private PostIdResponse getPostIdResponse (Posts post, int id) {
    counterView(post);
    PostIdResponse postIdResponse = new PostIdResponse();
    postIdResponse.setId(post.getId());
    postIdResponse.setTimestamp(post.getTime().getTime() / 1000);
    postIdResponse.setActive(post.isActive());
    postIdResponse.setUser(getUser(post.getUser()));
    postIdResponse.setTitle(post.getTitle());
    postIdResponse.setText(post.getText());

    Integer like = postsRepository.getLikeDislikeCountById(id, (short)1);
    Integer dislike = postsRepository.getLikeDislikeCountById(id, (short)-1);
    postIdResponse.setLikeCount(like == null? 0: like);
    postIdResponse.setDislikeCount(dislike == null? 0: dislike);

    postIdResponse.setViewCount(post.getViewCount());

    postIdResponse.setComments(getComments(id));
    postIdResponse.setTags(post.getTags()
        .stream()
        .map(e -> e.getName())
        .collect(Collectors.toList()));

    return postIdResponse;
  }

  private PostRespUserStruct getUser (Users user) {
    PostRespUserStruct postRespUserStruct = new PostRespUserStruct();
    postRespUserStruct.setId(user.getId());
    postRespUserStruct.setName(user.getName());
    return postRespUserStruct;
  }

  private Collection<CommentsStruct> getComments (int id) {
    Collection<CommentsStruct> commentsStructs = new ArrayList<>();
    List<Comments> comments = postsRepository.getUserList(id);

    for (Comments comment: comments) {
      CommentsStruct commentsStruct = new CommentsStruct();
      commentsStruct.setId(comment.getCommentId());
      commentsStruct.setText(comment.getText());
      commentsStruct.setTimestamp(comment.getTimestamp());
      commentsStruct.setUser(new PostRespUserStructId(comment.getUserId(),comment.getUserName(),
          comment.getUserPhoto()));
      commentsStructs.add(commentsStruct);
    }
    return commentsStructs;
  }

  private void counterView (Posts post) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth.getPrincipal() == "anonymousUser") {
      return;
    }

    Users currentUser = authenticationUser.getUser();

    if (currentUser.getId() == post.getUser().getId() ||
        currentUser.getId() == post.getModeratorId()) {
      return;
    }

    int countView = post.getViewCount();
    post.setViewCount(countView + 1);
    postsRepository.save(post);

  }
}
