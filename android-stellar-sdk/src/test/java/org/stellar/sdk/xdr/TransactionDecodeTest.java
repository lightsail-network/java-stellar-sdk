package org.stellar.sdk.xdr;

import org.apache.commons.android.codec.binary.Base64;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionDecodeTest {

    @Test
    public void testDecodeTxBody() throws IOException {
        // pubnet - ledgerseq 5845058, txid  d5ec6645d86cdcae8212cbe60feaefb8d6b1a8b7d11aeea590608b0863ace4de

        String txBody = "AAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAZAAIbkEAACD7AAAAAAAAAAN43bSwpXw8tSAhl7TBtQeOZTQAXwAAAAAAAAAAAAAAAAAAAAEAAAABAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAAAAAAAAAADdVhDVFrUiS/jPrRpblXY4bAW9u4hbRI2Hhw+2ATsFpQAAAAAtPWvAAAAAAAAAAAGPO3yQAAAAQHGWVHCBsjTyap/OY9JjPHmzWtN2Y2sL98aMERc/xJ3hcWz6kdQAwjlEhilItCyokDHCrvALZy3v/1TlaDqprA0=";
        Base64 base64Codec = new Base64();
        byte[] bytes = base64Codec.decode(txBody);

        TransactionEnvelope transactionEnvelope = TransactionEnvelope.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(new Long(2373025265623291L), transactionEnvelope.getTx().getSeqNum().getSequenceNumber().getUint64());
    }

    @Test
    public void testDecodeTxResult() throws IOException {
        // pubnet - ledgerseq 5845058, txid  d5ec6645d86cdcae8212cbe60feaefb8d6b1a8b7d11aeea590608b0863ace4de

        String txResult = "1exmRdhs3K6CEsvmD+rvuNaxqLfRGu6lkGCLCGOs5N4AAAAAAAAAZAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAA==";
        Base64 base64Codec = new Base64();
        byte[] bytes = base64Codec.decode(txResult);

        TransactionResultPair transactionResult = TransactionResultPair.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(TransactionResultCode.txSUCCESS, transactionResult.getResult().getResult().getDiscriminant());
    }

    @Test
    public void testDecodeTxMeta() throws IOException {
        // pubnet - ledgerseq 5845058, txid  d5ec6645d86cdcae8212cbe60feaefb8d6b1a8b7d11aeea590608b0863ace4de

        String txMeta = "AAAAAAAAAAEAAAADAAAAAABZMEIAAAAAAAAAAN1WENUWtSJL+M+tGluVdjhsBb27iFtEjYeHD7YBOwWlAAAAAC09a8AAWTBCAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAwBZL8QAAAAAAAAAAP1qe44j+i4uIT+arbD4QDQBt8ryEeJd7a0jskQ3nwDeAALU1gZ4V7UACD1BAAAAHgAAAAoAAAAAAAAAAAAAAAABAAAAAAAACgAAAAARC07BokpLTOF+/vVKBwiAlop7hHGJTNeGGlY4MoPykwAAAAEAAAAAK+Lzfd3yDD+Ov0GbYu1g7SaIBrKZeBUxoCunkLuI7aoAAAABAAAAAERmsKL73CyLV/HvjyQCERDXXpWE70Xhyb6MR5qPO3yQAAAAAQAAAABSORGwAdyuanN3sNOHqNSpACyYdkUM3L8VafUu69EvEgAAAAEAAAAAeCzqJNkMM/jLvyuMIfyFHljBlLCtDyj17RMycPuNtRMAAAABAAAAAIEi4R7juq15ymL00DNlAddunyFT4FyUD4muC4t3bobdAAAAAQAAAACaNpLL5YMfjOTdXVEqrAh99LM12sN6He6pHgCRAa1f1QAAAAEAAAAAqB+lfAPV9ak+Zkv4aTNZwGaFFAfui4+yhM3dGhoYJ+sAAAABAAAAAMNJrEvdMg6M+M+n4BDIdzsVSj/ZI9SvAp7mOOsvAD/WAAAAAQAAAADbHA6xiKB1+G79mVqpsHMOleOqKa5mxDpP5KEp/Xdz9wAAAAEAAAAAAAAAAAAAAAEAWTBCAAAAAAAAAAD9anuOI/ouLiE/mq2w+EA0AbfK8hHiXe2tI7JEN58A3gAC1NXZOuv1AAg9QQAAAB4AAAAKAAAAAAAAAAAAAAAAAQAAAAAAAAoAAAAAEQtOwaJKS0zhfv71SgcIgJaKe4RxiUzXhhpWODKD8pMAAAABAAAAACvi833d8gw/jr9Bm2LtYO0miAaymXgVMaArp5C7iO2qAAAAAQAAAABEZrCi+9wsi1fx748kAhEQ116VhO9F4cm+jEeajzt8kAAAAAEAAAAAUjkRsAHcrmpzd7DTh6jUqQAsmHZFDNy/FWn1LuvRLxIAAAABAAAAAHgs6iTZDDP4y78rjCH8hR5YwZSwrQ8o9e0TMnD7jbUTAAAAAQAAAACBIuEe47qtecpi9NAzZQHXbp8hU+BclA+JrguLd26G3QAAAAEAAAAAmjaSy+WDH4zk3V1RKqwIffSzNdrDeh3uqR4AkQGtX9UAAAABAAAAAKgfpXwD1fWpPmZL+GkzWcBmhRQH7ouPsoTN3RoaGCfrAAAAAQAAAADDSaxL3TIOjPjPp+AQyHc7FUo/2SPUrwKe5jjrLwA/1gAAAAEAAAAA2xwOsYigdfhu/ZlaqbBzDpXjqimuZsQ6T+ShKf13c/cAAAABAAAAAAAAAAA=";
        Base64 base64Codec = new Base64();
        byte[] bytes = base64Codec.decode(txMeta);

        TransactionMeta transactionMeta = TransactionMeta.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(1, transactionMeta.getOperations().length);
    }

    @Test
    public void testTransactionEnvelopeWithMemo() throws IOException {
        String transactionEnvelopeToDecode = "AAAAACq1Ixcw1fchtF5aLTSw1zaYAYjb3WbBRd4jqYJKThB9AAAAZAA8tDoAAAALAAAAAAAAAAEAAAAZR29sZCBwYXltZW50IGZvciBzZXJ2aWNlcwAAAAAAAAEAAAAAAAAAAQAAAAARREGslec48mbJJygIwZoLvRtL6/gGL4ss2TOpnOUOhgAAAAFHT0xEAAAAACq1Ixcw1fchtF5aLTSw1zaYAYjb3WbBRd4jqYJKThB9AAAAADuaygAAAAAAAAAAAA==";
        Base64 base64Codec = new Base64();
        byte[] bytes = base64Codec.decode(transactionEnvelopeToDecode);

        TransactionEnvelope transactionEnvelope = TransactionEnvelope.decode(new XdrDataInputStream(new ByteArrayInputStream(bytes)));
        assertEquals(1, transactionEnvelope.getTx().getOperations().length);
        assertTrue(Arrays.equals(new byte[]{'G', 'O', 'L', 'D'}, transactionEnvelope.getTx().getOperations()[0].getBody().getPaymentOp().getAsset().getAlphaNum4().getAssetCode()));
    }

}
