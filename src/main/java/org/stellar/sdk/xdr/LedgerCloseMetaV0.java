// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import static org.stellar.sdk.xdr.Constants.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * LedgerCloseMetaV0's original definition in the XDR file is:
 *
 * <pre>
 * struct LedgerCloseMetaV0
 * {
 *     LedgerHeaderHistoryEntry ledgerHeader;
 *     // NB: txSet is sorted in "Hash order"
 *     TransactionSet txSet;
 *
 *     // NB: transactions are sorted in apply order here
 *     // fees for all transactions are processed first
 *     // followed by applying transactions
 *     TransactionResultMeta txProcessing&lt;&gt;;
 *
 *     // upgrades are applied last
 *     UpgradeEntryMeta upgradesProcessing&lt;&gt;;
 *
 *     // other misc information attached to the ledger close
 *     SCPHistoryEntry scpInfo&lt;&gt;;
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LedgerCloseMetaV0 implements XdrElement {
  private LedgerHeaderHistoryEntry ledgerHeader;
  private TransactionSet txSet;
  private TransactionResultMeta[] txProcessing;
  private UpgradeEntryMeta[] upgradesProcessing;
  private SCPHistoryEntry[] scpInfo;

  public static void encode(XdrDataOutputStream stream, LedgerCloseMetaV0 encodedLedgerCloseMetaV0)
      throws IOException {
    LedgerHeaderHistoryEntry.encode(stream, encodedLedgerCloseMetaV0.ledgerHeader);
    TransactionSet.encode(stream, encodedLedgerCloseMetaV0.txSet);
    int txProcessingSize = encodedLedgerCloseMetaV0.getTxProcessing().length;
    stream.writeInt(txProcessingSize);
    for (int i = 0; i < txProcessingSize; i++) {
      TransactionResultMeta.encode(stream, encodedLedgerCloseMetaV0.txProcessing[i]);
    }
    int upgradesProcessingSize = encodedLedgerCloseMetaV0.getUpgradesProcessing().length;
    stream.writeInt(upgradesProcessingSize);
    for (int i = 0; i < upgradesProcessingSize; i++) {
      UpgradeEntryMeta.encode(stream, encodedLedgerCloseMetaV0.upgradesProcessing[i]);
    }
    int scpInfoSize = encodedLedgerCloseMetaV0.getScpInfo().length;
    stream.writeInt(scpInfoSize);
    for (int i = 0; i < scpInfoSize; i++) {
      SCPHistoryEntry.encode(stream, encodedLedgerCloseMetaV0.scpInfo[i]);
    }
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
  }

  public static LedgerCloseMetaV0 decode(XdrDataInputStream stream) throws IOException {
    LedgerCloseMetaV0 decodedLedgerCloseMetaV0 = new LedgerCloseMetaV0();
    decodedLedgerCloseMetaV0.ledgerHeader = LedgerHeaderHistoryEntry.decode(stream);
    decodedLedgerCloseMetaV0.txSet = TransactionSet.decode(stream);
    int txProcessingSize = stream.readInt();
    decodedLedgerCloseMetaV0.txProcessing = new TransactionResultMeta[txProcessingSize];
    for (int i = 0; i < txProcessingSize; i++) {
      decodedLedgerCloseMetaV0.txProcessing[i] = TransactionResultMeta.decode(stream);
    }
    int upgradesProcessingSize = stream.readInt();
    decodedLedgerCloseMetaV0.upgradesProcessing = new UpgradeEntryMeta[upgradesProcessingSize];
    for (int i = 0; i < upgradesProcessingSize; i++) {
      decodedLedgerCloseMetaV0.upgradesProcessing[i] = UpgradeEntryMeta.decode(stream);
    }
    int scpInfoSize = stream.readInt();
    decodedLedgerCloseMetaV0.scpInfo = new SCPHistoryEntry[scpInfoSize];
    for (int i = 0; i < scpInfoSize; i++) {
      decodedLedgerCloseMetaV0.scpInfo[i] = SCPHistoryEntry.decode(stream);
    }
    return decodedLedgerCloseMetaV0;
  }

  @Override
  public String toXdrBase64() throws IOException {
    return Base64Factory.getInstance().encodeToString(toXdrByteArray());
  }

  @Override
  public byte[] toXdrByteArray() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    XdrDataOutputStream xdrDataOutputStream = new XdrDataOutputStream(byteArrayOutputStream);
    encode(xdrDataOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static LedgerCloseMetaV0 fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static LedgerCloseMetaV0 fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
