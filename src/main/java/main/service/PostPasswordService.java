package main.service;

import java.util.List;
import java.util.Optional;
import main.api.request.PostPasswordRequest;
import main.api.response.postPassword.PostPasswordError;
import main.api.response.postPassword.PostPasswordResponse;
import main.config.SecurityConfig;
import main.model.repositories.CaptchaRepository;
import main.model.repositories.UsersRepository;
import main.model.tables.CaptchaCodes;
import main.model.tables.Users;
import org.springframework.stereotype.Service;

@Service
public class PostPasswordService {
  private final UsersRepository usersRepository;
  private final CaptchaRepository captchaRepository;

  public PostPasswordService(UsersRepository usersRepository,
      CaptchaRepository captchaRepository) {
    this.usersRepository = usersRepository;
    this.captchaRepository = captchaRepository;
  }

  public PostPasswordResponse postPassword(PostPasswordRequest request) {
    PostPasswordResponse postPasswordResponse= new PostPasswordResponse();
    postPasswordResponse.setResult(true);
    PostPasswordError error = new PostPasswordError();


    if (request.getPassword().length() < 6) {
      postPasswordResponse.setResult(false);
      error.setPassword("Пароль меньше 6 символов");
    }

    if (checkCode(request.getCode())) {
      postPasswordResponse.setResult(false);
      error.setCode( "Ссылка для восстановления пароля устарела. <a href= \"/login/restore-password\">Запросить ссылку снова</a>");
    }

    if (!request.getCaptcha().equals(codeCaptcha(request.getCaptchaSecret()))) {
      postPasswordResponse.setResult(false);
      error.setCaptcha("Код с картинки введён неверно");
    }

    postPasswordResponse.setErrors(error);

    if (postPasswordResponse.isResult()) {
      Optional<Users> user = usersRepository.findByCode(request.getCode());
      Users currentUser = user.get();
      currentUser.setPassword(SecurityConfig.passwordEncoder().encode(request.getPassword()));
      usersRepository.save(currentUser);
    }
    return postPasswordResponse;
  }

  private boolean checkCode(String code) {
    Optional<Users> user = usersRepository.findByCode(code) ;
    if (user.isPresent()) {
      return false;
    }
    return true;
  }

  private String codeCaptcha(String secretCode) {
    List<CaptchaCodes> code = captchaRepository.getCodeCaptcha(secretCode);
    if (code.size() == 0) {
      return "";
    }
    return  code.get(0).getCode();
  }

}
