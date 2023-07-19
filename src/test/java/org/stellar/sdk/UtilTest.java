package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.stellar.sdk.Util.xdrToSorobanTransactionData;

import org.junit.Test;
import org.stellar.sdk.xdr.*;

public class UtilTest {
  @Test
  public void testXdrToSorobanTransactionData() {
    LedgerKey ledgerKey =
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
    SorobanTransactionData sorobanData =
        new SorobanTransactionData.Builder()
            .resources(
                new SorobanResources.Builder()
                    .footprint(
                        new LedgerFootprint.Builder()
                            .readOnly(new LedgerKey[] {ledgerKey})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .extendedMetaDataSizeBytes(new Uint32(216))
                    .readBytes(new Uint32(699))
                    .writeBytes(new Uint32(0))
                    .instructions(new Uint32(34567))
                    .build())
            .refundableFee(new Int64(100L))
            .ext(new ExtensionPoint.Builder().discriminant(0).build())
            .build();
    String data = xdrToSorobanTransactionData(sorobanData);
    String expected =
        "AAAAAAAAAAEAAAAAAAAAAH8wYjTJienWf2nf2TEZi2APPWzmtkwiQHAftisIgyuHAAAAAAAAhwcAAAK7AAAAAAAAANgAAAAAAAAAZA==";
    assertEquals(expected, data);
  }
}
