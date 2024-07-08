package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.stellar.sdk.Base64Factory;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.TrustLineAsset;

/**
 * Represents account response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/accounts"
 *     target="_blank">Account documentation</a>
 * @see org.stellar.sdk.requests.AccountsRequestBuilder
 * @see org.stellar.sdk.Server#accounts()
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class AccountResponse extends Response implements org.stellar.sdk.TransactionBuilderAccount {
  @SerializedName("id")
  private String id;

  @SerializedName("account_id")
  private String accountId;

  @SerializedName("sequence")
  private Long sequenceNumber;

  @SerializedName("sequence_ledger")
  private Long sequenceLedger;

  @SerializedName("sequence_time")
  private Long sequenceTime;

  @SerializedName("subentry_count")
  private Integer subentryCount;

  @SerializedName("inflation_destination")
  private String inflationDestination;

  @SerializedName("home_domain")
  private String homeDomain;

  @SerializedName("last_modified_ledger")
  private Integer lastModifiedLedger;

  @SerializedName("last_modified_time")
  private String lastModifiedTime;

  @SerializedName("thresholds")
  private Thresholds thresholds;

  @SerializedName("flags")
  private Flags flags;

  @SerializedName("balances")
  private List<Balance> balances;

  @SerializedName("signers")
  private List<Signer> signers;

  @SerializedName("data")
  private Data data;

  @SerializedName("num_sponsoring")
  private Integer numSponsoring;

  @SerializedName("num_sponsored")
  private Integer numSponsored;

  @SerializedName("sponsor")
  private String sponsor;

  @SerializedName("paging_token")
  private String pagingToken;

  @SerializedName("_links")
  private Links links;

  @Override
  public String getAccountId() {
    return accountId;
  }

  @Override
  public KeyPair getKeyPair() {
    return KeyPair.fromAccountId(accountId);
  }

  @Override
  public Long getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public void setSequenceNumber(long seqNum) {
    sequenceNumber = seqNum;
  }

  @Override
  public Long getIncrementedSequenceNumber() {
    return sequenceNumber + 1;
  }

  @Override
  public void incrementSequenceNumber() {
    sequenceNumber++;
  }

  /** Represents account thresholds. */
  @Value
  public static class Thresholds {
    @SerializedName("low_threshold")
    int lowThreshold;

    @SerializedName("med_threshold")
    int medThreshold;

    @SerializedName("high_threshold")
    int highThreshold;
  }

  /** Represents account flags. */
  @Value
  public static class Flags {
    @SerializedName("auth_required")
    Boolean authRequired;

    @SerializedName("auth_revocable")
    Boolean authRevocable;

    @SerializedName("auth_immutable")
    Boolean authImmutable;

    @SerializedName("auth_clawback_enabled")
    Boolean authClawbackEnabled;
  }

  /** Represents account balance. */
  @Value
  public static class Balance {
    @SerializedName("asset_type")
    String assetType;

    @SerializedName("asset_code")
    String assetCode;

    @SerializedName("asset_issuer")
    String assetIssuer;

    @SerializedName("liquidity_pool_id")
    String liquidityPoolId;

    @SerializedName("limit")
    String limit;

    @SerializedName("balance")
    String balance;

    @SerializedName("buying_liabilities")
    String buyingLiabilities;

    @SerializedName("selling_liabilities")
    String sellingLiabilities;

    @SerializedName("is_authorized")
    Boolean isAuthorized;

    @SerializedName("is_authorized_to_maintain_liabilities")
    Boolean isAuthorizedToMaintainLiabilities;

    @SerializedName("is_clawback_enabled")
    Boolean isClawbackEnabled;

    @SerializedName("last_modified_ledger")
    Integer lastModifiedLedger;

    @SerializedName("sponsor")
    String sponsor;

    public TrustLineAsset getTrustLineAsset() {
      return Response.getTrustLineAsset(assetType, assetCode, assetIssuer, liquidityPoolId);
    }
  }

  /** Represents account signers. */
  @Value
  public static class Signer {
    @SerializedName("key")
    String key;

    @SerializedName("type")
    String type;

    @SerializedName("weight")
    int weight;

    @SerializedName("sponsor")
    String sponsor;
  }

  /** Data connected to account. */
  public static class Data extends HashMap<String, String> {
    @Override
    public int size() {
      return super.size();
    }

    /**
     * Gets base64-encoded value for a given key.
     *
     * @param key Data entry name
     * @return base64-encoded value
     */
    public String get(String key) {
      return super.get(key);
    }

    /**
     * Gets raw value for a given key.
     *
     * @param key Data entry name
     * @return raw value
     */
    public byte[] getDecoded(String key) {
      return Base64Factory.getInstance().decode(this.get(key));
    }
  }

  /** Links connected to account. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;

    @SerializedName("transactions")
    Link transactions;

    @SerializedName("operations")
    Link operations;

    @SerializedName("payments")
    Link payments;

    @SerializedName("effects")
    Link effects;

    @SerializedName("offers")
    Link offers;

    @SerializedName("trades")
    Link trades;

    @SerializedName("data")
    Link data;
  }
}
