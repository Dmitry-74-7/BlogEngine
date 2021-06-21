package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.github.cage.image.EffectConfig;
import com.github.cage.image.Painter;
import com.github.cage.image.Painter.Quality;
import com.github.cage.image.ScaleConfig;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import main.api.response.CaptchaResponse;
import main.model.repositories.CaptchaRepository;
import main.model.tables.CaptchaCodes;
import main.other.RandomGenerator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {
  final static String HEADER = "data:image/png;base64, ";
  @Value("${captcha.delete}")
  private int hour;

  private final CaptchaRepository captchaRepository;
  @Autowired
  public CaptchaService(CaptchaRepository captchaRepository) {
    this.captchaRepository = captchaRepository;
  }

  public CaptchaResponse getCaptcha() throws IOException {
    List<CaptchaCodes> captchaCodesList = captchaRepository.oldDate(hour);
    if (captchaCodesList.size() != 0) {
      captchaRepository.deleteAll(captchaCodesList);
    }
    GCage gCage = new GCage();
    String secretText = gCage.getTokenGenerator().next() + gCage.getTokenGenerator().next();
    String codeOfCaptcha = RandomGenerator.randomCode(5);

    Painter painter = new Painter(100, 35, Color.LIGHT_GRAY, Quality.DEFAULT, new EffectConfig(true, false, false, false,new ScaleConfig(1f,1f)), new Random());
    Cage cage = new Cage(painter, gCage.getFonts(), gCage.getForegrounds(), gCage.getFormat(), gCage.getCompressRatio(), gCage.getTokenGenerator(), new Random());

    BufferedImage image = cage.drawImage(codeOfCaptcha);

    CaptchaCodes captchaCodes = new CaptchaCodes();
    captchaCodes.setSecretCode(secretText);
    captchaCodes.setCode(codeOfCaptcha);
    captchaCodes.setTime(new Timestamp(System.currentTimeMillis()));

    captchaRepository.saveAndFlush(captchaCodes);
    return new CaptchaResponse(secretText, HEADER + getFormatBase64(image));
  }

  private String getFormatBase64 (BufferedImage image) throws IOException {

    File inputFile = new File("image.png");
    ImageIO.write(image, "png", inputFile);

    byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
    String encodedString = Base64
        .getEncoder()
        .encodeToString(fileContent);

    inputFile.delete();
    return encodedString;

  }


}
