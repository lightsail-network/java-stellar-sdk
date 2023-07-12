package org.stellar.sdk;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.io.BaseEncoding;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import org.stellar.sdk.xdr.*;

/** Abstract class for operations. */
public abstract class Operation {
  Operation() {}

  private String mSourceAccount;

  private static final BigDecimal ONE = new BigDecimal(10).pow(7);

  protected static long toXdrAmount(String value) {
    value = checkNotNull(value, "value cannot be null");
    BigDecimal amount = new BigDecimal(value).multiply(Operation.ONE);
    return amount.longValueExact();
  }

  protected static String fromXdrAmount(long value) {
    BigDecimal amount = new BigDecimal(value).divide(Operation.ONE);
    return amount.toPlainString();
  }

  /** Generates Operation XDR object. */
  public org.stellar.sdk.xdr.Operation toXdr(AccountConverter accountConverter) {
    org.stellar.sdk.xdr.Operation xdr = new org.stellar.sdk.xdr.Operation();
    if (getSourceAccount() != null) {
      xdr.setSourceAccount(accountConverter.encode(mSourceAccount));
    }
    xdr.setBody(toOperationBody(accountConverter));
    return xdr;
  }

  /** Generates Operation XDR object. */
  public org.stellar.sdk.xdr.Operation toXdr() {
    return toXdr(AccountConverter.enableMuxed());
  }

  /** Returns base64-encoded Operation XDR object. */
  public String toXdrBase64(AccountConverter accountConverter) {
    try {
      org.stellar.sdk.xdr.Operation operation = this.toXdr(accountConverter);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      XdrDataOutputStream xdrOutputStream = new XdrDataOutputStream(outputStream);
      org.stellar.sdk.xdr.Operation.encode(xdrOutputStream, operation);
      BaseEncoding base64Encoding = BaseEncoding.base64();
      return base64Encoding.encode(outputStream.toByteArray());
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  /** Returns base64-encoded Operation XDR object. */
  public String toXdrBase64() {
    return toXdrBase64(AccountConverter.enableMuxed());
  }

  /**
   * Returns new Operation object from Operation XDR object.
   *
   * @param xdr XDR object
   */
  public static Operation fromXdr(
      AccountConverter accountConverter, org.stellar.sdk.xdr.Operation xdr) {
    org.stellar.sdk.xdr.Operation.OperationBody body = xdr.getBody();
    Operation operation;
    switch (body.getDiscriminant()) {
      case CREATE_ACCOUNT:
        operation = new CreateAccountOperation.Builder(body.getCreateAccountOp()).build();
        break;
      case PAYMENT:
        operation = new PaymentOperation.Builder(accountConverter, body.getPaymentOp()).build();
        break;
      case PATH_PAYMENT_STRICT_RECEIVE:
        operation =
            new PathPaymentStrictReceiveOperation.Builder(
                    accountConverter, body.getPathPaymentStrictReceiveOp())
                .build();
        break;
      case MANAGE_SELL_OFFER:
        operation = new ManageSellOfferOperation.Builder(body.getManageSellOfferOp()).build();
        break;
      case MANAGE_BUY_OFFER:
        operation = new ManageBuyOfferOperation.Builder(body.getManageBuyOfferOp()).build();
        break;
      case CREATE_PASSIVE_SELL_OFFER:
        operation =
            new CreatePassiveSellOfferOperation.Builder(body.getCreatePassiveSellOfferOp()).build();
        break;
      case SET_OPTIONS:
        operation = new SetOptionsOperation.Builder(body.getSetOptionsOp()).build();
        break;
      case CHANGE_TRUST:
        operation = new ChangeTrustOperation.Builder(body.getChangeTrustOp()).build();
        break;
      case ALLOW_TRUST:
        operation = new AllowTrustOperation.Builder(body.getAllowTrustOp()).build();
        break;
      case ACCOUNT_MERGE:
        operation = new AccountMergeOperation.Builder(accountConverter, body).build();
        break;
      case INFLATION:
        operation = new InflationOperation();
        break;
      case MANAGE_DATA:
        operation = new ManageDataOperation.Builder(body.getManageDataOp()).build();
        break;
      case BUMP_SEQUENCE:
        operation = new BumpSequenceOperation.Builder(body.getBumpSequenceOp()).build();
        break;
      case PATH_PAYMENT_STRICT_SEND:
        operation =
            new PathPaymentStrictSendOperation.Builder(
                    accountConverter, body.getPathPaymentStrictSendOp())
                .build();
        break;
      case CREATE_CLAIMABLE_BALANCE:
        operation =
            new CreateClaimableBalanceOperation.Builder(body.getCreateClaimableBalanceOp()).build();
        break;
      case CLAIM_CLAIMABLE_BALANCE:
        operation =
            new ClaimClaimableBalanceOperation.Builder(body.getClaimClaimableBalanceOp()).build();
        break;
      case BEGIN_SPONSORING_FUTURE_RESERVES:
        operation =
            new BeginSponsoringFutureReservesOperation.Builder(
                    body.getBeginSponsoringFutureReservesOp())
                .build();
        break;
      case END_SPONSORING_FUTURE_RESERVES:
        operation = new EndSponsoringFutureReservesOperation();
        break;
      case REVOKE_SPONSORSHIP:
        switch (body.getRevokeSponsorshipOp().getDiscriminant()) {
          case REVOKE_SPONSORSHIP_SIGNER:
            operation =
                new RevokeSignerSponsorshipOperation.Builder(body.getRevokeSponsorshipOp()).build();
            break;
          case REVOKE_SPONSORSHIP_LEDGER_ENTRY:
            switch (body.getRevokeSponsorshipOp().getLedgerKey().getDiscriminant()) {
              case DATA:
                operation =
                    new RevokeDataSponsorshipOperation.Builder(body.getRevokeSponsorshipOp())
                        .build();
                break;
              case OFFER:
                operation =
                    new RevokeOfferSponsorshipOperation.Builder(body.getRevokeSponsorshipOp())
                        .build();
                break;
              case ACCOUNT:
                operation =
                    new RevokeAccountSponsorshipOperation.Builder(body.getRevokeSponsorshipOp())
                        .build();
                break;
              case TRUSTLINE:
                operation =
                    new RevokeTrustlineSponsorshipOperation.Builder(body.getRevokeSponsorshipOp())
                        .build();
                break;
              case CLAIMABLE_BALANCE:
                operation =
                    new RevokeClaimableBalanceSponsorshipOperation.Builder(
                            body.getRevokeSponsorshipOp())
                        .build();
                break;
              default:
                throw new RuntimeException(
                    "Unknown revoke sponsorship ledger entry type "
                        + body.getRevokeSponsorshipOp().getLedgerKey().getDiscriminant());
            }
            break;
          default:
            throw new RuntimeException(
                "Unknown revoke sponsorship body "
                    + body.getRevokeSponsorshipOp().getDiscriminant());
        }
        break;
      case CLAWBACK:
        operation = new ClawbackOperation.Builder(accountConverter, body.getClawbackOp()).build();
        break;
      case CLAWBACK_CLAIMABLE_BALANCE:
        operation =
            new ClawbackClaimableBalanceOperation.Builder(body.getClawbackClaimableBalanceOp())
                .build();
        break;
      case SET_TRUST_LINE_FLAGS:
        operation = new SetTrustlineFlagsOperation.Builder(body.getSetTrustLineFlagsOp()).build();
        break;
      case LIQUIDITY_POOL_DEPOSIT:
        operation = new LiquidityPoolDepositOperation(body.getLiquidityPoolDepositOp());
        break;
      case LIQUIDITY_POOL_WITHDRAW:
        operation = new LiquidityPoolWithdrawOperation(body.getLiquidityPoolWithdrawOp());
        break;
      default:
        throw new RuntimeException("Unknown operation body " + body.getDiscriminant());
    }
    if (xdr.getSourceAccount() != null) {
      operation.setSourceAccount(accountConverter.decode(xdr.getSourceAccount()));
    }
    return operation;
  }

  /**
   * Returns new Operation object from Operation XDR object.
   *
   * @param xdr XDR object
   */
  public static Operation fromXdr(org.stellar.sdk.xdr.Operation xdr) {
    return fromXdr(AccountConverter.enableMuxed(), xdr);
  }

  /** Returns operation source account. */
  public String getSourceAccount() {
    return mSourceAccount;
  }

  /**
   * Sets operation source account.
   *
   * @param sourceAccount
   */
  void setSourceAccount(String sourceAccount) {
    mSourceAccount = checkNotNull(sourceAccount, "sourceAccount cannot be null");
  }

  /**
   * Generates OperationBody XDR object
   *
   * @return OperationBody XDR object
   */
  abstract org.stellar.sdk.xdr.Operation.OperationBody toOperationBody(
      AccountConverter accountConverter);
}
