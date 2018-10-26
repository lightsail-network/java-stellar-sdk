package org.stellar.sdk;

import org.junit.Test;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.SSEManager;
import org.stellar.sdk.responses.operations.OperationResponse;

public class StreamingSmokeTest {

  @Test
  public void shouldStreamPaymentsFromTestNet() {
    Server server = new Server("https://horizon-testnet.stellar.org/");
    Network.useTestNetwork();
    SSEManager<OperationResponse> manager = null;
    try {
        manager = server.payments().limit(200).streamAccounts(new EventListener<OperationResponse>() {
          @Override
          public void onEvent(OperationResponse or) {
            System.err.println(or.getTransactionHash());
          }
        });

        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
    } finally {
      if(manager != null) {
        manager.close();
      }
    }
  }
}
