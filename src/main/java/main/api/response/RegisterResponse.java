package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.api.request.registerRequest.Register;
import main.api.request.registerRequest.RegisterError;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse implements Register {
  private boolean result;
  private RegisterError errors;

}
