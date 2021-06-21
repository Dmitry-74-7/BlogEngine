package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostPasswordRequest {
  private String code;
  private String password;
  private String captcha;
  @JsonProperty("captcha_secret")
  private String captchaSecret;
}
