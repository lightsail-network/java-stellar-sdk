package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

/**
 * Represents BumpSequence operation response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public class BumpSequenceOperationResponse extends OperationResponse {
  @SerializedName("bump_to")
  protected final Long bumpTo;

  public BumpSequenceOperationResponse(Long bumpTo) {
    this.bumpTo = bumpTo;
  }

  public Long getBumpTo() {
    return bumpTo;
  }
}
