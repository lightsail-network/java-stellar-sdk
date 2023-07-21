package org.stellar.sdk.requests.sorobanrpc;

import com.google.gson.annotations.SerializedName;

public enum EventFilterType {
  @SerializedName("system")
  SYSTEM,
  @SerializedName("contract")
  CONTRACT,
  @SerializedName("diagnostic")
  DIAGNOSTIC
}
