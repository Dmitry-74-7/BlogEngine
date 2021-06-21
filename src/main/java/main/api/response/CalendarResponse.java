package main.api.response;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class CalendarResponse {
  Collection<Integer> years;
  Map<String, Integer> posts;


}
