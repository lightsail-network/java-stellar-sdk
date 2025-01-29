package network.lightsail;

import java.io.IOException;
import java.util.List;
import org.stellar.sdk.Address;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.SorobanServer;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.TransactionBuilder;
import org.stellar.sdk.TransactionBuilderAccount;
import org.stellar.sdk.exception.NetworkException;
import org.stellar.sdk.exception.PrepareTransactionException;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.responses.sorobanrpc.GetTransactionResponse;
import org.stellar.sdk.responses.sorobanrpc.SendTransactionResponse;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.TransactionMeta;

public class SorobanCreateContract {
  public static void main(String[] args) throws IOException {
    String secret = "SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV";
    String rpcServerUrl = "https://soroban-testnet.stellar.org:443";
    Network network = Network.TESTNET;

    KeyPair keyPair = KeyPair.fromSecretSeed(secret);
    GetTransactionResponse getTransactionResponse;
    try (SorobanServer sorobanServer = new SorobanServer(rpcServerUrl)) {
      TransactionBuilderAccount source = sorobanServer.getAccount(keyPair.getAccountId());

      String wasmId = "406edc375a4334ea2849d22e490919a5456ee176dd2f9fc3e1e557cd242ec593";
      List<SCVal> constructorArgs =
          List.of(Scv.toString("World!")); // Scv is a helper class to create SCVal objects
      InvokeHostFunctionOperation invokeHostFunctionOperation =
          InvokeHostFunctionOperation.createContractOperationBuilder(
                  wasmId, new Address(keyPair.getAccountId()), constructorArgs, null)
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
      transaction.sign(keyPair);

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

      // Check the transaction status
      while (true) {
        try {
          getTransactionResponse = sorobanServer.getTransaction(sendTransactionResponse.getHash());
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
}
