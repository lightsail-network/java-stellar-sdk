package org.stellar.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
  public static final String RPC_URL = "http://127.0.0.1:8000/rpc";
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

  public static byte[] uploadWasm(byte[] contractWasm, KeyPair source) {
    try (SorobanServer server = new SorobanServer(RPC_URL)) {
      TransactionBuilderAccount account = server.getAccount(source.getAccountId());
      InvokeHostFunctionOperation op =
          InvokeHostFunctionOperation.uploadContractWasmOperationBuilder(contractWasm).build();
      TransactionBuilder transactionBuilder =
          new TransactionBuilder(account, NETWORK).setBaseFee(100).addOperation(op).setTimeout(300);
      AssembledTransaction<byte[]> assembledTransaction =
          new AssembledTransaction<>(transactionBuilder, server, source, Scv::fromBytes, 300);
      AssembledTransaction<byte[]> simulated = assembledTransaction.simulate(false);
      if (simulated.isReadCall()) {
        return simulated.result();
      } else {
        return simulated.signAndSubmit(source, false);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getRandomContractId(KeyPair source) {
    byte[] wasm;
    try (InputStream is =
        IntegrationUtils.class.getResourceAsStream(
            "/wasm_files/soroban_hello_world_contract.wasm")) {
      if (is == null) {
        throw new RuntimeException("soroban_hello_world_contract.wasm not found");
      }
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      byte[] data = new byte[1024];
      int bytesRead;
      while ((bytesRead = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, bytesRead);
      }
      wasm = buffer.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    byte[] wasmId = uploadWasm(wasm, source);

    try (SorobanServer server = new SorobanServer(RPC_URL)) {
      TransactionBuilderAccount account = server.getAccount(source.getAccountId());
      InvokeHostFunctionOperation op =
          InvokeHostFunctionOperation.createContractOperationBuilder(
                  wasmId, new Address(source.getAccountId()), null, null)
              .build();
      TransactionBuilder transactionBuilder =
          new TransactionBuilder(account, NETWORK).setBaseFee(100).addOperation(op).setTimeout(300);
      AssembledTransaction<Address> assembledTransaction =
          new AssembledTransaction<>(transactionBuilder, server, source, Scv::fromAddress, 300);
      Address contractId = assembledTransaction.simulate(true).signAndSubmit(source, false);
      // TODO: This is strange; after the transaction above is submitted, the account's seq does not
      // update immediately.
      Thread.sleep(1000);
      return contractId.toString();
    } catch (IOException | InterruptedException e) {
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
