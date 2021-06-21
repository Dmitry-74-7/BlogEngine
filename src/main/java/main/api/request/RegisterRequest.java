package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import main.api.request.registerRequest.Register;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class RegisterRequest implements Register {
  @JsonProperty("e_mail")
  private String email;
  private String password;
  private String name;
  private String captcha;

  @JsonProperty("captcha_secret")
  private String captchaSecret;
}
