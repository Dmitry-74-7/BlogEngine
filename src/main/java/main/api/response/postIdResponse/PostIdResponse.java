package main.api.response.postIdResponse;


import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import main.api.response.postResponse.PostRespUserStruct;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class PostIdResponse {
  private int id;
  private long timestamp;
  private boolean active;
  private PostRespUserStruct user;
  private String title;
  private String text;
  private int likeCount;
  private int dislikeCount;
  private int viewCount;
  Collection<CommentsStruct> comments;
  Collection<String> tags;
}
