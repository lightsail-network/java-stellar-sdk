package org.stellar.sdk;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;

public abstract class Operation {
  @SerializedName("id")
  protected Long id;
  @SerializedName("source_account")
  protected Keypair sourceAccount;
  @SerializedName("type")
  protected String type;
  @SerializedName("_links")
  private Links links;

  public Long getId() {
    return id;
  }

  public Keypair getSourceAccount() {
    return sourceAccount;
  }

  public String getType() {
    return type;
  }

  public Links getLinks() {
    return links;
  }

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
