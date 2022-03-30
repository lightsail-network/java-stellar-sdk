package org.stellar.sdk;

import org.junit.Test;
import org.stellar.sdk.xdr.Duration;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.PreconditionType;
import org.stellar.sdk.xdr.Preconditions;
import org.stellar.sdk.xdr.PreconditionsV2;
import org.stellar.sdk.xdr.SequenceNumber;
import org.stellar.sdk.xdr.SignerKey;
import org.stellar.sdk.xdr.Uint32;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TransactionPreconditionsTest {

    @Test
    public void itConvertsFromXdr() {

        Preconditions.Builder preconditionsBuilder = new Preconditions.Builder();
        preconditionsBuilder.discriminant(PreconditionType.PRECOND_V2);
        PreconditionsV2.Builder v2Builder = new PreconditionsV2.Builder();

        v2Builder.extraSigners(new SignerKey[]{});
        v2Builder.minSeqAge(new Duration(new Int64(2L)));
        v2Builder.ledgerBounds(new org.stellar.sdk.xdr.LedgerBounds.Builder()
                .minLedger(new Uint32(1))
                .maxLedger(new Uint32(2))
                .build());
        v2Builder.minSeqNum(new SequenceNumber(new Int64(4L)));
        preconditionsBuilder.v2(v2Builder.build());
        Preconditions xdr = preconditionsBuilder.build();

        TransactionPreconditions transactionPreconditions = TransactionPreconditions.fromXdr(xdr);

        assertEquals(transactionPreconditions.getMinSeqAge(), Long.valueOf(2));
        assertEquals(transactionPreconditions.getLedgerBounds().getMinLedger(), 1);
        assertEquals(transactionPreconditions.getLedgerBounds().getMaxLedger(), 2);
        assertEquals(transactionPreconditions.getMinSeqNumber(), Long.valueOf(4));
    }

    @Test
    public void itConvertsToV2Xdr() {
        TransactionPreconditions preconditions = TransactionPreconditions.builder()
                .timeBounds(new TimeBounds(1, 2))
                .minSeqNumber(3L)
                .extraSigners(newArrayList(new SignerKey.Builder().build(), new SignerKey.Builder().build(), new SignerKey.Builder().build()))
                .build();

        Preconditions xdr = preconditions.toXdr();
        assertEquals(xdr.getDiscriminant(), PreconditionType.PRECOND_V2);
        assertEquals(xdr.getV2().getTimeBounds().getMinTime().getTimePoint().getUint64(), Long.valueOf(1));
        assertEquals(xdr.getV2().getTimeBounds().getMaxTime().getTimePoint().getUint64(), Long.valueOf(2));
        assertEquals(xdr.getV2().getMinSeqNum().getSequenceNumber().getInt64(), Long.valueOf(3));
        assertEquals(xdr.getV2().getExtraSigners().length, 3);
    }

    @Test
    public void itConvertsOnlyTimeBoundsXdr() {
        TransactionPreconditions preconditions = TransactionPreconditions.builder()
                .timeBounds(new TimeBounds(1, 2))
                .build();

        Preconditions xdr = preconditions.toXdr();
        assertEquals(xdr.getDiscriminant(), PreconditionType.PRECOND_TIME);
        assertEquals(xdr.getTimeBounds().getMinTime().getTimePoint().getUint64(), Long.valueOf(1));
        assertEquals(xdr.getTimeBounds().getMaxTime().getTimePoint().getUint64(), Long.valueOf(2));
    }

    @Test
    public void itConvertsNullTimeBoundsXdr() {
        // there was precedence in the sdk test coverage in TransactionTest.java for edge case of passing a null timebounds
        // into a transaction, which occurrs when infinite timeout is set and timebounds is not set through TransactionBuilder.
        // TransactionPreconditions continues to support that edge case.
        TransactionPreconditions preconditions = TransactionPreconditions.builder().build();

        Preconditions xdr = preconditions.toXdr();
        assertEquals(xdr.getDiscriminant(), PreconditionType.PRECOND_NONE);
        assertNull(xdr.getTimeBounds());
    }

    @Test
    public void itChecksValidityWhenTimebounds() {
        TransactionPreconditions preconditions = TransactionPreconditions.builder().timeBounds(new TimeBounds(1, 2)).build();
        preconditions.isValid(false);
    }

    @Test
    public void itChecksNonValidityOfTimeBounds() {
        TransactionPreconditions preconditions = TransactionPreconditions.builder().build();
        try {
            preconditions.isValid(false);
            fail();
        } catch (FormatException ignored) {}
    }

    @Test
    public void itChecksNonValidityOfExtraSignersSize() {
        TransactionPreconditions preconditions = TransactionPreconditions.builder()
                .timeBounds(new TimeBounds(1, 2))
                .extraSigners(newArrayList(new SignerKey.Builder().build(), new SignerKey.Builder().build(), new SignerKey.Builder().build()))
                .build();
        try {
            preconditions.isValid(false);
            fail();
        } catch (FormatException ignored) {}
    }

    @Test
    public void itChecksValidityWhenNoTimeboundsButTimeoutSet() {
        TransactionPreconditions preconditions = TransactionPreconditions.builder().build();
        preconditions.isValid(true);
    }

    @Test
    public void itChecksValidityWhenNoTimeboundsAndNoTimeoutSet() {
        TransactionPreconditions preconditions = TransactionPreconditions.builder().build();
        try {
            preconditions.isValid(false);
            fail();
        } catch (FormatException exception) {
            assertTrue(exception.getMessage().contains("Invalid preconditions, must define timebounds or set infinite timeout"));
        }
    }


    @Test
    public void itChecksV2Status() {
        Preconditions.Builder preconditionsBuilder = new Preconditions.Builder();
        preconditionsBuilder.discriminant(PreconditionType.PRECOND_V2);
        PreconditionsV2.Builder v2Builder = new PreconditionsV2.Builder();

        v2Builder.extraSigners(new SignerKey[]{});
        v2Builder.minSeqAge(new Duration(new Int64(2L)));
        v2Builder.ledgerBounds(new org.stellar.sdk.xdr.LedgerBounds.Builder()
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
