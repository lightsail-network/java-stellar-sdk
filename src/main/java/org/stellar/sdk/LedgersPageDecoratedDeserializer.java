package org.stellar.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class LedgersPageDecoratedDeserializer implements JsonDeserializer<LedgersPageDecorated> {
  @Override
  public LedgersPageDecorated deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Flatten the object so it has two fields `records` and `links`
    JsonObject newJson = new JsonObject();
    newJson.add("records", json.getAsJsonObject().get("_embedded").getAsJsonObject().get("records"));
    newJson.add("links", json.getAsJsonObject().get("_links"));

    LedgersPage page = new Gson().fromJson(newJson, LedgersPage.class);
    return new LedgersPageDecorated(page);
  }
}
