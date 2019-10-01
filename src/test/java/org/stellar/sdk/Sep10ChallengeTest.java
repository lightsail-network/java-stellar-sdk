package org.stellar.sdk;

import com.google.common.io.BaseEncoding;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Sep10ChallengeTest {

  @Test
  public void testChallenge() throws IOException {
    KeyPair server = KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS");
    String client = "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR";

    long now = System.currentTimeMillis() / 1000L;
    long end = now + 300;
    TimeBounds timeBounds = new TimeBounds(now, end);

    String challenge = Sep10Challenge.newChallenge(
        server,
        Network.TESTNET,
        client,
        "angkor wat",
        timeBounds
    );

    Transaction transaction = Transaction.fromEnvelopeXdr(challenge, Network.TESTNET);

    assertEquals(Network.TESTNET, transaction.getNetwork());
    assertEquals(100, transaction.getFee());
    assertEquals(timeBounds, transaction.getTimeBounds());
    assertEquals(server.getAccountId(), transaction.getSourceAccount());
    assertEquals(0, transaction.getSequenceNumber());

    assertEquals(1, transaction.getOperations().length);
    ManageDataOperation op = (ManageDataOperation)transaction.getOperations()[0];
    assertEquals(client, op.getSourceAccount());
    assertEquals("angkor wat auth", op.getName());

    assertEquals(64, op.getValue().length);
    BaseEncoding base64Encoding = BaseEncoding.base64();
    assertTrue(base64Encoding.canDecode(new String(op.getValue())));

    assertEquals(1, transaction.getSignatures().size());
    assertTrue(
        server.verify(transaction.hash(), transaction.getSignatures().get(0).getSignature().getSignature())
    );
  }
}
