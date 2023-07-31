package org.stellar.sdk.scval;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.XdrDataInputStream;

public class ScvAddressTest {
  @Test
  public void testConstructorAccountId() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    ScvAddress address = new ScvAddress(accountId);
    assertEquals(address.toString(), accountId);
    assertEquals(address.getAddressType(), ScvAddress.AddressType.ACCOUNT);
  }

  @Test
  public void testConstructorContractId() {
    String contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    ScvAddress address = new ScvAddress(contractId);
    assertEquals(address.toString(), contractId);
    assertEquals(address.getAddressType(), ScvAddress.AddressType.CONTRACT);
  }

  @Test
  public void testConstructorInvalidAddressThrows() {
    String accountId = "GINVALID";
    try {
      new ScvAddress(accountId);
      fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Unsupported address type", e.getMessage());
    }
  }

  @Test
  public void testConstructorSecretThrows() {
    String secret = "SBUIAXRYKAEJWBSJZYE6P4N4X4ATXP5GAFK5TZ6SKKQ6TS4MLX6G6E4M";
    try {
      new ScvAddress(secret);
      fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Unsupported address type", e.getMessage());
    }
  }

  @Test
  public void testFromAccountByte() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    byte[] accountIdBytes = StrKey.decodeStellarAccountId(accountId);
    ScvAddress address = ScvAddress.fromAccount(accountIdBytes);
    assertEquals(address.toString(), accountId);
    assertEquals(address.getAddressType(), ScvAddress.AddressType.ACCOUNT);
  }

  @Test
  public void testFromContractByte() {
    String contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    byte[] contractIdBytes = StrKey.decodeContractId(contractId);
    ScvAddress address = ScvAddress.fromContract(contractIdBytes);
    assertEquals(address.toString(), contractId);
    assertEquals(address.getAddressType(), ScvAddress.AddressType.CONTRACT);
  }

  @Test
  public void testToSCAddressAccount() throws IOException {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    ScvAddress address = new ScvAddress(accountId);
    SCAddress scAddress = address.toSCAddress();

    String xdr = "AAAAAAAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    SCAddress expectScAddress =
        SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(scAddress, expectScAddress);
  }

  @Test
  public void testToSCAddressContract() throws IOException {
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    ScvAddress address = new ScvAddress(contract);
    SCAddress scAddress = address.toSCAddress();

    String xdr = "AAAAAT8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    SCAddress expectScAddress =
        SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(scAddress, expectScAddress);
  }

  @Test
  public void testFromSCAddressAccount() throws IOException {
    String xdr = "AAAAAAAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    SCAddress scAddress = SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));

    ScvAddress address = ScvAddress.fromSCAddress(scAddress);
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    assertEquals(address.toString(), accountId);
    assertEquals(address.getAddressType(), ScvAddress.AddressType.ACCOUNT);
  }

  @Test
  public void testFromSCAddressContract() throws IOException {
    String xdr = "AAAAAT8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    SCAddress scAddress = SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));

    ScvAddress address = ScvAddress.fromSCAddress(scAddress);
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    assertEquals(address.toString(), contract);
    assertEquals(address.getAddressType(), ScvAddress.AddressType.CONTRACT);
  }

  @Test
  public void testToSCVal() throws IOException {
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    ScvAddress address = new ScvAddress(contract);
    SCVal scVal = address.toSCVal();

    String xdr = "AAAAEgAAAAE/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    SCVal expectSCVal = SCVal.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(scVal, expectSCVal);
    assertEquals(address.getSCValType(), SCValType.SCV_ADDRESS);
  }

  @Test
  public void testFromSCVal() throws IOException {
    String xdr = "AAAAEgAAAAE/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    BaseEncoding base64Encoding = BaseEncoding.base64();
    byte[] bytes = base64Encoding.decode(xdr);
    SCVal scVal = SCVal.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));

    ScvAddress address = ScvAddress.fromSCVal(scVal);
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    assertEquals(address.toString(), contract);
    assertEquals(address.getAddressType(), ScvAddress.AddressType.CONTRACT);
  }

  @Test
  public void testToStringAccountId() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    ScvAddress address = new ScvAddress(accountId);
    assertEquals(address.toString(), accountId);
  }

  @Test
  public void testToStringContractId() {
    String contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    ScvAddress address = new ScvAddress(contractId);
    assertEquals(address.toString(), contractId);
  }
}
