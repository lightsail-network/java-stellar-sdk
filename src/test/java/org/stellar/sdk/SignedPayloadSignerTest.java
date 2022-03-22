package org.stellar.sdk;

import org.junit.Test;
import org.stellar.sdk.xdr.AccountID;

import static org.junit.Assert.fail;

public class SignedPayloadSignerTest {
    @Test
    public void itFailsWhenAccoutIDIsNull() {
        try {
            new SignedPayloadSigner(
                    (AccountID)null,
                    new byte[]{});
            fail("should not create when accountid is null");
        } catch (NullPointerException ignored){}
    }
}
