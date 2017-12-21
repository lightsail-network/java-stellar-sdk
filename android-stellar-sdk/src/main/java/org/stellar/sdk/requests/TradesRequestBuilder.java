package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import org.apache.http.client.fluent.Request;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.TradeResponse;

import java.io.IOException;
import java.net.URI;

/**
 * Builds requests connected to trades.
 */
public class TradesRequestBuilder extends RequestBuilder {
    public TradesRequestBuilder(URI serverURI) {
        super(serverURI, "order_book/trades");
    }

    public TradesRequestBuilder buyingAsset(Asset asset) {
        uriBuilder.addParameter("buying_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addParameter("buying_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addParameter("buying_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
        return this;
    }

    public TradesRequestBuilder sellingAsset(Asset asset) {
        uriBuilder.addParameter("selling_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.addParameter("selling_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.addParameter("selling_asset_issuer", creditAlphaNumAsset.getIssuer().getAccountId());
        }
        return this;
    }

    public static TradeResponse execute(URI uri) throws IOException, TooManyRequestsException {
        TypeToken type = new TypeToken<TradeResponse>() {};
        ResponseHandler<TradeResponse> responseHandler = new ResponseHandler<TradeResponse>(type);
        return (TradeResponse) Request.Get(uri).execute().handleResponse(responseHandler);
    }

    public TradeResponse execute() throws IOException, TooManyRequestsException {
        return this.execute(this.buildUri());
    }
}
