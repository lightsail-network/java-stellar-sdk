package network.lightsail;

import java.io.IOException;
import java.util.List;

import org.stellar.sdk.*;
import org.stellar.sdk.exception.NetworkException;
import org.stellar.sdk.exception.PrepareTransactionException;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SendTransactionResponse;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.TransactionMeta;

/** This example shows how to deploy a contract with an installed (uploaded) wasm id. */
public class SorobanCreateContract {
  public static void main(String[] args) throws IOException {
    String secret = "SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV";
    String rpcServerUrl = "https://soroban-testnet.stellar.org:443";
    KeyPair sourceAccount = KeyPair.fromSecretSeed(secret);

    try (SorobanServer sorobanServer = new SorobanServer(rpcServerUrl)) {
      List<SCVal> constructorArgs = List.of(Scv.toString("World!"));
      String txHash =
          createContractWithWasmId(
              sorobanServer,
              Network.TESTNET,
              "406edc375a4334ea2849d22e490919a5456ee176dd2f9fc3e1e557cd242ec593",
              sourceAccount,
              constructorArgs); // Scv is a helper class to create SCVal objects

      GetTransactionResponse getTransactionResponse;
      // Check the transaction status
      while (true) {
        try {
          getTransactionResponse = sorobanServer.getTransaction(txHash);
        } catch (NetworkException e) {
          throw new RuntimeException("Get transaction failed", e);
        }

        if (!GetTransactionResponse.GetTransactionStatus.NOT_FOUND.equals(
            getTransactionResponse.getStatus())) {
          break;
        }
        // Wait for 3 seconds before checking the transaction status again
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      if (GetTransactionResponse.GetTransactionStatus.SUCCESS.equals(
          getTransactionResponse.getStatus())) {
        System.out.println("Transaction succeeded: " + getTransactionResponse);
        // parse the function return value
        TransactionMeta transactionMeta = getTransactionResponse.parseResultMetaXdr();
        byte[] hash =
            transactionMeta
                .getV3()
                .getSorobanMeta()
                .getReturnValue()
                .getAddress()
                .getContractId()
                .getHash();
        String contractId = StrKey.encodeContract(hash);
        System.out.println("Contract ID: " + contractId);
      } else {
        System.out.println("Transaction failed: " + getTransactionResponse);
      }
    }
  }

  /**
   * Creates a contract with the given WASM ID and constructor arguments.
   *
   * @param sorobanServer the Soroban server
   * @param network the network (e.g., TESTNET)
   * @param wasmId the WASM ID of the contract
   * @param sourceAccount the source account
   * @param constructorArgs the constructor arguments
   * @return the transaction hash
   */
  private static String createContractWithWasmId(
      SorobanServer sorobanServer,
      Network network,
      String wasmId,
      KeyPair sourceAccount,
      List<SCVal> constructorArgs) {

    GetTransactionResponse getTransactionResponse;
    TransactionBuilderAccount source = sorobanServer.getAccount(sourceAccount.getAccountId());

    InvokeHostFunctionOperation invokeHostFunctionOperation =
        InvokeHostFunctionOperation.createContractOperationBuilder(
                wasmId, new Address(sourceAccount.getAccountId()), constructorArgs, null)
            .build();

    // Build the transaction
    Transaction unpreparedTransaction =
        new TransactionBuilder(source, network)
            .setBaseFee(Transaction.MIN_BASE_FEE)
            .addOperation(invokeHostFunctionOperation)
            .setTimeout(300)
            .build();

    // Prepare the transaction
    Transaction transaction;
    try {
      transaction = sorobanServer.prepareTransaction(unpreparedTransaction);
    } catch (PrepareTransactionException e) {
      throw new RuntimeException("Prepare transaction failed", e);
    } catch (NetworkException e) {
      throw new RuntimeException("Network error", e);
    }

    // Sign the transaction
    transaction.sign(sourceAccount);

    // Send the transaction
    SendTransactionResponse sendTransactionResponse;
    try {
      sendTransactionResponse = sorobanServer.sendTransaction(transaction);
    } catch (NetworkException e) {
      throw new RuntimeException("Send transaction failed", e);
    }
    if (!SendTransactionResponse.SendTransactionStatus.PENDING.equals(
        sendTransactionResponse.getStatus())) {
      throw new RuntimeException("Send transaction failed: " + sendTransactionResponse);
    }

    return sendTransactionResponse.getHash();
  }
}
