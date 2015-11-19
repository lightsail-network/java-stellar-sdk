package org.stellar.sdk.responses.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.stellar.sdk.responses.SubmitTransactionResponse;

import java.lang.reflect.Type;

public class SubmitTransactionResponseExtrasDeserializer implements JsonDeserializer<SubmitTransactionResponse.Extras> {
  @Override
  public SubmitTransactionResponse.Extras deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject object = json.getAsJsonObject();
    String envelopeXdr = null;
    JsonElement envelopeXdrElement = object.get("envelope_xdr");
    if (envelopeXdrElement != null) {
      envelopeXdr = envelopeXdrElement.getAsString();
    }
    String resultXdr = null;
    JsonElement resultXdrElement = object.get("result_xdr");
    if (resultXdrElement != null) {
      resultXdr = resultXdrElement.getAsString();
    }
    return new SubmitTransactionResponse.Extras(envelopeXdr, resultXdr);
  }
}
