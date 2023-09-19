package org.stellar.sdk;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.LedgerEntryType;
import org.stellar.sdk.xdr.LedgerFootprint;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SorobanResources;
import org.stellar.sdk.xdr.SorobanTransactionData;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class SorobanDataBuilderTest {
  LedgerKey readOnly =
      new LedgerKey.Builder()
          .discriminant(LedgerEntryType.ACCOUNT)
          .account(
              new LedgerKey.LedgerKeyAccount.Builder()
                  .accountID(
                      KeyPair.fromAccountId(
                              "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO")
                          .getXdrAccountId())
                  .build())
          .build();
  LedgerKey readWrite =
      new LedgerKey.Builder()
          .discriminant(LedgerEntryType.ACCOUNT)
          .account(
              new LedgerKey.LedgerKeyAccount.Builder()
                  .accountID(
                      KeyPair.fromAccountId(
                              "GAHJJJKMOKYE4RVPZEWZTKH5FVI4PA3VL7GK2LFNUBSGBV6OJP7TQSLX")
                          .getXdrAccountId())
                  .build())
          .build();

  SorobanTransactionData emptySorobanData =
      new SorobanTransactionData.Builder()
          .resources(
              new SorobanResources.Builder()
                  .footprint(
                      new LedgerFootprint.Builder()
                          .readOnly(new LedgerKey[] {})
                          .readWrite(new LedgerKey[] {})
                          .build())
                  .instructions(new Uint32(new XdrUnsignedInteger(0)))
                  .readBytes(new Uint32(new XdrUnsignedInteger(0)))
                  .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                  .build())
          .refundableFee(new Int64(0L))
          .ext(new ExtensionPoint.Builder().discriminant(0).build())
          .build();

  SorobanTransactionData presetSorobanData =
      new SorobanTransactionData.Builder()
          .resources(
              new SorobanResources.Builder()
                  .footprint(
                      new LedgerFootprint.Builder()
                          .readOnly(new LedgerKey[] {readOnly})
                          .readWrite(new LedgerKey[] {readWrite})
                          .build())
                  .instructions(new Uint32(new XdrUnsignedInteger(1)))
                  .readBytes(new Uint32(new XdrUnsignedInteger(2)))
                  .writeBytes(new Uint32(new XdrUnsignedInteger(3)))
                  .build())
          .refundableFee(new Int64(5L))
          .ext(new ExtensionPoint.Builder().discriminant(0).build())
          .build();

  @Test
  public void testConstructorFromEmpty() {
    SorobanTransactionData actualData = new SorobanDataBuilder().build();
    assertEquals(emptySorobanData, actualData);
  }

  @Test
  public void testConstructorFromBase64() throws IOException {
    String base64 = presetSorobanData.toXdrBase64();
    SorobanTransactionData actualData = new SorobanDataBuilder(base64).build();
    assertEquals(presetSorobanData, actualData);
  }

  @Test
  public void testConstructorFromSorobanTransactionData() {
    SorobanTransactionData actualData = new SorobanDataBuilder(presetSorobanData).build();
    assertEquals(presetSorobanData, actualData);
  }

  @Test
  public void testSetProperties() {
    SorobanTransactionData actualData =
        new SorobanDataBuilder()
            .setReadOnly(singletonList(readOnly))
            .setReadWrite(singletonList(readWrite))
            .setRefundableFee(5)
            .setResources(
                new SorobanDataBuilder.Resources.ResourcesBuilder()
                    .cpuInstructions(1L)
                    .readBytes(2L)
                    .writeBytes(3L)
                    .build())
            .build();
    assertEquals(presetSorobanData, actualData);
  }

  @Test
  public void testLeavesUntouchedFootprintsUntouched() {
    SorobanTransactionData data0 =
        new SorobanDataBuilder(presetSorobanData).setReadOnly(null).build();
    assertArrayEquals(
        new LedgerKey[] {readOnly}, data0.getResources().getFootprint().getReadOnly());

    SorobanTransactionData data1 =
        new SorobanDataBuilder(presetSorobanData).setReadOnly(new ArrayList<>()).build();
    assertArrayEquals(new LedgerKey[] {}, data1.getResources().getFootprint().getReadOnly());

    SorobanTransactionData data3 =
        new SorobanDataBuilder(presetSorobanData).setReadWrite(null).build();
    assertArrayEquals(
        new LedgerKey[] {readWrite}, data3.getResources().getFootprint().getReadWrite());

    SorobanTransactionData data4 =
        new SorobanDataBuilder(presetSorobanData).setReadWrite(new ArrayList<>()).build();
    assertArrayEquals(new LedgerKey[] {}, data4.getResources().getFootprint().getReadWrite());
  }

  @Test
  public void testBuildCopy() {
    SorobanTransactionData actualData = new SorobanDataBuilder(presetSorobanData).build();
    assertEquals(presetSorobanData, actualData);
    assertNotSame(presetSorobanData, actualData);
  }
}
