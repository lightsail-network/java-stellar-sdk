package org.stellar.sdk;

import java.io.IOException;
import java.util.Collection;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.stellar.sdk.xdr.ExtensionPoint;
import org.stellar.sdk.xdr.Int64;
import org.stellar.sdk.xdr.LedgerFootprint;
import org.stellar.sdk.xdr.LedgerKey;
import org.stellar.sdk.xdr.SorobanResources;
import org.stellar.sdk.xdr.SorobanTransactionData;
import org.stellar.sdk.xdr.Uint32;
import org.stellar.sdk.xdr.XdrUnsignedInteger;

/**
 * Supports building {@link SorobanTransactionData} structures with various items set to specific
 * values.
 *
 * <p>This is recommended for when you are building {@link BumpFootprintExpirationOperation} and
 * {@link RestoreFootprintOperation} operations to avoid (re)building the entire data structure from
 * scratch.
 */
public class SorobanDataBuilder {
  private final SorobanTransactionData data;

  /** Creates a new builder with an empty {@link SorobanTransactionData}. */
  public SorobanDataBuilder() {
    data =
        new SorobanTransactionData.Builder()
            .resources(
                new SorobanResources.Builder()
                    .footprint(
                        new LedgerFootprint.Builder()
                            .readOnly(new LedgerKey[] {})
                            .readWrite(new LedgerKey[] {})
                            .build())
                    .instructions(new Uint32(new XdrUnsignedInteger(0)))
                    .readBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .writeBytes(new Uint32(new XdrUnsignedInteger(0)))
                    .build())
            .refundableFee(new Int64(0L))
            .ext(new ExtensionPoint.Builder().discriminant(0).build())
            .build();
  }

  /**
   * Creates a new builder from a base64 representation of {@link SorobanTransactionData}.
   *
   * @param sorobanData base64 representation of {@link SorobanTransactionData}
   */
  public SorobanDataBuilder(String sorobanData) {
    try {
      data = SorobanTransactionData.fromXdrBase64(sorobanData);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid SorobanData: " + sorobanData, e);
    }
  }

  /**
   * Creates a new builder from a {@link SorobanTransactionData}.
   *
   * @param sorobanData {@link SorobanTransactionData}.
   */
  public SorobanDataBuilder(SorobanTransactionData sorobanData) {
    try {
      data = SorobanTransactionData.fromXdrByteArray(sorobanData.toXdrByteArray());
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid SorobanData: " + sorobanData, e);
    }
  }

  /**
   * Sets the "refundable" fee portion of the Soroban data.
   *
   * @param fee the refundable fee to set (int64)
   * @return this builder instance
   */
  public SorobanDataBuilder setRefundableFee(long fee) {
    data.setRefundableFee(new Int64(fee));
    return this;
  }

  /**
   * Sets up the resource metrics.
   *
   * <p>You should almost NEVER need this, as its often generated/provided to you by transaction
   * simulation/preflight from a Soroban RPC server.
   *
   * @param resources the resource metrics to set
   * @return this builder instance
   */
  public SorobanDataBuilder setResources(Resources resources) {
    data.getResources()
        .setInstructions(new Uint32(new XdrUnsignedInteger(resources.getCpuInstructions())));
    data.getResources().setReadBytes(new Uint32(new XdrUnsignedInteger(resources.getReadBytes())));
    data.getResources()
        .setWriteBytes(new Uint32(new XdrUnsignedInteger(resources.getWriteBytes())));
    return this;
  }

  /**
   * Sets the read-only portion of the storage access footprint to be a certain set of ledger keys.
   *
   * <p>Passing {@code null} will leave that portion of the footprint untouched. If you want to
   * clear a portion of the footprint, pass an empty collection.
   *
   * @param readOnly the set of ledger keys to set in the read-only portion of the transaction's
   *     sorobanData
   * @return this builder instance
   */
  public SorobanDataBuilder setReadOnly(@Nullable Collection<LedgerKey> readOnly) {
    if (readOnly != null) {
      data.getResources().getFootprint().setReadOnly(readOnly.toArray(new LedgerKey[0]));
    }
    return this;
  }

  /**
   * Sets the read-write portion of the storage access footprint to be a certain set of ledger keys.
   *
   * <p>Passing {@code null} will leave that portion of the footprint untouched. If you want to
   * clear a portion of the footprint, pass an empty collection.
   *
   * @param readWrite the set of ledger keys to set in the read-write portion of the transaction's
   *     sorobanData
   * @return this builder instance
   */
  public SorobanDataBuilder setReadWrite(@Nullable Collection<LedgerKey> readWrite) {
    if (readWrite != null) {
      data.getResources().getFootprint().setReadWrite(readWrite.toArray(new LedgerKey[0]));
    }
    return this;
  }

  /**
   * @return the copy of the final {@link SorobanTransactionData}.
   */
  public SorobanTransactionData build() {
    try {
      return SorobanTransactionData.fromXdrByteArray(data.toXdrByteArray());
    } catch (IOException e) {
      throw new IllegalArgumentException("Copy SorobanData failed, please report this bug.", e);
    }
  }

  /** Represents the resource metrics of the Soroban data. */
  @Builder(toBuilder = true)
  @Value
  public static class Resources {
    // number of CPU instructions (uint32)
    @NonNull Long cpuInstructions;
    // number of bytes being read (uint32)
    @NonNull Long readBytes;
    // number of bytes being written (uint32)
    @NonNull Long writeBytes;
  }
}
