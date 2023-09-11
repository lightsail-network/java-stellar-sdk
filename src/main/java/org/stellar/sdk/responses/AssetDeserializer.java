package org.stellar.sdk.responses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Optional;
import org.stellar.sdk.Asset;

class AssetDeserializer implements JsonDeserializer<Asset> {
  @Override
  public Asset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (!json.isJsonObject()) {
      // Probably a canonical string
      return Asset.create(json.getAsString());
    }

    return Asset.create(
        json.getAsJsonObject().get("asset_type").getAsString(),
        getValueAsString(json.getAsJsonObject().get("asset_code")),
        getValueAsString(json.getAsJsonObject().get("asset_issuer")),
        getValueAsString(json.getAsJsonObject().get("liquidity_pool_id")));
  }

  private String getValueAsString(JsonElement element) {
    return Optional.ofNullable(element).map(JsonElement::getAsString).orElse(null);
  }
}
