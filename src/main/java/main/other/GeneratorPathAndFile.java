package main.other;

import java.util.Random;

public class GeneratorPathAndFile {
  public static String generatePath (String folder) {
    String randomDigits = Double.toString(new Random().nextDouble());
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(folder);
    stringBuilder.append(getFolders(randomDigits.substring(2, 4)) + "/");
    stringBuilder.append(getFolders(randomDigits.substring(5, 7)) + "/");
    stringBuilder.append(getFolders(randomDigits.substring(8, 10)) + "/");
    stringBuilder.append(randomDigits.substring(11));
    return stringBuilder.toString();
  }

  private static String getFolders (String randomDigits) {
    String alphabet = "abcdefghij";
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < randomDigits.length(); i++) {
      String digit = Character.toString(randomDigits.charAt(i));
      stringBuilder.append(alphabet.charAt(Integer.parseInt(digit)));
    }
    return stringBuilder.toString();
  }

}
