package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.Link;
import org.stellar.sdk.responses.Response;

/**
 * Abstract class for effect responses.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/effect.html" target="_blank">Effect documentation</a>
 * @see org.stellar.sdk.requests.EffectsRequestBuilder
 * @see org.stellar.sdk.Server#effects()
 */
public abstract class EffectResponse extends Response {
  @SerializedName("id")
  protected String id;
  @SerializedName("account")
  protected KeyPair account;
  @SerializedName("type")
  protected String type;
  @SerializedName("created_at")
  protected String createdAt;
  @SerializedName("paging_token")
  protected String pagingToken;
  @SerializedName("_links")
  private Links links;

  public String getId() {
    return id;
  }

  public KeyPair getAccount() {
    return account;
  }

  /**
   * <p>Returns effect type. Possible types:</p>
   * <ul>
   *   <li>account_created</li>
   *   <li>account_removed</li>
   *   <li>account_credited</li>
   *   <li>account_debited</li>
   *   <li>account_thresholds_updated</li>
   *   <li>account_home_domain_updated</li>
   *   <li>account_flags_updated</li>
   *   <li>account_inflation_destination_updated</li>
   *   <li>signer_created</li>
   *   <li>signer_removed</li>
   *   <li>signer_updated</li>
   *   <li>trustline_created</li>
   *   <li>trustline_removed</li>
   *   <li>trustline_updated</li>
   *   <li>trustline_authorized</li>
   *   <li>trustline_deauthorized</li>
   *   <li>offer_created</li>
   *   <li>offer_removed</li>
   *   <li>offer_updated</li>
   *   <li>trade</li>
   *   <li>data_created</li>
   *   <li>data_removed</li>
   *   <li>data_updated</li>
   *   <li>sequence_bumped</li>
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

  /**
   * Represents effect links.
   */
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
