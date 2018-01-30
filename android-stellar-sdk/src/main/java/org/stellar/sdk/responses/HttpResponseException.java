package org.stellar.sdk.responses;


public class HttpResponseException extends ClientProtocolException {

  private final int statusCode;

  public HttpResponseException(int statusCode, final String s) {
    super(s);
    this.statusCode = statusCode;
  }
  public int getStatusCode() {
    return this.statusCode;
  }

}
