package org.stellar.sdk.responses;

import static org.stellar.sdk.Util.CHARSET_UTF8;
import static org.stellar.sdk.Util.checkNotNull;

import com.google.gson.annotations.SerializedName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.android.codec.binary.Base64;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.LedgerEntryChanges;
import org.stellar.sdk.Memo;
import org.stellar.sdk.Operation;
import org.stellar.sdk.xdr.OperationMeta;
import org.stellar.sdk.xdr.Transaction;
import org.stellar.sdk.xdr.TransactionMeta;
import org.stellar.sdk.xdr.XdrDataInputStream;

/**
 * Represents transaction response.
 *
 * @see <a href="https://www.stellar.org/developers/horizon/reference/resources/transaction.html" target="_blank">Transaction documentation</a>
 * @see org.stellar.sdk.requests.TransactionsRequestBuilder
 * @see org.stellar.sdk.Server#transactions()
 */
public class TransactionResponse extends Response {
  @SerializedName("hash")
  private final String hash;
  @SerializedName("ledger")
  private final Long ledger;
  @SerializedName("created_at")
  private final String createdAt;
  @SerializedName("source_account")
  private final KeyPair sourceAccount;
  @SerializedName("paging_token")
  private final String pagingToken;
  @SerializedName("source_account_sequence")
  private final Long sourceAccountSequence;
  @SerializedName("fee_paid")
  private final Long feePaid;
  @SerializedName("operation_count")
  private final Integer operationCount;
  @SerializedName("envelope_xdr")
  private final String envelopeXdr;
  @SerializedName("result_xdr")
  private final String resultXdr;
  @SerializedName("result_meta_xdr")
  private final String resultMetaXdr;
  @SerializedName("_links")
  private final Links links;

  // GSON won't serialize `transient` variables automatically. We need this behaviour
  // because Memo is an abstract class and GSON tries to instantiate it.
  private transient Memo memo;

  TransactionResponse(String hash, Long ledger, String createdAt, KeyPair sourceAccount, String pagingToken, Long sourceAccountSequence, Long feePaid, Integer operationCount, String envelopeXdr, String resultXdr, String resultMetaXdr, Memo memo, Links links) {
    this.hash = hash;
    this.ledger = ledger;
    this.createdAt = createdAt;
    this.sourceAccount = sourceAccount;
    this.pagingToken = pagingToken;
    this.sourceAccountSequence = sourceAccountSequence;
    this.feePaid = feePaid;
    this.operationCount = operationCount;
    this.envelopeXdr = envelopeXdr;
    this.resultXdr = resultXdr;
    this.resultMetaXdr = resultMetaXdr;
    this.memo = memo;
    this.links = links;
  }

  public String getHash() {
    return hash;
  }

  public Long getLedger() {
    return ledger;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public KeyPair getSourceAccount() {
    return sourceAccount;
  }

  public String getPagingToken() {
    return pagingToken;
  }

  public Long getSourceAccountSequence() {
    return sourceAccountSequence;
  }

  public Long getFeePaid() {
    return feePaid;
  }

  public Integer getOperationCount() {
    return operationCount;
  }

  public String getEnvelopeXdr() {
    return envelopeXdr;
  }

  public String getResultXdr() {
    return resultXdr;
  }

  public String getResultMetaXdr() {
    return resultMetaXdr;
  }

  public Memo getMemo() {
    return memo;
  }

  public List<Operation> getOperations() {
    String envelopeXdr = getEnvelopeXdr();
    try {
      Transaction transaction = extractTransaction(envelopeXdr);
      org.stellar.sdk.xdr.Operation[] xdrOperations = transaction.getOperations();
      ArrayList<Operation> operationsList = new ArrayList<>(xdrOperations.length);
      for (org.stellar.sdk.xdr.Operation xdrOperation : xdrOperations) {
        operationsList.add(Operation.fromXdr(xdrOperation));
      }
      return operationsList;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Transaction extractTransaction(String envelopeXdr) throws IOException {
    XdrDataInputStream xdrDataInputStream = createXdrDataInputStream(envelopeXdr);
    return Transaction.decode(xdrDataInputStream);
  }

  public List<LedgerEntryChanges> getLedgerChanges() {
    String resultMetaXdr = getResultMetaXdr();
    try {
      TransactionMeta transactionMeta = extractTransactionMeta(resultMetaXdr);
      OperationMeta[] operationMetas = transactionMeta.getOperations();
      ArrayList<LedgerEntryChanges> ledgerChangesList = new ArrayList<>(operationMetas.length);
      for (OperationMeta operationMeta : operationMetas) {
        ledgerChangesList.add(LedgerEntryChanges.fromXdr(operationMeta.getChanges()));
      }
      return ledgerChangesList;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private TransactionMeta extractTransactionMeta(String envelopeXdr) throws IOException {
    XdrDataInputStream xdrDataInputStream = createXdrDataInputStream(envelopeXdr);
    return TransactionMeta.decode(xdrDataInputStream);
  }

  private XdrDataInputStream createXdrDataInputStream(String envelopeXdr) throws UnsupportedEncodingException {
    Base64 base64 = new Base64();
    byte[] decoded = base64.decode(envelopeXdr.getBytes(CHARSET_UTF8));
    InputStream is = new ByteArrayInputStream(decoded);
    return new XdrDataInputStream(is);
  }

  public void setMemo(Memo memo) {
    memo = checkNotNull(memo, "memo cannot be null");
    if (this.memo != null) {
      throw new RuntimeException("Memo has been already set.");
    }
    this.memo = memo;
  }

  public Links getLinks() {
    return links;
  }

  /**
   * Links connected to transaction.
   */
  public static class Links {
    @SerializedName("account")
    private final Link account;
    @SerializedName("effects")
    private final Link effects;
    @SerializedName("ledger")
    private final Link ledger;
    @SerializedName("operations")
    private final Link operations;
    @SerializedName("precedes")
    private final Link precedes;
    @SerializedName("self")
    private final Link self;
    @SerializedName("succeeds")
    private final Link succeeds;

    Links(Link account, Link effects, Link ledger, Link operations, Link self, Link precedes, Link succeeds) {
      this.account = account;
      this.effects = effects;
      this.ledger = ledger;
      this.operations = operations;
      this.self = self;
      this.precedes = precedes;
      this.succeeds = succeeds;
    }

    public Link getAccount() {
      return account;
    }

    public Link getEffects() {
      return effects;
    }

    public Link getLedger() {
      return ledger;
    }

    public Link getOperations() {
      return operations;
    }

    public Link getPrecedes() {
      return precedes;
    }

    public Link getSelf() {
      return self;
    }

    public Link getSucceeds() {
      return succeeds;
    }
  }
}
