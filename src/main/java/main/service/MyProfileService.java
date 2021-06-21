package main.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import main.api.request.myProfileRequest.MyProfileRequest;
import main.api.response.myProfileResponse.MyProfileErrResponse;
import main.api.response.myProfileResponse.MyProfileError;
import main.config.SecurityConfig;
import main.model.repositories.UsersRepository;
import main.model.tables.Users;
import main.other.AuthenticationUser;
import main.other.Cloudinary;
import main.other.GeneratorPathAndFile;
import main.other.RandomGenerator;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MyProfileService {
  private final UsersRepository usersRepository;
  private final RegisterService registerService;
  private final AuthenticationUser authenticationUser;

  @Autowired
  public MyProfileService(UsersRepository usersRepository, RegisterService registerService,
      AuthenticationUser authenticationUser) {
    this.usersRepository = usersRepository;
    this.registerService = registerService;
    this.authenticationUser = authenticationUser;
  }

  public MyProfileErrResponse myProfileResponse(MyProfileRequest myProfileRequest, HttpServletRequest request) {

    String email = "Этот e-mail уже зарегистрирован";
    String  photo = "Фото слишком большое, нужно не более 5 Мб";
    String  name = "Имя указано неверно";
    String password = "Пароль короче 6-ти символов";
    MyProfileErrResponse response = new MyProfileErrResponse();
    MyProfileError errors = new MyProfileError();
    response.setResult(true);

    Users currentUser = authenticationUser.getUser();

    if (!myProfileRequest.getEmail().equals(currentUser.getEmail()) &&
        chekEmail(myProfileRequest.getEmail())) {

      response.setResult(false);
      errors.setEmail(email);
    }

    if (registerService.checkName(myProfileRequest.getName())) {
      response.setResult(false);
      errors.setName(name);
    }

    if (myProfileRequest.getPassword() != null && myProfileRequest.getPassword().length() < 6 && myProfileRequest.getPassword().length() !=0 ) {
      response.setResult(false);
      errors.setPassword(password);
    }

    if (!response.isResult()) {
      return response;
    }

    currentUser.setEmail(myProfileRequest.getEmail());
    currentUser.setName(myProfileRequest.getName());

    if (myProfileRequest.getPassword() != null && myProfileRequest.getPassword().length() !=0) {
      currentUser.setPassword(SecurityConfig.passwordEncoder().encode(myProfileRequest.getPassword()));
    }
    if (myProfileRequest.getRemovePhoto() == 1) {
      currentUser.setPhoto("");
    }

    if (myProfileRequest.getPassword() != null && myProfileRequest.getRemovePhoto() == 0 && !myProfileRequest.getPhoto().equals("")) {
        String image = getImage(myProfileRequest.getPhoto(), currentUser.getEmail(), request);
       currentUser.setPhoto(image);
    }

    usersRepository.save(currentUser);
    return response;
  }

  private boolean chekEmail(String email) {
    Optional<Users> currentUser = usersRepository.findByEmail(email);
    if (currentUser.isPresent()) {
      return true;
    }
    return false;
  }

  private String getImage(MultipartFile image, String email, HttpServletRequest request)  {

    String imageStr = image.getOriginalFilename();
    String type = imageStr.substring(imageStr.indexOf('.') + 1);
    try {
      BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

      int height = bufferedImage.getHeight();
      int width = bufferedImage.getWidth();
      int minSize = Math.min(height,width);

      if (minSize == width) {
        bufferedImage = Scalr.crop(bufferedImage, 0, (height - minSize)/2, minSize, minSize);
      } else {
        bufferedImage = Scalr.crop(bufferedImage, (width - minSize)/2, 0, minSize, minSize);
      }
      BufferedImage newImage = Scalr.resize(bufferedImage, 35, 35);

      String path =System.getProperty("user.dir") + "/src/main/resources/" + RandomGenerator.randomCode(5) + "." + type;
   //   String realPath = request.getServletContext().getRealPath(path);

//      try {
//        Files.createDirectories(Paths.get(path.substring(0,path.lastIndexOf("/"))));
//
//      } catch (IOException e) {
//        e.printStackTrace();
//      }

      File file = new File(path);
      ImageIO.write(newImage, type, file);

      String url = Cloudinary.loadImage(file,"avatars", GeneratorPathAndFile.generatePath("upload/"));
      file.delete();
      return url;

    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }

  }

}
