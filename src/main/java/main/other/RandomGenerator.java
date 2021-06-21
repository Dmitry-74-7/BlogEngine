package main.other;

public class RandomGenerator {
  public static String randomCode (int countSymbol) {
    String symbol = "abcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < countSymbol; i++) {
      int random = (int)Math.round(Math.random() * (symbol.length() - 1));
      stringBuilder.append(symbol.charAt(random));
    }
    return stringBuilder.toString();
  }
}
