package org.stellar.sdk.responses;

import com.google.gson.reflect.TypeToken;

/**
 * Indicates a generic container that requires type information to be provided after initialisation.
 *
 * @param <T> the type of the objects in this response container.
 */
public interface TypedResponse<T> {

  void setType(TypeToken<T> type);
}
