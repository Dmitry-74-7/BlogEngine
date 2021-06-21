package main.api.response.postIdResponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class CommentsStruct  {
  int id;
  long timestamp;
  String text;
  PostRespUserStructId user;
}
