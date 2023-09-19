package org.stellar.sdk;

import java.util.Objects;
import org.stellar.sdk.xdr.OperationType;

/**
 * Represents <a href="https://developers.stellar.org/docs/start/list-of-operations/#inflation"
 * target="_blank">Inflation</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/start/list-of-operations/" target="_blank">List
 *     of Operations</a>
 */
public class InflationOperation extends Operation {
  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.INFLATION);
    return body;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getSourceAccount());
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof InflationOperation)) {
      return false;
    }

    InflationOperation other = (InflationOperation) object;
    return Objects.equals(this.getSourceAccount(), other.getSourceAccount());
  }
}
