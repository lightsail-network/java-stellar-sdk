package org.stellar.sdk.responses;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.stellar.sdk.LiquidityPoolID;

class LiquidityPoolIDDeserializer implements JsonDeserializer<LiquidityPoolID> {
  @Override
  public LiquidityPoolID deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    return new LiquidityPoolID(json.getAsString());
  }
}
