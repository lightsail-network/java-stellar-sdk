package org.stellar.sdk.responses;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;

import java.lang.reflect.Type;

public class TransactionDeserializer implements JsonDeserializer<TransactionResponse> {
  @Override
  public TransactionResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    // Create new Gson object with adapters needed in Transaction
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(KeyPair.class, new KeyPairTypeAdapter().nullSafe())
            .create();

    TransactionResponse transaction = gson.fromJson(json, TransactionResponse.class);

    String memoType = json.getAsJsonObject().get("memo_type").getAsString();
    Memo memo;
    if (memoType.equals("none")) {
      memo = Memo.none();
    } else {
      // Because of the way "encoding/json" works on structs in Go, if transaction
      // has an empty `memo_text` value, the `memo` field won't be present in a JSON
      // representation of a transaction. That's why we need to handle a special case
      // here.
      if (memoType.equals("text")) {
        JsonElement memoField = json.getAsJsonObject().get("memo");
        if (memoField != null) {
          memo = Memo.text(memoField.getAsString());
        } else {
          memo = Memo.text("");
        }
      } else {
        String memoValue = json.getAsJsonObject().get("memo").getAsString();
        BaseEncoding base64Encoding = BaseEncoding.base64();
        if (memoType.equals("id")) {
          memo = Memo.id(Long.parseUnsignedLong(memoValue));
        } else if (memoType.equals("hash")) {
          memo = Memo.hash(base64Encoding.decode(memoValue));
        } else if (memoType.equals("return")) {
          memo = Memo.returnHash(base64Encoding.decode(memoValue));
        } else {
          throw new JsonParseException("Unknown memo type.");
        }
      }
    }

    transaction.setMemo(memo);
    return transaction;
  }
}
