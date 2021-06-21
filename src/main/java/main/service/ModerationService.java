package main.service;


import java.util.Collection;
import java.util.List;
import main.api.response.postResponse.PostResponse;
import main.api.response.postResponse.PostsRespStruct;
import main.model.repositories.PostsRepository;
import main.model.tables.Posts;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ModerationService extends PostService{


  private final PostsRepository postsRepository;
  private final AuthenticationUser authenticationUser;

  @Autowired
  public ModerationService(PostsRepository postsRepository,
      AuthenticationUser authenticationUser) {
    super(postsRepository);
    this.postsRepository = postsRepository;
    this.authenticationUser = authenticationUser;
  }

  public PostResponse moderationResponse(int offset, int limit, String status) {

    PostResponse postResponse = new PostResponse();
    Collection<PostsRespStruct> collectionPosts;
    List<Posts> posts;

    Users currentUser = authenticationUser.getUser();

    if (status.toLowerCase().equals("new")) {
      posts = postsRepository.moderationActionPosts(
          PageRequest.of(offset, limit, Sort.by("time").descending()), status);
    } else {
       posts = postsRepository.moderationActionPosts(
          PageRequest.of(offset, limit, Sort.by("time").descending()), status,
          currentUser.getId());
    }

    collectionPosts = getPostRespStruct(posts);
    postResponse.setCount(collectionPosts.size());
    postResponse.setPosts(collectionPosts);
    return postResponse;
  }
}
