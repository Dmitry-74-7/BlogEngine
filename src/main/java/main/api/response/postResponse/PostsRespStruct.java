package main.api.response.postResponse;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class PostsRespStruct {
  private int id;
  private long timestamp;
  private PostRespUserStruct user;
  private String title;
  private String announce;
  private int likeCount;
  private int dislikeCount;
  private int commentCount;
  private int viewCount;

}
