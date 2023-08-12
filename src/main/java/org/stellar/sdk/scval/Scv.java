package org.stellar.sdk.scval;

import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.NoArgsConstructor;
import org.stellar.sdk.Address;
import org.stellar.sdk.xdr.SCContractInstance;
import org.stellar.sdk.xdr.SCError;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;

/** Provides a range of methods to help you build and parse {@link SCVal} more conveniently. */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Scv {

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}.
   *
   * @param address address to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}
   */
  public static SCVal toAddress(String address) {
    return ScvAddress.toSCVal(new Address(address));
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}.
   *
   * @param address address to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS}
   */
  public static SCVal toAddress(Address address) {
    return ScvAddress.toSCVal(address);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_ADDRESS} to {@link Address}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the address
   */
  public static Address fromAddress(SCVal scVal) {
    return ScvAddress.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_BOOL}.
   *
   * @param bool boolean to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_BOOL}
   */
  public static SCVal toBoolean(boolean bool) {
    return ScvBoolean.toSCVal(bool);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_BOOL} to boolean.
   *
   * @param scVal {@link SCVal} to convert
   * @return boolean
   */
  public static boolean fromBoolean(SCVal scVal) {
    return ScvBoolean.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_BYTES}.
   *
   * @param bytes bytes to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_BYTES}
   */
  public static SCVal toBytes(byte[] bytes) {
    return ScvBytes.toSCVal(bytes);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_BYTES} to byte[].
   *
   * @param scVal {@link SCVal} to convert
   * @return the bytes
   */
  public static byte[] fromBytes(SCVal scVal) {
    return ScvBytes.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}.
   *
   * @param instance {@link SCContractInstance} to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE}
   */
  public static SCVal toContractInstance(SCContractInstance instance) {
    return ScvContractInstance.toSCVal(instance);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_CONTRACT_INSTANCE} to {@link
   * SCContractInstance}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the contract instance
   */
  public static SCContractInstance fromContractInstance(SCVal scVal) {
    return ScvContractInstance.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_DURATION}.
   *
   * @param duration duration to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_DURATION}
   */
  public static SCVal toDuration(BigInteger duration) {
    return ScvDuration.toSCVal(duration);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_DURATION} to {@link
   * BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the duration
   */
  public static BigInteger fromDuration(SCVal scVal) {
    return ScvDuration.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_ERROR}.
   *
   * @param error {@link SCError} to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_ERROR}
   */
  public static SCVal toError(SCError error) {
    return ScvError.toSCVal(error);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_ERROR} to {@link SCError}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the error
   */
  public static SCError fromError(SCVal scVal) {
    return ScvError.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I128}.
   *
   * @param int32 int32 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I128}
   */
  public static SCVal toInt32(int int32) {
    return ScvInt32.toSCVal(int32);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I128} to int32.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int32
   */
  public static int fromInt32(SCVal scVal) {
    return ScvInt32.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I64}.
   *
   * @param int64 int64 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I64}
   */
  public static SCVal toInt64(long int64) {
    return ScvInt64.toSCVal(int64);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I64} to int64.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int64
   */
  public static long fromInt64(SCVal scVal) {
    return ScvInt64.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I128}.
   *
   * @param int128 int128 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I128}
   */
  public static SCVal toInt128(BigInteger int128) {
    return ScvInt128.toSCVal(int128);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I128} to int128.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int128
   */
  public static BigInteger fromInt128(SCVal scVal) {
    return ScvInt128.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_I256}.
   *
   * @param int256 int256 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_I256}
   */
  public static SCVal toInt256(BigInteger int256) {
    return ScvInt256.toSCVal(int256);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_I256} to int256.
   *
   * @param scVal {@link SCVal} to convert
   * @return the int256
   */
  public static BigInteger fromInt256(SCVal scVal) {
    return ScvInt256.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}.
   *
   * @return {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE}
   */
  public static SCVal toLedgerKeyContractInstance() {
    return ScvLedgerKeyContractInstance.toSCVal();
  }

  /**
   * Parse from {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_CONTRACT_INSTANCE},
   * this function returns void if conversion succeeds.
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
    return ScvLedgerKeyNonce.toSCVal(nonce);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_LEDGER_KEY_NONCE} to long.
   *
   * @param scVal {@link SCVal} to convert
   * @return the nonce
   */
  public static long fromLedgerKeyNonce(SCVal scVal) {
    return ScvLedgerKeyNonce.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_MAP}.
   *
   * @param map map to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_MAP}
   */
  public static SCVal toMap(LinkedHashMap<SCVal, SCVal> map) {
    return ScvMap.toSCVal(map);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_MAP} to {@link Map}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the map
   */
  public static LinkedHashMap<SCVal, SCVal> fromMap(SCVal scVal) {
    return ScvMap.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_STRING}.
   *
   * @param string string to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_STRING}
   */
  public static SCVal toString(String string) {
    return ScvString.toSCVal(string.getBytes());
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_STRING}.
   *
   * @param string string to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_STRING}
   */
  public static SCVal toString(byte[] string) {
    return ScvString.toSCVal(string);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_STRING} to String.
   *
   * @param scVal {@link SCVal} to convert
   * @return the string value in bytes
   */
  public static byte[] fromString(SCVal scVal) {
    return ScvString.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL}.
   *
   * @param symbol symbol to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL}
   */
  public static SCVal toSymbol(String symbol) {
    return ScvSymbol.toSCVal(symbol);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_SYMBOL} to String.
   *
   * @param scVal {@link SCVal} to convert
   * @return the symbol
   */
  public static String fromSymbol(SCVal scVal) {
    return ScvSymbol.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_TIMEPOINT}.
   *
   * @param timePoint timePoint to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_TIMEPOINT}
   */
  public static SCVal toTimePoint(BigInteger timePoint) {
    return ScvTimePoint.toSCVal(timePoint);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_TIMEPOINT} to {@link
   * BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the timePoint
   */
  public static BigInteger fromTimePoint(SCVal scVal) {
    return ScvTimePoint.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U32}.
   *
   * @param uint32 uint32 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U32}
   */
  public static SCVal toUint32(long uint32) {
    return ScvUint32.toSCVal(uint32);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U32} to int.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint32
   */
  public static long fromUint32(SCVal scVal) {
    return ScvUint32.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U64}.
   *
   * @param uint64 uint64 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U64}
   */
  public static SCVal toUint64(BigInteger uint64) {
    return ScvUint64.toSCVal(uint64);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U64} to {@link BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint64
   */
  public static BigInteger fromUint64(SCVal scVal) {
    return ScvUint64.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U128}.
   *
   * @param uint128 uint128 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U128}
   */
  public static SCVal toUint128(BigInteger uint128) {
    return ScvUint128.toSCVal(uint128);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U128} to {@link BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint128
   */
  public static BigInteger fromUint128(SCVal scVal) {
    return ScvUint128.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_U256}.
   *
   * @param uint256 uint256 to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_U256}
   */
  public static SCVal toUint256(BigInteger uint256) {
    return ScvUint256.toSCVal(uint256);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_U256} to {@link BigInteger}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the uint256
   */
  public static BigInteger fromUint256(SCVal scVal) {
    return ScvUint256.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_VEC}.
   *
   * @param vec vec to convert
   * @return {@link SCVal} with the type of {@link SCValType#SCV_VEC}
   */
  public static SCVal toVec(Collection<SCVal> vec) {
    return ScvVec.toSCVal(vec);
  }

  /**
   * Convert from {@link SCVal} with the type of {@link SCValType#SCV_VEC} to {@link Collection}.
   *
   * @param scVal {@link SCVal} to convert
   * @return the vec
   */
  public static Collection<SCVal> fromVec(SCVal scVal) {
    return ScvVec.fromSCVal(scVal);
  }

  /**
   * Build a {@link SCVal} with the type of {@link SCValType#SCV_VOID}.
   *
   * @return {@link SCVal} with the type of {@link SCValType#SCV_VOID}
   */
  public static SCVal toVoid() {
    return ScvVoid.toSCVal();
  }

  /**
   * Parse from {@link SCVal} with the type of {@link SCValType#SCV_VOID}, this function returns
   * void if conversion succeeds.
   *
   * @param scVal {@link SCVal} to convert
   */
  public static void fromVoid(SCVal scVal) {
    ScvVoid.fromSCVal(scVal);
  }
}
