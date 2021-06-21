package main.api.request.myProfileRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileRequest {
  private String name ;
  private String email;
  private String password;
  private MultipartFile photo;
  private int removePhoto;

}
