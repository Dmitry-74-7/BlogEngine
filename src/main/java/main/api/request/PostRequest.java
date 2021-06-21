package main.api.request;

import java.sql.Timestamp;
import java.util.Collection;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PostRequest {
  private Timestamp timestamp;
  private boolean active;
  private String title;
  private Collection<String> tags;
  private String text;
}
