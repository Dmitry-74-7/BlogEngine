package main.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import main.api.request.CommentRequest;
import main.api.response.postCommentResponse.PostCommentsErrResponse;
import main.api.response.postCommentResponse.PostTextResponse;
import main.model.repositories.PostCommentsRepository;
import main.model.repositories.PostsRepository;
import main.model.repositories.UsersRepository;
import main.model.tables.PostComments;
import main.model.tables.Posts;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {
  private final PostCommentsRepository postCommentsRepository;
  private final AuthenticationUser authenticationUser;
  private final PostsRepository postsRepository;
  private final UsersRepository usersRepository;

  @Autowired
  public PostCommentService(PostCommentsRepository postCommentsRepository,
       AuthenticationUser authenticationUser,
      PostsRepository postsRepository,
      UsersRepository usersRepository) {
    this.postCommentsRepository = postCommentsRepository;
    this.authenticationUser = authenticationUser;
    this.postsRepository = postsRepository;
    this.usersRepository = usersRepository;
  }

  public PostCommentsErrResponse postCommentsResponse(CommentRequest commentRequest) {
    PostCommentsErrResponse postCommentsErrResponse = null;
    if (commentRequest.getText().equals("") || commentRequest.getText().length() < 10) {
      return new PostCommentsErrResponse(false,
          new PostTextResponse("Текст комментария не задан или слишком короткий"));
    }

    if (commentRequest.getPostId() == null) {
      return new PostCommentsErrResponse(false,
          new PostTextResponse("Ошибка"));
    }

    List<PostComments> commentsList = postCommentsRepository.findByPostId(commentRequest.getPostId());
    Optional<Posts> post = postsRepository.findById(commentRequest.getPostId());

    if (commentsList.size() == 0 && commentRequest.getParentId() != null || !post.isPresent()) {
      return new PostCommentsErrResponse(false,
          new PostTextResponse("Ошибка! Пост не найден"));
    }

    if (commentRequest.getParentId() != null) {
      Optional<PostComments> parentList = postCommentsRepository.findById((commentRequest.getParentId()));
      if (!parentList.isPresent()) {
        return new PostCommentsErrResponse(false,
            new PostTextResponse("Невозможно ответить на комменарий"));
      }
    }

    savePostComment(commentRequest);
    return new PostCommentsErrResponse(true, new PostTextResponse(""));
  }

  private void savePostComment(CommentRequest commentRequest) {
    PostComments postComments = new PostComments();
    postComments.setText(commentRequest.getText());

    if (commentRequest.getParentId() != null){
      postComments.setParentId((commentRequest.getParentId()));
    }

    postComments.setPost(postsRepository.findById((commentRequest.getPostId())).get());

    Users currentUsers = authenticationUser.getUser();
    postComments.setUser(usersRepository.findById(currentUsers.getId()).get());
    postComments.setTime(new Timestamp(System.currentTimeMillis()));

    postCommentsRepository.save(postComments);
  }
}
