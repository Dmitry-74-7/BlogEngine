package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class SettingsResponse {
  @JsonProperty("MULTIUSER_MODE")
  private boolean multiuserMode;

  @JsonProperty("POST_PREMODERATION")
  private boolean postPremoderation;

  @JsonProperty("STATISTICS_IS_PUBLIC")
  private boolean statisticsIsPublic;
}
