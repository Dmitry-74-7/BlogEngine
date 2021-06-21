package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LoginRequest {
  @JsonProperty("e_mail")
  private String email;
  private String password;
}
