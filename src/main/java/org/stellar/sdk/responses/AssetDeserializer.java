package org.stellar.sdk.responses;

import static com.google.common.base.Optional.fromNullable;

import com.google.common.base.Function;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
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
        fromNullable(json.getAsJsonObject().get("asset_code"))
            .transform(ToString.FUNCTION)
            .orNull(),
        fromNullable(json.getAsJsonObject().get("asset_issuer"))
            .transform(ToString.FUNCTION)
            .orNull(),
        fromNullable(json.getAsJsonObject().get("liquidity_pool_id"))
            .transform(ToString.FUNCTION)
            .orNull());
  }

  enum ToString implements Function<JsonElement, String> {
    FUNCTION;

    @Override
    public String apply(JsonElement input) {
      return input.getAsString();
    }
  }
}
