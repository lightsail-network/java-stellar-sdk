package org.stellar.sdk.xdr;

import com.google.common.io.BaseEncoding;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class AccountEntryDecodeTest {

    BaseEncoding base64Encoding = BaseEncoding.base64();

    @Test
    public void testDecodeSignerPayload() throws IOException {
        AccountEntry.Builder bldr = new AccountEntry.Builder();
        Signer signer = new Signer();
        SignerKey signerKey = new SignerKey();
        signerKey.setDiscriminant(SignerKeyType.SIGNER_KEY_TYPE_ED25519_SIGNED_PAYLOAD);
        signerKey.setEd25519SignedPayload(new SignerKey.SignerKeyEd25519SignedPayload());
        signerKey.getEd25519SignedPayload().setPayload(new byte[]{1,2,3,4});
        signerKey.getEd25519SignedPayload().setEd25519(new Uint256(new byte[32]));
        signer.setKey(signerKey);
        signer.setWeight(new Uint32(1));
        bldr.signers(new Signer[]{signer});
        bldr.accountID(new AccountID(new PublicKey.Builder().discriminant(PublicKeyType.PUBLIC_KEY_TYPE_ED25519).ed25519(new Uint256(new byte[32])).build()));
        bldr.seqNum(new SequenceNumber(new Int64(1L)));
        bldr.balance(new Int64(0L));
        bldr.numSubEntries(new Uint32(0));
        bldr.flags(new Uint32(0));
        bldr.homeDomain(new String32(new XdrString("")));
        bldr.thresholds(new Thresholds(new byte[3]));
        bldr.ext(new AccountEntry.AccountEntryExt.Builder().discriminant(0).build());

        AccountEntry xdr = bldr.build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XdrDataOutputStream outputStream = new XdrDataOutputStream(baos);
        AccountEntry.encode(outputStream, xdr);
        String encodedXdr = base64Encoding.encode(baos.toByteArray());

        byte[] decodedbBytes = base64Encoding.decode(encodedXdr);

        AccountEntry accountEntry = AccountEntry.decode(new XdrDataInputStream(new ByteArrayInputStream(decodedbBytes)));
        assertArrayEquals(new byte[32], accountEntry.getSigners()[0].getKey().getEd25519SignedPayload().getEd25519().getUint256());
        assertArrayEquals(new byte[]{1,2,3,4}, accountEntry.getSigners()[0].getKey().getEd25519SignedPayload().getPayload());
    }
}
