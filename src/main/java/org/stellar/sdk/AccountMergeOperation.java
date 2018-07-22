package org.stellar.sdk;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.stellar.sdk.xdr.AccountID;
import org.stellar.sdk.xdr.Operation.OperationBody;
import org.stellar.sdk.xdr.OperationType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html#account-merge" target="_blank">AccountMerge</a> operation.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">List of Operations</a>
 */
public class AccountMergeOperation extends Operation {

    private final KeyPair destination;

    private AccountMergeOperation(KeyPair destination) {
        this.destination = checkNotNull(destination, "destination cannot be null");
    }

    /**
     * The account that receives the remaining XLM balance of the source account.
     */
    public KeyPair getDestination() {
        return destination;
    }

    @Override
    OperationBody toOperationBody() {
        OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
        AccountID destination = new AccountID();
        destination.setAccountID(this.destination.getXdrPublicKey());
        body.setDestination(destination);
        body.setDiscriminant(OperationType.ACCOUNT_MERGE);
        return body;
    }

    /**
     * Builds AccountMerge operation.
     * @see AccountMergeOperation
     */
    public static class Builder {
        private final KeyPair destination;

        private KeyPair mSourceAccount;

        Builder(OperationBody op) {
            destination = KeyPair.fromXdrPublicKey(op.getDestination().getAccountID());
        }

        /**
         * Creates a new AccountMerge builder.
         * @param destination The account that receives the remaining XLM balance of the source account.
         */
        public Builder(KeyPair destination) {
            this.destination = destination;
        }

        /**
         * Set source account of this operation
         * @param sourceAccount Source account
         * @return Builder object so you can chain methods.
         */
        public Builder setSourceAccount(KeyPair sourceAccount) {
            mSourceAccount = sourceAccount;
            return this;
        }

        /**
         * Builds an operation
         */
        public AccountMergeOperation build() {
            AccountMergeOperation operation = new AccountMergeOperation(destination);
            if (mSourceAccount != null) {
                operation.setSourceAccount(mSourceAccount);
            }
            return operation;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AccountMergeOperation that = (AccountMergeOperation) o;

        return super.equals(o) && new EqualsBuilder()
                .append(getDestination().getAccountId(), that.getDestination().getAccountId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getDestination().getAccountId())
                .toHashCode();
    }
}
