package main.other;

import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Cloudinary {
  public static String loadImage(File image, String folder, String name) {
    com.cloudinary.Cloudinary cloudinary = new com.cloudinary.Cloudinary(ObjectUtils.asMap(
        "cloud_name", "humwk6ue0",
        "api_key", "625621244394436",
        "api_secret", "MWaGMJWmm7ye_4yHYOImkAgRSN8"));
    try {
      Map params = ObjectUtils.asMap("public_id", folder + "/" + name);
      Map uploadResult = cloudinary.uploader().upload(image, params);
      return uploadResult.get("url").toString();

    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

}
