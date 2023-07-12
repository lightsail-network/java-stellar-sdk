package org.stellar.sdk;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.xdr.DecoratedSignature;

public class KeyPairTest {

  private static final String SEED =
      "1123740522f11bfef6b3671f51e159ccf589ccf8965262dd5f97d1721d383dd4";

  @Test
  public void testFromSecretSeedCharArray() {
    KeyPair original =
        KeyPair.fromSecretSeed("SDMMJC2BSGESMFQ53MF3WECMCQGJVRY3TJ45J7PYZ53GZZ36NDDDWEDM");

    char[] seed = original.getSecretSeed();
    KeyPair newPair = KeyPair.fromSecretSeed(seed);

    assertArrayEquals(original.getSecretSeed(), newPair.getSecretSeed());
    assertEquals(original.getAccountId(), newPair.getAccountId());
  }

  @Test
  public void testInvalidPublicKey() {
    try {
      KeyPair.fromAccountId("GAH6H2XPCZS27WMKPTZJPTDN7JMBCDHTLU5WQP7TUI2ORA2M5FY5DHNU");
      fail();
    } catch (RuntimeException e) {
      assertEquals("Public key is invalid", e.getMessage());
    }
  }

  @Test
  public void testSign() {
    String expectedSig =
        "587d4b472eeef7d07aafcd0b049640b0bb3f39784118c2e2b73a04fa2f64c9c538b4b2d0f5335e968a480021fdc23e98c0ddf424cb15d8131df8cb6c4bb58309";
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    String data = "hello world";
    byte[] sig = keypair.sign(data.getBytes());
    Assert.assertArrayEquals(Util.hexToBytes(expectedSig), sig);
  }

  @Test
  public void testVerifyTrue() throws Exception {
    String sig =
        "587d4b472eeef7d07aafcd0b049640b0bb3f39784118c2e2b73a04fa2f64c9c538b4b2d0f5335e968a480021fdc23e98c0ddf424cb15d8131df8cb6c4bb58309";
    String data = "hello world";
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    Assert.assertTrue(keypair.verify(data.getBytes(), Util.hexToBytes(sig)));
  }

  @Test
  public void testVerifyFalse() throws Exception {
    String badSig =
        "687d4b472eeef7d07aafcd0b049640b0bb3f39784118c2e2b73a04fa2f64c9c538b4b2d0f5335e968a480021fdc23e98c0ddf424cb15d8131df8cb6c4bb58309";
    byte[] corrupt = {0x00};
    String data = "hello world";
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    Assert.assertFalse(keypair.verify(data.getBytes(), Util.hexToBytes(badSig)));
    Assert.assertFalse(keypair.verify(data.getBytes(), corrupt));
  }

  @Test
  public void testFromSecretSeed() throws Exception {
    Map<String, String> keypairs = new HashMap<String, String>();
    keypairs.put(
        "SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE",
        "GCZHXL5HXQX5ABDM26LHYRCQZ5OJFHLOPLZX47WEBP3V2PF5AVFK2A5D");
    keypairs.put(
        "SDTQN6XUC3D2Z6TIG3XLUTJIMHOSON2FMSKCTM2OHKKH2UX56RQ7R5Y4",
        "GDEAOZWTVHQZGGJY6KG4NAGJQ6DXATXAJO3AMW7C4IXLKMPWWB4FDNFZ");
    keypairs.put(
        "SDIREFASXYQVEI6RWCQW7F37E6YNXECQJ4SPIOFMMMJRU5CMDQVW32L5",
        "GD2EVR7DGDLNKWEG366FIKXO2KCUAIE3HBUQP4RNY7LEZR5LDKBYHMM6");
    keypairs.put(
        "SDAPE6RHEJ7745VQEKCI2LMYKZB3H6H366I33A42DG7XKV57673XLCC2",
        "GDLXVH2BTLCLZM53GF7ELZFF4BW4MHH2WXEA4Z5Z3O6DPNZNR44A56UJ");
    keypairs.put(
        "SDYZ5IYOML3LTWJ6WIAC2YWORKVO7GJRTPPGGNJQERH72I6ZCQHDAJZN",
        "GABXJTV7ELEB2TQZKJYEGXBUIG6QODJULKJDI65KZMIZZG2EACJU5EA7");

    for (String seed : keypairs.keySet()) {
      String accountId = keypairs.get(seed);
      KeyPair keypair = KeyPair.fromSecretSeed(seed);
      assertEquals(accountId, keypair.getAccountId());
      assertEquals(seed, String.valueOf(keypair.getSecretSeed()));
    }
  }

  @Test
  public void testCanSign() throws Exception {
    KeyPair keypair;
    keypair = KeyPair.fromSecretSeed("SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE");
    assertTrue(keypair.canSign());
    keypair = KeyPair.fromAccountId("GABXJTV7ELEB2TQZKJYEGXBUIG6QODJULKJDI65KZMIZZG2EACJU5EA7");
    assertFalse(keypair.canSign());
  }

  @Test
  public void testSignWithoutSecret() {
    KeyPair keypair =
        KeyPair.fromAccountId("GDEAOZWTVHQZGGJY6KG4NAGJQ6DXATXAJO3AMW7C4IXLKMPWWB4FDNFZ");
    String data = "hello world";
    try {
      byte[] sig = keypair.sign(data.getBytes());
      fail();
    } catch (RuntimeException e) {
      assertEquals(
          "KeyPair does not contain secret key. Use KeyPair.fromSecretSeed method to create a new KeyPair with a secret key.",
          e.getMessage());
    }
  }

  @Test
  public void testSignPayloadSigner() {
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    // the hint from this keypair is [254,66,4,55]

    byte[] payload = new byte[] {1, 2, 3, 4, 5};
    DecoratedSignature sig = keypair.signPayloadDecorated(payload);
    Assert.assertArrayEquals(
        sig.getHint().getSignatureHint(), new byte[] {(byte) (0xFF & 252), 65, 0, 50});
  }

  @Test
  public void testSignPayloadSignerLessThanHint() {
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    // the hint from this keypair is [254,66,4,55]

    byte[] payload = new byte[] {1, 2, 3};
    DecoratedSignature sig = keypair.signPayloadDecorated(payload);
    // the hint could only be derived off of 3 bytes from payload
    Assert.assertArrayEquals(
        sig.getHint().getSignatureHint(), new byte[] {(byte) (255), 64, 7, 55});
  }

  @Test
  public void testPublicEqual() {
    KeyPair keypair =
        KeyPair.fromAccountId("GDEAOZWTVHQZGGJY6KG4NAGJQ6DXATXAJO3AMW7C4IXLKMPWWB4FDNFZ");
    KeyPair keypairCopy =
        KeyPair.fromAccountId("GDEAOZWTVHQZGGJY6KG4NAGJQ6DXATXAJO3AMW7C4IXLKMPWWB4FDNFZ");
    Assert.assertEquals(keypairCopy, keypair);
  }

  @Test
  public void testPublicPrivateNotEquals() {
    KeyPair keypair = KeyPair.random();
    KeyPair keypairPublicCopy = KeyPair.fromAccountId(keypair.getAccountId());
    Assert.assertNotEquals(keypairPublicCopy, keypair);
  }

  @Test
  public void testPrivateEquals() {
    KeyPair keyPair = KeyPair.random();
    KeyPair keypairCopy = KeyPair.fromSecretSeed(keyPair.getSecretSeed());
    Assert.assertEquals(keyPair, keypairCopy);
  }
}
