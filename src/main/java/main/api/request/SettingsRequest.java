package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SettingsRequest {
  @JsonProperty("MULTIUSER_MODE")
  private boolean multiUserMode;
  @JsonProperty("POST_PREMODERATION")
  private boolean postPremoderation;
  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean statisticsIsPublic;

}
