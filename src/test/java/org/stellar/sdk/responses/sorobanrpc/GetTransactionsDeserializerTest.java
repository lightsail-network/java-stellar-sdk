package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetTransactionsDeserializerTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_transactions.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));

    SorobanRpcResponse<GetTransactionsResponse> getTransactionsResponseSorobanRpcResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<GetTransactionsResponse>>() {}.getType());

    GetTransactionsResponse getTransactionsResponse =
        getTransactionsResponseSorobanRpcResponse.getResult();
    assertEquals(getTransactionsResponse.getLatestLedger().longValue(), 1888542L);
    assertEquals(getTransactionsResponse.getLatestLedgerCloseTimestamp().longValue(), 1717166057L);
    assertEquals(getTransactionsResponse.getOldestLedger().longValue(), 1871263L);
    assertEquals(getTransactionsResponse.getOldestLedgerCloseTimestamp().longValue(), 1717075350L);
    assertEquals(getTransactionsResponse.getCursor(), "8111217537191937");
    assertEquals(getTransactionsResponse.getTransactions().size(), 5);
    assertEquals(
        getTransactionsResponse.getTransactions().get(0).getStatus(),
        GetTransactionsResponse.TransactionStatus.FAILED);
    assertEquals(
        getTransactionsResponse.getTransactions().get(0).getApplicationOrder().longValue(), 1L);
    assertEquals(getTransactionsResponse.getTransactions().get(0).getFeeBump(), false);
    assertEquals(
        getTransactionsResponse.getTransactions().get(0).getEnvelopeXdr(),
        getTransactionsResponse.getTransactions().get(0).parseEnvelopeXdr().toXdrBase64());
    assertEquals(
        getTransactionsResponse.getTransactions().get(0).getResultXdr(),
        getTransactionsResponse.getTransactions().get(0).parseResultXdr().toXdrBase64());
    assertEquals(
        getTransactionsResponse.getTransactions().get(0).getResultMetaXdr(),
        getTransactionsResponse.getTransactions().get(0).parseResultMetaXdr().toXdrBase64());
    assertEquals(
        getTransactionsResponse.getTransactions().get(0).getLedger().longValue(), 1888539L);
    assertEquals(
        getTransactionsResponse.getTransactions().get(1).getCreatedAt().longValue(), 1717166042L);

    assertEquals(
        getTransactionsResponse.getTransactions().get(3).getStatus(),
        GetTransactionsResponse.TransactionStatus.SUCCESS);
    assertEquals(
        getTransactionsResponse.getTransactions().get(3).getApplicationOrder().longValue(), 4L);
    assertEquals(getTransactionsResponse.getTransactions().get(3).getFeeBump(), false);
    assertEquals(
        getTransactionsResponse.getTransactions().get(3).getEnvelopeXdr(),
        getTransactionsResponse.getTransactions().get(3).parseEnvelopeXdr().toXdrBase64());
    assertEquals(
        getTransactionsResponse.getTransactions().get(3).getResultXdr(),
        getTransactionsResponse.getTransactions().get(3).parseResultXdr().toXdrBase64());
    assertEquals(
        getTransactionsResponse.getTransactions().get(3).getResultMetaXdr(),
        getTransactionsResponse.getTransactions().get(3).parseResultMetaXdr().toXdrBase64());
    assertEquals(
        getTransactionsResponse.getTransactions().get(3).getLedger().longValue(), 1888539L);
    assertEquals(
        getTransactionsResponse.getTransactions().get(3).getDiagnosticEventsXdr().size(), 19);
    for (int i = 0; i < 19; i++) {
      assertEquals(
          getTransactionsResponse.getTransactions().get(3).getDiagnosticEventsXdr().get(i),
          getTransactionsResponse
              .getTransactions()
              .get(3)
              .parseDiagnosticEventsXdr()
              .get(i)
              .toXdrBase64());
    }
    assertEquals(
        getTransactionsResponse.getTransactions().get(0).getLedger().longValue(), 1888539L);
    assertEquals(
        getTransactionsResponse.getTransactions().get(1).getCreatedAt().longValue(), 1717166042L);
  }
}
