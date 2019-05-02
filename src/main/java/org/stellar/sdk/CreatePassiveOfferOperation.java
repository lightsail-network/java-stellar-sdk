package org.stellar.sdk;

/**
 * @deprecated Use {@link CreatePassiveSellOfferOperation}
 */
@Deprecated
public class CreatePassiveOfferOperation extends Operation {

    @Override
    org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
        return null;
    }
}
