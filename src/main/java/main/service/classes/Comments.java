package main.service.classes;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comments implements Serializable {
  private int commentId;
  private long timestamp;
  private String text;
  private int userId;
  private String userName;
  private String userPhoto;

}
