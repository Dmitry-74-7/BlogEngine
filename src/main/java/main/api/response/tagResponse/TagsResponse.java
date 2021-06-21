package main.api.response.tagResponse;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class TagsResponse {
  private Collection<TagsStruct> tags;
}
