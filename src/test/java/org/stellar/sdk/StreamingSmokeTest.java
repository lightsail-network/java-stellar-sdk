package org.stellar.sdk;

import okhttp3.sse.EventSource;
import org.junit.Test;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.PaymentsRequestBuilder;
import org.stellar.sdk.responses.operations.OperationResponse;

public class StreamingSmokeTest {

  @Test
  public void shouldStreamPaymentsFromTestNet() {
    Server server = new Server("https://horizon-testnet.stellar.org/");
    Network.useTestNetwork();

    EventSource eventSource = server.payments().limit(1000).streamAccounts(new EventListener<OperationResponse>() {
      @Override
      public void onEvent(OperationResponse or) {
        System.err.println(or.getTransactionHash());
      }
    });

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
