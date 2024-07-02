package org.stellar.sdk.responses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Optional;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.TrustLineAsset;

class TrustLineAssetDeserializer implements JsonDeserializer<TrustLineAsset> {
  @Override
  public TrustLineAsset deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (!json.isJsonObject()) {
      // Probably a canonical string
      return new TrustLineAsset(Asset.create(json.getAsString()));
    }

    String assetType = json.getAsJsonObject().get("asset_type").getAsString();

    if ("liquidity_pool_shares".equals(assetType)) {
      return new TrustLineAsset(
          new LiquidityPoolID(json.getAsJsonObject().get("liquidity_pool_id").getAsString()));
    } else {
      return new TrustLineAsset(
          Asset.create(
              json.getAsJsonObject().get("asset_type").getAsString(),
              getValueAsString(json.getAsJsonObject().get("asset_code")),
              getValueAsString(json.getAsJsonObject().get("asset_issuer"))));
    }
  }

  private String getValueAsString(JsonElement element) {
    return Optional.ofNullable(element).map(JsonElement::getAsString).orElse(null);
  }
}
