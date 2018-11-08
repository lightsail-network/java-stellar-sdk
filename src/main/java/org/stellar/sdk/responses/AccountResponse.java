package org.stellar.sdk.responses;

import com.google.common.io.BaseEncoding;
import com.google.gson.annotations.SerializedName;

import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;

import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents account response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/account.html" target="_blank">Account documentation</a>
 * @see org.stellar.sdk.requests.AccountsRequestBuilder
 * @see org.stellar.sdk.Server#accounts()
 */
public class AccountResponse extends Response implements org.stellar.sdk.TransactionBuilderAccount {
  @SerializedName("account_id") /* KeyPairTypeAdapter used */
  private KeyPair keypair;
  @SerializedName("sequence")
  private Long sequenceNumber;
  @SerializedName("paging_token")
  private String pagingToken;
  @SerializedName("subentry_count")
  private Integer subentryCount;
  @SerializedName("inflation_destination")
  private String inflationDestination;
  @SerializedName("home_domain")
  private String homeDomain;
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

  AccountResponse(KeyPair keypair) {
    this.keypair = keypair;
  }

  public AccountResponse(KeyPair keypair, Long sequenceNumber) {
    this.keypair = keypair;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public KeyPair getKeypair() {
    return keypair;
  }

  @Override
  public Long getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public Long getIncrementedSequenceNumber() {
    return new Long(sequenceNumber + 1);
  }

  @Override
  public void incrementSequenceNumber() {
    sequenceNumber++;
  }

  public String getPagingToken() {
    return pagingToken;
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

  /**
   * Represents account thresholds.
   */
  public static class Thresholds {
    @SerializedName("low_threshold")
    private final int lowThreshold;
    @SerializedName("med_threshold")
    private final int medThreshold;
    @SerializedName("high_threshold")
    private final int highThreshold;

    Thresholds(int lowThreshold, int medThreshold, int highThreshold) {
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

  /**
   * Represents account flags.
   */
  public static class Flags {
    @SerializedName("auth_required")
    private final boolean authRequired;
    @SerializedName("auth_revocable")
    private final boolean authRevocable;
    @SerializedName("auth_immutable")
    private final boolean authImmutable;

    Flags(boolean authRequired, boolean authRevocable, boolean authImmutable) {
      this.authRequired = authRequired;
      this.authRevocable = authRevocable;
      this.authImmutable = authImmutable;
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
  }

  /**
   * Represents account balance.
   */
  public static class Balance {
    @SerializedName("asset_type")
    private final String assetType;
    @SerializedName("asset_code")
    private final String assetCode;
    @SerializedName("asset_issuer")
    private final String assetIssuer;
    @SerializedName("limit")
    private final String limit;
    @SerializedName("balance")
    private final String balance;
    @SerializedName("buying_liabilities")
    private final String buyingLiabilities;
    @SerializedName("selling_liabilities")
    private final String sellingLiabilities;

    Balance(String assetType, String assetCode, String assetIssuer, String balance, String limit, String buyingLiabilities, String sellingLiabilities) {
      this.assetType = checkNotNull(assetType, "assertType cannot be null");
      this.balance = checkNotNull(balance, "balance cannot be null");
      this.limit = limit;
      this.assetCode = assetCode;
      this.assetIssuer = assetIssuer;
      this.buyingLiabilities = checkNotNull(buyingLiabilities, "buyingLiabilities cannot be null");
      this.sellingLiabilities = checkNotNull(sellingLiabilities, "sellingLiabilities cannot be null");
    }

    public Asset getAsset() {
      if (assetType.equals("native")) {
        return new AssetTypeNative();
      } else {
        return Asset.createNonNativeAsset(assetCode, getAssetIssuer());
      }
    }

    public String getAssetType() {
      return assetType;
    }

    public String getAssetCode() {
      return assetCode;
    }

    public KeyPair getAssetIssuer() {
      return KeyPair.fromAccountId(assetIssuer);
    }

    public String getBalance() {
      return balance;
    }

    public String getBuyingLiabilities() {
      return buyingLiabilities;
    }

    public String getSellingLiabilities() {
      return sellingLiabilities;
    }

    public String getLimit() {
      return limit;
    }
  }

  /**
   * Represents account signers.
   */
  public static class Signer {
    @SerializedName("key")
    private final String key;
    @SerializedName("type")
    private final String type;
    @SerializedName("weight")
    private final int weight;

    Signer(String key, String type, int weight) {
      this.key = checkNotNull(key, "key cannot be null");
      this.type = checkNotNull(type, "type cannot be null");
      this.weight = checkNotNull(weight, "weight cannot be null");
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
  }

  public Links getLinks() {
    return links;
  }

  /**
   * Data connected to account.
   */
  public static class Data extends HashMap<String,String> {
    @Override
    public int size() {
      return super.size();
    }

    /**
     * Gets base64-encoded value for a given key.
     * @param key Data entry name
     * @return base64-encoded value
     */
    public String get(String key) {
      return super.get(key);
    }

    /**
     * Gets raw value for a given key.
     * @param key Data entry name
     * @return raw value
     */
    public byte[] getDecoded(String key) {
      BaseEncoding base64Encoding = BaseEncoding.base64();
      return base64Encoding.decode(this.get(key));
    }
  }

  /**
   * Links connected to account.
   */
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

    Links(Link effects, Link offers, Link operations, Link self, Link transactions) {
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
