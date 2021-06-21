package main.service.classes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateFormat {
  public String getDate (Timestamp time) {
    return new SimpleDateFormat("yyyy-MM-dd").format(time);
  }

  public String getYear (Timestamp time) {
    return new SimpleDateFormat("yyyy").format(time);
  }

  public String getMonth (Timestamp time) {
    return new SimpleDateFormat("MM").format(time);
  }

  public String getDay (Timestamp time) {
    return new SimpleDateFormat("dd").format(time);
  }

}
