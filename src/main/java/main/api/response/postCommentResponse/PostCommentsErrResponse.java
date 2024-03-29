package main.api.response.postCommentResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentsErrResponse {
  private boolean result;
  private PostTextResponse errors;
}
