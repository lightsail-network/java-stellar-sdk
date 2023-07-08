package org.stellar.sdk.responses.effects;

import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import org.stellar.sdk.responses.Link;
import org.stellar.sdk.responses.MuxedAccount;
import org.stellar.sdk.responses.Pageable;
import org.stellar.sdk.responses.Response;

/**
 * Abstract class for effect responses.
 *
 * @see <a href="https://developers.stellar.org/api/resources/effects/" target="_blank">Effect
 *     documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public abstract class EffectResponse extends Response implements Pageable {
  @SerializedName("id")
  private String id;

  @SerializedName("account")
  private String account;

  @SerializedName("account_muxed")
  private String accountMuxed;

  @SerializedName("account_muxed_id")
  private BigInteger accountMuxedId;

  @SerializedName("type")
  private String type;

  @SerializedName("created_at")
  private String createdAt;

  @SerializedName("paging_token")
  private String pagingToken;

  @SerializedName("_links")
  private Links links;

  public String getId() {
    return id;
  }

  public String getAccount() {
    return account;
  }

  public Optional<MuxedAccount> getAccountMuxed() {
    if (this.accountMuxed == null || this.accountMuxed.isEmpty()) {
      return Optional.absent();
    }
    return Optional.of(new MuxedAccount(this.accountMuxed, this.account, this.accountMuxedId));
  }

  /**
   * Returns effect type. Possible types:
   *
   * <ul>
   *   <li>account_created
   *   <li>account_removed
   *   <li>account_credited
   *   <li>account_debited
   *   <li>account_thresholds_updated
   *   <li>account_home_domain_updated
   *   <li>account_flags_updated
   *   <li>account_inflation_destination_updated
   *   <li>signer_created
   *   <li>signer_removed
   *   <li>signer_updated
   *   <li>trustline_created
   *   <li>trustline_removed
   *   <li>trustline_updated
   *   <li>trustline_authorized
   *   <li>trustline_authorized_to_maintain_liabilities
   *   <li>trustline_deauthorized
   *   <li>offer_created
   *   <li>offer_removed
   *   <li>offer_updated
   *   <li>trade
   *   <li>data_created
   *   <li>data_removed
   *   <li>data_updated
   *   <li>sequence_bumped
   * </ul>
   */
  public String getType() {
    return type;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public Links getLinks() {
    return links;
  }

  /** Represents effect links. */
  public static class Links {
    @SerializedName("operation")
    private final Link operation;

    @SerializedName("precedes")
    private final Link precedes;

    @SerializedName("succeeds")
    private final Link succeeds;

    public Links(Link operation, Link precedes, Link succeeds) {
      this.operation = operation;
      this.precedes = precedes;
      this.succeeds = succeeds;
    }

    public Link getOperation() {
      return operation;
    }

    public Link getPrecedes() {
      return precedes;
    }

    public Link getSucceeds() {
      return succeeds;
    }
  }
}
