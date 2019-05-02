package org.stellar.sdk;

/**
 * @deprecated Use {@link ManageSellOfferOperation}
 */
@Deprecated
public class ManageOfferOperation extends Operation {


    @Override
    org.stellar.sdk.xdr.Operation.OperationBody toOperationBody() {
        return null;
    }
}
