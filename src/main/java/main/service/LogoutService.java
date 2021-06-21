package main.service;

import main.api.response.chekResponse.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

  public Result logOut() {
    SecurityContextHolder.clearContext();
    return new Result(true);
  }
}
