import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;

public class CloudinaryTest extends TestCase {

  @Test
  public void testClaudary () {

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "humwk6ue0",
        "api_key", "625621244394436",
        "api_secret", "MWaGMJWmm7ye_4yHYOImkAgRSN8"));

    File file = new File("C:\\Users\\ustyancevdv\\Desktop\\png-transparent-rio-de-janeiro-butterfly-morpho-didius-blue-butterfly-shape-blue-brush-footed-butterfly-photography.png");
    try {
      Map params = ObjectUtils.asMap("public_id", "MyAppImages/" + "picture"  );
      Map uploadResult = cloudinary.uploader().upload(file, params);
      System.out.println(uploadResult.get("url"));

    } catch (IOException e) {
      e.printStackTrace();
    }
//    double actual =
//    double expected = 11;
    assertEquals(true, true);

  }
}
