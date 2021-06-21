package main.service;

import java.util.Optional;
import main.api.request.LoginRequest;
import main.api.response.loginResponse.LoginResponse;
import main.api.response.loginResponse.UserLogin;
import main.model.repositories.PostsRepository;
import main.model.repositories.UsersRepository;
import main.model.tables.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
  private final UsersRepository usersRepository;
  private final AuthenticationManager authenticationManager;
  private final PostsRepository postsRepository;

  @Autowired
  public LoginService(UsersRepository usersRepository,
      AuthenticationManager authenticationManager,
      PostsRepository postsRepository) {
    this.usersRepository = usersRepository;
    this.authenticationManager = authenticationManager;
    this.postsRepository = postsRepository;
  }


  public LoginResponse loginResponse(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    User user = (User)authentication.getPrincipal();
    return getLoginResponse(user.getUsername());
  }

  public LoginResponse getLoginResponse(String email) {
    Optional<Users> currentUser = usersRepository.findByEmail(email);
    if (currentUser.isPresent()) {
      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setResult(true);
      loginResponse.setUser(getUserLogin(currentUser));
      return loginResponse;
    }
    return null;
  }

  private UserLogin getUserLogin(Optional<Users> currentUser) {
    UserLogin userLogin = new UserLogin();
    userLogin.setId(currentUser.get().getId());
    userLogin.setEmail(currentUser.get().getEmail());
    userLogin.setName(currentUser.get().getName());
    userLogin.setPhoto(currentUser.get().getPhoto());
    userLogin.setModeration(currentUser.get().isModerator());
    userLogin.setSettings(currentUser.get().isModerator());
    userLogin.setModerationCount(
        currentUser.get().isModerator() == true? postsRepository.moderationActionPosts("new").size(): 0);

    return userLogin;
  }
}
