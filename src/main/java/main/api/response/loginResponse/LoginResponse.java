package main.api.response.loginResponse;

import java.io.Serializable;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class LoginResponse implements Serializable {
  private boolean result;
  private UserLogin user;
}
