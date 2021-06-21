package main.service;

import java.sql.Timestamp;
import java.util.List;
import main.api.request.registerRequest.Register;
import main.api.request.registerRequest.RegisterError;
import main.api.request.RegisterRequest;
import main.api.response.RegisterResponse;
import main.api.response.chekResponse.Result;
import main.config.SecurityConfig;
import main.model.repositories.CaptchaRepository;
import main.model.repositories.GlobalSettingsRepository;
import main.model.repositories.UsersRepository;
import main.model.tables.CaptchaCodes;
import main.model.tables.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

  private final UsersRepository usersRepository;
  private final CaptchaRepository captchaRepository;
  private final GlobalSettingsRepository globalSettingsRepository;

  @Autowired
  public RegisterService(UsersRepository usersRepository,
      CaptchaRepository captchaRepository,
      GlobalSettingsRepository globalSettingsRepository) {
    this.usersRepository = usersRepository;
    this.captchaRepository = captchaRepository;
    this.globalSettingsRepository = globalSettingsRepository;
  }

  public Register registerReceive(RegisterRequest registerRequest) {

    if (globalSettingsRepository.findByCode("MULTIUSER_MODE").get().getValue().equals("no")) {
      return null;
    }

    List<Users> databaseUsers = usersRepository.findEmail(registerRequest.getEmail());
    boolean emailFlag;
    boolean nameFlag;
    boolean passwordFlag;
    boolean captchaFlag;


    emailFlag = databaseUsers.size() != 0;
    nameFlag = checkName(registerRequest.getName());
    passwordFlag = checkPassword(registerRequest.getPassword());
    captchaFlag =checkCaptcha(registerRequest.getCaptchaSecret(), registerRequest.getCaptcha());

    if (emailFlag || nameFlag || passwordFlag || captchaFlag) {
      return sendRegisterError(emailFlag, nameFlag, passwordFlag , captchaFlag);
    } else {

      Users user = new Users();
      user.setName(registerRequest.getName());
      user.setEmail(registerRequest.getEmail());
      user.setPassword(SecurityConfig.passwordEncoder().encode(registerRequest.getPassword()));
      user.setCode(registerRequest.getCaptcha());
      user.setRegTime(new Timestamp(System.currentTimeMillis()));

      usersRepository.save(user);
      return new Result(true);
    }

  }

  public boolean checkName(String name) {
    String alphabet="abcdefghigklmnopqrstuwvwxyzабвгдеёжзиклмнопрстуфхцчшщэюя";
    String firstLetter = name.toLowerCase().substring(0,1);

      if (!alphabet.contains(firstLetter)) {
        return true;
    }
      return false;
  }

  public boolean checkPassword(String password) {
    if (password.length() < 6) {
      return true;
    }
    return false;
  }

  private boolean checkCaptcha(String captcha, String code) {
    List<CaptchaCodes> captchaCodesList = captchaRepository.getCodeCaptcha(captcha);

    if (captchaCodesList.size() != 0) {
      if (captchaCodesList.get(0).getCode().equals(code)) {
        return false;
      }
    }
    return true;
  }

  private RegisterResponse sendRegisterError(boolean emailFlag, boolean nameFlag,
      boolean passwordFlag, boolean captchaFlag) {
    RegisterError registerError = new RegisterError();
    if (emailFlag) {
      registerError.setEmail("Этот e-mail уже зарегистрирован");
    }

    if (nameFlag) {
      registerError.setName("Имя указано неверно");
    }

    if (passwordFlag) {
      registerError.setPassword("Пароль короче 6-ти символов");
    }

    if (captchaFlag) {
      registerError.setCaptcha("Код с картинки введён неверно");
    }

    return new RegisterResponse(false, registerError);
  }
}
