package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class SendTransactionDeserializerTest {
  @Test
  public void testDeserializeError() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/send_transaction_error.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertEquals(data.getErrorResultXdr(), "AAAAAAAAf67////6AAAAAA==");
    assertEquals(data.getDiagnosticEventsXdr().size(), 1);
    assertEquals(
        data.getDiagnosticEventsXdr().get(0),
        "AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgr/p6gt6h8MrmSw+WNJnu3+sCP9dHXx7jR8IH0sG6Cy0AAAAPAAAABWhlbGxvAAAAAAAADwAAAAVBbG9oYQAAAA==");
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.ERROR);
    assertEquals(
        data.getHash(), "3b0c982bb8245be869d34ec822f999deb68f3a8480cf6e663643cf2f6e397e64");
    assertEquals(data.getLatestLedger().longValue(), 62L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690447331L);
    assertNotNull(data.parseDiagnosticEventsXdr());
    assertNotNull(data.parseErrorResultXdr());
    assertEquals(
        data.parseDiagnosticEventsXdr().get(0).toXdrBase64(), data.getDiagnosticEventsXdr().get(0));
    assertEquals(data.parseErrorResultXdr().toXdrBase64(), data.getErrorResultXdr());
  }

  @Test
  public void testDeserializePending() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/send_transaction_pending.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertNull(data.getErrorResultXdr());
    assertNull(data.getDiagnosticEventsXdr());
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.PENDING);
    assertEquals(
        data.getHash(), "5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13");
    assertEquals(data.getLatestLedger().longValue(), 3449L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690444223L);
    assertNull(data.parseDiagnosticEventsXdr());
    assertNull(data.parseErrorResultXdr());
  }

  @Test
  public void testDeserializeDuplicate() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/send_transaction_duplicate.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertNull(data.getErrorResultXdr());
    assertNull(data.getDiagnosticEventsXdr());
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.DUPLICATE);
    assertEquals(
        data.getHash(), "5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13");
    assertEquals(data.getLatestLedger().longValue(), 3449L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690444223L);
    assertNull(data.parseDiagnosticEventsXdr());
    assertNull(data.parseErrorResultXdr());
  }

  @Test
  public void testDeserializeTryAgainLater() throws IOException {
    String filePath =
        "src/test/resources/responses/sorobanrpc/send_transaction_try_again_later.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertNull(data.getErrorResultXdr());
    assertNull(data.getDiagnosticEventsXdr());
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.TRY_AGAIN_LATER);
    assertEquals(
        data.getHash(), "5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13");
    assertEquals(data.getLatestLedger().longValue(), 3449L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690444223L);
    assertNull(data.parseDiagnosticEventsXdr());
    assertNull(data.parseErrorResultXdr());
  }
}
