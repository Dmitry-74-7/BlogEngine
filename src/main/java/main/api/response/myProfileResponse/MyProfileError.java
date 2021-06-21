package main.api.response.myProfileResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.request.myProfileRequest.MyProfileRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileError {
  private String name ;
  private String email;
  private String password;
  private String photo;
  private int removePhoto;

}
