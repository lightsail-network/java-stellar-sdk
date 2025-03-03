package org.stellar.sdk;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import org.stellar.sdk.contract.AssembledTransaction;
import org.stellar.sdk.contract.ContractClient;
import org.stellar.sdk.contract.exception.SimulationFailedException;
import org.stellar.sdk.operations.ChangeTrustOperation;
import org.stellar.sdk.operations.InvokeHostFunctionOperation;
import org.stellar.sdk.operations.PaymentOperation;
import org.stellar.sdk.responses.AccountResponse;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.SCVal;

public class IntegrationUtils {
  public static final String RPC_URL = "http://127.0.0.1:8000/soroban/rpc";
  public static final String HORIZON_URL = "http://127.0.0.1:8000";
  public static final String FRIEND_BOT_URL = "http://127.0.0.1:8000/friendbot";
  public static final Network NETWORK = Network.STANDALONE;

  public static void fundAccount(String accountId) throws IOException {
    HttpURLConnection connection = null;
    try {
      URL url = new URL(FRIEND_BOT_URL + "?addr=" + accountId);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      if (responseCode >= 400) {
        throw new IOException("HTTP error code: " + responseCode);
      }
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  public static void createAssetContract(Asset asset, KeyPair source) {
    try (SorobanServer server = new SorobanServer(RPC_URL)) {
      TransactionBuilderAccount account = server.getAccount(source.getAccountId());
      InvokeHostFunctionOperation op =
          InvokeHostFunctionOperation.createStellarAssetContractOperationBuilder(asset).build();
      TransactionBuilder transactionBuilder =
          new TransactionBuilder(account, NETWORK).setBaseFee(100).addOperation(op).setTimeout(300);
      AssembledTransaction<SCVal> assembledTransaction =
          new AssembledTransaction<>(transactionBuilder, server, source, null, 300);
      assembledTransaction.simulate(true).signAndSubmit(source, false);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SimulationFailedException ignored) {

    }
  }

  public static String getRandomContractId(KeyPair source) {
    try (SorobanServer server = new SorobanServer(RPC_URL)) {
      TransactionBuilderAccount account = server.getAccount(source.getAccountId());
      InvokeHostFunctionOperation op =
          InvokeHostFunctionOperation.createStellarAssetContractOperationBuilder(
                  new Address(source.getAccountId()), null)
              .build();
      TransactionBuilder transactionBuilder =
          new TransactionBuilder(account, NETWORK).setBaseFee(100).addOperation(op).setTimeout(300);
      AssembledTransaction<Address> assembledTransaction =
          new AssembledTransaction<>(transactionBuilder, server, source, Scv::fromAddress, 300);
      Address contractId = assembledTransaction.simulate(true).signAndSubmit(source, false);
      return contractId.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static BigInteger getBalanceForContract(String contractId, Asset asset, KeyPair source) {
    try (ContractClient contractClient =
        new ContractClient(asset.getContractId(NETWORK), RPC_URL, NETWORK)) {
      return contractClient
          .invoke(
              "balance",
              Collections.singletonList(Scv.toAddress(contractId)),
              source.getAccountId(),
              source,
              Scv::fromInt128,
              100)
          .result();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void issueAsset(
      String assetCode, KeyPair issuer, KeyPair receiver, BigDecimal amount) {
    Asset asset = Asset.createNonNativeAsset(assetCode, issuer.getAccountId());
    try (Server server = new Server(HORIZON_URL)) {
      AccountResponse issuerAccount = server.accounts().account(issuer.getAccountId());

      Transaction transaction =
          new TransactionBuilder(issuerAccount, NETWORK)
              .setBaseFee(100)
              .addOperation(
                  ChangeTrustOperation.builder()
                      .asset(new ChangeTrustAsset(asset))
                      .sourceAccount(receiver.getAccountId())
                      .limit(new BigDecimal("922337203685.4775807"))
                      .build())
              .addOperation(
                  PaymentOperation.builder()
                      .asset(asset)
                      .amount(amount)
                      .destination(receiver.getAccountId())
                      .sourceAccount(issuer.getAccountId())
                      .build())
              .setTimeout(300)
              .build();

      transaction.sign(issuer);
      transaction.sign(receiver);
      server.submitTransaction(transaction);
    }
  }
}
