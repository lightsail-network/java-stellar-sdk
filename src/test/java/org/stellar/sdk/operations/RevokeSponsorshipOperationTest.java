package org.stellar.sdk.operations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.TrustLineAsset;
import org.stellar.sdk.xdr.SignerKey;

public class RevokeSponsorshipOperationTest {

  @Test
  public void testRevokeAccountSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    RevokeAccountSponsorshipOperation operation =
        RevokeAccountSponsorshipOperation.builder()
            .accountId(accountId)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAAAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7Sw==",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    RevokeAccountSponsorshipOperation parsedOperation =
        (RevokeAccountSponsorshipOperation) Operation.fromXdr(xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
  }

  @Test
  public void testRevokeDataSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    String dataName = "data_name";
    RevokeDataSponsorshipOperation operation =
        RevokeDataSponsorshipOperation.builder()
            .accountId(accountId)
            .dataName(dataName)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAwAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAlkYXRhX25hbWUAAAA=",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    RevokeDataSponsorshipOperation parsedOperation =
        (RevokeDataSponsorshipOperation) Operation.fromXdr(xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(dataName, parsedOperation.getDataName());
  }

  @Test
  public void testRevokeClaimableBalanceSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String balanceId = "00000000550e14acbdafcd3089289363b3b0c8bec9b4edd87298c690655b4b2456d68ba0";
    RevokeClaimableBalanceSponsorshipOperation operation =
        RevokeClaimableBalanceSponsorshipOperation.builder()
            .balanceId(balanceId)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAABAAAAABVDhSsva/NMIkok2OzsMi+ybTt2HKYxpBlW0skVtaLoA==",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    RevokeClaimableBalanceSponsorshipOperation parsedOperation =
        (RevokeClaimableBalanceSponsorshipOperation) Operation.fromXdr(xdr);
    assertEquals(balanceId, parsedOperation.getBalanceId());
    assertEquals(source, parsedOperation.getSourceAccount());
  }

  @Test
  public void testRevokeOfferSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String seller = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    Long offerId = 123456L;
    RevokeOfferSponsorshipOperation operation =
        RevokeOfferSponsorshipOperation.builder()
            .seller(seller)
            .offerId(offerId)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAgAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAAAAeJA",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    RevokeOfferSponsorshipOperation parsedOperation =
        (RevokeOfferSponsorshipOperation) Operation.fromXdr(xdr);
    assertEquals(offerId, parsedOperation.getOfferId());
    assertEquals(seller, parsedOperation.getSeller());
    assertEquals(source, parsedOperation.getSourceAccount());
  }

  @Test
  public void testRevokeSignerSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    SignerKey signerKey =
        KeyPair.fromAccountId("GBOSQJIV4VJMWMPVPB7EFVIRJT7A7SAAAB4FA23ZDJRUMXMYHBYWY57L")
            .getXdrSignerKey();
    RevokeSignerSponsorshipOperation operation =
        RevokeSignerSponsorshipOperation.builder()
            .accountId(accountId)
            .signer(signerKey)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAABAAAAAA0PjbClYR862HI77a+kYknlCAd8JFFkq8deOOGREDtLAAAAAF0oJRXlUssx9Xh+QtURTP4PyAAAeFBreRpjRl2YOHFs",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    RevokeSignerSponsorshipOperation parsedOperation =
        (RevokeSignerSponsorshipOperation) Operation.fromXdr(xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(signerKey, parsedOperation.getSigner());
  }

  @Test
  public void testRevokeTrustlineSponsorshipOperation() {
    String source = "GA2N7NI5WEMJILMK4UPDTF2ZX2BIRQUM3HZUE27TRUNRFN5M5EXU6RQV";
    String accountId = "GAGQ7DNQUVQR6OWYOI563L5EMJE6KCAHPQSFCZFLY5PDRYMRCA5UWCMP";
    TrustLineAsset asset =
        new TrustLineAsset(
            Asset.create("DEMO:GCWPICV6IV35FQ2MVZSEDLORHEMMIAODRQPVDEIKZOW2GC2JGGDCXVVV"));
    RevokeTrustlineSponsorshipOperation operation =
        RevokeTrustlineSponsorshipOperation.builder()
            .accountId(accountId)
            .asset(asset)
            .sourceAccount(source)
            .build();
    assertEquals(
        "AAAAAQAAAAA037UdsRiULYrlHjmXWb6CiMKM2fNCa/ONGxK3rOkvTwAAABIAAAAAAAAAAQAAAAAND42wpWEfOthyO+2vpGJJ5QgHfCRRZKvHXjjhkRA7SwAAAAFERU1PAAAAAKz0Cr5Fd9LDTK5kQa3RORjEAcOMH1GRCsutowtJMYYr",
        operation.toXdrBase64());

    org.stellar.sdk.xdr.Operation xdr = operation.toXdr();
    RevokeTrustlineSponsorshipOperation parsedOperation =
        (RevokeTrustlineSponsorshipOperation) Operation.fromXdr(xdr);
    assertEquals(accountId, parsedOperation.getAccountId());
    assertEquals(source, parsedOperation.getSourceAccount());
    assertEquals(asset, parsedOperation.getAsset());
  }
}
