package main.api.response.postIdResponse;

import lombok.Data;
import main.api.response.postResponse.ErrorPost;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostErrorResponse {
  private boolean result;
  private ErrorPost errors;
}
