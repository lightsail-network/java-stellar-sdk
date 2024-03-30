package org.stellar.sdk.responses;

import com.google.gson.*;
import java.lang.reflect.Type;
import org.stellar.sdk.Memo;

public class TransactionDeserializer extends BaseTransactionDeserializer
    implements JsonDeserializer<TransactionResponse> {
  @Override
  public TransactionResponse deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    // Create new Gson object with adapters needed in Transaction
    Gson gson = new GsonBuilder().create();
    TransactionResponse transaction = gson.fromJson(json, TransactionResponse.class);
    Memo memo = extractMemoFromJson(json);
    transaction.setMemo(memo);
    return transaction;
  }
}
