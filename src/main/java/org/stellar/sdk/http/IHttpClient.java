package org.stellar.sdk.http;

import java.io.IOException;

public interface IHttpClient extends AutoCloseable {
  StringResponse get(GetRequest request) throws IOException;

  StringResponse post(PostRequest request) throws IOException;
}
