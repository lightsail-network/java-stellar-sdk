package org.stellar.sdk;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class KeyPairTest {

  private static final String SEED = "1123740522f11bfef6b3671f51e159ccf589ccf8965262dd5f97d1721d383dd4";

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
    String expectedSig = "587d4b472eeef7d07aafcd0b049640b0bb3f39784118c2e2b73a04fa2f64c9c538b4b2d0f5335e968a480021fdc23e98c0ddf424cb15d8131df8cb6c4bb58309";
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    String data = "hello world";
    byte[] sig = keypair.sign(data.getBytes());
    Assert.assertArrayEquals(Util.hexToBytes(expectedSig), sig);
  }

  @Test
  public void testVerifyTrue() throws Exception {
    String sig = "587d4b472eeef7d07aafcd0b049640b0bb3f39784118c2e2b73a04fa2f64c9c538b4b2d0f5335e968a480021fdc23e98c0ddf424cb15d8131df8cb6c4bb58309";
    String data = "hello world";
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    Assert.assertTrue(keypair.verify(data.getBytes(), Util.hexToBytes(sig)));
  }

  @Test
  public void testVerifyFalse() throws Exception {
    String badSig = "687d4b472eeef7d07aafcd0b049640b0bb3f39784118c2e2b73a04fa2f64c9c538b4b2d0f5335e968a480021fdc23e98c0ddf424cb15d8131df8cb6c4bb58309";
    byte[] corrupt = {0x00};
    String data = "hello world";
    KeyPair keypair = KeyPair.fromSecretSeed(Util.hexToBytes(SEED));
    Assert.assertFalse(keypair.verify(data.getBytes(), Util.hexToBytes(badSig)));
    Assert.assertFalse(keypair.verify(data.getBytes(), corrupt));
  }

  @Test
  public void testFromSecretSeed() throws Exception {
    Map<String, String> keypairs = new HashMap<String, String>();
    keypairs.put("SDJHRQF4GCMIIKAAAQ6IHY42X73FQFLHUULAPSKKD4DFDM7UXWWCRHBE", "GCZHXL5HXQX5ABDM26LHYRCQZ5OJFHLOPLZX47WEBP3V2PF5AVFK2A5D");
    keypairs.put("SDTQN6XUC3D2Z6TIG3XLUTJIMHOSON2FMSKCTM2OHKKH2UX56RQ7R5Y4", "GDEAOZWTVHQZGGJY6KG4NAGJQ6DXATXAJO3AMW7C4IXLKMPWWB4FDNFZ");
    keypairs.put("SDIREFASXYQVEI6RWCQW7F37E6YNXECQJ4SPIOFMMMJRU5CMDQVW32L5", "GD2EVR7DGDLNKWEG366FIKXO2KCUAIE3HBUQP4RNY7LEZR5LDKBYHMM6");
    keypairs.put("SDAPE6RHEJ7745VQEKCI2LMYKZB3H6H366I33A42DG7XKV57673XLCC2", "GDLXVH2BTLCLZM53GF7ELZFF4BW4MHH2WXEA4Z5Z3O6DPNZNR44A56UJ");
    keypairs.put("SDYZ5IYOML3LTWJ6WIAC2YWORKVO7GJRTPPGGNJQERH72I6ZCQHDAJZN", "GABXJTV7ELEB2TQZKJYEGXBUIG6QODJULKJDI65KZMIZZG2EACJU5EA7");

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
    KeyPair keypair = KeyPair.fromAccountId("GDEAOZWTVHQZGGJY6KG4NAGJQ6DXATXAJO3AMW7C4IXLKMPWWB4FDNFZ");
    String data = "hello world";
    try {
      byte[] sig = keypair.sign(data.getBytes());
      fail();
    } catch (RuntimeException e) {
      assertEquals("KeyPair does not contain secret key. Use KeyPair.fromSecretSeed method to create a new KeyPair with a secret key.", e.getMessage());
    }
  }
}
