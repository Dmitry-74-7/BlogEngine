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
public class SearchService extends PostService{


  private final PostsRepository postsRepository;

  @Autowired
  public SearchService(PostsRepository postsRepository) {
    super(postsRepository);
    this.postsRepository = postsRepository;
  }

  public PostResponse searchResponse (int offset, int limit, String query) {
    PostResponse postResponse = new PostResponse();
    Collection<PostsRespStruct> collectionPosts;

    List<Posts> posts ;
    posts = postsRepository.searchPosts(PageRequest.of(offset, limit, Sort.by("time").descending()), query);;
    if (posts.size() == 0) {
      posts = postsRepository.findPosts(PageRequest.of(offset, limit, Sort.by("time").descending()));
    }

      collectionPosts =  getPostRespStruct(posts);
      postResponse.setCount(collectionPosts.size());
      postResponse.setPosts(collectionPosts);
      return postResponse;
  }
}
