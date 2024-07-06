package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.stellar.sdk.responses.*;

/**
 * Abstract class for operation responses.
 *
 * @see <a href="https://developers.stellar.org/api/resources/operations/" target="_blank">Operation
 *     documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class OperationResponse extends Response implements Pageable {
  @SerializedName("id")
  private Long id;

  @SerializedName("source_account")
  private String sourceAccount;

  @SerializedName("source_account_muxed")
  private String sourceAccountMuxed;

  @SerializedName("source_account_muxed_id")
  private BigInteger sourceAccountMuxedId;

  @SerializedName("paging_token")
  private String pagingToken;

  @SerializedName("created_at")
  private String createdAt;

  @SerializedName("transaction_hash")
  private String transactionHash;

  @SerializedName("transaction_successful")
  private Boolean transactionSuccessful;

  @SerializedName("type")
  private String type;

  @SerializedName("_links")
  private Links links;

  @SerializedName("transaction")
  private TransactionResponse transaction;

  /** Represents operation links. */
  @Value
  public static class Links {
    @SerializedName("effects")
    Link effects;

    @SerializedName("precedes")
    Link precedes;

    @SerializedName("self")
    Link self;

    @SerializedName("succeeds")
    Link succeeds;

    @SerializedName("transaction")
    Link transaction;
  }
}
