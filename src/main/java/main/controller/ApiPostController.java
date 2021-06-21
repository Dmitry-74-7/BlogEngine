package main.controller;

import main.api.request.PostRequest;
import main.api.request.LikeDislikeRequest;
import main.api.request.SettingsRequest;
import main.api.response.postIdResponse.PostErrorResponse;
import main.api.response.postIdResponse.PostIdResponse;
import main.api.response.postResponse.PostResponse;
import main.api.response.chekResponse.Result;
import main.service.AddPostService;
import main.service.ByDateService;
import main.service.ByTagService;
import main.service.EditPostService;
import main.service.GlobalSettingsService;
import main.service.LikeDislikeService;
import main.service.ModerationService;
import main.service.PostIdService;
import main.service.PostMyService;
import main.service.PostService;
import main.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPostController {

  private final PostService postService;
  private final SearchService searchService;
  private final ByDateService byDateService;
  private final ByTagService byTagService;
  private final PostIdService postIdService;
  private final ModerationService moderationService;
  private final PostMyService postMyService;
  private final AddPostService addPostService;
  private final EditPostService editPostService;
  private final LikeDislikeService likeDislikeService;

  @Autowired
  public ApiPostController(PostService postService, SearchService searchService,
      ByDateService byDateService, ByTagService byTagService,
      PostIdService postIdService, ModerationService moderationService,
      PostMyService postMyService, AddPostService addPostService,
      EditPostService editPostService, LikeDislikeService likeDislikeService) {
    this.postService = postService;
    this.searchService = searchService;
    this.byDateService = byDateService;
    this.byTagService = byTagService;
    this.postIdService = postIdService;
    this.moderationService = moderationService;
    this.postMyService = postMyService;
    this.addPostService = addPostService;
    this.editPostService = editPostService;
    this.likeDislikeService = likeDislikeService;
  }

  @GetMapping("/api/post")
 // @PreAuthorize("hasAuthority('user:write')")
  public PostResponse post(@RequestParam(defaultValue="0") Integer offset,
      @RequestParam(defaultValue="10") Integer limit,
      @RequestParam(defaultValue="recent") String mode) {

    return postService.postResponse(offset/limit, limit, mode);
  }

  @GetMapping("/api/post/search")
 // @PreAuthorize("hasAuthority('user:moderate')")
  public PostResponse search(@RequestParam(defaultValue="0") Integer offset,
      @RequestParam(defaultValue="10") Integer limit,
      @RequestParam(defaultValue="") String query) {
    return searchService.searchResponse(offset/limit, limit, query);
  }

  @GetMapping("/api/post/byDate")
  public PostResponse byDate(@RequestParam(defaultValue="0") Integer offset,
      @RequestParam(defaultValue="10") Integer limit,
      @RequestParam(defaultValue="") String date) {
    return byDateService.byDateResponse(offset/limit, limit, date);
  }

  @GetMapping("/api/post/byTag")
  public PostResponse byTag(@RequestParam(defaultValue="0") Integer offset,
      @RequestParam(defaultValue="10") Integer limit,
      @RequestParam(defaultValue="") String tag) {
    return byTagService.byTagResponse(offset/limit, limit, tag);
  }

  @GetMapping("/api/post/{id}")
  public ResponseEntity postId (@PathVariable int id) {
      PostIdResponse postIdResponse = postIdService.postIdResponse(id);
      if (postIdResponse == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    return new ResponseEntity(postIdResponse, HttpStatus.OK);
  }

  @GetMapping("/api/post/moderation")
  @PreAuthorize("hasAuthority('user:moderate')")
  public PostResponse moderation(@RequestParam(defaultValue="0") Integer offset,
      @RequestParam(defaultValue="10") Integer limit,
      @RequestParam(defaultValue="accepted") String status) {

    return moderationService.moderationResponse(offset/limit, limit, status);
  }

  @GetMapping("/api/post/my")
  @PreAuthorize("hasAuthority('user:write')")
  public PostResponse myPosts(@RequestParam(defaultValue="0") Integer offset,
      @RequestParam(defaultValue="10") Integer limit,
      @RequestParam(defaultValue="accepted") String status) {

    return postMyService.postMyResponse(offset/limit, limit, status);
  }

  @PostMapping("/api/post")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity addPost(@RequestBody PostRequest postRequest) {
    PostErrorResponse result = addPostService.addPost(postRequest);
    if (result == null) {
      return ResponseEntity.ok(new Result(true));
    }
    return ResponseEntity.ok(result);
  }

  @PutMapping("/api/post/{id}")
  @PreAuthorize("hasAuthority('user:write')")
  public ResponseEntity editPost(@RequestBody PostRequest postRequest, @PathVariable int id) {
    PostErrorResponse result = editPostService.editPost(postRequest, id);
    if (result == null) {
      return ResponseEntity.ok(new Result(true));
    }
    return ResponseEntity.ok(result);
  }

  @PostMapping("/api/post/like")
  @PreAuthorize("hasAuthority('user:write')")
  public Result like(@RequestBody LikeDislikeRequest likeDislikeRequest) {
    return likeDislikeService.likeDislike(likeDislikeRequest, 1);
  }

  @PostMapping("/api/post/dislike")
  @PreAuthorize("hasAuthority('user:write')")
  public Result dislike(@RequestBody LikeDislikeRequest likeDislikeRequest) {
    return likeDislikeService.likeDislike(likeDislikeRequest, -1);
  }

}

