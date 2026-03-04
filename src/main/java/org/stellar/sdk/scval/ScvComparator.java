package org.stellar.sdk.scval;

import java.util.Comparator;
import org.stellar.sdk.xdr.ContractExecutable;
import org.stellar.sdk.xdr.SCAddress;
import org.stellar.sdk.xdr.SCMap;
import org.stellar.sdk.xdr.SCMapEntry;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/**
 * Comparator for {@link SCVal} values following Soroban runtime ordering rules.
 *
 * <p>This mirrors Rust's {@code #[derive(Ord)]} on the {@code ScVal} enum in {@code
 * rs-soroban-env}.
 *
 * <p>Comparison rules:
 *
 * <ol>
 *   <li><b>Cross-type</b>: compare by {@link SCValType} discriminant value ({@code SCV_BOOL=0 <
 *       SCV_VOID=1 < ... < SCV_LEDGER_KEY_NONCE=21}).
 *   <li><b>Same-type</b> (by variant):
 *       <ul>
 *         <li>{@code SCV_BOOL}: {@code False (0) < True (1)}
 *         <li>{@code SCV_VOID}, {@code SCV_LEDGER_KEY_CONTRACT_INSTANCE}: always equal
 *         <li>{@code SCV_U32 / I32 / U64 / I64}: numeric comparison
 *         <li>{@code SCV_TIMEPOINT / DURATION}: numeric comparison of the underlying uint64
 *         <li>{@code SCV_U128}: tuple comparison {@code (hi, lo)} (both unsigned)
 *         <li>{@code SCV_I128}: tuple comparison {@code (hi, lo)} (hi signed, lo unsigned)
 *         <li>{@code SCV_U256}: tuple comparison {@code (hi_hi, hi_lo, lo_hi, lo_lo)} (all
 *             unsigned)
 *         <li>{@code SCV_I256}: tuple comparison {@code (hi_hi, hi_lo, lo_hi, lo_lo)} (hi_hi
 *             signed)
 *         <li>{@code SCV_BYTES / STRING / SYMBOL}: lexicographic byte comparison
 *         <li>{@code SCV_VEC}: element-by-element, shorter &lt; longer
 *         <li>{@code SCV_MAP}: entry-by-entry (key first, then val), shorter &lt; longer
 *         <li>{@code SCV_ADDRESS}: by address type discriminant, then structurally per variant
 *         <li>{@code SCV_ERROR}: by error type discriminant, then contract_code or error code
 *         <li>{@code SCV_CONTRACT_INSTANCE}: by executable type, then wasm_hash, then storage
 *         <li>{@code SCV_LEDGER_KEY_NONCE}: signed numeric comparison of nonce
 *       </ul>
 * </ol>
 */
class ScvComparator implements Comparator<SCVal> {
  static final ScvComparator INSTANCE = new ScvComparator();

  @Override
  public int compare(SCVal a, SCVal b) {
    return compareScVal(a, b);
  }

  static int compareScVal(SCVal a, SCVal b) {
    if (a.getDiscriminant() != b.getDiscriminant()) {
      return Integer.compare(a.getDiscriminant().getValue(), b.getDiscriminant().getValue());
    }

    SCValType t = a.getDiscriminant();
    switch (t) {
      case SCV_BOOL:
        return Boolean.compare(a.getB(), b.getB());
      case SCV_VOID:
      case SCV_LEDGER_KEY_CONTRACT_INSTANCE:
        return 0;
      case SCV_U32:
        return Long.compare(a.getU32().getUint32().getNumber(), b.getU32().getUint32().getNumber());
      case SCV_I32:
        return Integer.compare(a.getI32().getInt32(), b.getI32().getInt32());
      case SCV_U64:
        return a.getU64().getUint64().getNumber().compareTo(b.getU64().getUint64().getNumber());
      case SCV_I64:
        return Long.compare(a.getI64().getInt64(), b.getI64().getInt64());
      case SCV_TIMEPOINT:
        return a.getTimepoint()
            .getTimePoint()
            .getUint64()
            .getNumber()
            .compareTo(b.getTimepoint().getTimePoint().getUint64().getNumber());
      case SCV_DURATION:
        return a.getDuration()
            .getDuration()
            .getUint64()
            .getNumber()
            .compareTo(b.getDuration().getDuration().getUint64().getNumber());
      case SCV_U128:
        {
          int cmp =
              a.getU128()
                  .getHi()
                  .getUint64()
                  .getNumber()
                  .compareTo(b.getU128().getHi().getUint64().getNumber());
          if (cmp != 0) return cmp;
          return a.getU128()
              .getLo()
              .getUint64()
              .getNumber()
              .compareTo(b.getU128().getLo().getUint64().getNumber());
        }
      case SCV_I128:
        {
          int cmp = Long.compare(a.getI128().getHi().getInt64(), b.getI128().getHi().getInt64());
          if (cmp != 0) return cmp;
          return a.getI128()
              .getLo()
              .getUint64()
              .getNumber()
              .compareTo(b.getI128().getLo().getUint64().getNumber());
        }
      case SCV_U256:
        {
          int cmp =
              a.getU256()
                  .getHi_hi()
                  .getUint64()
                  .getNumber()
                  .compareTo(b.getU256().getHi_hi().getUint64().getNumber());
          if (cmp != 0) return cmp;
          cmp =
              a.getU256()
                  .getHi_lo()
                  .getUint64()
                  .getNumber()
                  .compareTo(b.getU256().getHi_lo().getUint64().getNumber());
          if (cmp != 0) return cmp;
          cmp =
              a.getU256()
                  .getLo_hi()
                  .getUint64()
                  .getNumber()
                  .compareTo(b.getU256().getLo_hi().getUint64().getNumber());
          if (cmp != 0) return cmp;
          return a.getU256()
              .getLo_lo()
              .getUint64()
              .getNumber()
              .compareTo(b.getU256().getLo_lo().getUint64().getNumber());
        }
      case SCV_I256:
        {
          int cmp =
              Long.compare(a.getI256().getHi_hi().getInt64(), b.getI256().getHi_hi().getInt64());
          if (cmp != 0) return cmp;
          cmp =
              a.getI256()
                  .getHi_lo()
                  .getUint64()
                  .getNumber()
                  .compareTo(b.getI256().getHi_lo().getUint64().getNumber());
          if (cmp != 0) return cmp;
          cmp =
              a.getI256()
                  .getLo_hi()
                  .getUint64()
                  .getNumber()
                  .compareTo(b.getI256().getLo_hi().getUint64().getNumber());
          if (cmp != 0) return cmp;
          return a.getI256()
              .getLo_lo()
              .getUint64()
              .getNumber()
              .compareTo(b.getI256().getLo_lo().getUint64().getNumber());
        }
      case SCV_BYTES:
        return compareByteArrays(a.getBytes().getSCBytes(), b.getBytes().getSCBytes());
      case SCV_STRING:
        return compareByteArrays(
            a.getStr().getSCString().getBytes(), b.getStr().getSCString().getBytes());
      case SCV_SYMBOL:
        return compareByteArrays(
            a.getSym().getSCSymbol().getBytes(), b.getSym().getSCSymbol().getBytes());
      case SCV_VEC:
        {
          SCVal[] av = a.getVec().getSCVec();
          SCVal[] bv = b.getVec().getSCVec();
          int len = Math.min(av.length, bv.length);
          for (int i = 0; i < len; i++) {
            int cmp = compareScVal(av[i], bv[i]);
            if (cmp != 0) return cmp;
          }
          return Integer.compare(av.length, bv.length);
        }
      case SCV_MAP:
        {
          SCMapEntry[] am = a.getMap().getSCMap();
          SCMapEntry[] bm = b.getMap().getSCMap();
          int len = Math.min(am.length, bm.length);
          for (int i = 0; i < len; i++) {
            int cmp = compareScVal(am[i].getKey(), bm[i].getKey());
            if (cmp != 0) return cmp;
            cmp = compareScVal(am[i].getVal(), bm[i].getVal());
            if (cmp != 0) return cmp;
          }
          return Integer.compare(am.length, bm.length);
        }
      case SCV_ADDRESS:
        return compareScAddress(a.getAddress(), b.getAddress());
      case SCV_ERROR:
        {
          int cmp =
              Integer.compare(
                  a.getError().getDiscriminant().getValue(),
                  b.getError().getDiscriminant().getValue());
          if (cmp != 0) return cmp;
          switch (a.getError().getDiscriminant()) {
            case SCE_CONTRACT:
              return Long.compare(
                  a.getError().getContractCode().getUint32().getNumber(),
                  b.getError().getContractCode().getUint32().getNumber());
            default:
              return Integer.compare(
                  a.getError().getCode().getValue(), b.getError().getCode().getValue());
          }
        }
      case SCV_CONTRACT_INSTANCE:
        {
          int cmp =
              compareContractExecutable(
                  a.getInstance().getExecutable(), b.getInstance().getExecutable());
          if (cmp != 0) return cmp;
          return compareOptionalScMap(a.getInstance().getStorage(), b.getInstance().getStorage());
        }
      case SCV_LEDGER_KEY_NONCE:
        return Long.compare(
            a.getNonce_key().getNonce().getInt64(), b.getNonce_key().getNonce().getInt64());
      default:
        throw new IllegalArgumentException("Unsupported SCVal type: " + t);
    }
  }

  static int compareScAddress(SCAddress a, SCAddress b) {
    int cmp = Integer.compare(a.getDiscriminant().getValue(), b.getDiscriminant().getValue());
    if (cmp != 0) return cmp;

    switch (a.getDiscriminant()) {
      case SC_ADDRESS_TYPE_ACCOUNT:
        return compareByteArrays(
            a.getAccountId().getAccountID().getEd25519().getUint256(),
            b.getAccountId().getAccountID().getEd25519().getUint256());
      case SC_ADDRESS_TYPE_CONTRACT:
        return compareByteArrays(
            a.getContractId().getContractID().getHash(),
            b.getContractId().getContractID().getHash());
      case SC_ADDRESS_TYPE_MUXED_ACCOUNT:
        {
          cmp =
              a.getMuxedAccount()
                  .getId()
                  .getUint64()
                  .getNumber()
                  .compareTo(b.getMuxedAccount().getId().getUint64().getNumber());
          if (cmp != 0) return cmp;
          return compareByteArrays(
              a.getMuxedAccount().getEd25519().getUint256(),
              b.getMuxedAccount().getEd25519().getUint256());
        }
      case SC_ADDRESS_TYPE_CLAIMABLE_BALANCE:
        if (a.getClaimableBalanceId().getDiscriminant()
            != org.stellar.sdk.xdr.ClaimableBalanceIDType.CLAIMABLE_BALANCE_ID_TYPE_V0) {
          throw new IllegalArgumentException(
              "Unsupported ClaimableBalanceID type: "
                  + a.getClaimableBalanceId().getDiscriminant());
        }
        return compareByteArrays(
            a.getClaimableBalanceId().getV0().getHash(),
            b.getClaimableBalanceId().getV0().getHash());
      case SC_ADDRESS_TYPE_LIQUIDITY_POOL:
        return compareByteArrays(
            a.getLiquidityPoolId().getPoolID().getHash(),
            b.getLiquidityPoolId().getPoolID().getHash());
      default:
        throw new IllegalArgumentException("Unsupported SCAddress type: " + a.getDiscriminant());
    }
  }

  static int compareContractExecutable(ContractExecutable a, ContractExecutable b) {
    int cmp = Integer.compare(a.getDiscriminant().getValue(), b.getDiscriminant().getValue());
    if (cmp != 0) return cmp;

    switch (a.getDiscriminant()) {
      case CONTRACT_EXECUTABLE_WASM:
        return compareByteArrays(a.getWasm_hash().getHash(), b.getWasm_hash().getHash());
      case CONTRACT_EXECUTABLE_STELLAR_ASSET:
        return 0;
      default:
        throw new IllegalArgumentException(
            "Unsupported ContractExecutable type: " + a.getDiscriminant());
    }
  }

  static int compareOptionalScMap(SCMap a, SCMap b) {
    if (a == null && b == null) return 0;
    if (a == null) return -1;
    if (b == null) return 1;

    SCMapEntry[] am = a.getSCMap();
    SCMapEntry[] bm = b.getSCMap();
    int len = Math.min(am.length, bm.length);
    for (int i = 0; i < len; i++) {
      int cmp = compareScVal(am[i].getKey(), bm[i].getKey());
      if (cmp != 0) return cmp;
      cmp = compareScVal(am[i].getVal(), bm[i].getVal());
      if (cmp != 0) return cmp;
    }
    return Integer.compare(am.length, bm.length);
  }

  /** Lexicographic unsigned byte comparison. */
  private static int compareByteArrays(byte[] a, byte[] b) {
    int len = Math.min(a.length, b.length);
    for (int i = 0; i < len; i++) {
      int cmp = Integer.compare(a[i] & 0xFF, b[i] & 0xFF);
      if (cmp != 0) return cmp;
    }
    return Integer.compare(a.length, b.length);
  }
}
