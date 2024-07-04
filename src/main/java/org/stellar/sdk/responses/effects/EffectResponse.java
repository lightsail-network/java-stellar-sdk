package org.stellar.sdk.responses.effects;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
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
@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class EffectResponse extends Response implements Pageable {
  @SerializedName("id")
  private String id;

  @SerializedName("account")
  private String account;

  @SerializedName("account_muxed")
  private String accountMuxed;

  @SerializedName("account_muxed_id")
  private BigInteger accountMuxedId;

  /**
   * Type of effect
   *
   * @see <a href="https://developers.stellar.org/api/horizon/resources/effects/types"
   *     target="_blank">Effect Types</a>
   */
  @SerializedName("type")
  private String type;

  @SerializedName("created_at")
  private String createdAt;

  @SerializedName("paging_token")
  private String pagingToken;

  @SerializedName("_links")
  private Links links;

  // TODO
  public Optional<MuxedAccount> getAccountMuxed() {
    if (this.accountMuxed == null || this.accountMuxed.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new MuxedAccount(this.accountMuxed, this.account, this.accountMuxedId));
  }

  /** Represents effect links. */
  @Value
  public static class Links {
    @SerializedName("operation")
    Link operation;

    @SerializedName("precedes")
    Link precedes;

    @SerializedName("succeeds")
    Link succeeds;
  }
}
