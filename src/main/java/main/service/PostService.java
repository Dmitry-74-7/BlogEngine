package main.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import main.service.classes.LikeDislike;
import main.api.response.postResponse.PostResponse;
import main.api.response.postResponse.PostRespUserStruct;
import main.api.response.postResponse.PostsRespStruct;
import main.model.tables.PostVotes;
import main.model.tables.Posts;
import main.model.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostsRepository postsRepository;

  @Autowired
  public PostService(PostsRepository postsRepository) {
    this.postsRepository = postsRepository;
  }

  public PostResponse postResponse(int offset, int limit, String mode) {
    PostResponse postResponse = new PostResponse();

    postResponse.setCount(postsRepository.countPosts());
    Collection<PostsRespStruct> collection = getPostRespStruct(getSortPost( offset,  limit,  mode));
    postResponse.setPosts(collection);
    return postResponse;
  }

  public List<Posts> getPosts() {
    List<Posts> list = postsRepository.findPosts(Sort.by("time").descending());
    return list;
  }

  private List<Posts> getSortPost(Integer offset, Integer limit, String mode) {
    switch (mode) {
      case "recent":
        return postsRepository.findPosts(PageRequest.of(offset, limit, Sort.by("time").descending()));
      case "popular":
        return  postsRepository.findPosts(PageRequest.of(offset, limit, Sort.by("postComments.size").descending()));
      case "best":
        return  postsRepository.likePosts();
      case "early":
        return postsRepository.findPosts(PageRequest.of(offset, limit, Sort.by("time")));
    }
    return new ArrayList<>();
  }


  public Collection<PostsRespStruct> getPostRespStruct (List<Posts> posts) {
    List<PostsRespStruct> postsRespStructList = new ArrayList<>();

    for (Posts post: posts) {
      PostsRespStruct postsRespStruct = new PostsRespStruct();
      postsRespStruct.setId(post.getId());
      postsRespStruct.setTimestamp(post.getTime().getTime() / 1000);
      int userId = post.getUser().getId();
      String userName = post.getUser().getName();
      postsRespStruct.setUser(new PostRespUserStruct(userId, userName));

      postsRespStruct.setTitle(post.getTitle());
      postsRespStruct.setAnnounce(announce(post.getText()));

      LikeDislike likeDislike = likeDislikeCount(post);
      postsRespStruct.setLikeCount(likeDislike.getLike());
      postsRespStruct.setDislikeCount(likeDislike.getDislike());

      postsRespStruct.setCommentCount(post.getPostComments().size());
      postsRespStruct.setViewCount(post.getViewCount());

      postsRespStructList.add(postsRespStruct);
    }
    return postsRespStructList;
  }

  private String announce (String str) {
    String newStr = str.replaceAll("<.+?>", "");
    if (newStr.length() > 150) {
      return newStr.substring(0, 150) + "...";
    }
    return newStr;
  }

  public LikeDislike likeDislikeCount (Posts post) {
    List<PostVotes> postVotesList = post.getPostVotes();
    LikeDislike likeDislike = new LikeDislike();

    postVotesList.forEach(e -> {
      if (e.getValue() == 1) {
         likeDislike.addLike();
      }
      if (e. getValue() == -1) {
        likeDislike.addDislike();
      }
    });
    return likeDislike;
  }
}
