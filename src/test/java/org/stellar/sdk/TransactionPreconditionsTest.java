package org.stellar.sdk;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.xdr.Duration;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.PreconditionType;
import org.stellar.sdk.xdr.Preconditions;
import org.stellar.sdk.xdr.PreconditionsV2;
import org.stellar.sdk.xdr.SequenceNumber;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.SignerKeyType;
import org.stellar.sdk.xdr.TimePoint;
import org.stellar.sdk.xdr.Uint256;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.Uint64;
import org.stellar.sdk.xdr.XdrDataInputStream;
import org.stellar.sdk.xdr.XdrDataOutputStream;

public class TransactionPreconditionsTest {

  @Test
  public void itConvertsFromXdr() throws IOException {

    Preconditions.Builder preconditionsBuilder = new Preconditions.Builder();
    preconditionsBuilder.discriminant(PreconditionType.PRECOND_V2);
    PreconditionsV2.Builder v2Builder = new PreconditionsV2.Builder();

    v2Builder.extraSigners(new SignerKey[] {});
    v2Builder.minSeqAge(new Duration(new Uint64(2L)));
    v2Builder.ledgerBounds(
        new org.stellar.sdk.xdr.LedgerBounds.Builder()
            .minLedger(new Uint32(1))
            .maxLedger(new Uint32(2))
            .build());
    v2Builder.minSeqNum(new SequenceNumber(new Int64(4L)));
    v2Builder.minSeqLedgerGap(new Uint32(0));
    preconditionsBuilder.v2(v2Builder.build());
    Preconditions xdr = preconditionsBuilder.build();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xdr.encode(new XdrDataOutputStream(baos));
    xdr =
        Preconditions.decode(new XdrDataInputStream(new ByteArrayInputStream(baos.toByteArray())));

    TransactionPreconditions transactionPreconditions = TransactionPreconditions.fromXdr(xdr);

    assertEquals(transactionPreconditions.getMinSeqAge(), 2);
    assertEquals(transactionPreconditions.getLedgerBounds().getMinLedger(), 1);
    assertEquals(transactionPreconditions.getLedgerBounds().getMaxLedger(), 2);
    assertEquals(transactionPreconditions.getMinSeqNumber(), Long.valueOf(4));
    assertEquals(transactionPreconditions.getMinSeqLedgerGap(), 0);
  }

  @Test
  public void itRoundTripsFromV2ToV1IfOnlyTimeboundsPresent() throws IOException {

    Preconditions.Builder preconditionsBuilder = new Preconditions.Builder();
    preconditionsBuilder.discriminant(PreconditionType.PRECOND_V2);
    PreconditionsV2.Builder v2Builder = new PreconditionsV2.Builder();
    org.stellar.sdk.xdr.TimeBounds xdrTimeBounds =
        new org.stellar.sdk.xdr.TimeBounds.Builder()
            .minTime(new TimePoint(new Uint64(1L)))
            .maxTime(new TimePoint(new Uint64(2L)))
            .build();
    v2Builder.timeBounds(xdrTimeBounds);
    v2Builder.minSeqLedgerGap(new Uint32(0));
    v2Builder.minSeqAge(new Duration(new Uint64(0L)));
    v2Builder.extraSigners(new SignerKey[] {});
    preconditionsBuilder.v2(v2Builder.build());
    // create V2 Precond with just timebounds
    Preconditions xdr = preconditionsBuilder.build();

    // serialize to binary
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xdr.encode(new XdrDataOutputStream(baos));
    xdr =
        Preconditions.decode(new XdrDataInputStream(new ByteArrayInputStream(baos.toByteArray())));

    // marshal it to pojo
    TransactionPreconditions transactionPreconditions = TransactionPreconditions.fromXdr(xdr);
    assertEquals(transactionPreconditions.getTimeBounds(), new TimeBounds(1L, 2L));

    // marshal the pojo with just timebounds back to xdr, since only timebounds, precond type should
    // be optimized to V!(PRECOND_TIME)
    xdr = transactionPreconditions.toXdr();
    assertEquals(xdr.getDiscriminant(), PreconditionType.PRECOND_TIME);
    assertEquals(xdr.getTimeBounds(), xdrTimeBounds);
    assertNull(xdr.getV2());
  }

  @Test
  public void itConvertsToV2Xdr() throws IOException {

    byte[] payload =
        BaseEncoding.base16()
            .decode(
                "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20".toUpperCase());
    SignerKey signerKey =
        new SignerKey.Builder()
            .discriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD)
            .ed25519SignedPayload(
                new SignerKey.SignerKeyEd25519SignedPayload.Builder()
                    .payload(payload)
                    .ed25519(
                        new Uint256(
                            StrKey.decodeStellarAccountId(
                                "GDW6AUTBXTOC7FIKUO5BOO3OGLK4SF7ZPOBLMQHMZDI45J2Z6VXRB5NR")))
                    .build())
            .build();

    TransactionPreconditions preconditions =
        TransactionPreconditions.builder()
            .timeBounds(new TimeBounds(1, 2))
            .minSeqNumber(3L)
            .extraSigners(newArrayList(signerKey, signerKey, signerKey))
            .build();

    Preconditions xdr = preconditions.toXdr();

    // serialize to binary
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xdr.encode(new XdrDataOutputStream(baos));
    xdr =
        Preconditions.decode(new XdrDataInputStream(new ByteArrayInputStream(baos.toByteArray())));

    assertEquals(xdr.getDiscriminant(), PreconditionType.PRECOND_V2);
    assertEquals(
        xdr.getV2().getTimeBounds().getMinTime().getTimePoint().getUint64(), Long.valueOf(1));
    assertEquals(
        xdr.getV2().getTimeBounds().getMaxTime().getTimePoint().getUint64(), Long.valueOf(2));
    assertEquals(xdr.getV2().getMinSeqNum().getSequenceNumber().getInt64(), Long.valueOf(3));
    // xdr encoding requires non-null for min ledger gap
    assertEquals(xdr.getV2().getMinSeqLedgerGap().getUint32().intValue(), 0);
    // xdr encoding requires non-null for min seq age
    assertEquals(xdr.getV2().getMinSeqAge().getDuration().getUint64().longValue(), 0);
    assertEquals(xdr.getV2().getExtraSigners().length, 3);
  }

  @Test
  public void itConvertsOnlyTimeBoundsXdr() throws IOException {
    TransactionPreconditions preconditions =
        TransactionPreconditions.builder().timeBounds(new TimeBounds(1, 2)).build();

    Preconditions xdr = preconditions.toXdr();

    // serialize to binary
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xdr.encode(new XdrDataOutputStream(baos));
    xdr =
        Preconditions.decode(new XdrDataInputStream(new ByteArrayInputStream(baos.toByteArray())));

    assertEquals(xdr.getDiscriminant(), PreconditionType.PRECOND_TIME);
    assertEquals(xdr.getTimeBounds().getMinTime().getTimePoint().getUint64(), Long.valueOf(1));
    assertEquals(xdr.getTimeBounds().getMaxTime().getTimePoint().getUint64(), Long.valueOf(2));
    assertNull(xdr.getV2());
  }

  @Test
  public void itConvertsNullTimeBoundsXdr() throws IOException {
    // there was precedence in the sdk test coverage in TransactionTest.java for edge case of
    // passing a null timebounds
    // into a transaction, which occurrs when infinite timeout is set and timebounds is not set
    // through TransactionBuilder.
    // TransactionPreconditions continues to support that edge case.
    TransactionPreconditions preconditions = TransactionPreconditions.builder().build();

    Preconditions xdr = preconditions.toXdr();

    // serialize to binary
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xdr.encode(new XdrDataOutputStream(baos));
    xdr =
        Preconditions.decode(new XdrDataInputStream(new ByteArrayInputStream(baos.toByteArray())));

    assertEquals(xdr.getDiscriminant(), PreconditionType.PRECOND_NONE);
    assertNull(xdr.getTimeBounds());
  }

  @Test
  public void itChecksValidityWhenTimebounds() {
    TransactionPreconditions preconditions =
        TransactionPreconditions.builder().timeBounds(new TimeBounds(1, 2)).build();
    preconditions.isValid();
  }

  @Test
  public void itChecksNonValidityOfTimeBounds() {
    TransactionPreconditions preconditions = TransactionPreconditions.builder().build();
    try {
      preconditions.isValid();
      fail();
    } catch (FormatException ignored) {
    }
  }

  @Test
  public void itChecksNonValidityOfExtraSignersSize() {
    TransactionPreconditions preconditions =
        TransactionPreconditions.builder()
            .timeBounds(new TimeBounds(1, 2))
            .extraSigners(
                newArrayList(
                    new SignerKey.Builder().build(),
                    new SignerKey.Builder().build(),
                    new SignerKey.Builder().build()))
            .build();
    try {
      preconditions.isValid();
      fail();
    } catch (FormatException ignored) {
    }
  }

  @Test
  public void itChecksValidityWhenNoTimeboundsSet() {
    TransactionPreconditions preconditions = TransactionPreconditions.builder().build();
    try {
      preconditions.isValid();
      fail();
    } catch (FormatException exception) {
      assertTrue(exception.getMessage().contains("Invalid preconditions, must define timebounds"));
    }
  }

  @Test
  public void itChecksV2Status() {
    Preconditions.Builder preconditionsBuilder = new Preconditions.Builder();
    preconditionsBuilder.discriminant(PreconditionType.PRECOND_V2);
    PreconditionsV2.Builder v2Builder = new PreconditionsV2.Builder();

    v2Builder.extraSigners(new SignerKey[] {});
    v2Builder.minSeqAge(new Duration(new Uint64(2L)));
    v2Builder.ledgerBounds(
        new org.stellar.sdk.xdr.LedgerBounds.Builder()
            .minLedger(new Uint32(1))
            .maxLedger(new Uint32(2))
            .build());
    v2Builder.minSeqNum(new SequenceNumber(new Int64(4L)));
    preconditionsBuilder.v2(v2Builder.build());
    Preconditions xdr = preconditionsBuilder.build();

    TransactionPreconditions transactionPreconditions = TransactionPreconditions.fromXdr(xdr);
    assertTrue(transactionPreconditions.hasV2());
  }
}
