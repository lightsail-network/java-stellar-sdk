package org.stellar.sdk.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.stellar.sdk.Asset;
import org.stellar.sdk.responses.operations.*;
import org.stellar.sdk.xdr.OperationType;

import java.lang.reflect.Type;

class OperationDeserializer implements JsonDeserializer<OperationResponse> {
  private static final OperationType[] AllOperationTypes = OperationType.values();
  @Override
  public OperationResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Create new Gson object with adapters needed in Operation
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Asset.class, new AssetDeserializer())
            .create();

    int type = json.getAsJsonObject().get("type_i").getAsInt();
    if (type < 0 || type >= AllOperationTypes.length) {
      throw new RuntimeException("Invalid operation type");
    }

    switch (AllOperationTypes[type]) {
      case CREATE_ACCOUNT:
        return gson.fromJson(json, CreateAccountOperationResponse.class);
      case PAYMENT:
        return gson.fromJson(json, PaymentOperationResponse.class);
      case PATH_PAYMENT_STRICT_RECEIVE:
        // Will change to PathPaymentStrictReceiveOperationResponse in 0.11.0
        return gson.fromJson(json, PathPaymentOperationResponse.class);
      case MANAGE_SELL_OFFER:
        return gson.fromJson(json, ManageSellOfferOperationResponse.class);
      case CREATE_PASSIVE_SELL_OFFER:
        return gson.fromJson(json, CreatePassiveSellOfferOperationResponse.class);
      case SET_OPTIONS:
        return gson.fromJson(json, SetOptionsOperationResponse.class);
      case CHANGE_TRUST:
        return gson.fromJson(json, ChangeTrustOperationResponse.class);
      case ALLOW_TRUST:
        return gson.fromJson(json, AllowTrustOperationResponse.class);
      case ACCOUNT_MERGE:
        return gson.fromJson(json, AccountMergeOperationResponse.class);
      case INFLATION:
        return gson.fromJson(json, InflationOperationResponse.class);
      case MANAGE_DATA:
        return gson.fromJson(json, ManageDataOperationResponse.class);
      case BUMP_SEQUENCE:
        return gson.fromJson(json, BumpSequenceOperationResponse.class);
      case MANAGE_BUY_OFFER:
        return gson.fromJson(json, ManageBuyOfferOperationResponse.class);
      case PATH_PAYMENT_STRICT_SEND:
        return gson.fromJson(json, PathPaymentStrictSendOperationResponse.class);
      default:
        throw new RuntimeException("Invalid operation type");
    }
  }
}
