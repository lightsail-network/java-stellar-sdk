package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.stellar.sdk.Base64Factory;
import org.stellar.sdk.Server;
import org.stellar.sdk.xdr.OperationResult;
import org.stellar.sdk.xdr.OperationType;
import org.stellar.sdk.xdr.TransactionResult;
import org.stellar.sdk.xdr.XdrDataInputStream;

/**
 * Represents server response after submitting transaction.
 *
 * @see Server#submitTransaction(org.stellar.sdk.Transaction)
 */
@EqualsAndHashCode(callSuper = true)
public class SubmitTransactionResponse extends TransactionResponse {
  /**
   * Additional information returned by a server. This will be <code>null</code> if transaction
   * succeeded.
   */
  @Getter
  @SerializedName("extras")
  private final Extras extras;

  private TransactionResult transactionResult;

  public SubmitTransactionResponse(
      String id,
      String pagingToken,
      Boolean successful,
      String hash,
      Long ledger,
      String createdAt,
      String sourceAccount,
      String accountMuxed,
      BigInteger accountMuxedId,
      Long sourceAccountSequence,
      String feeAccount,
      String feeAccountMuxed,
      BigInteger feeAccountMuxedId,
      Long feeCharged,
      Long maxFee,
      Integer operationCount,
      String envelopeXdr,
      String resultXdr,
      String resultMetaXdr,
      String feeMetaXdr,
      List<String> signatures,
      Preconditions preconditions,
      FeeBumpTransaction feeBumpTransaction,
      InnerTransaction innerTransaction,
      String memoType,
      String memoBytes,
      String memoValue,
      Links links,
      Extras extras) {
    super(
        id,
        pagingToken,
        successful,
        hash,
        ledger,
        createdAt,
        sourceAccount,
        accountMuxed,
        accountMuxedId,
        sourceAccountSequence,
        feeAccount,
        feeAccountMuxed,
        feeAccountMuxedId,
        feeCharged,
        maxFee,
        operationCount,
        envelopeXdr,
        resultXdr,
        resultMetaXdr,
        feeMetaXdr,
        signatures,
        preconditions,
        feeBumpTransaction,
        innerTransaction,
        memoType,
        memoBytes,
        memoValue,
        links);
    this.extras = extras;
  }

  public boolean isSuccess() {
    return this.getLedger() != null;
  }

  /**
   * Helper method that returns Offer ID for ManageOffer from TransactionResult Xdr. This is helpful
   * when you need ID of an offer to update it later.
   *
   * @param position Position of ManageSellOffer/ManageBuyOffer operation. If it is second operation
   *     in this transaction this should be equal <code>1</code>.
   * @return Offer ID or <code>null</code> when operation at <code>position</code> is not a
   *     ManageSellOffer/ManageBuyOffer operation or error has occurred.
   */
  public Optional<Long> getOfferIdFromResult(int position) throws IOException {
    if (!this.isSuccess()) {
      return Optional.empty();
    }

    Optional<TransactionResult> optionalResult = getDecodedTransactionResult();
    if (!optionalResult.isPresent()) {
      return Optional.empty();
    }
    TransactionResult result = optionalResult.get();

    if (result.getResult().getResults()[position] == null) {
      return Optional.empty();
    }

    OperationResult operationResult = result.getResult().getResults()[position];
    OperationType operationType = operationResult.getTr().getDiscriminant();
    OperationResult.OperationResultTr operationResultTr = operationResult.getTr();

    if (operationType == OperationType.MANAGE_SELL_OFFER) {
      if (operationResultTr.getManageSellOfferResult().getSuccess().getOffer().getOffer() == null) {
        return Optional.empty();
      }
      return Optional.of(
          operationResultTr
              .getManageSellOfferResult()
              .getSuccess()
              .getOffer()
              .getOffer()
              .getOfferID()
              .getInt64());
    }

    if (operationType == OperationType.MANAGE_BUY_OFFER) {
      if (operationResultTr.getManageBuyOfferResult().getSuccess().getOffer().getOffer() == null) {
        return Optional.empty();
      }
      return Optional.of(
          operationResultTr
              .getManageBuyOfferResult()
              .getSuccess()
              .getOffer()
              .getOffer()
              .getOfferID()
              .getInt64());
    }

    return Optional.empty();
  }

  /**
   * Decoding "TransactionResult" from "resultXdr". This will be <code>Optional.empty()()</code> if
   * transaction has failed.
   */
  public Optional<TransactionResult> getDecodedTransactionResult() throws IOException {
    if (!this.isSuccess()) {
      return Optional.empty();
    }

    if (this.transactionResult == null) {
      String resultXDR = this.getResultXdr();
      if (resultXDR == null) {
        return Optional.empty();
      }
      byte[] bytes = Base64Factory.getInstance().decode(resultXDR);
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
      XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
      this.transactionResult = TransactionResult.decode(xdrInputStream);
    }

    return Optional.of(this.transactionResult);
  }

  /** Additional information returned by a server. */
  @Value
  public static class Extras {
    @SerializedName("envelope_xdr")
    String envelopeXdr;

    @SerializedName("result_xdr")
    String resultXdr;

    @SerializedName("result_codes")
    ResultCodes resultCodes;

    /**
     * Contains result codes for this transaction.
     *
     * @see <a
     *     href="https://github.com/stellar/horizon/blob/master/src/github.com/stellar/horizon/codes/main.go"
     *     target="_blank">Possible values</a>
     */
    @Value
    public static class ResultCodes {
      @SerializedName("transaction")
      String transactionResultCode;

      @SerializedName("inner_transaction")
      String innerTransactionResultCode;

      @SerializedName("operations")
      ArrayList<String> operationsResultCodes;
    }
  }
}
