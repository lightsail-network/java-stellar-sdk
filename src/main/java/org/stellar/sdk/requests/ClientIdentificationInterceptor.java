package org.stellar.sdk.requests;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.Util;

/**
 * An OkHttp {@link Interceptor} that adds client identification headers to every outgoing HTTP
 * request.
 *
 * <p>Attaches {@code X-Client-Name} ({@code "java-stellar-sdk"}) and {@code X-Client-Version}
 * headers, allowing Horizon and Stellar RPC servers to track SDK usage.
 */
public class ClientIdentificationInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request originalRequest = chain.request();
    Request requestWithHeaders;

    requestWithHeaders =
        originalRequest
            .newBuilder()
            .addHeader("X-Client-Name", "java-stellar-sdk")
            .addHeader("X-Client-Version", Util.getSdkVersion())
            .build();
    return chain.proceed(requestWithHeaders);
  }
}
