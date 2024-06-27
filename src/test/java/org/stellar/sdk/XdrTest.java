package org.stellar.sdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class XdrTest {
  public static void main(String[] args) {
    String csvFile = "/Users/overcat/Desktop/bq-results-20240511-030411-1715396689438.csv";
    String line;
    String csvSplitBy = ",";

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      // 读取CSV文件的标题行
      String[] headers = br.readLine().split(csvSplitBy);
      int txEnvelopeIndex = -1;
      int transactionHashIndex = -1;

      // 查找标题行中对应的列索引
      for (int i = 0; i < headers.length; i++) {
        if (headers[i].equals("tx_envelope")) {
          txEnvelopeIndex = i;
        } else if (headers[i].equals("transaction_hash")) {
          transactionHashIndex = i;
        }
      }

      // 读取数据行并获取对应的值
      while ((line = br.readLine()) != null) {
        String[] values = line.split(csvSplitBy);
        if (txEnvelopeIndex != -1 && transactionHashIndex != -1) {
          String txEnvelope = values[txEnvelopeIndex];
          String transactionHash = values[transactionHashIndex];
          System.out.println("transactionHash: " + transactionHash);

          if (!Transaction.fromEnvelopeXdr(txEnvelope, Network.PUBLIC)
              .hashHex()
              .equals(transactionHash)) {
            throw new RuntimeException("Transaction hash mismatch");
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
