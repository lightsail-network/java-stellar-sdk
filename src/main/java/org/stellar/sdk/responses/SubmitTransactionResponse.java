package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
@EqualsAndHashCode(callSuper = false)
public class SubmitTransactionResponse extends Response {
  @SerializedName("hash")
  @Getter
  private final String hash;

  @SerializedName("ledger")
  @Getter
  private final Long ledger;

  @SerializedName("envelope_xdr")
  private final String envelopeXdr;

  @SerializedName("result_xdr")
  private final String resultXdr;

  /**
   * Additional information returned by a server. This will be <code>null</code> if transaction
   * succeeded.
   */
  @Getter
  @SerializedName("extras")
  private final Extras extras;

  TransactionResult transactionResult;

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

  public Optional<String> getEnvelopeXdr() {
    if (this.isSuccess()) {
      return Optional.of(this.envelopeXdr);
    } else {
      if (this.getExtras() != null) {
        return Optional.of(this.getExtras().getEnvelopeXdr());
      }
      return Optional.empty();
    }
  }

  public Optional<String> getResultXdr() {
    if (this.isSuccess()) {
      return Optional.of(this.resultXdr);
    } else {
      if (this.getExtras() != null) {
        return Optional.of(this.getExtras().getResultXdr());
      }
      return Optional.empty();
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
      Optional<String> resultXDR = this.getResultXdr();
      if (!resultXDR.isPresent()) {
        return Optional.empty();
      }
      byte[] bytes = Base64Factory.getInstance().decode(resultXDR.get());
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
