package main.service;

import java.security.Principal;
import main.api.response.loginResponse.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckService {


  private final LoginService loginService;

  @Autowired
  public CheckService(LoginService loginService) {
    this.loginService = loginService;
  }


  public LoginResponse checkResponse(Principal principal) {
    if (principal == null) {
      return null;
    }
    return loginService.getLoginResponse(principal.getName());
  }

}
