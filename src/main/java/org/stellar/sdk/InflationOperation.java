package org.stellar.sdk;

import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html#inflation" target="_blank">Inflation</a> operation.
 * @see <a href="https://www.stellar.org/developers/learn/concepts/list-of-operations.html" target="_blank">List of Operations</a>
 */
public class InflationOperation extends Operation {
    @Override
    org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
        org.stellar.sdk.xdr.Operation.OperationBody body = new org.stellar.sdk.xdr.Operation.OperationBody();
        body.setDiscriminant(OperationType.INFLATION);
        return body;
    }


    @Override
    public boolean equals(Object o) {
        return this == o || super.equals(o) && o instanceof InflationOperation;
    }

    @Override
    public int hashCode() {
        return InflationOperation.class.hashCode();
    }
}
