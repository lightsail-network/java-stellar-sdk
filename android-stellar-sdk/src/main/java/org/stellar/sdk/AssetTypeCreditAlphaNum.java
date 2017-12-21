package org.stellar.sdk;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for AssetTypeCreditAlphaNum4 and AssetTypeCreditAlphaNum12 subclasses.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/assets.html" target="_blank">Assets</a>
 */
public abstract class AssetTypeCreditAlphaNum extends Asset {
    protected final String mCode;
    protected final KeyPair mIssuer;

    public AssetTypeCreditAlphaNum(String code, KeyPair issuer) {
        checkNotNull(code, "code cannot be null");
        checkNotNull(issuer, "issuer cannot be null");
        mCode = new String(code);
        mIssuer = KeyPair.fromAccountId(issuer.getAccountId());
    }

    /**
     * Returns asset code
     */
    public String getCode() {
        return new String(mCode);
    }

    /**
     * Returns asset issuer
     */
    public KeyPair getIssuer() {
        return KeyPair.fromAccountId(mIssuer.getAccountId());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.getCode(), this.getIssuer().getAccountId()});
    }

    @Override
    public boolean equals(Object object) {
        if (!this.getClass().equals(object.getClass())) {
            return false;
        }

        AssetTypeCreditAlphaNum o = (AssetTypeCreditAlphaNum) object;

        return this.getCode().equals(o.getCode()) &&
                this.getIssuer().getAccountId().equals(o.getIssuer().getAccountId());
    }
}
