package org.stellar.sdk.requests;

import com.google.common.base.Optional;

/** This interface is used in {@link RequestBuilder} classes <code>stream</code> method. */
public interface EventListener<T> {
  /**
   * This method will be called when new event is sent by a server.
   *
   * @param object object deserialized from the event data
   */
  void onEvent(T object);

  /**
   * This method will be called when the stream encounters an error.
   *
   * @param error exception which caused the failure
   * @param responseCode the HTTP status code from the event stream response
   */
  void onFailure(Optional<Throwable> error, Optional<Integer> responseCode);
}
