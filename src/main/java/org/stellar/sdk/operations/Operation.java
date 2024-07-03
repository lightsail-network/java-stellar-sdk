package org.stellar.sdk.operations;

import java.io.IOException;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.stellar.sdk.StrKey;
import org.stellar.sdk.exception.UnexpectedException;

/** Abstract class for operations. */
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
public abstract class Operation {
  Operation() {}

  /** The source account for the operation. */
  @Getter @Setter private String sourceAccount;

  private static final BigDecimal ONE = new BigDecimal(10).pow(7);

  protected static long toXdrAmount(@NonNull String value) {
    BigDecimal amount = new BigDecimal(value).multiply(Operation.ONE);
    return amount.longValueExact();
  }

  protected static String fromXdrAmount(long value) {
    BigDecimal amount = new BigDecimal(value).divide(Operation.ONE);
    return amount.toPlainString();
  }

  /** Generates Operation XDR object. */
  public org.stellar.sdk.xdr.Operation toXdr() {
    org.stellar.sdk.xdr.Operation xdr = new org.stellar.sdk.xdr.Operation();
    if (getSourceAccount() != null) {
      xdr.setSourceAccount(StrKey.encodeToXDRMuxedAccount(sourceAccount));
    }
    xdr.setBody(toOperationBody());
    return xdr;
  }

  /** Returns base64-encoded Operation XDR object. */
  public String toXdrBase64() {
    try {
      return toXdr().toXdrBase64();
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }

  /**
   * Returns new Operation object from Operation XDR object.
   *
   * @param xdr XDR object
   */
  @SuppressWarnings("deprecation")
  public static Operation fromXdr(org.stellar.sdk.xdr.Operation xdr) {
    org.stellar.sdk.xdr.Operation.OperationBody body = xdr.getBody();
    Operation operation;
    switch (body.getDiscriminant()) {
      case CREATE_ACCOUNT:
        operation = CreateAccountOperation.fromXdr(body.getCreateAccountOp());
        break;
      case PAYMENT:
        operation = PaymentOperation.fromXdr(body.getPaymentOp());
        break;
      case PATH_PAYMENT_STRICT_RECEIVE:
        operation = PathPaymentStrictReceiveOperation.fromXdr(body.getPathPaymentStrictReceiveOp());
        break;
      case MANAGE_SELL_OFFER:
        operation = ManageSellOfferOperation.fromXdr(body.getManageSellOfferOp());
        break;
      case MANAGE_BUY_OFFER:
        operation = ManageBuyOfferOperation.fromXdr(body.getManageBuyOfferOp());
        break;
      case CREATE_PASSIVE_SELL_OFFER:
        operation = CreatePassiveSellOfferOperation.fromXdr(body.getCreatePassiveSellOfferOp());
        break;
      case SET_OPTIONS:
        operation = SetOptionsOperation.fromXdr(body.getSetOptionsOp());
        break;
      case CHANGE_TRUST:
        operation = ChangeTrustOperation.fromXdr(body.getChangeTrustOp());
        break;
      case ALLOW_TRUST:
        operation = AllowTrustOperation.fromXdr(body.getAllowTrustOp());
        break;
      case ACCOUNT_MERGE:
        operation = AccountMergeOperation.fromXdr(body.getDestination());
        break;
      case INFLATION:
        operation = InflationOperation.builder().build();
        break;
      case MANAGE_DATA:
        operation = ManageDataOperation.fromXdr(body.getManageDataOp());
        break;
      case BUMP_SEQUENCE:
        operation = BumpSequenceOperation.fromXdr(body.getBumpSequenceOp());
        break;
      case PATH_PAYMENT_STRICT_SEND:
        operation = PathPaymentStrictSendOperation.fromXdr(body.getPathPaymentStrictSendOp());
        break;
      case CREATE_CLAIMABLE_BALANCE:
        operation = CreateClaimableBalanceOperation.fromXdr(body.getCreateClaimableBalanceOp());
        break;
      case CLAIM_CLAIMABLE_BALANCE:
        operation = ClaimClaimableBalanceOperation.fromXdr(body.getClaimClaimableBalanceOp());
        break;
      case BEGIN_SPONSORING_FUTURE_RESERVES:
        operation =
            BeginSponsoringFutureReservesOperation.fromXdr(
                body.getBeginSponsoringFutureReservesOp());
        break;
      case END_SPONSORING_FUTURE_RESERVES:
        operation = EndSponsoringFutureReservesOperation.builder().build();
        break;
      case REVOKE_SPONSORSHIP:
        switch (body.getRevokeSponsorshipOp().getDiscriminant()) {
          case REVOKE_SPONSORSHIP_SIGNER:
            operation = RevokeSignerSponsorshipOperation.fromXdr(body.getRevokeSponsorshipOp());
            break;
          case REVOKE_SPONSORSHIP_LEDGER_ENTRY:
            switch (body.getRevokeSponsorshipOp().getLedgerKey().getDiscriminant()) {
              case DATA:
                operation = RevokeDataSponsorshipOperation.fromXdr(body.getRevokeSponsorshipOp());
                break;
              case OFFER:
                operation = RevokeOfferSponsorshipOperation.fromXdr(body.getRevokeSponsorshipOp());
                break;
              case ACCOUNT:
                operation =
                    RevokeAccountSponsorshipOperation.fromXdr(body.getRevokeSponsorshipOp());
                break;
              case TRUSTLINE:
                operation =
                    RevokeTrustlineSponsorshipOperation.fromXdr(body.getRevokeSponsorshipOp());
                break;
              case CLAIMABLE_BALANCE:
                operation =
                    RevokeClaimableBalanceSponsorshipOperation.fromXdr(
                        body.getRevokeSponsorshipOp());
                break;
              default:
                throw new IllegalArgumentException(
                    "Unknown revoke sponsorship ledger entry type "
                        + body.getRevokeSponsorshipOp().getLedgerKey().getDiscriminant());
            }
            break;
          default:
            throw new IllegalArgumentException(
                "Unknown revoke sponsorship body "
                    + body.getRevokeSponsorshipOp().getDiscriminant());
        }
        break;
      case CLAWBACK:
        operation = ClawbackOperation.fromXdr(body.getClawbackOp());
        break;
      case CLAWBACK_CLAIMABLE_BALANCE:
        operation = ClawbackClaimableBalanceOperation.fromXdr(body.getClawbackClaimableBalanceOp());
        break;
      case SET_TRUST_LINE_FLAGS:
        operation = SetTrustlineFlagsOperation.fromXdr(body.getSetTrustLineFlagsOp());
        break;
      case LIQUIDITY_POOL_DEPOSIT:
        operation = LiquidityPoolDepositOperation.fromXdr(body.getLiquidityPoolDepositOp());
        break;
      case LIQUIDITY_POOL_WITHDRAW:
        operation = LiquidityPoolWithdrawOperation.fromXdr(body.getLiquidityPoolWithdrawOp());
        break;
      case INVOKE_HOST_FUNCTION:
        operation = InvokeHostFunctionOperation.fromXdr(body.getInvokeHostFunctionOp());
        break;
      case EXTEND_FOOTPRINT_TTL:
        operation = ExtendFootprintTTLOperation.fromXdr(body.getExtendFootprintTTLOp());
        break;
      case RESTORE_FOOTPRINT:
        operation = RestoreFootprintOperation.fromXdr(body.getRestoreFootprintOp());
        break;
      default:
        throw new IllegalArgumentException("Unknown operation body " + body.getDiscriminant());
    }
    if (xdr.getSourceAccount() != null) {
      operation.setSourceAccount(StrKey.encodeMuxedAccount(xdr.getSourceAccount()));
    }
    return operation;
  }

  /**
   * Generates OperationBody XDR object
   *
   * @return OperationBody XDR object
   */
  abstract org.stellar.sdk.xdr.Operation.OperationBody toOperationBody();
}
