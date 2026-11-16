// SEP-51 names of enum members and union arms: the common prefix is stripped and the
// rest is written in snake case, word by word.

// The common prefix `tx` doesn't end in an underscore, so nothing is stripped.
enum ResultCode
{
    txSUCCESS = 0,
    txFAILED = -1,
    txFEE_BUMP_INNER_SUCCESS = 1
};

// Members in CamelCase, without a common prefix.
enum CostType
{
    WasmInsnExec = 0,
    ComputeSha256Hash = 1,
    Bls12381G1Add = 2
};

enum AddrType
{
    IPv4 = 0,
    IPv6 = 1
};

// A member that starts with a digit once the prefix is stripped keeps its first letter,
// as rs-stellar-xdr does (a Rust identifier can't start with a digit): `f8_bit`.
enum FilterType
{
    FILTER_8_BIT = 0,
    FILTER_16_BIT = 1
};

union Result switch (ResultCode code)
{
case txSUCCESS:
case txFAILED:
    int results<>;
default:
    void;
};

// An optional arm without a value is written as null.
union MaybeInts switch (int type)
{
case 0:
    Result* result;
};
