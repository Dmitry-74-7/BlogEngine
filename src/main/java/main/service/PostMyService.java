package main.service;

import java.util.ArrayList;
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
public class PostMyService extends PostService {
  private final AuthenticationUser authenticationUser;
  private final PostsRepository postsRepository;

  @Autowired
  public PostMyService(AuthenticationUser authenticationUser, PostsRepository postsRepository) {
    super(postsRepository);
    this.authenticationUser = authenticationUser;
    this.postsRepository = postsRepository;
  }

  public PostResponse postMyResponse (int offset, int limit, String status) {

    PostResponse postResponse = new PostResponse();
    Collection<PostsRespStruct> collectionPosts;
    List<Posts> posts = new ArrayList<>();

    Users currentUser = authenticationUser.getUser();

    switch (status.toLowerCase()) {
      case "inactive":
        posts = postsRepository.findMyNoActionPosts(
            PageRequest.of(offset, limit, Sort.by("time").descending()),
            currentUser.getId());
        break;

      case "pending":
        posts = postsRepository.findMyActionPosts(
            PageRequest.of(offset, limit, Sort.by("time").descending()), "NEW",
            currentUser.getId());
        break;

      case "declined":
        posts = postsRepository.findMyActionPosts(
            PageRequest.of(offset, limit, Sort.by("time").descending()), "DECLINED",
            currentUser.getId());
        break;

      case "published":
        posts = postsRepository.findMyActionPosts(
            PageRequest.of(offset, limit, Sort.by("time").descending()), "ACCEPTED",
            currentUser.getId());
        break;
    }

    collectionPosts = getPostRespStruct(posts);
    postResponse.setCount(collectionPosts.size());
    postResponse.setPosts(collectionPosts);
    return postResponse;
  }

}
