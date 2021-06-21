package main.api.response.myProfileResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileErrResponse {
  private boolean result;
  private MyProfileError errors;
}
