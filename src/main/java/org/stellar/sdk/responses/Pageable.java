package org.stellar.sdk.responses;

/**
 * Marks a Horizon response type as supporting cursor-based pagination.
 *
 * <p>Response types that implement this interface provide a paging token that can be passed to
 * {@link org.stellar.sdk.requests.RequestBuilder#cursor(String)} to retrieve subsequent pages of
 * results.
 */
public interface Pageable {
  String getPagingToken();
}
