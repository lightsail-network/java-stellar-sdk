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
import org.stellar.sdk.xdr.LiquidityPoolType;

class LiquidityPoolDeserializer implements JsonDeserializer<LiquidityPoolResponse> {
  @Override
  public LiquidityPoolResponse deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    // Create new Gson object with adapters needed in Operation
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(Asset.class, new AssetDeserializer())
            .registerTypeAdapter(LiquidityPoolID.class, new LiquidityPoolIDDeserializer())
            .registerTypeAdapter(LiquidityPoolType.class, new LiquidityPoolTypeDeserializer())
            .create();

    return gson.fromJson(json, LiquidityPoolResponse.class);
  }
}
