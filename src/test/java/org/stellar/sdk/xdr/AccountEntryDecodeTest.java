package org.stellar.sdk.xdr;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

public class AccountEntryDecodeTest {

  @Test
  public void testDecodeSignerPayload() throws IOException {
    AccountEntry.AccountEntryBuilder bldr = AccountEntry.builder();
    Signer signer = new Signer();
    SignerKey signerKey = new SignerKey();
    signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);
    signerKey.setEd25519SignedPayload(new SignerKey.SignerKeyEd25519SignedPayload());
    signerKey.getEd25519SignedPayload().setPayload(new byte[] {1, 2, 3, 4});
    signerKey.getEd25519SignedPayload().setEd25519(new Uint256(new byte[32]));
    signer.setKey(signerKey);
    signer.setWeight(new Uint32(new XdrUnsignedInteger(1)));
    bldr.signers(new Signer[] {signer});
    bldr.accountID(
        new AccountID(
            PublicKey.builder()
                .discriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519)
                .ed25519(new Uint256(new byte[32]))
                .build()));
    bldr.seqNum(new SequenceNumber(new Int64(1L)));
    bldr.balance(new Int64(0L));
    bldr.numSubEntries(new Uint32(new XdrUnsignedInteger(0)));
    bldr.flags(new Uint32(new XdrUnsignedInteger(0)));
    bldr.homeDomain(new String32(new XdrString("")));
    bldr.thresholds(new Thresholds(new byte[4]));
    bldr.ext(
        AccountEntry.AccountEntryExt.builder()
            .discriminant(1)
            .v1(
                AccountEntryExtensionV1.builder()
                    .liabilities(
                        Liabilities.builder().buying(new Int64(0L)).selling(new Int64(0L)).build())
                    .ext(
                        AccountEntryExtensionV1.AccountEntryExtensionV1Ext.builder()
                            .discriminant(2)
                            .v2(
                                AccountEntryExtensionV2.builder()
                                    .numSponsored(new Uint32(new XdrUnsignedInteger(0)))
                                    .numSponsoring(new Uint32(new XdrUnsignedInteger(0)))
                                    .signerSponsoringIDs(new SponsorshipDescriptor[] {})
                                    .ext(
                                        AccountEntryExtensionV2.AccountEntryExtensionV2Ext.builder()
                                            .discriminant(3)
                                            .v3(
                                                AccountEntryExtensionV3.builder()
                                                    .seqLedger(
                                                        new Uint32(new XdrUnsignedInteger(1)))
                                                    .seqTime(
                                                        new TimePoint(
                                                            new Uint64(
                                                                new XdrUnsignedHyperInteger(2L))))
                                                    .ext(
                                                        ExtensionPoint.builder()
                                                            .discriminant(0)
                                                            .build())
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .build())
            .build());

    AccountEntry xdr = bldr.build();

    AccountEntry accountEntry = AccountEntry.fromXdrBase64(xdr.toXdrBase64());
    assertArrayEquals(
        new byte[32],
        accountEntry.getSigners()[0].getKey().getEd25519SignedPayload().getEd25519().getUint256());
    assertArrayEquals(
        new byte[] {1, 2, 3, 4},
        accountEntry.getSigners()[0].getKey().getEd25519SignedPayload().getPayload());
    assertEquals(
        1,
        accountEntry
            .getExt()
            .getV1()
            .getExt()
            .getV2()
            .getExt()
            .getV3()
            .getSeqLedger()
            .getUint32()
            .getNumber()
            .longValue());
    assertEquals(
        2L,
        accountEntry
            .getExt()
            .getV1()
            .getExt()
            .getV2()
            .getExt()
            .getV3()
            .getSeqTime()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .longValue());
  }
}
