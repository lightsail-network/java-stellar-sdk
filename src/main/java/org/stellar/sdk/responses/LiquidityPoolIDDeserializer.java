package org.stellar.sdk.responses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.stellar.sdk.LiquidityPoolId;

class LiquidityPoolIDDeserializer implements JsonDeserializer<LiquidityPoolId> {
  @Override
  public LiquidityPoolId deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return new LiquidityPoolId(json.getAsString());
  }
}
