package org.stellar.sdk;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.stellar.base.Asset;
import org.stellar.base.AssetTypeNative;
import org.stellar.base.Keypair;

import java.lang.reflect.Type;

class AssetDeserializer implements JsonDeserializer<Asset> {
  @Override
  public Asset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    String type = json.getAsJsonObject().get("asset_type").getAsString();
    if (type == "native") {
      return new AssetTypeNative();
    } else {
      String code = json.getAsJsonObject().get("asset_code").getAsString();
      String issuer = json.getAsJsonObject().get("asset_issuer").getAsString();
      return Asset.createNonNativeAsset(code, Keypair.fromAddress(issuer));
    }
  }
}
