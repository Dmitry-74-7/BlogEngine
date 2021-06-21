package main.api.response;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsMyResponse {
  private int postsCount;
  private int likesCount;
  private int dislikesCount;
  private int viewsCount;
  private long firstPublication;
}
