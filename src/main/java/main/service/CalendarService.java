package main.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import main.api.response.CalendarResponse;
import main.model.repositories.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService extends PostService {

  private final PostsRepository postsRepository;

  @Autowired
  public CalendarService(PostsRepository postsRepository) {
    super(postsRepository);
    this.postsRepository = postsRepository;
  }

  public CalendarResponse calendarResponse (int year) {
    CalendarResponse calendarResponse = new CalendarResponse();

    List<Integer> yearsList = postsRepository.yearList();
    if (year == 0 && !yearsList.contains(year)) {
      year = LocalDateTime.now().getYear();
    }

    List<Map<String, String>> datesFromBase = postsRepository.dateList(year);
    Map<String, Integer> dates = getDateMap(datesFromBase);

    calendarResponse.setYears(yearsList);
    calendarResponse.setPosts(dates);

    return calendarResponse;
  }

  private Map<String, Integer> getDateMap (List<Map<String, String>> datesFromBase) {
    Map<String, Integer> dates = new TreeMap<>();

     datesFromBase.forEach(e -> {
       List<String> keyValue = new ArrayList<>(e.values());
       String key = keyValue.get(0);
       String value = (keyValue.get(1));
       dates.put(key,Integer.parseInt(value));
     });
     return dates;
  }


}
