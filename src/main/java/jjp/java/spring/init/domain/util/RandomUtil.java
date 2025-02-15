package jjp.java.spring.init.domain.util;

import java.util.Random;

public class RandomUtil {

  public static String intNumber(int length) {
    if (length < 1 || length > 9) {
      throw new IllegalArgumentException("자릿수는 1에서 9 사이여야 합니다.");
    }
    int maxInt = (int) Math.pow(10, length);
    return String.format("%0" + length + "d", new Random().nextInt(maxInt));
  }
}
