package network.lightsail;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;
import org.stellar.sdk.SorobanServer;
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

public class SorobanInvokeContractFunction {
  public static void main(String[] args) {
    String secret = "SAAPYAPTTRZMCUZFPG3G66V4ZMHTK4TWA6NS7U4F7Z3IMUD52EK4DDEV";
    String rpcServerUrl = "https://soroban-testnet.stellar.org:443";
    Network network = Network.TESTNET;

    KeyPair keyPair = KeyPair.fromSecretSeed(secret);
    SorobanServer sorobanServer = new SorobanServer(rpcServerUrl);
    TransactionBuilderAccount source = sorobanServer.getAccount(keyPair.getAccountId());

    // The invoke host function operation
    // https://github.com/stellar/soroban-examples/tree/main/hello_world
    String contractId = "CDZJVZWCY4NFGHCCZMX6QW5AK3ET5L3UUAYBVNDYOXDLQXW7PHXGYOBJ";
    String functionName = "hello";
    List<SCVal> parameters =
        List.of(Scv.toString("World!")); // Scv is a helper class to create SCVal objects
    InvokeHostFunctionOperation invokeHostFunctionOperation =
        InvokeHostFunctionOperation.invokeContractFunctionOperationBuilder(
                contractId, functionName, parameters)
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
    GetTransactionResponse getTransactionResponse;
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
      SCVal rawReturnValue = transactionMeta.getV3().getSorobanMeta().getReturnValue();
      System.out.println("Raw return value: " + rawReturnValue);
      List<SCVal> vec = Scv.fromVec(rawReturnValue).stream().toList();
      String returnValue0 = new String(Scv.fromString(vec.get(0)), StandardCharsets.UTF_8);
      String returnValue1 = new String(Scv.fromString(vec.get(1)), StandardCharsets.UTF_8);
      System.out.println("Return value 0: " + returnValue0);
      System.out.println("Return value 1: " + returnValue1);
    } else {
      System.out.println("Transaction failed: " + getTransactionResponse);
    }
  }
}
