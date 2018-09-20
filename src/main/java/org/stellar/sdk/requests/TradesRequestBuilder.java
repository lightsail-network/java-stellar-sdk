package org.stellar.sdk.requests;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.stellar.sdk.Asset;
import org.stellar.sdk.AssetTypeCreditAlphaNum;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.TradeResponse;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds requests connected to trades.
 */
public class TradesRequestBuilder extends RequestBuilder {
    public TradesRequestBuilder(OkHttpClient httpClient, HttpUrl serverURI) {
        super(httpClient, serverURI, "trades");
    }

    public TradesRequestBuilder baseAsset(Asset asset) {
        uriBuilder.setQueryParameter("base_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.setQueryParameter("base_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.setQueryParameter("base_asset_issuer", creditAlphaNumAsset.getIssuer());
        }
        return this;
    }

    public TradesRequestBuilder counterAsset(Asset asset) {
        uriBuilder.setQueryParameter("counter_asset_type", asset.getType());
        if (asset instanceof AssetTypeCreditAlphaNum) {
            AssetTypeCreditAlphaNum creditAlphaNumAsset = (AssetTypeCreditAlphaNum) asset;
            uriBuilder.setQueryParameter("counter_asset_code", creditAlphaNumAsset.getCode());
            uriBuilder.setQueryParameter("counter_asset_issuer", creditAlphaNumAsset.getIssuer());
        }
        return this;
    }

    /**
     * Builds request to <code>GET /accounts/{account}/trades</code>
     * @see <a href="https://www.stellar.org/developers/horizon/reference/endpoints/trades-for-account.html">Trades for Account</a>
     * @param account Account for which to get trades
     */
    public TradesRequestBuilder forAccount(String account) {
        account = checkNotNull(account, "account cannot be null");
        this.setSegments("accounts", account, "trades");
        return this;
    }

    public static Page<TradeResponse> execute(OkHttpClient httpClient, HttpUrl uri)
        throws IOException, TooManyRequestsException {
        TypeToken type = new TypeToken<Page<TradeResponse>>() {};
        ResponseHandler<Page<TradeResponse>> responseHandler = new ResponseHandler<Page<TradeResponse>>(
            type);

        Request request = new Request.Builder().get().url(uri).build();
        Response response = httpClient.newCall(request).execute();
        return responseHandler.handleResponse(response);
    }

    public Page<TradeResponse> execute() throws IOException, TooManyRequestsException {
        return this.execute(this.httpClient, this.buildUri());
    }

    public TradesRequestBuilder offerId(String offerId) {
        uriBuilder.setQueryParameter("offer_id", offerId);
        return this;
    }
}
