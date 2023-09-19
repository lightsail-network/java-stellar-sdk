package org.stellar.sdk;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.XdrDataInputStream;

public class AddressTest {
  @Test
  public void testConstructorAccountId() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    Address address = new Address(accountId);
    assertEquals(address.toString(), accountId);
    assertEquals(address.getAddressType(), Address.AddressType.ACCOUNT);
  }

  @Test
  public void testConstructorContractId() {
    String contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    Address address = new Address(contractId);
    assertEquals(address.toString(), contractId);
    assertEquals(address.getAddressType(), Address.AddressType.CONTRACT);
  }

  @Test
  public void testConstructorInvalidAddressThrows() {
    String accountId = "GINVALID";
    try {
      new Address(accountId);
      fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Unsupported address type", e.getMessage());
    }
  }

  @Test
  public void testConstructorSecretThrows() {
    String secret = "SBUIAXRYKAEJWBSJZYE6P4N4X4ATXP5GAFK5TZ6SKKQ6TS4MLX6G6E4M";
    try {
      new Address(secret);
      fail();
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Unsupported address type", e.getMessage());
    }
  }

  @Test
  public void testFromAccountByte() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    byte[] accountIdBytes = StrKey.decodeStellarAccountId(accountId);
    Address address = Address.fromAccount(accountIdBytes);
    assertEquals(address.toString(), accountId);
    assertEquals(address.getAddressType(), Address.AddressType.ACCOUNT);
  }

  @Test
  public void testFromContractByte() {
    String contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    byte[] contractIdBytes = StrKey.decodeContractId(contractId);
    Address address = Address.fromContract(contractIdBytes);
    assertEquals(address.toString(), contractId);
    assertEquals(address.getAddressType(), Address.AddressType.CONTRACT);
  }

  @Test
  public void testToSCAddressAccount() throws IOException {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    Address address = new Address(accountId);
    SCAddress scAddress = address.toSCAddress();

    String xdr = "AAAAAAAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    byte[] bytes = Base64.getDecoder().decode(xdr);
    SCAddress expectScAddress =
        SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(scAddress, expectScAddress);
  }

  @Test
  public void testToSCAddressContract() throws IOException {
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    Address address = new Address(contract);
    SCAddress scAddress = address.toSCAddress();

    String xdr = "AAAAAT8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia";
    byte[] bytes = Base64.getDecoder().decode(xdr);
    SCAddress expectScAddress =
        SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(scAddress, expectScAddress);
  }

  @Test
  public void testFromSCAddressAccount() throws IOException {
    String xdr = "AAAAAAAAAAA/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    byte[] bytes = Base64.getDecoder().decode(xdr);
    SCAddress scAddress = SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));

    Address address = Address.fromSCAddress(scAddress);
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    assertEquals(address.toString(), accountId);
    assertEquals(address.getAddressType(), Address.AddressType.ACCOUNT);
  }

  @Test
  public void testFromSCAddressContract() throws IOException {
    String xdr = "AAAAAT8MNL+TrQ2ZcdBMzJD3BVEcg4qtlzSkovsNegP8f+ia";
    byte[] bytes = Base64.getDecoder().decode(xdr);
    SCAddress scAddress = SCAddress.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));

    Address address = Address.fromSCAddress(scAddress);
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    assertEquals(address.toString(), contract);
    assertEquals(address.getAddressType(), Address.AddressType.CONTRACT);
  }

  @Test
  public void testToSCVal() throws IOException {
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    Address address = new Address(contract);
    SCVal scVal = address.toSCVal();

    String xdr = "AAAAEgAAAAE/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    byte[] bytes = Base64.getDecoder().decode(xdr);
    SCVal expectSCVal = SCVal.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
    assertEquals(scVal, expectSCVal);
  }

  @Test
  public void testFromSCVal() throws IOException {
    String xdr = "AAAAEgAAAAE/DDS/k60NmXHQTMyQ9wVRHIOKrZc0pKL7DXoD/H/omg==";
    byte[] bytes = Base64.getDecoder().decode(xdr);
    SCVal scVal = SCVal.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));

    Address address = Address.fromSCVal(scVal);
    String contract = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    assertEquals(address.toString(), contract);
    assertEquals(address.getAddressType(), Address.AddressType.CONTRACT);
  }

  @Test
  public void testToStringAccountId() {
    String accountId = "GA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJVSGZ";
    Address address = new Address(accountId);
    assertEquals(address.toString(), accountId);
  }

  @Test
  public void testToStringContractId() {
    String contractId = "CA7QYNF7SOWQ3GLR2BGMZEHXAVIRZA4KVWLTJJFC7MGXUA74P7UJUWDA";
    Address address = new Address(contractId);
    assertEquals(address.toString(), contractId);
  }
}
