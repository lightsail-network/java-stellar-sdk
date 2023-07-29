package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;

/** Represents the type of event. */
public enum EventFilterType {
  @SerializedName("system")
  SYSTEM,
  @SerializedName("contract")
  CONTRACT,
  @SerializedName("diagnostic")
  DIAGNOSTIC
}
