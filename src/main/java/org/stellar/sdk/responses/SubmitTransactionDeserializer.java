package org.stellar.sdk.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.stellar.sdk.Memo;

public class SubmitTransactionDeserializer extends BaseTransactionDeserializer
    implements JsonDeserializer<SubmitTransactionResponse> {
  @Override
  public SubmitTransactionResponse deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    // Create new Gson object with adapters needed in Transaction
    Gson gson = new GsonBuilder().create();
    SubmitTransactionResponse transaction = gson.fromJson(json, SubmitTransactionResponse.class);
    Memo memo = extractMemoFromJson(json);
    transaction.setMemo(memo);
    return transaction;
  }
}
