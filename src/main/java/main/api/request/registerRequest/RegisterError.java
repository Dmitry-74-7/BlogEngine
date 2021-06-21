package main.api.request.registerRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterError implements Register {
  private String email;
  private String password;
  private String name;
  private String captcha;
}
