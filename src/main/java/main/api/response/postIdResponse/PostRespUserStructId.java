package main.api.response.postIdResponse;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.api.response.postResponse.PostRespUserStruct;
import org.springframework.stereotype.Component;


@Component
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRespUserStructId extends PostRespUserStruct implements Serializable {
  String photo;

  public PostRespUserStructId(int id, String name, String photo) {
    super(id, name);
    this.photo = photo;
  }
}
