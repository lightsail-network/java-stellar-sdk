package org.stellar.sdk.responses;

import java.io.IOException;

public class ClientProtocolException extends IOException {

  public ClientProtocolException() {
    super();
  }

  public ClientProtocolException(String s) {
    super(s);
  }

  public ClientProtocolException(Throwable cause) {
    initCause(cause);
  }

  public ClientProtocolException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }


}