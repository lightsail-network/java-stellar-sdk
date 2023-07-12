package org.stellar.sdk.responses;

import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import com.google.gson.annotations.SerializedName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
public class SubmitTransactionResponse extends Response {
  @SerializedName("hash")
  private final String hash;

  @SerializedName("ledger")
  private final Long ledger;

  @SerializedName("envelope_xdr")
  private final String envelopeXdr;

  @SerializedName("result_xdr")
  private final String resultXdr;

  @SerializedName("extras")
  private final Extras extras;

  private TransactionResult transactionResult;

  SubmitTransactionResponse(
      Extras extras, Long ledger, String hash, String envelopeXdr, String resultXdr) {
    this.extras = extras;
    this.ledger = ledger;
    this.hash = hash;
    this.envelopeXdr = envelopeXdr;
    this.resultXdr = resultXdr;
  }

  public boolean isSuccess() {
    return ledger != null;
  }

  public String getHash() {
    return hash;
  }

  public Long getLedger() {
    return ledger;
  }

  public Optional<String> getEnvelopeXdr() {
    if (this.isSuccess()) {
      return Optional.of(this.envelopeXdr);
    } else {
      if (this.getExtras() != null) {
        return Optional.of(this.getExtras().getEnvelopeXdr());
      }
      return Optional.absent();
    }
  }

  public Optional<String> getResultXdr() {
    if (this.isSuccess()) {
      return Optional.of(this.resultXdr);
    } else {
      if (this.getExtras() != null) {
        return Optional.of(this.getExtras().getResultXdr());
      }
      return Optional.absent();
    }
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
      return Optional.absent();
    }

    Optional<TransactionResult> optionalResult = getDecodedTransactionResult();
    if (!optionalResult.isPresent()) {
      return Optional.absent();
    }
    TransactionResult result = optionalResult.get();

    if (result.getResult().getResults()[position] == null) {
      return Optional.absent();
    }

    OperationResult operationResult = result.getResult().getResults()[position];
    OperationType operationType = operationResult.getTr().getDiscriminant();
    OperationResult.OperationResultTr operationResultTr = operationResult.getTr();

    if (operationType == OperationType.MANAGE_SELL_OFFER) {
      if (operationResultTr.getManageSellOfferResult().getSuccess().getOffer().getOffer() == null) {
        return Optional.absent();
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
        return Optional.absent();
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

    return Optional.absent();
  }

  /**
   * Decoding "TransactionResult" from "resultXdr". This will be <code>Optional.absent()</code> if
   * transaction has failed.
   */
  public Optional<TransactionResult> getDecodedTransactionResult() throws IOException {
    if (!this.isSuccess()) {
      return Optional.absent();
    }

    if (this.transactionResult == null) {
      Optional<String> resultXDR = this.getResultXdr();
      if (!resultXDR.isPresent()) {
        return Optional.absent();
      }
      BaseEncoding base64Encoding = BaseEncoding.base64();
      byte[] bytes = base64Encoding.decode(resultXDR.get());
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
      XdrDataInputStream xdrInputStream = new XdrDataInputStream(inputStream);
      this.transactionResult = TransactionResult.decode(xdrInputStream);
    }

    return Optional.of(this.transactionResult);
  }

  /**
   * Additional information returned by a server. This will be <code>null</code> if transaction
   * succeeded.
   */
  public Extras getExtras() {
    return extras;
  }

  /** Additional information returned by a server. */
  public static class Extras {
    @SerializedName("envelope_xdr")
    private final String envelopeXdr;

    @SerializedName("result_xdr")
    private final String resultXdr;

    @SerializedName("result_codes")
    private final ResultCodes resultCodes;

    Extras(String envelopeXdr, String resultXdr, ResultCodes resultCodes) {
      this.envelopeXdr = envelopeXdr;
      this.resultXdr = resultXdr;
      this.resultCodes = resultCodes;
    }

    /**
     * Returns XDR TransactionEnvelope base64-encoded string. Use <a
     * href="http://stellar.github.io/xdr-viewer/">xdr-viewer</a> to debug.
     */
    public String getEnvelopeXdr() {
      return envelopeXdr;
    }

    /**
     * Returns XDR TransactionResult base64-encoded string Use <a
     * href="http://stellar.github.io/xdr-viewer/">xdr-viewer</a> to debug.
     */
    public String getResultXdr() {
      return resultXdr;
    }

    /** Returns ResultCodes object that contains result codes for transaction. */
    public ResultCodes getResultCodes() {
      return resultCodes;
    }

    /**
     * Contains result codes for this transaction.
     *
     * @see <a
     *     href="https://github.com/stellar/horizon/blob/master/src/github.com/stellar/horizon/codes/main.go"
     *     target="_blank">Possible values</a>
     */
    public static class ResultCodes {
      @SerializedName("transaction")
      private final String transactionResultCode;

      @SerializedName("inner_transaction")
      private final String innerTransactionResultCode;

      @SerializedName("operations")
      private final ArrayList<String> operationsResultCodes;

      public ResultCodes(
          String transactionResultCode,
          String innerTransactionResultCode,
          ArrayList<String> operationsResultCodes) {
        this.transactionResultCode = transactionResultCode;
        this.innerTransactionResultCode = innerTransactionResultCode;
        this.operationsResultCodes = operationsResultCodes;
      }

      public String getTransactionResultCode() {
        return transactionResultCode;
      }

      public String getInnerTransactionResultCode() {
        return innerTransactionResultCode;
      }

      public ArrayList<String> getOperationsResultCodes() {
        return operationsResultCodes;
      }
    }
  }
}
