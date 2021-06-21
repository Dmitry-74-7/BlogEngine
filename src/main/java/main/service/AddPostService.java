package main.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import main.api.request.PostRequest;
import main.api.response.postIdResponse.PostErrorResponse;
import main.api.response.postResponse.ErrorPost;
import main.model.repositories.GlobalSettingsRepository;
import main.model.repositories.PostsRepository;
import main.model.repositories.Tag2postRepository;
import main.model.repositories.TagsRepository;
import main.model.repositories.UsersRepository;
import main.model.tables.ModerationStatus;
import main.model.tables.Posts;
import main.model.tables.Tag2post;
import main.model.tables.Tags;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddPostService {
  private final PostsRepository postsRepository;
  private final UsersRepository usersRepository;
  private final AuthenticationUser authenticationUser;
  private final GlobalSettingsRepository globalSettingsRepository;
  private final TagsRepository tagsRepository;
  private final Tag2postRepository tag2postRepository;

  @Autowired
  public AddPostService(PostsRepository postsRepository,
      UsersRepository usersRepository, AuthenticationUser authenticationUser,
      GlobalSettingsRepository globalSettingsRepository,
      TagsRepository tagsRepository, Tag2postRepository tag2postRepository) {
    this.postsRepository = postsRepository;
    this.usersRepository = usersRepository;
    this.authenticationUser = authenticationUser;
    this.globalSettingsRepository = globalSettingsRepository;
    this.tagsRepository = tagsRepository;
    this.tag2postRepository = tag2postRepository;
  }

  public PostErrorResponse addPost (PostRequest postRequest) {
    PostErrorResponse postErrorResponse = checkRequest(postRequest);
    if (postErrorResponse == null) {
      postsRepository.save(createPost(postRequest));
      tagDependence(postRequest);
    }
    return postErrorResponse;
  }

  public PostErrorResponse checkRequest (PostRequest postRequest) {
    PostErrorResponse postErrorResponse = new PostErrorResponse();
    ErrorPost errorPost = new ErrorPost();
    postErrorResponse.setResult(true);
    if (postRequest.getTitle().length() < 3) {
      errorPost.setTitle("Заголовок не установлен");
      postErrorResponse.setResult(false);
    }

    Optional post = postsRepository.findPostByTitleAndText(postRequest.getTitle(),
        postRequest.getText());
    if (post.isPresent() && ((Posts) post.get()).isActive() == postRequest.isActive()){
      errorPost.setTitle("Ошибка! Такой пост уже существует!");
      postErrorResponse.setResult(false);
    }

    if (postRequest.getText().length() < 50) {
      errorPost.setText("Текст публикации сильно короткий");
      postErrorResponse.setResult(false);
    }

    if (!postErrorResponse.isResult()) {
      postErrorResponse.setErrors(errorPost);
      return postErrorResponse;
    }
    return null;
  }

  private Posts createPost (PostRequest postRequest) {
    Posts post = new Posts();
    post.setActive(postRequest.isActive());
    post.setTitle(postRequest.getTitle());
    post.setText(postRequest.getText());
    post.setTags(getTags(postRequest.getTags()));
    if (postRequest.getTimestamp().compareTo(new Timestamp(System.currentTimeMillis())) > 0) {
      post.setTime(postRequest.getTimestamp());
    } else {
      post.setTime(new Timestamp(System.currentTimeMillis()));
    }

    post.setUser(authenticationUser.getUser());

    if (globalSettingsRepository.findByCode("POST_PREMODERATION").get().getValue().equals("yes")) {
      post.setModerationStatus(ModerationStatus.NEW);
    } else {
      post.setModerationStatus(ModerationStatus.ACCEPTED);
    }

    return post;
  }

  public List<Tags> getTags (Collection<String> tags) {
    if (tags.size() == 0) {
      return new ArrayList<>();
    }

    List<Tags> tagsList = new ArrayList<>();
    for (String item: tags) {
      Tags tag = new Tags();
      tag.setName(item);
      List<Tags> tagsListQuery = tagsRepository.tagsListQuery(item);

      if (tagsListQuery.size() == 0) {
        tagsRepository.save(tag);
      }
    }
    return tagsList;
  }

  public void tagDependence(PostRequest postRequest) {

    int postId = postsRepository.findPostByTitleAndText(postRequest.getTitle(), postRequest.getText()).get().getId();
    for (String item: postRequest.getTags()) {
      int tagId = tagsRepository.tagsListQuery(item).get(0).getId();
      Optional<Tag2post> tag = tag2postRepository.findPostTag(postId, tagId);
      if (tag.isPresent()) {
        continue;
      }
      Tag2post tag2post = new Tag2post();

      tag2post.setPostId(postId);
      tag2post.setTagId(tagId);
      tag2postRepository.save(tag2post);
    }
  }

}