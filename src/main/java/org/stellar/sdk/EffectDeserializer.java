package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.stellar.base.Keypair;
import org.stellar.sdk.effects.*;

import java.lang.reflect.Type;

class EffectDeserializer implements JsonDeserializer<Effect> {
  @Override
  public Effect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Create new Gson object with adapters needed in Operation
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Keypair.class, new KeypairTypeAdapter().nullSafe())
            .create();

    int type = json.getAsJsonObject().get("type_i").getAsInt();
    switch (type) {
      // Account effects
      case 0:
        return gson.fromJson(json, AccountCreatedEffect.class);
      case 1:
        return gson.fromJson(json, AccountRemovedEffect.class);
      case 2:
        return gson.fromJson(json, AccountCreditedEffect.class);
      case 3:
        return gson.fromJson(json, AccountDebitedEffect.class);
      case 4:
        return gson.fromJson(json, AccountThresholdsUpdatedEffect.class);
      case 5:
        return gson.fromJson(json, AccountHomeDomainUpdatedEffect.class);
      case 6:
        return gson.fromJson(json, AccountFlagsUpdatedEffect.class);
      // Signer effects
      case 10:
        return gson.fromJson(json, SignerCreatedEffect.class);
      case 11:
        return gson.fromJson(json, SignerRemovedEffect.class);
      case 12:
        return gson.fromJson(json, SignerUpdatedEffect.class);
      // Trustline effects
      case 20:
        return gson.fromJson(json, TrustlineCreatedEffect.class);
      case 21:
        return gson.fromJson(json, TrustlineRemovedEffect.class);
      case 22:
        return gson.fromJson(json, TrustlineUpdatedEffect.class);
      case 23:
        return gson.fromJson(json, TrustlineAuthorizedEffect.class);
      case 24:
        return gson.fromJson(json, TrustlineDeauthorizedEffect.class);
      // Trading effects
      case 30:
        return gson.fromJson(json, OfferCreatedEffect.class);
      case 31:
        return gson.fromJson(json, OfferRemovedEffect.class);
      case 32:
        return gson.fromJson(json, OfferUpdatedEffect.class);
      case 33:
        return gson.fromJson(json, TradeEffect.class);
      default:
        throw new RuntimeException("Invalid operation type");
    }
  }
}
