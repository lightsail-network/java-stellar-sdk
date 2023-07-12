package org.stellar.sdk.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.responses.effects.*;
import org.stellar.sdk.xdr.LiquidityPoolType;

class EffectDeserializer implements JsonDeserializer<EffectResponse> {
  @Override
  public EffectResponse deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    // Create new Gson object with adapters needed in Operation
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(Asset.class, new AssetDeserializer())
            .registerTypeAdapter(LiquidityPoolID.class, new LiquidityPoolIDDeserializer())
            .registerTypeAdapter(LiquidityPoolType.class, new LiquidityPoolTypeDeserializer())
            .registerTypeAdapter(Predicate.class, new PredicateDeserializer())
            .create();

    int type = json.getAsJsonObject().get("type_i").getAsInt();
    switch (type) {
        // Account effects
      case 0:
        return gson.fromJson(json, AccountCreatedEffectResponse.class);
      case 1:
        return gson.fromJson(json, AccountRemovedEffectResponse.class);
      case 2:
        return gson.fromJson(json, AccountCreditedEffectResponse.class);
      case 3:
        return gson.fromJson(json, AccountDebitedEffectResponse.class);
      case 4:
        return gson.fromJson(json, AccountThresholdsUpdatedEffectResponse.class);
      case 5:
        return gson.fromJson(json, AccountHomeDomainUpdatedEffectResponse.class);
      case 6:
        return gson.fromJson(json, AccountFlagsUpdatedEffectResponse.class);
      case 7:
        return gson.fromJson(json, AccountInflationDestinationUpdatedEffectResponse.class);
        // Signer effects
      case 10:
        return gson.fromJson(json, SignerCreatedEffectResponse.class);
      case 11:
        return gson.fromJson(json, SignerRemovedEffectResponse.class);
      case 12:
        return gson.fromJson(json, SignerUpdatedEffectResponse.class);
        // Trustline effects
      case 20:
        return gson.fromJson(json, TrustlineCreatedEffectResponse.class);
      case 21:
        return gson.fromJson(json, TrustlineRemovedEffectResponse.class);
      case 22:
        return gson.fromJson(json, TrustlineUpdatedEffectResponse.class);
      case 23:
        return gson.fromJson(json, TrustlineAuthorizedEffectResponse.class);
      case 24:
        return gson.fromJson(json, TrustlineDeauthorizedEffectResponse.class);
      case 25:
        return gson.fromJson(json, TrustlineAuthorizedToMaintainLiabilitiesEffectResponse.class);
      case 26:
        return gson.fromJson(json, TrustlineFlagsUpdatedEffectResponse.class);
        // Trading effects
      case 30:
        return gson.fromJson(json, OfferCreatedEffectResponse.class);
      case 31:
        return gson.fromJson(json, OfferRemovedEffectResponse.class);
      case 32:
        return gson.fromJson(json, OfferUpdatedEffectResponse.class);
      case 33:
        return gson.fromJson(json, TradeEffectResponse.class);
        // Data effects
      case 40:
        return gson.fromJson(json, DataCreatedEffectResponse.class);
      case 41:
        return gson.fromJson(json, DataRemovedEffectResponse.class);
      case 42:
        return gson.fromJson(json, DataUpdatedEffectResponse.class);
        // Bump Sequence effects
      case 43:
        return gson.fromJson(json, SequenceBumpedEffectResponse.class);
        // claimable balance effects
      case 50:
        return gson.fromJson(json, ClaimableBalanceCreatedEffectResponse.class);
      case 51:
        return gson.fromJson(json, ClaimableBalanceClaimantCreatedEffectResponse.class);
      case 52:
        return gson.fromJson(json, ClaimableBalanceClaimedEffectResponse.class);
        // sponsorship effects
      case 60:
        return gson.fromJson(json, AccountSponsorshipCreatedEffectResponse.class);
      case 61:
        return gson.fromJson(json, AccountSponsorshipUpdatedEffectResponse.class);
      case 62:
        return gson.fromJson(json, AccountSponsorshipRemovedEffectResponse.class);
      case 63:
        return gson.fromJson(json, TrustlineSponsorshipCreatedEffectResponse.class);
      case 64:
        return gson.fromJson(json, TrustlineSponsorshipUpdatedEffectResponse.class);
      case 65:
        return gson.fromJson(json, TrustlineSponsorshipRemovedEffectResponse.class);
      case 66:
        return gson.fromJson(json, DataSponsorshipCreatedEffectResponse.class);
      case 67:
        return gson.fromJson(json, DataSponsorshipUpdatedEffectResponse.class);
      case 68:
        return gson.fromJson(json, DataSponsorshipRemovedEffectResponse.class);
      case 69:
        return gson.fromJson(json, ClaimableBalanceSponsorshipCreatedEffectResponse.class);
      case 70:
        return gson.fromJson(json, ClaimableBalanceSponsorshipUpdatedEffectResponse.class);
      case 71:
        return gson.fromJson(json, ClaimableBalanceSponsorshipRemovedEffectResponse.class);
      case 72:
        return gson.fromJson(json, SignerSponsorshipCreatedEffectResponse.class);
      case 73:
        return gson.fromJson(json, SignerSponsorshipUpdatedEffectResponse.class);
      case 74:
        return gson.fromJson(json, SignerSponsorshipRemovedEffectResponse.class);
      case 80:
        return gson.fromJson(json, ClaimableBalanceClawedBackEffectResponse.class);
      case 90:
        return gson.fromJson(json, LiquidityPoolDepositedEffectResponse.class);
      case 91:
        return gson.fromJson(json, LiquidityPoolWithdrewEffectResponse.class);
      case 92:
        return gson.fromJson(json, LiquidityPoolTradeEffectResponse.class);
      case 93:
        return gson.fromJson(json, LiquidityPoolCreatedEffectResponse.class);
      case 94:
        return gson.fromJson(json, LiquidityPoolRemovedEffectResponse.class);
      case 95:
        return gson.fromJson(json, LiquidityPoolRevokedEffectResponse.class);
      default:
        throw new RuntimeException("Invalid effect type");
    }
  }
}
