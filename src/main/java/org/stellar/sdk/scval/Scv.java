package org.stellar.sdk.scval;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.stellar.sdk.Address;
import org.stellar.sdk.xdr.SCContractInstance;
import org.stellar.sdk.xdr.SCError;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Provides a range of methods to help you build and parse {@link SCVal} more conveniently. */
public abstract class Scv {

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}.
   *
   * @param address address to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}
   */
  public static SCVal toAddress(String address) {
    return new Address(address).toSCVal();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}.
   *
   * @param address address to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}
   */
  public static SCVal toAddress(Address address) {
    return address.toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS} to {@link Address}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the address
   */
  public static Address fromAddress(SCVal scVal) {
    return Address.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_BOOL}.
   *
   * @param bool boolean to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_BOOL}
   */
  public static SCVal toBoolean(boolean bool) {
    return new ScvBoolean(bool).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_BOOL} to boolean.
   *
   * @param scVal {@link SCVal} to convert
   * @return boolean
   */
  public static boolean fromBoolean(SCVal scVal) {
    return ScvBoolean.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_BYTES}.
   *
   * @param bytes bytes to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_BYTES}
   */
  public static SCVal toBytes(byte[] bytes) {
    return new ScvBytes(bytes).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_BYTES} to byte[].
   *
   * @param scVal {@link SCVal} to convert
   * @return the bytes
   */
  public static byte[] fromBytes(SCVal scVal) {
    return ScvBytes.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}.
   *
   * @param instance {@link SCContractInstance} to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}
   */
  public static SCVal toContractInstance(SCContractInstance instance) {
    return new ScvContractInstance(instance).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE} to {@link
   * SCContractInstance}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the contract instance
   */
  public static SCContractInstance fromContractInstance(SCVal scVal) {
    return ScvContractInstance.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_DURATION}.
   *
   * @param duration duration to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_DURATION}
   */
  public static SCVal toDuration(BigInteger duration) {
    return new ScvDuration(duration).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_DURATION} to {@link
   * BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the duration
   */
  public static BigInteger fromDuration(SCVal scVal) {
    return ScvDuration.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_ERROR}.
   *
   * @param error {@link SCError} to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_ERROR}
   */
  public static SCVal toError(SCError error) {
    return new ScvError(error).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_ERROR} to {@link SCError}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the error
   */
  public static SCError fromError(SCVal scVal) {
    return ScvError.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I128}.
   *
   * @param int32 int32 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I128}
   */
  public static SCVal toInt32(int int32) {
    return new ScvInt32(int32).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I128} to int32.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int32
   */
  public static int fromInt32(SCVal scVal) {
    return ScvInt32.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I64}.
   *
   * @param int64 int64 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I64}
   */
  public static SCVal toInt64(long int64) {
    return new ScvInt64(int64).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I64} to int64.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int64
   */
  public static long fromInt64(SCVal scVal) {
    return ScvInt64.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I128}.
   *
   * @param int128 int128 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I128}
   */
  public static SCVal toInt128(BigInteger int128) {
    return new ScvInt128(int128).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I128} to int128.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int128
   */
  public static BigInteger fromInt128(SCVal scVal) {
    return ScvInt128.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I256}.
   *
   * @param int256 int256 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I256}
   */
  public static SCVal toInt256(BigInteger int256) {
    return new ScvInt256(int256).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I256} to int256.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int256
   */
  public static BigInteger fromInt256(SCVal scVal) {
    return ScvInt256.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}.
   *
   * @return {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}
   */
  public static SCVal toLedgerKeyContractInstance() {
    return new ScvLedgerKeyContractInstance().toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE} to {@link
   * SCContractInstance}.
   *
   * <p>This function returns void if conversion succeeds.
   *
   * @param scVal {@link SCVal} to convert
   */
  public static void fromLedgerKeyContractInstance(SCVal scVal) {
    ScvLedgerKeyContractInstance.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}.
   *
   * @param nonce nonce to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}
   */
  public static SCVal toLedgerKeyNonce(long nonce) {
    return new ScvLedgerKeyNonce(nonce).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE} to long.
   *
   * @param scVal {@link SCVal} to convert
   * @return the nonce
   */
  public static long fromLedgerKeyNonce(SCVal scVal) {
    return ScvLedgerKeyNonce.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_MAP}.
   *
   * @param map map to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_MAP}
   */
  public static SCVal toMap(LinkedHashMap<SCVal, SCVal> map) {
    return new ScvMap(map).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_MAP} to {@link Map}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the map
   */
  public static LinkedHashMap<SCVal, SCVal> fromMap(SCVal scVal) {
    return ScvMap.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_STRING}.
   *
   * @param string string to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_STRING}
   */
  public static SCVal toString(String string) {
    return new ScvString(string).toSCVal();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_STRING}.
   *
   * @param string string to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_STRING}
   */
  public static SCVal toString(byte[] string) {
    return new ScvString(string).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_STRING} to String.
   *
   * @param scVal {@link SCVal} to convert
   * @return the string value in bytes
   */
  public static byte[] fromString(SCVal scVal) {
    return ScvString.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL}.
   *
   * @param symbol symbol to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL}
   */
  public static SCVal toSymbol(String symbol) {
    return new ScvSymbol(symbol).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL} to String.
   *
   * @param scVal {@link SCVal} to convert
   * @return the symbol
   */
  public static String fromSymbol(SCVal scVal) {
    return ScvSymbol.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_TIMEPOINT}.
   *
   * @param timePoint timePoint to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_TIMEPOINT}
   */
  public static SCVal toTimePoint(BigInteger timePoint) {
    return new ScvTimePoint(timePoint).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_TIMEPOINT} to {@link
   * BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the timePoint
   */
  public static BigInteger fromTimePoint(SCVal scVal) {
    return ScvTimePoint.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U32}.
   *
   * @param uint32 uint32 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U32}
   */
  public static SCVal toUint32(long uint32) {
    return new ScvUint32(uint32).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U32} to int.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint32
   */
  public static long fromUint32(SCVal scVal) {
    return ScvUint32.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U64}.
   *
   * @param uint64 uint64 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U64}
   */
  public static SCVal toUint64(BigInteger uint64) {
    return new ScvUint64(uint64).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U64} to {@link BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint64
   */
  public static BigInteger fromUint64(SCVal scVal) {
    return ScvUint64.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U128}.
   *
   * @param uint128 uint128 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U128}
   */
  public static SCVal toUint128(BigInteger uint128) {
    return new ScvUint128(uint128).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U128} to {@link BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint128
   */
  public static BigInteger fromUint128(SCVal scVal) {
    return ScvUint128.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U256}.
   *
   * @param uint256 uint256 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U256}
   */
  public static SCVal toUint256(BigInteger uint256) {
    return new ScvUint256(uint256).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U256} to {@link BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint256
   */
  public static BigInteger fromUint256(SCVal scVal) {
    return ScvUint256.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_VEC}.
   *
   * @param vec vec to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_VEC}
   */
  public static SCVal toVec(Collection<SCVal> vec) {
    return new ScvVec(vec).toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_VEC} to {@link Collection}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the vec
   */
  public static Collection<SCVal> fromVec(SCVal scVal) {
    return ScvVec.fromSCVal(scVal).getValue();
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_VOID}.
   *
   * @return {@link SCVal} with the type of {@link SCValType#SCV_VOID}
   */
  public static SCVal toVoid() {
    return new ScvVoid().toSCVal();
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_VOID} to {@link SCVal}.
   *
   * <p>This function returns void if conversion succeeds.
   *
   * @param scVal {@link SCVal} to convert
   */
  public static void fromVoid(SCVal scVal) {
    ScvVoid.fromSCVal(scVal);
  }

  static byte[] getBytes(BigInteger value) {
    byte[] bytes = value.toByteArray();
    if (bytes.length < 8) {
      byte[] temp = new byte[8];
      System.arraycopy(bytes, 0, temp, 8 - bytes.length, bytes.length);
      bytes = temp;
    } else if (bytes.length > 8) {
      bytes = Arrays.copyOfRange(bytes, bytes.length - 8, bytes.length);
    }
    return bytes;
  }

  /**
   * Convert this object to {@link SCVal}.
   *
   * @return {@link SCVal}
   */
  public abstract SCVal toSCVal();
}
