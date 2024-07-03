package org.stellar.sdk.operations;

import static junit.framework.TestCase.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum12;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.ChangeTrustAsset;
import org.stellar.sdk.LiquidityPoolParameters;

public class ChangeTrustOperationTest {
  @Test
  public void testBuilderWithAsset() {
    // from stellar_sdk import *
    //
    // op = ChangeTrust(
    //    asset=Asset("USDC", "GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"),
    //    limit="922337203685.4775807",
    //    source="GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW",
    // )
    // print(op.to_xdr_object().to_xdr())
    ChangeTrustAsset changeTrustAsset =
        new ChangeTrustAsset(
            Asset.create("USDC:GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"));
    String limit = "922337203685.4775807";
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ChangeTrustOperation op =
        ChangeTrustOperation.builder()
            .asset(changeTrustAsset)
            .limit(limit)
            .sourceAccount(source)
            .build();
    assertEquals(changeTrustAsset, op.getAsset());
    assertEquals(limit, op.getLimit());
    assertEquals(source, op.getSourceAccount());
    String expectXdr =
        "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAAAYAAAABVVNEQwAAAAA7mRE4Dv6Yi6CokA6xz+RPNm99vpRr7QdyQPf2JN8VxX//////////";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testBuilderWithLiquidityPoolAsset() {
    // from stellar_sdk import *
    //
    // asset1 = Asset("USD", "GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY")
    // asset2 = Asset("CATCOIN", "GDJVFDG5OCW5PYWHB64MGTHGFF57DRRJEDUEFDEL2SLNIOONHYJWHA3Z")
    // liquidity_pool_asset = LiquidityPoolAsset(asset1, asset2, LIQUIDITY_POOL_FEE_V18)
    //
    //
    // op = ChangeTrust(
    //    asset=liquidity_pool_asset,
    //    limit="922337203685.4775807",
    //    source="GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW",
    // )
    // print(op.to_xdr_object().to_xdr())
    LiquidityPoolParameters liquidityPoolParameters =
        new LiquidityPoolParameters(
            new AssetTypeCreditAlphaNum4(
                "USD", "GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY"),
            new AssetTypeCreditAlphaNum12(
                "CATCOIN", "GDJVFDG5OCW5PYWHB64MGTHGFF57DRRJEDUEFDEL2SLNIOONHYJWHA3Z"));
    ChangeTrustAsset changeTrustAsset = new ChangeTrustAsset(liquidityPoolParameters);
    String limit = "922337203685.4775807";
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ChangeTrustOperation op =
        ChangeTrustOperation.builder()
            .asset(changeTrustAsset)
            .limit(limit)
            .sourceAccount(source)
            .build();
    assertEquals(changeTrustAsset, op.getAsset());
    assertEquals(limit, op.getLimit());
    assertEquals(source, op.getSourceAccount());
    String expectXdr =
        "AAAAAQAAAAAk4TTtavBWsGnEN3KxHw4Ohwi22ZJHWi8hlamN5pm0TgAAAAYAAAADAAAAAAAAAAFVU0QAAAAAAJuOuviWOFUdz56k90MgcRBrh6sOLbPWm3WlOCJy91nYAAAAAkNBVENPSU4AAAAAAAAAAADTUozdcK3X4scPuMNM5il78cYpIOhCjIvUltQ5zT4TYwAAAB5//////////w==";
    assertEquals(expectXdr, op.toXdrBase64());
  }

  @Test
  public void testFromXdrWithAsset() {
    ChangeTrustAsset changeTrustAsset =
        new ChangeTrustAsset(
            Asset.create("USDC:GA5ZSEJYB37JRC5AVCIA5MOP4RHTM335X2KGX3IHOJAPP5RE34K4KZVN"));
    String limit = "922337203685.4775807";
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ChangeTrustOperation op =
        ChangeTrustOperation.builder()
            .asset(changeTrustAsset)
            .limit(limit)
            .sourceAccount(source)
            .build();
    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    ChangeTrustOperation restoreOp = (ChangeTrustOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }

  @Test
  public void testFromXdrWithLiquidityPoolAsset() {
    LiquidityPoolParameters liquidityPoolParameters =
        new LiquidityPoolParameters(
            new AssetTypeCreditAlphaNum4(
                "USD", "GCNY5OXYSY4FKHOPT2SPOQZAOEIGXB5LBYW3HVU3OWSTQITS65M5RCNY"),
            new AssetTypeCreditAlphaNum12(
                "CATCOIN", "GDJVFDG5OCW5PYWHB64MGTHGFF57DRRJEDUEFDEL2SLNIOONHYJWHA3Z"));
    ChangeTrustAsset changeTrustAsset = new ChangeTrustAsset(liquidityPoolParameters);
    String limit = "922337203685.4775807";
    String source = "GASOCNHNNLYFNMDJYQ3XFMI7BYHIOCFW3GJEOWRPEGK2TDPGTG2E5EDW";
    ChangeTrustOperation op =
        ChangeTrustOperation.builder()
            .asset(changeTrustAsset)
            .limit(limit)
            .sourceAccount(source)
            .build();
    org.stellar.sdk.xdr.Operation xdrObject = op.toXdr();
    ChangeTrustOperation restoreOp = (ChangeTrustOperation) Operation.fromXdr(xdrObject);
    Assert.assertEquals(restoreOp, op);
  }
}
