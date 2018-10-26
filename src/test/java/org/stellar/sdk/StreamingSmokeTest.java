package org.stellar.sdk;

import org.junit.Ignore;
import org.junit.Test;
import org.stellar.sdk.requests.EventListener;
import org.stellar.sdk.requests.SSEManager;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicInteger;

public class StreamingSmokeTest {

  @Test
  @Ignore // lets not run this by default for now
  public void shouldStreamPaymentsFromTestNet() {
    final AtomicInteger events = new AtomicInteger();
    Server server = new Server("https://horizon-testnet.stellar.org/");
    Network.useTestNetwork();
    SSEManager<OperationResponse> manager = null;
    try {
        manager = server.payments().limit(100).streamAccounts(new EventListener<OperationResponse>() {
          @Override
          public void onEvent(OperationResponse or) {
//            System.err.println(or.getTransactionHash());
            events.incrementAndGet();
          }
        });

        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        manager.stop();
      int eventCount = events.get();
      Assert.assertTrue(eventCount >0);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Assert.assertTrue(events.get() == eventCount);

    } finally {
      if(manager != null) {
        manager.close();
      }
    }
  }
}
