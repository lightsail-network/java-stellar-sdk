package network.lightsail;

import static org.stellar.sdk.AbstractTransaction.MIN_BASE_FEE;

import org.stellar.sdk.AssetTypeNative;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Network;
import org.stellar.sdk.Server;
import org.stellar.sdk.Transaction;
import org.stellar.sdk.TransactionBuilder;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.exception.BadResponseException;
import org.stellar.sdk.exception.NetworkException;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.responses.TransactionResponse;

/** Create, sign, and submit a transaction using Java Stellar SDK. */
public class Payment {
  public static void main(String[] args) {
    // The source account is the account we will be signing and sending from.
    KeyPair sourceKeypair =
        KeyPair.fromSecretSeed("SBKQHIZ3WFWHJQ3YNGPDZCBEGULAIR3XOCZJ4AXQSMVKBDGWQWXUIWRP");
    System.out.println("Source Account: " + sourceKeypair.getAccountId());

    // The destination account is the account we will be sending the lumens to.
    String destination = "GAL2A7FGNVK56NFOG3P7LQSAOZ6MDIH5MYCDTB4VB45WTAKKIQEZONNF";

    // Configure StellarSdk to talk to the horizon instance hosted by Stellar.org
    // To use the live network, set the hostname to 'horizon.stellar.org'
    Server server = new Server("https://horizon-testnet.stellar.org");

    // Transactions require a valid sequence number that is specific to this account.
    // We can fetch the current sequence number for the source account from Horizon.
    AccountResponse sourceAccount = server.accounts().account(sourceKeypair.getAccountId());

    // Build a payment operation, send 350.1234567 XLM to receiver
    PaymentOperation paymentOperation =
        PaymentOperation.builder()
            .destination(destination)
            .asset(new AssetTypeNative())
            .amount("350.1234567")
            .build();

    // Build the transaction
    Transaction transaction =
        new TransactionBuilder(
                sourceAccount,
                Network.TESTNET) // we are going to submit the transaction to the test network
            .setBaseFee(MIN_BASE_FEE) // set base fee, see
            // https://developers.stellar.org/docs/learn/glossary#base-fee
            .addMemo(Memo.text("Hello Stellar!")) // Add a text memo
            .setTimeout(180) // Make this transaction valid for the next 30 seconds only
            .addOperation(paymentOperation) // Add the payment operation to the transaction
            .build();

    // Sign this transaction with the secret key
    // NOTE: signing is transaction is network specific. Test network transactions
    // won't work in the public network. To switch networks, use the Network object
    // as explained above.
    transaction.sign(sourceKeypair);
    try {
      TransactionResponse transactionResponse = server.submitTransaction(transaction);
      System.out.println("Transaction successful!");
      System.out.println("Transaction Hash: " + transactionResponse.getHash());
    } catch (BadRequestException e) {
      System.out.println("Bad Request: " + e.getMessage());
    } catch (BadResponseException e) {
      System.out.println("Bad Response: " + e.getMessage());
    } catch (NetworkException e) {
      // This catch block will handle any NetworkException that was not caught by the previous catch
      // blocks.
      // NetworkException is a superclass of BadRequestException and BadResponseException.
      // It may have other subclasses that are not explicitly caught above.
      // By catching NetworkException here, we ensure that any uncaught subclasses of
      // NetworkException are handled.
      System.out.println("Network Issue: " + e.getMessage());
    }
    server.close();
  }
}
