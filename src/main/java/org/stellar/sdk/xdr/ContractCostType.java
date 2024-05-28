// Automatically generated by xdrgen
// DO NOT EDIT or your changes may be overwritten

package org.stellar.sdk.xdr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.stellar.sdk.Base64Factory;

/**
 * ContractCostType's original definition in the XDR file is:
 *
 * <pre>
 * enum ContractCostType {
 *     // Cost of running 1 wasm instruction
 *     WasmInsnExec = 0,
 *     // Cost of allocating a slice of memory (in bytes)
 *     MemAlloc = 1,
 *     // Cost of copying a slice of bytes into a pre-allocated memory
 *     MemCpy = 2,
 *     // Cost of comparing two slices of memory
 *     MemCmp = 3,
 *     // Cost of a host function dispatch, not including the actual work done by
 *     // the function nor the cost of VM invocation machinary
 *     DispatchHostFunction = 4,
 *     // Cost of visiting a host object from the host object storage. Exists to
 *     // make sure some baseline cost coverage, i.e. repeatly visiting objects
 *     // by the guest will always incur some charges.
 *     VisitObject = 5,
 *     // Cost of serializing an xdr object to bytes
 *     ValSer = 6,
 *     // Cost of deserializing an xdr object from bytes
 *     ValDeser = 7,
 *     // Cost of computing the sha256 hash from bytes
 *     ComputeSha256Hash = 8,
 *     // Cost of computing the ed25519 pubkey from bytes
 *     ComputeEd25519PubKey = 9,
 *     // Cost of verifying ed25519 signature of a payload.
 *     VerifyEd25519Sig = 10,
 *     // Cost of instantiation a VM from wasm bytes code.
 *     VmInstantiation = 11,
 *     // Cost of instantiation a VM from a cached state.
 *     VmCachedInstantiation = 12,
 *     // Cost of invoking a function on the VM. If the function is a host function,
 *     // additional cost will be covered by `DispatchHostFunction`.
 *     InvokeVmFunction = 13,
 *     // Cost of computing a keccak256 hash from bytes.
 *     ComputeKeccak256Hash = 14,
 *     // Cost of decoding an ECDSA signature computed from a 256-bit prime modulus
 *     // curve (e.g. secp256k1 and secp256r1)
 *     DecodeEcdsaCurve256Sig = 15,
 *     // Cost of recovering an ECDSA secp256k1 key from a signature.
 *     RecoverEcdsaSecp256k1Key = 16,
 *     // Cost of int256 addition (`+`) and subtraction (`-`) operations
 *     Int256AddSub = 17,
 *     // Cost of int256 multiplication (`&#42;`) operation
 *     Int256Mul = 18,
 *     // Cost of int256 division (`/`) operation
 *     Int256Div = 19,
 *     // Cost of int256 power (`exp`) operation
 *     Int256Pow = 20,
 *     // Cost of int256 shift (`shl`, `shr`) operation
 *     Int256Shift = 21,
 *     // Cost of drawing random bytes using a ChaCha20 PRNG
 *     ChaCha20DrawBytes = 22,
 *
 *     // Cost of parsing wasm bytes that only encode instructions.
 *     ParseWasmInstructions = 23,
 *     // Cost of parsing a known number of wasm functions.
 *     ParseWasmFunctions = 24,
 *     // Cost of parsing a known number of wasm globals.
 *     ParseWasmGlobals = 25,
 *     // Cost of parsing a known number of wasm table entries.
 *     ParseWasmTableEntries = 26,
 *     // Cost of parsing a known number of wasm types.
 *     ParseWasmTypes = 27,
 *     // Cost of parsing a known number of wasm data segments.
 *     ParseWasmDataSegments = 28,
 *     // Cost of parsing a known number of wasm element segments.
 *     ParseWasmElemSegments = 29,
 *     // Cost of parsing a known number of wasm imports.
 *     ParseWasmImports = 30,
 *     // Cost of parsing a known number of wasm exports.
 *     ParseWasmExports = 31,
 *     // Cost of parsing a known number of data segment bytes.
 *     ParseWasmDataSegmentBytes = 32,
 *
 *     // Cost of instantiating wasm bytes that only encode instructions.
 *     InstantiateWasmInstructions = 33,
 *     // Cost of instantiating a known number of wasm functions.
 *     InstantiateWasmFunctions = 34,
 *     // Cost of instantiating a known number of wasm globals.
 *     InstantiateWasmGlobals = 35,
 *     // Cost of instantiating a known number of wasm table entries.
 *     InstantiateWasmTableEntries = 36,
 *     // Cost of instantiating a known number of wasm types.
 *     InstantiateWasmTypes = 37,
 *     // Cost of instantiating a known number of wasm data segments.
 *     InstantiateWasmDataSegments = 38,
 *     // Cost of instantiating a known number of wasm element segments.
 *     InstantiateWasmElemSegments = 39,
 *     // Cost of instantiating a known number of wasm imports.
 *     InstantiateWasmImports = 40,
 *     // Cost of instantiating a known number of wasm exports.
 *     InstantiateWasmExports = 41,
 *     // Cost of instantiating a known number of data segment bytes.
 *     InstantiateWasmDataSegmentBytes = 42,
 *
 *     // Cost of decoding a bytes array representing an uncompressed SEC-1 encoded
 *     // point on a 256-bit elliptic curve
 *     Sec1DecodePointUncompressed = 43,
 *     // Cost of verifying an ECDSA Secp256r1 signature
 *     VerifyEcdsaSecp256r1Sig = 44
 * };
 * </pre>
 */
public enum ContractCostType implements XdrElement {
  WasmInsnExec(0),
  MemAlloc(1),
  MemCpy(2),
  MemCmp(3),
  DispatchHostFunction(4),
  VisitObject(5),
  ValSer(6),
  ValDeser(7),
  ComputeSha256Hash(8),
  ComputeEd25519PubKey(9),
  VerifyEd25519Sig(10),
  VmInstantiation(11),
  VmCachedInstantiation(12),
  InvokeVmFunction(13),
  ComputeKeccak256Hash(14),
  DecodeEcdsaCurve256Sig(15),
  RecoverEcdsaSecp256k1Key(16),
  Int256AddSub(17),
  Int256Mul(18),
  Int256Div(19),
  Int256Pow(20),
  Int256Shift(21),
  ChaCha20DrawBytes(22),
  ParseWasmInstructions(23),
  ParseWasmFunctions(24),
  ParseWasmGlobals(25),
  ParseWasmTableEntries(26),
  ParseWasmTypes(27),
  ParseWasmDataSegments(28),
  ParseWasmElemSegments(29),
  ParseWasmImports(30),
  ParseWasmExports(31),
  ParseWasmDataSegmentBytes(32),
  InstantiateWasmInstructions(33),
  InstantiateWasmFunctions(34),
  InstantiateWasmGlobals(35),
  InstantiateWasmTableEntries(36),
  InstantiateWasmTypes(37),
  InstantiateWasmDataSegments(38),
  InstantiateWasmElemSegments(39),
  InstantiateWasmImports(40),
  InstantiateWasmExports(41),
  InstantiateWasmDataSegmentBytes(42),
  Sec1DecodePointUncompressed(43),
  VerifyEcdsaSecp256r1Sig(44);

  private final int value;

  ContractCostType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static ContractCostType decode(XdrDataInputStream stream) throws IOException {
    int value = stream.readInt();
    switch (value) {
      case 0:
        return WasmInsnExec;
      case 1:
        return MemAlloc;
      case 2:
        return MemCpy;
      case 3:
        return MemCmp;
      case 4:
        return DispatchHostFunction;
      case 5:
        return VisitObject;
      case 6:
        return ValSer;
      case 7:
        return ValDeser;
      case 8:
        return ComputeSha256Hash;
      case 9:
        return ComputeEd25519PubKey;
      case 10:
        return VerifyEd25519Sig;
      case 11:
        return VmInstantiation;
      case 12:
        return VmCachedInstantiation;
      case 13:
        return InvokeVmFunction;
      case 14:
        return ComputeKeccak256Hash;
      case 15:
        return DecodeEcdsaCurve256Sig;
      case 16:
        return RecoverEcdsaSecp256k1Key;
      case 17:
        return Int256AddSub;
      case 18:
        return Int256Mul;
      case 19:
        return Int256Div;
      case 20:
        return Int256Pow;
      case 21:
        return Int256Shift;
      case 22:
        return ChaCha20DrawBytes;
      case 23:
        return ParseWasmInstructions;
      case 24:
        return ParseWasmFunctions;
      case 25:
        return ParseWasmGlobals;
      case 26:
        return ParseWasmTableEntries;
      case 27:
        return ParseWasmTypes;
      case 28:
        return ParseWasmDataSegments;
      case 29:
        return ParseWasmElemSegments;
      case 30:
        return ParseWasmImports;
      case 31:
        return ParseWasmExports;
      case 32:
        return ParseWasmDataSegmentBytes;
      case 33:
        return InstantiateWasmInstructions;
      case 34:
        return InstantiateWasmFunctions;
      case 35:
        return InstantiateWasmGlobals;
      case 36:
        return InstantiateWasmTableEntries;
      case 37:
        return InstantiateWasmTypes;
      case 38:
        return InstantiateWasmDataSegments;
      case 39:
        return InstantiateWasmElemSegments;
      case 40:
        return InstantiateWasmImports;
      case 41:
        return InstantiateWasmExports;
      case 42:
        return InstantiateWasmDataSegmentBytes;
      case 43:
        return Sec1DecodePointUncompressed;
      case 44:
        return VerifyEcdsaSecp256r1Sig;
      default:
        throw new RuntimeException("Unknown enum value: " + value);
    }
  }

  public static void encode(XdrDataOutputStream stream, ContractCostType value) throws IOException {
    stream.writeInt(value.getValue());
  }

  public void encode(XdrDataOutputStream stream) throws IOException {
    encode(stream, this);
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

  public static ContractCostType fromXdrBase64(String xdr) throws IOException {
    byte[] bytes = Base64Factory.getInstance().decode(xdr);
    return fromXdrByteArray(bytes);
  }

  public static ContractCostType fromXdrByteArray(byte[] xdr) throws IOException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xdr);
    XdrDataInputStream xdrDataInputStream = new XdrDataInputStream(byteArrayInputStream);
    return decode(xdrDataInputStream);
  }
}
