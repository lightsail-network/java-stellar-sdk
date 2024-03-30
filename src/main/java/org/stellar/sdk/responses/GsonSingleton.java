package org.stellar.sdk.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.stellar.sdk.Asset;
import org.stellar.sdk.LiquidityPoolID;
import org.stellar.sdk.Predicate;
import org.stellar.sdk.responses.effects.EffectResponse;
import org.stellar.sdk.responses.operations.OperationResponse;
import org.stellar.sdk.xdr.LiquidityPoolType;

public class GsonSingleton {
  private static Gson instance = null;

  protected GsonSingleton() {}

  public static Gson getInstance() {
    if (instance == null) {
      TypeToken<Page<AccountResponse>> accountPageType = new TypeToken<Page<AccountResponse>>() {};
      TypeToken<Page<AssetResponse>> assetPageType = new TypeToken<Page<AssetResponse>>() {};
      TypeToken<Page<EffectResponse>> effectPageType = new TypeToken<Page<EffectResponse>>() {};
      TypeToken<Page<LedgerResponse>> ledgerPageType = new TypeToken<Page<LedgerResponse>>() {};
      TypeToken<Page<LiquidityPoolResponse>> liquidityPoolPageType =
          new TypeToken<Page<LiquidityPoolResponse>>() {};
      TypeToken<Page<OfferResponse>> offerPageType = new TypeToken<Page<OfferResponse>>() {};
      TypeToken<Page<OperationResponse>> operationPageType =
          new TypeToken<Page<OperationResponse>>() {};
      TypeToken<Page<PathResponse>> pathPageType = new TypeToken<Page<PathResponse>>() {};
      TypeToken<Page<TradeResponse>> tradePageType = new TypeToken<Page<TradeResponse>>() {};
      TypeToken<Page<TradeAggregationResponse>> tradeAggregationPageType =
          new TypeToken<Page<TradeAggregationResponse>>() {};
      TypeToken<Page<TransactionResponse>> transactionPageType =
          new TypeToken<Page<TransactionResponse>>() {};
      TypeToken<Page<ClaimableBalanceResponse>> claimableBalancePageType =
          new TypeToken<Page<ClaimableBalanceResponse>>() {};

      instance =
          new GsonBuilder()
              .registerTypeAdapter(Asset.class, new AssetDeserializer())
              .registerTypeAdapter(Predicate.class, new PredicateDeserializer())
              .registerTypeAdapter(OperationResponse.class, new OperationDeserializer())
              .registerTypeAdapter(EffectResponse.class, new EffectDeserializer())
              .registerTypeAdapter(LiquidityPoolID.class, new LiquidityPoolIDDeserializer())
              .registerTypeAdapter(LiquidityPoolType.class, new LiquidityPoolTypeDeserializer())
              .registerTypeAdapter(
                  SubmitTransactionResponse.class, new SubmitTransactionDeserializer())
              .registerTypeAdapter(
                  accountPageType.getType(), new PageDeserializer<>(accountPageType))
              .registerTypeAdapter(assetPageType.getType(), new PageDeserializer<>(assetPageType))
              .registerTypeAdapter(effectPageType.getType(), new PageDeserializer<>(effectPageType))
              .registerTypeAdapter(ledgerPageType.getType(), new PageDeserializer<>(ledgerPageType))
              .registerTypeAdapter(
                  liquidityPoolPageType.getType(), new PageDeserializer<>(liquidityPoolPageType))
              .registerTypeAdapter(offerPageType.getType(), new PageDeserializer<>(offerPageType))
              .registerTypeAdapter(
                  operationPageType.getType(), new PageDeserializer<>(operationPageType))
              .registerTypeAdapter(pathPageType.getType(), new PageDeserializer<>(pathPageType))
              .registerTypeAdapter(tradePageType.getType(), new PageDeserializer<>(tradePageType))
              .registerTypeAdapter(
                  tradeAggregationPageType.getType(),
                  new PageDeserializer<>(tradeAggregationPageType))
              .registerTypeAdapter(
                  transactionPageType.getType(), new PageDeserializer<>(transactionPageType))
              .registerTypeAdapter(
                  claimableBalancePageType.getType(),
                  new PageDeserializer<>(claimableBalancePageType))
              .create();
    }
    return instance;
  }
}
