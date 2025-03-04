package network.lightsail;

import java.math.BigDecimal;
import org.stellar.sdk.*;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.responses.TransactionResponse;
import org.stellar.sdk.xdr.InvokeHostFunctionResultCode;
import org.stellar.sdk.xdr.TransactionResult;
import org.stellar.sdk.xdr.TransactionResultCode;

public class SendAssetToContractWithoutRPC {
  public static void main(String[] args) {
    KeyPair kp = KeyPair.fromSecretSeed("SCDG4ORIDX4QGPMMHQY36KDHHMTJEM4RQ2AWKH3G7AXHTVBJWEV6XOUM");
    String horizonUrl = "https://horizon-testnet.stellar.org";
    Network network = Network.TESTNET;
    try (Server server = new Server(horizonUrl)) {
      Asset asset = Asset.createNativeAsset();
      String destination = "CDNVQW44C3HALYNVQ4SOBXY5EWYTGVYXX6JPESOLQDABJI5FC5LTRRUE";
      BigDecimal amount = new BigDecimal("100.125");

      TransactionBuilderAccount source = server.loadAccount(kp.getAccountId());

      // 1. Try to send payment to a contract
      // example tx:
      // https://stellar.expert/explorer/public/tx/18c62daf7d5a180acee1bb9918eccc2a19343a4f3c7012a9103ff2ba4024cec1
      Transaction transaction =
          new TransactionBuilder(source, network)
              .setTimeout(180)
              .setBaseFee(Transaction.MIN_BASE_FEE)
              .buildPaymentToContractTransaction(destination, asset, amount, null);
      transaction.sign(kp);
      try {
        TransactionResponse response = server.submitTransaction(transaction);
        System.out.println("Success! " + response);
      } catch (BadRequestException e) {
        // However, it may fail due to the entry being archived; for this reason, we should try to
        // restore the data entry and then resend the transaction.
        // See https://developers.stellar.org/docs/learn/encyclopedia/storage/state-archival
        if (e.getProblem() != null
            && e.getProblem().getExtras() != null
            && e.getProblem().getExtras().getResultXdr() != null) {
          TransactionResult transactionResult =
              TransactionResult.fromXdrBase64(e.getProblem().getExtras().getResultXdr());
          if (transactionResult.getResult().getDiscriminant() == TransactionResultCode.txFAILED
              && transactionResult.getResult().getResults().length == 1
              && transactionResult
                      .getResult()
                      .getResults()[0]
                      .getTr()
                      .getInvokeHostFunctionResult()
                      .getDiscriminant()
                  == InvokeHostFunctionResultCode.INVOKE_HOST_FUNCTION_ENTRY_ARCHIVED) {
            System.out.println("The entry is archived, restoring the data entry");
            Transaction restoreTransaction =
                new TransactionBuilder(source, network)
                    .setTimeout(180)
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .buildRestoreAssetBalanceEntryTransaction(destination, asset, null);
            restoreTransaction.sign(kp);
            TransactionResponse restoreTransactionResponse =
                server.submitTransaction(restoreTransaction);
            System.out.println("Restored the data entry. " + restoreTransactionResponse);

            System.out.println("Retrying the payment");

            // 3. Try to send payment to a contract again
            // example tx:
            // https://stellar.expert/explorer/public/tx/7c2a77d538f803da8cf2b14250c3a41b7fa0f2cf37dd437107e3c825efe60b91
            Transaction resendTransaction =
                new TransactionBuilder(source, network)
                    .setTimeout(180)
                    .setBaseFee(Transaction.MIN_BASE_FEE)
                    .buildPaymentToContractTransaction(destination, asset, amount, null);

            resendTransaction.sign(kp);
            TransactionResponse resendTransactionResponse =
                server.submitTransaction(resendTransaction);
            System.out.println("Success! " + resendTransactionResponse);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
