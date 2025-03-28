package org.stellar.sdk;

import java.util.List;

public class StringUtil {
  public static boolean isBlank(String s) {
    return s == null || s.isBlank();
  }

  public static String join(List<String> strings, String separator) {
    return strings == null || strings.isEmpty() ? "" : String.join(separator, strings);
  }
}
