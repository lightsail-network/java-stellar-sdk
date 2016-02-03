package org.stellar.sdk.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.commons.codec.binary.Base64;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;

import java.lang.reflect.Type;

public class TransactionDeserializer implements JsonDeserializer<Transaction> {
  @Override
  public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Create new Gson object with adapters needed in Transaction
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(KeyPair.class, new KeyPairTypeAdapter().nullSafe())
            .create();

    Transaction transaction = gson.fromJson(json, Transaction.class);

    String memoType = json.getAsJsonObject().get("memo_type").getAsString();
    Memo memo;
    if (memoType.equals("none")) {
      memo = Memo.none();
    } else {
      String memoValue = json.getAsJsonObject().get("memo").getAsString();
      if (memoType.equals("text")) {
        memo = Memo.text(memoValue);
      } else if (memoType.equals("id")) {
        memo = Memo.id(Long.parseLong(memoValue));
      } else if (memoType.equals("hash")) {
        memo = Memo.hash(Base64.decodeBase64(memoValue));
      } else if (memoType.equals("return")) {
        memo = Memo.returnHash(Base64.decodeBase64(memoValue));
      } else {
        throw new JsonParseException("Unknown memo type.");
      }
    }

    transaction.setMemo(memo);
    return transaction;
  }
}
