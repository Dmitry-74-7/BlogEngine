package main.controller;

import javax.servlet.http.HttpServletRequest;
import main.api.request.CommentRequest;
import main.api.request.PostModerationRequest;
import main.api.request.SettingsRequest;
import main.api.request.myProfileRequest.MyProfileRequest;
import main.api.response.CalendarResponse;
import main.api.response.ImageResponse.ImageResponse;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.StatisticsMyResponse;
import main.api.response.myProfileResponse.MyProfileErrResponse;
import main.api.response.tagResponse.TagsResponse;
import main.api.response.chekResponse.Result;
import main.api.response.postCommentResponse.PostCommentsErrResponse;
import main.api.response.postCommentResponse.PostCommentsOkResponse;
import main.service.CalendarService;
import main.service.GlobalSettingsService;
import main.service.ImageService;
import main.service.MyProfileService;
import main.service.PostCommentService;
import main.service.PostModerationService;
import main.service.SettingsService;
import main.service.StatisticsService;
import main.service.TagsService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ApiGeneralController {
  private final InitResponse initResponse;
  private final SettingsService settingsService;
  private final TagsService tagsService;
  private final CalendarService calendarService;
  private final ImageService imageService;
  private final PostCommentService postCommentService;
  private final PostModerationService postModerationService;
  private final MyProfileService myProfileService;
  private final StatisticsService statisticsService;
  private final GlobalSettingsService globalSettingsService;

  public ApiGeneralController(
      InitResponse initResponse, SettingsService settingsService,
      TagsService tagsService,
      CalendarService calendarService, ImageService imageService,
      PostCommentService postCommentService,
      PostModerationService postModerationService, MyProfileService myProfileService,
      StatisticsService statisticsService, GlobalSettingsService globalSettingsService) {
    this.initResponse = initResponse;
    this.settingsService = settingsService;
    this.tagsService = tagsService;
    this.calendarService = calendarService;
    this.imageService = imageService;
    this.postCommentService = postCommentService;
    this.postModerationService = postModerationService;
    this.myProfileService = myProfileService;
    this.statisticsService = statisticsService;
    this.globalSettingsService = globalSettingsService;
  }

  @GetMapping("/api/init")
  public InitResponse init() {
    return initResponse;
  }

  @GetMapping("/api/settings")
  //@PreAuthorize("permitAll()")
  public SettingsResponse settings() {
    return settingsService.settingsResponse();
  }

  @GetMapping("/api/tag")
  public TagsResponse tags(@RequestParam(defaultValue="") String query) {
    return tagsService.tagsResponse(query);
  }

  @GetMapping("/api/calendar")
  public CalendarResponse calendar(@RequestParam(defaultValue= "0") int year) {
    return calendarService.calendarResponse(year);
  }

  @PostMapping(value = "/api/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity image(@RequestPart MultipartFile image, HttpServletRequest request) throws FileSizeLimitExceededException {
    ImageResponse imageResponse = imageService.imageResponse(image, request);

    if (imageResponse.isResult()) {
      return ResponseEntity.ok(imageResponse.getErrors().getImage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(imageResponse);
  }

  @PostMapping("/api/comment")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity postComment(@RequestBody CommentRequest commentRequest) {
    PostCommentsErrResponse result = postCommentService.postCommentsResponse(commentRequest);
    if (result.isResult()) {
      return ResponseEntity.ok(new PostCommentsOkResponse(345));
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
  }

  @PostMapping("/api/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public Result postModeration(@RequestBody PostModerationRequest moderationRequest) {
    return postModerationService.postModerationResponse(moderationRequest);
  }


  @PostMapping(value = "/api/profile/my", consumes = "multipart/form-data")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity myProfile(@RequestParam MultipartFile photo,
       @RequestParam String name, @RequestParam String email, @RequestParam int removePhoto,
      @RequestParam(defaultValue = "") String password, HttpServletRequest request) {
    MyProfileRequest requestStruct = new MyProfileRequest(name,email,password,photo, removePhoto);

    MyProfileErrResponse result = myProfileService.myProfileResponse(requestStruct, request);
    if (result.isResult()) {
      return ResponseEntity.ok(new Result(true));
    }
     return ResponseEntity.ok(result);
  }

  @PostMapping(value = "/api/profile/my", consumes = "application/json")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity myProfile(@RequestBody MyProfileRequest myProfileRequest, HttpServletRequest request) {

    MyProfileErrResponse result = myProfileService.myProfileResponse(myProfileRequest, request);
    if (result.isResult()) {
      return ResponseEntity.ok(new Result(true));
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping("/api/statistics/my")
  @PreAuthorize("hasAuthority('user:write')")
  public StatisticsMyResponse statisticsMy () {
    return statisticsService.statisticsMy();
  }

  @GetMapping("/api/statistics/all")
  public ResponseEntity statisticsAll () {
    StatisticsMyResponse statistics = statisticsService.statisticsAll();
    if (statistics == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    return ResponseEntity.ok(statistics);
  }

  @PutMapping("/api/settings")
  @PreAuthorize("hasAuthority('user:moderate')")
  public void settings(@RequestBody SettingsRequest settingsRequest) {
    globalSettingsService.globalSettings(settingsRequest);
  }
}
