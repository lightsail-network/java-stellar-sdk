package org.stellar.sdk.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.responses.effects.EffectResponse;
import org.stellar.sdk.responses.operations.OperationResponse;

import java.lang.reflect.Type;

class PageDeserializer<E> implements JsonDeserializer<Page<E>> {
  private TypeToken<Page<E>> pageType;

  /**
   * "Generics on a type are typically erased at runtime, except when the type is compiled with the
   * generic parameter bound. In that case, the compiler inserts the generic type information into
   * the compiled class. In other cases, that is not possible."
   * More info: http://stackoverflow.com/a/14506181
   * @param pageType
   */
  public PageDeserializer(TypeToken<Page<E>> pageType) {
    this.pageType = pageType;
  }

  @Override
  public Page<E> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Flatten the object so it has two fields `records` and `links`
    JsonObject newJson = new JsonObject();
    newJson.add("records", json.getAsJsonObject().get("_embedded").getAsJsonObject().get("records"));
    newJson.add("links", json.getAsJsonObject().get("_links"));

    // Create new Gson object with adapters needed in Page
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Asset.class, new AssetDeserializer())
            .registerTypeAdapter(KeyPair.class, new KeyPairTypeAdapter().nullSafe())
            .registerTypeAdapter(OperationResponse.class, new OperationDeserializer())
            .registerTypeAdapter(EffectResponse.class, new EffectDeserializer())
            .registerTypeAdapter(TransactionResponse.class, new TransactionDeserializer())
            .create();

    return gson.fromJson(newJson, pageType.getType());
  }
}
