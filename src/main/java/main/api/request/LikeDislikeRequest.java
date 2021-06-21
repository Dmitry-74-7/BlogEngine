package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LikeDislikeRequest {
  @JsonProperty("post_id")
  private int postId;
}
