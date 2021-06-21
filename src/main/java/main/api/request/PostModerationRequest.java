package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostModerationRequest {
  @JsonProperty("post_id")
  private int id;
  private String decision;
}
