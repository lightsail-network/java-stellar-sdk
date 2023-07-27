package org.stellar.sdk;

import java.util.Arrays;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.xdr.HostFunction;
import org.stellar.sdk.xdr.InvokeHostFunctionOp;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;

/**
 * Represents <a
 * href="https://github.com/stellar/go/blob/7ff6ffae29d278f979fcd6c6bed8cd0d4b4d2e08/txnbuild/invoke_host_function.go#L8-L13"
 * target="_blank">InvokeHostFunction</a> operation.
 *
 * @see <a href="https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations"
 *     target="_blank">List of Operations</a>
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Value
public class InvokeHostFunctionOperation extends Operation {

  /** The host function to invoke. */
  @NonNull HostFunction hostFunction;

  /** The authorizations required to execute the host function */
  @Singular("auth")
  @NonNull
  Collection<SorobanAuthorizationEntry> auth;

  /**
   * Constructs a new InvokeHostFunctionOperation object from the XDR representation of the {@link
   * InvokeHostFunctionOperation}.
   *
   * @param op the XDR representation of the {@link InvokeHostFunctionOperation}.
   */
  public static InvokeHostFunctionOperation fromXdr(InvokeHostFunctionOp op) {
    return InvokeHostFunctionOperation.builder()
        .hostFunction(op.getHostFunction())
        .auth(Arrays.asList(op.getAuth()))
        .build();
  }

  @Override
  org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(AccountConverter accountConverter) {
    InvokeHostFunctionOp op = new InvokeHostFunctionOp();
    op.setHostFunction(this.hostFunction);
    op.setAuth(this.auth.toArray(new SorobanAuthorizationEntry[0]));

    org.stellar.sdk.xdr.Operation.OperationBody body =
        new org.stellar.sdk.xdr.Operation.OperationBody();
    body.setDiscriminant(OperationType.INVOKE_HOST_FUNCTION);
    body.setInvokeHostFunctionOp(op);

    return body;
  }
}
