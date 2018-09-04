package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.responses.Link;
import org.stellar.sdk.responses.Pageable;
import org.stellar.sdk.responses.Response;

/**
 * Abstract class for operation responses.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/operation.html" target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
public abstract class OperationResponse extends Response implements Pageable {
  @SerializedName("id")
  protected Long id;
  @SerializedName("source_account")
  protected String sourceAccount;
  @SerializedName("paging_token")
  protected String pagingToken;
  @SerializedName("created_at")
  protected String createdAt;
  @SerializedName("transaction_hash")
  protected String transactionHash;
  @SerializedName("transaction_successful")
  protected Boolean transactionSuccessful;
  @SerializedName("type")
  protected String type;
  @SerializedName("_links")
  private Links links;

  public Long getId() {
    return id;
  }

  public String getSourceAccount() {
    return sourceAccount;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  /**
   * <p>Returns operation type. Possible types:</p>
   * <ul>
   *   <li>create_account</li>
   *   <li>payment</li>
   *   <li>allow_trust</li>
   *   <li>change_trust</li>
   *   <li>set_options</li>
   *   <li>account_merge</li>
   *   <li>manage_offer</li>
   *   <li>path_payment</li>
   *   <li>create_passive_offer</li>
   *   <li>inflation</li>
   *   <li>manage_data</li>
   * </ul>
   */
  public String getType() {
    return type;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  /**
   * Returns transaction hash of transaction this operation belongs to.
   */
  public String getTransactionHash() {
    return transactionHash;
  }

  public Boolean isTransactionSuccessful() {
    return transactionSuccessful;
  }

  public Links getLinks() {
    return links;
  }

  /**
   * Represents operation links.
   */
  public static class Links {
    @SerializedName("effects")
    private final Link effects;
    @SerializedName("precedes")
    private final Link precedes;
    @SerializedName("self")
    private final Link self;
    @SerializedName("succeeds")
    private final Link succeeds;
    @SerializedName("transaction")
    private final Link transaction;

    public Links(Link effects, Link precedes, Link self, Link succeeds, Link transaction) {
      this.effects = effects;
      this.precedes = precedes;
      this.self = self;
      this.succeeds = succeeds;
      this.transaction = transaction;
    }

    public Link getEffects() {
      return effects;
    }

    public Link getPrecedes() {
      return precedes;
    }

    public Link getSelf() {
      return self;
    }

    public Link getSucceeds() {
      return succeeds;
    }

    public Link getTransaction() {
      return transaction;
    }
  }
}
