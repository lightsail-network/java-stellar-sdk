package org.stellar.sdk.effects;

import com.google.gson.annotations.SerializedName;

import org.stellar.base.Keypair;
import org.stellar.sdk.Link;

public abstract class Effect {
  @SerializedName("id")
  protected String id;
  @SerializedName("account")
  protected Keypair account;
  @SerializedName("type")
  protected String type;
  @SerializedName("_links")
  private Links links;

  public String getId() {
    return id;
  }

  public Keypair getAccount() {
    return account;
  }

  public String getType() {
    return type;
  }

  public Links getLinks() {
    return links;
  }

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
