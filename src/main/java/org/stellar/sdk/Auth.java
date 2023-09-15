package org.stellar.sdk;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import org.stellar.sdk.scval.Scv;
import org.stellar.sdk.xdr.EnvelopeType;
import org.stellar.sdk.xdr.Hash;
import org.stellar.sdk.xdr.HashIDPreimage;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SorobanAddressCredentials;
import org.stellar.sdk.xdr.SorobanAuthorizationEntry;
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation;
import org.stellar.sdk.xdr.SorobanCredentials;
import org.stellar.sdk.xdr.SorobanCredentialsType;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

public class Auth {
  public SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry, KeyPair signer, Long validUntilLedgerSeq, Network network) {
    Signer signFunction =
        preimage -> {
          byte[] data;
          try {
            data = preimage.toXdrByteArray();
          } catch (IOException e) {
            throw new IllegalArgumentException("Unable to convert preimage to bytes", e);
          }
          byte[] payload = Util.hash(data);
          return signer.sign(payload);
        };

    return authorizeEntry(entry, signFunction, validUntilLedgerSeq, network);
  }

  public SorobanAuthorizationEntry authorizeEntry(
      SorobanAuthorizationEntry entry, Signer signer, Long validUntilLedgerSeq, Network network) {
    if (entry.getCredentials().getDiscriminant()
        != SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS) {
      return entry;
    }

    SorobanAuthorizationEntry clone;
    try {
      clone = SorobanAuthorizationEntry.fromXdrByteArray(entry.toXdrByteArray());
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to clone SorobanAuthorizationEntry", e);
    }

    SorobanAddressCredentials addrAuth = clone.getCredentials().getAddress();
    addrAuth.setSignatureExpirationLedger(new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)));

    HashIDPreimage preimage =
        new HashIDPreimage.Builder()
            .discriminant(EnvelopeType.ENVELOPE_TYPE_SOROBAN_AUTHORIZATION)
            .sorobanAuthorization(
                new HashIDPreimage.HashIDPreimageSorobanAuthorization.Builder()
                    .networkID(new Hash(network.getNetworkId()))
                    .nonce(addrAuth.getNonce())
                    .invocation(clone.getRootInvocation())
                    .signatureExpirationLedger(addrAuth.getSignatureExpirationLedger())
                    .build())
            .build();
    byte[] signature = signer.sign(preimage);
    byte[] publicKey = Address.fromSCAddress(addrAuth.getAddress()).getBytes();

    byte[] data = new byte[0];
    try {
      data = preimage.toXdrByteArray();
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to convert preimage to bytes", e);
    }
    byte[] payload = Util.hash(data);
    if (!KeyPair.fromPublicKey(publicKey).verify(payload, signature)) {
      throw new IllegalArgumentException("signature does not match payload");
    }

    // This structure is defined here:
    // https://soroban.stellar.org/docs/fundamentals-and-concepts/invoking-contracts-with-transactions#stellar-account-signatures
    SCVal sigScVal =
        Scv.toMap(
            new LinkedHashMap<SCVal, SCVal>() {
              {
                put(Scv.toSymbol("public_key"), Scv.toBytes(publicKey));
                put(Scv.toSymbol("signature"), Scv.toBytes(signature));
              }
            });
    addrAuth.setSignatureArgs(Scv.toVec(Collections.singleton(sigScVal)).getVec());
    return entry;
  }

  public SorobanAuthorizationEntry authorizeInvocation(
      KeyPair signer,
      Long validUntilLedgerSeq,
      SorobanAuthorizedInvocation invocation,
      Network network) {
    Signer signFunction =
        preimage -> {
          try {
            byte[] payload = Util.hash(preimage.toXdrByteArray());
            return signer.sign(payload);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        };
    return authorizeInvocation(
        signFunction, validUntilLedgerSeq, invocation, signer.getAccountId(), network);
  }

  public SorobanAuthorizationEntry authorizeInvocation(
      Signer signer,
      Long validUntilLedgerSeq,
      SorobanAuthorizedInvocation invocation,
      String publicKey,
      Network network) {
    // random int64 nonce
    long nonce = 1;

    SorobanAuthorizationEntry entry =
        new SorobanAuthorizationEntry.Builder()
            .credentials(
                new SorobanCredentials.Builder()
                    .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
                    .address(
                        new SorobanAddressCredentials.Builder()
                            .address(new Address(publicKey).toSCAddress())
                            .nonce(new Int64(nonce))
                            .signatureExpirationLedger(
                                new Uint32(new XdrUnsignedInteger(validUntilLedgerSeq)))
                            .signatureArgs(Scv.toVec(Collections.emptyList()).getVec())
                            .build())
                    .build())
            .rootInvocation(invocation)
            .build();
    return authorizeEntry(entry, signer, validUntilLedgerSeq, network);
  }

  public interface Signer {
    byte[] sign(HashIDPreimage preimage);
  }
}
