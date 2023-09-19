package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class SendTransactionDeserializerTest {
  @Test
  public void testDeserializeError() {
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonError,
                new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertEquals(data.getErrorResultXdr(), "AAAAAAAAf67////6AAAAAA==");
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.ERROR);
    assertEquals(
        data.getHash(), "3b0c982bb8245be869d34ec822f999deb68f3a8480cf6e663643cf2f6e397e64");
    assertEquals(data.getLatestLedger().longValue(), 62L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690447331L);
  }

  @Test
  public void testDeserializePending() {
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonPending,
                new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertNull(data.getErrorResultXdr());
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.PENDING);
    assertEquals(
        data.getHash(), "5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13");
    assertEquals(data.getLatestLedger().longValue(), 3449L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690444223L);
  }

  @Test
  public void testDeserializeDuplicate() {
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonDuplicate,
                new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertNull(data.getErrorResultXdr());
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.DUPLICATE);
    assertEquals(
        data.getHash(), "5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13");
    assertEquals(data.getLatestLedger().longValue(), 3449L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690444223L);
  }

  @Test
  public void testDeserializeTryAgainLater() {
    SorobanRpcResponse<SendTransactionResponse> getTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonTryAgainLater,
                new TypeToken<SorobanRpcResponse<SendTransactionResponse>>() {}.getType());
    SendTransactionResponse data = getTransactionResponse.getResult();
    assertNull(data.getErrorResultXdr());
    assertEquals(data.getStatus(), SendTransactionResponse.SendTransactionStatus.TRY_AGAIN_LATER);
    assertEquals(
        data.getHash(), "5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13");
    assertEquals(data.getLatestLedger().longValue(), 3449L);
    assertEquals(data.getLatestLedgerCloseTime().longValue(), 1690444223L);
  }

  String jsonError =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"b96311af98d54d7cbb8736dbb0ed7730\",\n"
          + "    \"result\": {\n"
          + "        \"errorResultXdr\": \"AAAAAAAAf67////6AAAAAA==\",\n"
          + "        \"status\": \"ERROR\",\n"
          + "        \"hash\": \"3b0c982bb8245be869d34ec822f999deb68f3a8480cf6e663643cf2f6e397e64\",\n"
          + "        \"latestLedger\": \"62\",\n"
          + "        \"latestLedgerCloseTime\": \"1690447331\"\n"
          + "    }\n"
          + "}";

  String jsonPending =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"ce651a0633e8407e9f377127bd649476\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"PENDING\",\n"
          + "        \"hash\": \"5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13\",\n"
          + "        \"latestLedger\": \"3449\",\n"
          + "        \"latestLedgerCloseTime\": \"1690444223\"\n"
          + "    }\n"
          + "}";

  String jsonDuplicate =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"ce651a0633e8407e9f377127bd649476\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"DUPLICATE\",\n"
          + "        \"hash\": \"5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13\",\n"
          + "        \"latestLedger\": \"3449\",\n"
          + "        \"latestLedgerCloseTime\": \"1690444223\"\n"
          + "    }\n"
          + "}";

  String jsonTryAgainLater =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"ce651a0633e8407e9f377127bd649476\",\n"
          + "    \"result\": {\n"
          + "        \"status\": \"TRY_AGAIN_LATER\",\n"
          + "        \"hash\": \"5e58bb3530cf4ff852805ad1a5077b181b227e541301bdfa17f5a66991910d13\",\n"
          + "        \"latestLedger\": \"3449\",\n"
          + "        \"latestLedgerCloseTime\": \"1690444223\"\n"
          + "    }\n"
          + "}";
}
