package main.api.response.loginResponse;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserLogin
{
  private int id;
  private String name;
  private String photo;
  private String email;
  private boolean moderation;
  private int moderationCount;
  private boolean settings;

}
