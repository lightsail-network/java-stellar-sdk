package org.stellar.sdk;

import java.net.URI;
import java.net.URISyntaxException;
import org.stellar.sdk.exception.UnexpectedException;

public class UriUtil {
  /**
   * Convenience method for parsing a URI string and handling a malformed URI with a helpful error
   * message.
   *
   * @param uriString the URI string
   * @return a URI object
   */
  public static URI toUri(String uriString) {
    try {
      return new URI(uriString);
    } catch (URISyntaxException e) {
      throw new UnexpectedException("Bad URI: " + uriString);
    }
  }
}
