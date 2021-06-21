package main.api.request;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RestoreRequest {
  private String email;
}
