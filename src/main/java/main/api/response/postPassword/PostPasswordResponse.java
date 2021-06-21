package main.api.response.postPassword;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostPasswordResponse {
  private boolean result;
  private PostPasswordError errors;
}
