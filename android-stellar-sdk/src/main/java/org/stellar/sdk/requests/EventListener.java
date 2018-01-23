package org.stellar.sdk.requests;

/**
 * This interface is used in {@link RequestBuilder} classes <code>stream</code> method.
 */
public interface EventListener<T> {
  /**
   * This method will be called when new event is sent by a server.
   * @param object object deserialized from the event data
   */
  void onEvent(T object);
}
