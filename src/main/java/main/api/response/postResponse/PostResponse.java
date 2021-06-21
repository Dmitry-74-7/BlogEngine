package main.api.response.postResponse;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class PostResponse {
  private int count;
  private Collection<PostsRespStruct> posts;
}
