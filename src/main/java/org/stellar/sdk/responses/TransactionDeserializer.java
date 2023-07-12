package org.stellar.sdk.responses;

import com.google.common.io.BaseEncoding;
import com.google.gson.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import org.stellar.sdk.Memo;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.XdrDataInputStream;

public class TransactionDeserializer implements JsonDeserializer<TransactionResponse> {

  private Memo extractTextMemo(TransactionEnvelope transactionEnvelope) {
    switch (transactionEnvelope.getDiscriminant()) {
      case ENVELOPE_TYPE_TX:
        return Memo.text(transactionEnvelope.getV1().getTx().getMemo().getText().getBytes());
      case ENVELOPE_TYPE_TX_V0:
        return Memo.text(transactionEnvelope.getV0().getTx().getMemo().getText().getBytes());
      case ENVELOPE_TYPE_TX_FEE_BUMP:
        return Memo.text(
            transactionEnvelope
                .getFeeBump()
                .getTx()
                .getInnerTx()
                .getV1()
                .getTx()
                .getMemo()
                .getText()
                .getBytes());
      default:
        throw new IllegalArgumentException(
            "invalid transaction type: " + transactionEnvelope.getDiscriminant());
    }
  }

  @Override
  public TransactionResponse deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    // Create new Gson object with adapters needed in Transaction
    Gson gson = new GsonBuilder().create();

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
        BaseEncoding base64Encoding = BaseEncoding.base64();
        JsonObject jsonResponse = json.getAsJsonObject();

        if (jsonResponse.has("memo_bytes")) {
          // we obtain the memo text from the "memo_bytes" field because the original byte sequence
          // may not be valid utf8
          String memoBase64 = json.getAsJsonObject().get("memo_bytes").getAsString();
          memo = Memo.text(base64Encoding.decode(memoBase64));
        } else {
          // memo_bytes is not available because horizon is running a version older than 1.2.0
          // so we will recover the bytes from the xdr
          String envelopeXdr = json.getAsJsonObject().get("envelope_xdr").getAsString();
          byte[] bytes = base64Encoding.decode(envelopeXdr);
          TransactionEnvelope transactionEnvelope = null;
          try {
            transactionEnvelope =
                TransactionEnvelope.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
          } catch (IOException e) {
            // JsonDeserializer<TransactionResponse> cannot throw IOExceptions
            // so we must throw it as a runtime exception
            throw new RuntimeException(e);
          }
          memo = extractTextMemo(transactionEnvelope);
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
