package main.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import main.api.response.ImageResponse.ImageResponse;
import main.api.response.ImageResponse.ErrorImage;
import main.other.Cloudinary;
import main.other.GeneratorPathAndFile;
import main.other.RandomGenerator;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

  public ImageResponse imageResponse(MultipartFile image, HttpServletRequest request) {
    String str = image.getOriginalFilename();
    String type = str.substring(str.indexOf('.') + 1);
    if (!(type.equals("png") || type.equals("jpg"))) {
      return new ImageResponse(false, new ErrorImage("Формат не соответствует ни jpg, ни png"));
    }

    String pathImage = createFile(image,type, request);

    if (pathImage.equals("")) {
      return new ImageResponse(false, new ErrorImage("Ошибка"));
    }
    return new ImageResponse(true, new ErrorImage(pathImage));
  }

  private String createFile (MultipartFile image, String type, HttpServletRequest request) {

    String path =System.getProperty("user.dir") + "/src/main/resources/" + RandomGenerator.randomCode(5) + "." + type;
  //  String realPath = request.getServletContext().getRealPath(path.substring(0,path.lastIndexOf("/")));

//    try {
//      Files.createDirectories(Paths.get(path.substring(0,path.lastIndexOf("/"))));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    File inputFile = new File(path);
    try{
       FileUtils.writeByteArrayToFile(inputFile, image.getBytes());
    //  ImageIO.write(image, type, inputFile);
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }

//    String fullURL = request.getRequestURL().toString();
//    String localHost = fullURL.substring(0,fullURL.lastIndexOf(request.getRequestURI()));
    String url = Cloudinary.loadImage(inputFile,"images", GeneratorPathAndFile.generatePath("upload/"));
    inputFile.delete();
    return url;
  }
}
