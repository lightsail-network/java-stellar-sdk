// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stellar.sdk.Base64Factory;

/**
 * ClaimPredicate's original definition in the XDR file is:
 *
 * <pre>
 * union ClaimPredicate switch (ClaimPredicateType type)
 * {
 * case CLAIM_PREDICATE_UNCONDITIONAL:
 *     void;
 * case CLAIM_PREDICATE_AND:
 *     ClaimPredicate andPredicates&lt;2&gt;;
 * case CLAIM_PREDICATE_OR:
 *     ClaimPredicate orPredicates&lt;2&gt;;
 * case CLAIM_PREDICATE_NOT:
 *     ClaimPredicate&#42; notPredicate;
 * case CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME:
 *     int64 absBefore; // Predicate will be true if closeTime &lt; absBefore
 * case CLAIM_PREDICATE_BEFORE_RELATIVE_TIME:
 *     int64 relBefore; // Seconds since closeTime of the ledger in which the
 *                      // ClaimableBalanceEntry was created
 * };
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClaimPredicate implements XdrElement {
  private ClaimPredicateType discriminant;
  private ClaimPredicate[] andPredicates;
  private ClaimPredicate[] orPredicates;
  private ClaimPredicate notPredicate;
  private Int64 absBefore;
  private Int64 relBefore;

  public void encode(XdrDataOutputStream stream) throws IOException {
    stream.writeInt(discriminant.getValue());
    switch (discriminant) {
      case CLAIM_PREDICATE_UNCONDITIONAL:
        break;
      case CLAIM_PREDICATE_AND:
        int andPredicatesSize = getAndPredicates().length;
        stream.writeInt(andPredicatesSize);
        for (int i = 0; i < andPredicatesSize; i++) {
          andPredicates[i].encode(stream);
        }
        break;
      case CLAIM_PREDICATE_OR:
        int orPredicatesSize = getOrPredicates().length;
        stream.writeInt(orPredicatesSize);
        for (int i = 0; i < orPredicatesSize; i++) {
          orPredicates[i].encode(stream);
        }
        break;
      case CLAIM_PREDICATE_NOT:
        if (notPredicate != null) {
          stream.writeInt(1);
          notPredicate.encode(stream);
        } else {
          stream.writeInt(0);
        }
        break;
      case CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME:
        absBefore.encode(stream);
        break;
      case CLAIM_PREDICATE_BEFORE_RELATIVE_TIME:
        relBefore.encode(stream);
        break;
    }
  }

  public static ClaimPredicate decode(XdrDataInputStream stream) throws IOException {
    ClaimPredicate decodedClaimPredicate = new ClaimPredicate();
    ClaimPredicateType discriminant = ClaimPredicateType.decode(stream);
    decodedClaimPredicate.setDiscriminant(discriminant);
    switch (decodedClaimPredicate.getDiscriminant()) {
      case CLAIM_PREDICATE_UNCONDITIONAL:
        break;
      case CLAIM_PREDICATE_AND:
        int andPredicatesSize = stream.readInt();
        decodedClaimPredicate.andPredicates = new ClaimPredicate[andPredicatesSize];
        for (int i = 0; i < andPredicatesSize; i++) {
          decodedClaimPredicate.andPredicates[i] = ClaimPredicate.decode(stream);
        }
        break;
      case CLAIM_PREDICATE_OR:
        int orPredicatesSize = stream.readInt();
        decodedClaimPredicate.orPredicates = new ClaimPredicate[orPredicatesSize];
        for (int i = 0; i < orPredicatesSize; i++) {
          decodedClaimPredicate.orPredicates[i] = ClaimPredicate.decode(stream);
        }
        break;
      case CLAIM_PREDICATE_NOT:
        int notPredicatePresent = stream.readInt();
        if (notPredicatePresent != 0) {
          decodedClaimPredicate.notPredicate = ClaimPredicate.decode(stream);
        }
        break;
      case CLAIM_PREDICATE_BEFORE_ABSOLUTE_TIME:
        decodedClaimPredicate.absBefore = Int64.decode(stream);
        break;
      case CLAIM_PREDICATE_BEFORE_RELATIVE_TIME:
        decodedClaimPredicate.relBefore = Int64.decode(stream);
        break;
    }
    return decodedClaimPredicate;
  }

  public static ClaimPredicate fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ClaimPredicate fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
