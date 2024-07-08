package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.stellar.sdk.Base64Factory;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.OperationMeta;
import org.stellar.sdk.xdr.TransactionEnvelope;
import org.stellar.sdk.xdr.TransactionMeta;
import org.stellar.sdk.xdr.TransactionResult;

/**
 * Represents transaction response.
 *
 * @see <a href="https://developers.stellar.org/api/resources/transactions/"
 *     target="_blank">Transaction documentation</a>
 * @see org.stellar.sdk.requests.TransactionsRequestBuilder
 * @see org.stellar.sdk.Server#transactions()
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class TransactionResponse extends Response implements Pageable {

  @SerializedName("id")
  String id;

  @SerializedName("paging_token")
  String pagingToken;

  @SerializedName("successful")
  Boolean successful;

  @SerializedName("hash")
  String hash;

  @SerializedName("ledger")
  Long ledger;

  @SerializedName("created_at")
  String createdAt;

  @SerializedName("source_account")
  String sourceAccount;

  @SerializedName("account_muxed")
  String accountMuxed;

  @SerializedName("account_muxed_id")
  BigInteger accountMuxedId;

  @SerializedName("source_account_sequence")
  Long sourceAccountSequence;

  @SerializedName("fee_account")
  String feeAccount;

  @SerializedName("fee_account_muxed")
  String feeAccountMuxed;

  @SerializedName("fee_account_muxed_id")
  BigInteger feeAccountMuxedId;

  @SerializedName("fee_charged")
  Long feeCharged;

  @SerializedName("max_fee")
  Long maxFee;

  @SerializedName("operation_count")
  Integer operationCount;

  @SerializedName("envelope_xdr")
  String envelopeXdr;

  @SerializedName("result_xdr")
  String resultXdr;

  @SerializedName("result_meta_xdr")
  String resultMetaXdr;

  @SerializedName("fee_meta_xdr")
  String feeMetaXdr;

  @SerializedName("signatures")
  List<String> signatures;

  @SerializedName("preconditions")
  Preconditions preconditions;

  @SerializedName("fee_bump_transaction")
  FeeBumpTransaction feeBumpTransaction;

  @SerializedName("inner_transaction")
  InnerTransaction innerTransaction;

  @SerializedName("memo_type")
  String memoType;

  @SerializedName("memo_bytes")
  String memoBytes;

  @SerializedName("memo")
  String memoValue;

  @SerializedName("_links")
  Links links;

  /**
   * Parses the {@code envelopeXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionEnvelope} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionEnvelope} object
   */
  public TransactionEnvelope parseEnvelopeXdr() {
    return Util.parseXdr(envelopeXdr, TransactionEnvelope::fromXdrBase64);
  }

  /**
   * Parses the {@code resultXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionResult} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionResult} object
   */
  public TransactionResult parseResultXdr() {
    return Util.parseXdr(resultXdr, TransactionResult::fromXdrBase64);
  }

  /**
   * Parses the {@code resultMetaXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.TransactionMeta} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.TransactionMeta} object
   */
  public TransactionMeta parseResultMetaXdr() {
    return Util.parseXdr(resultMetaXdr, TransactionMeta::fromXdrBase64);
  }

  /**
   * Parses the {@code feeMetaXdr} field from a string to an {@link
   * org.stellar.sdk.xdr.OperationMeta} object.
   *
   * @return the parsed {@link org.stellar.sdk.xdr.OperationMeta} object
   */
  public OperationMeta parseFeeMetaXdr() {
    return Util.parseXdr(feeMetaXdr, OperationMeta::fromXdrBase64);
  }

  /**
   * @return {@link Memo} object from this transaction.
   */
  public Memo getMemo() {
    if ("none".equals(memoType)) {
      return Memo.none();
    } else if ("text".equals(memoType)) {
      return Memo.text(Base64Factory.getInstance().decode(memoBytes));
    } else if ("id".equals(memoType)) {
      return Memo.id(new BigInteger(memoValue));
    } else if ("hash".equals(memoType)) {
      return Memo.hash(Base64Factory.getInstance().decode(memoValue));
    } else if ("return".equals(memoType)) {
      return Memo.returnHash(Base64Factory.getInstance().decode(memoValue));
    } else {
      throw new IllegalArgumentException("Invalid memo type: " + memoType);
    }
  }

  /**
   * Preconditions of a transaction per <a
   * href="https://github.com/stellar/stellar-protocol/blob/master/core/cap-0021.md#specification">CAP-21</a>
   */
  @Value
  public static class Preconditions {
    @SerializedName("timebounds")
    TimeBounds timeBounds;

    @SerializedName("ledgerbounds")
    LedgerBounds ledgerBounds;

    @SerializedName("min_account_sequence")
    Long minAccountSequence;

    @SerializedName("min_account_sequence_age")
    Long minAccountSequenceAge;

    @SerializedName("min_account_sequence_ledger_gap")
    Long minAccountSequenceLedgerGap;

    @SerializedName("extra_signers")
    List<String> signatures;

    @Value
    public static class TimeBounds {
      @SerializedName("min_time")
      BigInteger minTime;

      @SerializedName("max_time")
      BigInteger maxTime;
    }

    @Value
    public static class LedgerBounds {
      @SerializedName("min_ledger")
      Long minTime;

      @SerializedName("max_ledger")
      Long maxTime;
    }
  }

  /**
   * FeeBumpTransaction is only present in a TransactionResponse if the transaction is a fee bump
   * transaction or is wrapped by a fee bump transaction. The object has two fields: the hash of the
   * fee bump transaction and the signatures present in the fee bump transaction envelope.
   */
  @Value
  public static class FeeBumpTransaction {
    @SerializedName("hash")
    String hash;

    @SerializedName("signatures")
    List<String> signatures;
  }

  /**
   * InnerTransaction is only present in a TransactionResponse if the transaction is a fee bump
   * transaction or is wrapped by a fee bump transaction. The object has three fields: the hash of
   * the inner transaction wrapped by the fee bump transaction, the max fee set in the inner
   * transaction, and the signatures present in the inner transaction envelope.
   */
  @Value
  public static class InnerTransaction {
    @SerializedName("hash")
    String hash;

    @SerializedName("signatures")
    List<String> signatures;

    @SerializedName("max_fee")
    Long maxFee;
  }

  /** Links connected to transaction. */
  @Value
  public static class Links {
    @SerializedName("self")
    Link self;

    @SerializedName("account")
    Link account;

    @SerializedName("ledger")
    Link ledger;

    @SerializedName("operations")
    Link operations;

    @SerializedName("effects")
    Link effects;

    @SerializedName("precedes")
    Link precedes;

    @SerializedName("succeeds")
    Link succeeds;
  }
}
