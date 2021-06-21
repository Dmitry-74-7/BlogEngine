package main.service;

import java.util.Collection;
import java.util.List;
import main.api.response.postResponse.PostResponse;
import main.api.response.postResponse.PostsRespStruct;
import main.model.repositories.PostsRepository;
import main.model.tables.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ByDateService extends PostService{

  private final PostsRepository postsRepository;

  @Autowired
  public ByDateService(PostsRepository postsRepository) {
    super(postsRepository);
    this.postsRepository = postsRepository;
  }

  public PostResponse byDateResponse (int offset, int limit, String date) {
    PostResponse postResponse = new PostResponse();

    List<Posts> posts = postsRepository.postsDate(PageRequest.of(offset, limit,Sort.by("time").descending()),date);
    Collection<PostsRespStruct> postsRespStructs = getPostRespStruct(posts);

    postResponse.setCount(posts.size());
    postResponse.setPosts(postsRespStructs);

    return postResponse;
  }
}
