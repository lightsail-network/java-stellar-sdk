package org.stellar.sdk;

import org.junit.Test;
import org.stellar.sdk.responses.LedgerResponse;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.effects.EffectResponse;
import org.stellar.sdk.responses.effects.TrustlineCreatedEffectResponse;

import static org.junit.Assert.assertEquals;

public class AccountFlagTest {
  @Test
  public void testValues() throws Exception {

    Server server = new Server("https://horizon.stellar.org");
    LedgerResponse resposne = server.ledgers()
            .ledger(38115883);

    Page<EffectResponse> resposnes = server.effects()
            .forLedger(38115883).limit(200).execute();

    for (EffectResponse effectResponse : resposnes.getRecords()) {
         if (effectResponse.getType().equals("trustline_created")) {
             TrustlineCreatedEffectResponse changeTrustOperation = (TrustlineCreatedEffectResponse)effectResponse;
             Asset asset = changeTrustOperation.getAsset();
             System.out.print(asset);
         }
    }



    assertEquals(1, AccountFlag.AUTH_REQUIRED_FLAG.getValue());
    assertEquals(2, AccountFlag.AUTH_REVOCABLE_FLAG.getValue());
    assertEquals(4, AccountFlag.AUTH_IMMUTABLE_FLAG.getValue());
  }
}
