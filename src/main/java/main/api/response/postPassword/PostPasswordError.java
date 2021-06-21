package main.api.response.postPassword;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostPasswordError{
  private String code;
  private String password;
  private String captcha;
}
