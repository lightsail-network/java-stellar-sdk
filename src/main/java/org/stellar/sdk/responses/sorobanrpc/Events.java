package org.stellar.sdk.responses.sorobanrpc;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;
import org.stellar.sdk.Util;
import org.stellar.sdk.xdr.ContractEvent;
import org.stellar.sdk.xdr.DiagnosticEvent;
import org.stellar.sdk.xdr.TransactionEvent;

@Value
public class Events {
  List<String> diagnosticEventsXdr;

  List<String> transactionEventsXdr;

  List<List<String>> contractEventsXdr;

  /**
   * Parses the {@code diagnosticEventsXdr} field from a list of strings to a list of {@link
   * org.stellar.sdk.xdr.DiagnosticEvent} objects.
   *
   * @return a list of parsed {@link org.stellar.sdk.xdr.DiagnosticEvent} objects
   */
  public List<DiagnosticEvent> parseDiagnosticEventsXdr() {
    if (diagnosticEventsXdr == null) {
      return null;
    }
    return diagnosticEventsXdr.stream()
        .map(xdr -> Util.parseXdr(xdr, DiagnosticEvent::fromXdrBase64))
        .collect(Collectors.toList());
  }

  /**
   * Parses the {@code transactionEventsXdr} field from a list of strings to a list of {@link
   * org.stellar.sdk.xdr.TransactionEvent} objects.
   *
   * @return a list of parsed {@link org.stellar.sdk.xdr.TransactionEvent} objects
   */
  public List<TransactionEvent> parseTransactionEventsXdr() {
    if (transactionEventsXdr == null) {
      return null;
    }
    return transactionEventsXdr.stream()
        .map(xdr -> Util.parseXdr(xdr, TransactionEvent::fromXdrBase64))
        .collect(Collectors.toList());
  }

  /**
   * Parses the {@code contractEventsXdr} field from a list of lists of strings to a list of lists
   * of {@link org.stellar.sdk.xdr.ContractEvent} objects.
   *
   * @return a list of lists of parsed {@link org.stellar.sdk.xdr.ContractEvent} objects
   */
  public List<List<ContractEvent>> parseContractEventsXdr() {
    if (contractEventsXdr == null) {
      return null;
    }
    return contractEventsXdr.stream()
        .map(
            events ->
                events.stream()
                    .map(xdr -> Util.parseXdr(xdr, ContractEvent::fromXdrBase64))
                    .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }
}
