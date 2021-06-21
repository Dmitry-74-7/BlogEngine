package main.controller;

import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import main.api.request.LoginRequest;
import main.api.request.PostPasswordRequest;
import main.api.request.RestoreRequest;
import main.api.request.registerRequest.Register;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.api.response.loginResponse.LoginResponse;
import main.api.response.chekResponse.Result;
import main.api.response.postPassword.PostPasswordResponse;
import main.service.CaptchaService;
import main.service.CheckService;
import main.service.LoginService;
import main.service.LogoutService;
import main.service.PostPasswordService;
import main.service.RegisterService;
import main.service.RestoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiAuthController {

  private final CheckService checkService;
  private final CaptchaService captchaService;
  private final RegisterService registerService;
  private final LoginService loginService;
  private final LogoutService logoutService;
  private final RestoreService restoreService;
  private final PostPasswordService postPasswordService;

  public ApiAuthController(CheckService checkService, CaptchaService captchaService,
      RegisterService registerService, LoginService loginService,
      LogoutService logoutService, RestoreService restoreService,
      PostPasswordService postPasswordService) {
    this.checkService = checkService;
    this.captchaService = captchaService;
    this.registerService = registerService;
    this.loginService = loginService;
    this.logoutService = logoutService;
    this.restoreService = restoreService;
    this.postPasswordService = postPasswordService;
  }

  @GetMapping("/api/auth/check")
  public ResponseEntity check(Principal principal) {
    LoginResponse checkResponse = checkService.checkResponse(principal);
    if (checkResponse == null) {
      return ResponseEntity.ok(new Result(false));
    }
    return ResponseEntity.ok(checkResponse);

  //  return ResponseEntity.ok(new Result(true));
  }

  @GetMapping("/api/auth/captcha")
  public CaptchaResponse captcha() throws IOException {
    return captchaService.getCaptcha();
  }

  @PostMapping("/api/auth/register")
  public ResponseEntity register(@RequestBody RegisterRequest registerRequest)  {
    Register register =  registerService.registerReceive(registerRequest);
    if (register == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }
    return ResponseEntity.ok(register);
  }

  @PostMapping("/api/auth/login")
  public ResponseEntity login(@RequestBody LoginRequest loginRequest)  {
    LoginResponse loginResponse = loginService.loginResponse(loginRequest);
    if (loginResponse == null) {
      return ResponseEntity.ok(new Result(false));
    }
    return ResponseEntity.ok(loginResponse);
  }

  @GetMapping("/api/auth/logout")
  //@PreAuthorize("hasAuthority('user:write')")
  public Result logout(){
    return logoutService.logOut();
  }

  @PostMapping("/api/auth/restore")
  public Result restore(@RequestBody RestoreRequest restoreRequest, HttpServletRequest request){
    return restoreService.restore(restoreRequest, request);
  }

  @PostMapping("/api/auth/password")
  public ResponseEntity pasword(@RequestBody PostPasswordRequest postPasswordRequest){
    PostPasswordResponse result = postPasswordService.postPassword(postPasswordRequest);
    if (result.isResult()) {
      return ResponseEntity.ok(new Result(true));
    }
    return ResponseEntity.ok(result);
  }

}
