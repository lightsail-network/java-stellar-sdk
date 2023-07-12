package org.stellar.sdk.responses;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.stellar.sdk.Asset.create;

import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.LiquidityPoolID;

/**
 * Represents account response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/accounts/" target="_blank">Account
 *     documentation</a>
 * @see org.stellar.sdk.requests.AccountsRequestBuilder
 * @see org.stellar.sdk.Server#accounts()
 */
public class AccountResponse extends Response implements org.stellar.sdk.TransactionBuilderAccount {
  @SerializedName("account_id")
  private String accountId;

  @SerializedName("sequence")
  private Long sequenceNumber;

  @SerializedName("subentry_count")
  private Integer subentryCount;

  @SerializedName("sequence_ledger")
  private Long sequenceUpdatedAtLedger;

  @SerializedName("sequence_time")
  private Long sequenceUpdatedAtTime;

  @SerializedName("inflation_destination")
  private String inflationDestination;

  @SerializedName("home_domain")
  private String homeDomain;

  @SerializedName("last_modified_ledger")
  private Integer lastModifiedLedger;

  @SerializedName("thresholds")
  private Thresholds thresholds;

  @SerializedName("flags")
  private Flags flags;

  @SerializedName("balances")
  private Balance[] balances;

  @SerializedName("signers")
  private Signer[] signers;

  @SerializedName("data")
  private Data data;

  @SerializedName("_links")
  private Links links;

  @SerializedName("num_sponsoring")
  private Integer numSponsoring;

  @SerializedName("num_sponsored")
  private Integer numSponsored;

  @SerializedName("sponsor")
  private String sponsor;

  AccountResponse(String accountId) {
    this.accountId = accountId;
  }

  public AccountResponse(String accountId, Long sequenceNumber) {
    this.accountId = accountId;
    this.sequenceNumber = sequenceNumber;
  }

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
    return new Long(sequenceNumber + 1);
  }

  @Override
  public void incrementSequenceNumber() {
    sequenceNumber++;
  }

  public Long getSequenceUpdatedAtLedger() {
    return sequenceUpdatedAtLedger;
  }

  public Long getSequenceUpdatedAtTime() {
    return sequenceUpdatedAtTime;
  }

  public Integer getSubentryCount() {
    return subentryCount;
  }

  public String getInflationDestination() {
    return inflationDestination;
  }

  public String getHomeDomain() {
    return homeDomain;
  }

  public Integer getLastModifiedLedger() {
    return lastModifiedLedger;
  }

  public Thresholds getThresholds() {
    return thresholds;
  }

  public Flags getFlags() {
    return flags;
  }

  public Balance[] getBalances() {
    return balances;
  }

  public Signer[] getSigners() {
    return signers;
  }

  public Data getData() {
    return data;
  }

  public Integer getNumSponsoring() {
    return numSponsoring;
  }

  public Integer getNumSponsored() {
    return numSponsored;
  }

  public Optional<String> getSponsor() {
    return Optional.fromNullable(this.sponsor);
  }

  /** Represents account thresholds. */
  public static class Thresholds {
    @SerializedName("low_threshold")
    private final int lowThreshold;

    @SerializedName("med_threshold")
    private final int medThreshold;

    @SerializedName("high_threshold")
    private final int highThreshold;

    public Thresholds(int lowThreshold, int medThreshold, int highThreshold) {
      this.lowThreshold = lowThreshold;
      this.medThreshold = medThreshold;
      this.highThreshold = highThreshold;
    }

    public int getLowThreshold() {
      return lowThreshold;
    }

    public int getMedThreshold() {
      return medThreshold;
    }

    public int getHighThreshold() {
      return highThreshold;
    }
  }

  /** Represents account flags. */
  public static class Flags {
    @SerializedName("auth_required")
    private final boolean authRequired;

    @SerializedName("auth_revocable")
    private final boolean authRevocable;

    @SerializedName("auth_immutable")
    private final boolean authImmutable;

    @SerializedName("auth_clawback_enabled")
    private final boolean authClawbackEnabled;

    public Flags(boolean authRequired, boolean authRevocable, boolean authImmutable) {
      this(authRequired, authRevocable, authImmutable, false);
    }

    public Flags(
        boolean authRequired,
        boolean authRevocable,
        boolean authImmutable,
        boolean authClawbackEnabled) {
      this.authRequired = authRequired;
      this.authRevocable = authRevocable;
      this.authImmutable = authImmutable;
      this.authClawbackEnabled = authClawbackEnabled;
    }

    public boolean getAuthRequired() {
      return authRequired;
    }

    public boolean getAuthRevocable() {
      return authRevocable;
    }

    public boolean getAuthImmutable() {
      return authImmutable;
    }

    public boolean getAuthClawbackEnabled() {
      return authClawbackEnabled;
    }
  }

  /** Represents account balance. */
  public static class Balance {
    @SerializedName("asset_type")
    private final String assetType;

    @SerializedName("asset_code")
    private String assetCode;

    @SerializedName("asset_issuer")
    private String assetIssuer;

    @SerializedName("liquidity_pool_id")
    private LiquidityPoolID liquidityPoolID;

    @SerializedName("limit")
    private final String limit;

    @SerializedName("balance")
    private final String balance;

    @SerializedName("buying_liabilities")
    private final String buyingLiabilities;

    @SerializedName("selling_liabilities")
    private final String sellingLiabilities;

    @SerializedName("is_authorized")
    private final Boolean isAuthorized;

    @SerializedName("is_authorized_to_maintain_liabilities")
    private final Boolean isAuthorizedToMaintainLiabilities;

    @SerializedName("last_modified_ledger")
    private final Integer lastModifiedLedger;

    @SerializedName("sponsor")
    private String sponsor;

    public Balance(
        String assetType,
        String assetCode,
        String assetIssuer,
        LiquidityPoolID liquidityPoolID,
        String balance,
        String limit,
        String buyingLiabilities,
        String sellingLiabilities,
        Boolean isAuthorized,
        Boolean isAuthorizedToMaintainLiabilities,
        Integer lastModifiedLedger,
        String sponsor) {
      this.assetType = checkNotNull(assetType, "assertType cannot be null");
      this.balance = checkNotNull(balance, "balance cannot be null");
      this.limit = limit;
      this.assetCode = assetCode;
      this.assetIssuer = assetIssuer;
      this.liquidityPoolID = liquidityPoolID;
      this.buyingLiabilities = checkNotNull(buyingLiabilities, "buyingLiabilities cannot be null");
      this.sellingLiabilities =
          checkNotNull(sellingLiabilities, "sellingLiabilities cannot be null");
      this.isAuthorized = isAuthorized;
      this.isAuthorizedToMaintainLiabilities = isAuthorizedToMaintainLiabilities;
      this.lastModifiedLedger = lastModifiedLedger;
      // sponsor is an optional field
      this.sponsor = sponsor;
    }

    public Optional<Asset> getAsset() {
      if (liquidityPoolID != null) {
        return Optional.of(create(assetType, assetCode, assetIssuer, liquidityPoolID.toString()));
      } else {
        return Optional.of(create(assetType, assetCode, assetIssuer));
      }
    }

    public String getAssetType() {
      return assetType;
    }

    public Optional<String> getAssetCode() {
      return Optional.fromNullable(assetCode);
    }

    public Optional<String> getAssetIssuer() {
      return Optional.fromNullable(assetIssuer);
    }

    public Optional<LiquidityPoolID> getLiquidityPoolID() {
      return Optional.fromNullable(liquidityPoolID);
    }

    public String getBalance() {
      return balance;
    }

    public Optional<String> getBuyingLiabilities() {
      return Optional.fromNullable(buyingLiabilities);
    }

    public Optional<String> getSellingLiabilities() {
      return Optional.fromNullable(sellingLiabilities);
    }

    public String getLimit() {
      return limit;
    }

    public Boolean getAuthorized() {
      return isAuthorized;
    }

    public Boolean getAuthorizedToMaintainLiabilities() {
      return isAuthorizedToMaintainLiabilities;
    }

    public Integer getLastModifiedLedger() {
      return lastModifiedLedger;
    }

    public Optional<String> getSponsor() {
      return Optional.fromNullable(this.sponsor);
    }
  }

  /** Represents account signers. */
  public static class Signer {
    @SerializedName("key")
    private final String key;

    @SerializedName("type")
    private final String type;

    @SerializedName("weight")
    private final int weight;

    @SerializedName("sponsor")
    private String sponsor;

    public Signer(String key, String type, int weight, String sponsor) {
      this.key = checkNotNull(key, "key cannot be null");
      this.type = checkNotNull(type, "type cannot be null");
      this.weight = checkNotNull(weight, "weight cannot be null");
      // sponsor is an optional field
      this.sponsor = sponsor;
    }

    /**
     * @deprecated Use {@link Signer#getKey()}
     * @return
     */
    public String getAccountId() {
      return key;
    }

    public String getKey() {
      return key;
    }

    public int getWeight() {
      return weight;
    }

    public String getType() {
      return type;
    }

    public Optional<String> getSponsor() {
      return Optional.fromNullable(this.sponsor);
    }
  }

  public Links getLinks() {
    return links;
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
      BaseEncoding base64Encoding = BaseEncoding.base64();
      return base64Encoding.decode(this.get(key));
    }
  }

  /** Links connected to account. */
  public static class Links {
    @SerializedName("effects")
    private final Link effects;

    @SerializedName("offers")
    private final Link offers;

    @SerializedName("operations")
    private final Link operations;

    @SerializedName("self")
    private final Link self;

    @SerializedName("transactions")
    private final Link transactions;

    public Links(Link effects, Link offers, Link operations, Link self, Link transactions) {
      this.effects = effects;
      this.offers = offers;
      this.operations = operations;
      this.self = self;
      this.transactions = transactions;
    }

    public Link getEffects() {
      return effects;
    }

    public Link getOffers() {
      return offers;
    }

    public Link getOperations() {
      return operations;
    }

    public Link getSelf() {
      return self;
    }

    public Link getTransactions() {
      return transactions;
    }
  }
}
