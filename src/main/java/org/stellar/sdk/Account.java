package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;

import static com.google.common.base.Preconditions.checkNotNull;

public class Account implements org.stellar.base.TransactionBuilderAccount {
  @SerializedName("address") /* KeypairTypeAdapter used */
  private Keypair keypair;
  @SerializedName("sequence")
  private Long sequenceNumber;
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
  private Signers[] signers;
  @SerializedName("_links")
  private Links links;

  Account(Keypair keypair) {
    this.keypair = keypair;
  }

  public Account(Keypair keypair, Long sequenceNumber) {
    this.keypair = keypair;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public Keypair getKeypair() {
    return keypair;
  }

  @Override
  public Long getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public void incrementSequenceNumber() {
    sequenceNumber++;
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

  public Signers[] getSigners() {
    return signers;
  }

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

  public static class Flags {
    @SerializedName("auth_required")
    private final boolean authRequired;
    @SerializedName("auth_revocable")
    private final boolean authRevocable;

    Flags(boolean authRequired, boolean authRevocable) {
      this.authRequired = authRequired;
      this.authRevocable = authRevocable;
    }

    public boolean getAuthRequired() {
      return authRequired;
    }

    public boolean getAuthRevocable() {
      return authRevocable;
    }
  }

  public static class Balance {
    @SerializedName("asset_type")
    private final String assetType;
    @SerializedName("asset_code")
    private final String assetCode;
    @SerializedName("asset_issuer")
    private final String assetIssuer;
    @SerializedName("balance")
    private final String balance;

    Balance(String assetType, String assetCode, String assetIssuer, String balance) {
      this.assetType = checkNotNull(assetType, "assertType cannot be null");
      this.balance = checkNotNull(balance, "balance cannot be null");
      this.assetCode = assetCode;
      this.assetIssuer = assetIssuer;
    }

    public String getAssetType() {
      return assetType;
    }

    public String getAssetCode() {
      return assetCode;
    }

    public String getAssetIssuer() {
      return assetIssuer;
    }

    public String getBalance() {
      return balance;
    }
  }

  public static class Signers {
    @SerializedName("address")
    private final String address;
    @SerializedName("weight")
    private final int weight;

    Signers(String address, int weight) {
      this.address = checkNotNull(address, "address cannot be null");
      this.weight = checkNotNull(weight, "weight cannot be null");
    }

    public String getAddress() {
      return address;
    }

    public int getWeight() {
      return weight;
    }
  }

  public Links getLinks() {
    return links;
  }

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
