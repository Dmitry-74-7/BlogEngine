package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class CommentRequest {
  @JsonProperty("parent_id")
  private Integer parentId;
  @JsonProperty("post_id")
  private Integer postId;
  private String text;
}
