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
      TypeToken accountPageType = new TypeToken<Page<AccountResponse>>() {};
      TypeToken assetPageType = new TypeToken<Page<AssetResponse>>() {};
      TypeToken effectPageType = new TypeToken<Page<EffectResponse>>() {};
      TypeToken ledgerPageType = new TypeToken<Page<LedgerResponse>>() {};
      TypeToken liquidityPoolPageType = new TypeToken<Page<LiquidityPoolResponse>>() {};
      TypeToken offerPageType = new TypeToken<Page<OfferResponse>>() {};
      TypeToken operationPageType = new TypeToken<Page<OperationResponse>>() {};
      TypeToken pathPageType = new TypeToken<Page<PathResponse>>() {};
      TypeToken tradePageType = new TypeToken<Page<TradeResponse>>() {};
      TypeToken tradeAggregationPageType = new TypeToken<Page<TradeAggregationResponse>>() {};
      TypeToken transactionPageType = new TypeToken<Page<TransactionResponse>>() {};
      TypeToken claimableBalancePageType = new TypeToken<Page<ClaimableBalanceResponse>>() {};

      instance =
          new GsonBuilder()
              .registerTypeAdapter(Asset.class, new AssetDeserializer())
              .registerTypeAdapter(Predicate.class, new PredicateDeserializer())
              .registerTypeAdapter(OperationResponse.class, new OperationDeserializer())
              .registerTypeAdapter(EffectResponse.class, new EffectDeserializer())
              .registerTypeAdapter(LiquidityPoolID.class, new LiquidityPoolIDDeserializer())
              .registerTypeAdapter(LiquidityPoolType.class, new LiquidityPoolTypeDeserializer())
              .registerTypeAdapter(TransactionResponse.class, new TransactionDeserializer())
              .registerTypeAdapter(
                  accountPageType.getType(), new PageDeserializer<AccountResponse>(accountPageType))
              .registerTypeAdapter(
                  assetPageType.getType(), new PageDeserializer<AssetResponse>(assetPageType))
              .registerTypeAdapter(
                  effectPageType.getType(), new PageDeserializer<AccountResponse>(effectPageType))
              .registerTypeAdapter(
                  ledgerPageType.getType(), new PageDeserializer<LedgerResponse>(ledgerPageType))
              .registerTypeAdapter(
                  liquidityPoolPageType.getType(),
                  new PageDeserializer<LiquidityPoolResponse>(liquidityPoolPageType))
              .registerTypeAdapter(
                  offerPageType.getType(), new PageDeserializer<OfferResponse>(offerPageType))
              .registerTypeAdapter(
                  operationPageType.getType(),
                  new PageDeserializer<OperationResponse>(operationPageType))
              .registerTypeAdapter(
                  pathPageType.getType(), new PageDeserializer<PathResponse>(pathPageType))
              .registerTypeAdapter(
                  tradePageType.getType(), new PageDeserializer<TradeResponse>(tradePageType))
              .registerTypeAdapter(
                  tradeAggregationPageType.getType(),
                  new PageDeserializer<TradeAggregationResponse>(tradeAggregationPageType))
              .registerTypeAdapter(
                  transactionPageType.getType(),
                  new PageDeserializer<TransactionResponse>(transactionPageType))
              .registerTypeAdapter(
                  claimableBalancePageType.getType(),
                  new PageDeserializer<ClaimableBalanceResponse>(claimableBalancePageType))
              .create();
    }
    return instance;
  }
}
