package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.KeyPair;

/**
 * Represents trades response.
 * @see <a href="https://www.stellar.org/developers/horizon/reference/endpoints/trades-for-orderbook.html" target="_blank">Trades for Orderbook documentation</a>
 * @see org.stellar.sdk.requests.TradesRequestBuilder
 * @see org.stellar.sdk.Server#trades()
 */
public class TradeResponse extends Response {
    @SerializedName("id")
    private final String id;
    @SerializedName("paging_token")
    private final String pagingToken;
    @SerializedName("created_at")
    private final String createdAt;

    @SerializedName("seller")
    protected final KeyPair seller;
    @SerializedName("sold_amount")
    protected final String soldAmount;
    @SerializedName("sold_asset_type")
    protected final String soldAssetType;
    @SerializedName("sold_asset_code")
    protected final String soldAssetCode;
    @SerializedName("sold_asset_issuer")
    protected final String soldAssetIssuer;

    @SerializedName("buyer")
    protected final KeyPair buyer;
    @SerializedName("bought_amount")
    protected final String boughtAmount;
    @SerializedName("bought_asset_type")
    protected final String boughtAssetType;
    @SerializedName("bought_asset_code")
    protected final String boughtAssetCode;
    @SerializedName("bought_asset_issuer")
    protected final String boughtAssetIssuer;

    @SerializedName("_links")
    private TradeResponse.Links links;

    public TradeResponse(String id, String pagingToken, String createdAt, KeyPair seller, String soldAmount, String soldAssetType, String soldAssetCode, String soldAssetIssuer, KeyPair buyer, String boughtAmount, String boughtAssetType, String boughtAssetCode, String boughtAssetIssuer) {
        this.id = id;
        this.pagingToken = pagingToken;
        this.createdAt = createdAt;
        this.seller = seller;
        this.soldAmount = soldAmount;
        this.soldAssetType = soldAssetType;
        this.soldAssetCode = soldAssetCode;
        this.soldAssetIssuer = soldAssetIssuer;
        this.buyer = buyer;
        this.boughtAmount = boughtAmount;
        this.boughtAssetType = boughtAssetType;
        this.boughtAssetCode = boughtAssetCode;
        this.boughtAssetIssuer = boughtAssetIssuer;
    }

    public String getId() {
        return id;
    }

    public String getPagingToken() {
        return pagingToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public KeyPair getSeller() {
        return seller;
    }

    public String getSoldAmount() {
        return soldAmount;
    }

    public String getSoldAssetType() {
        return soldAssetType;
    }

    public String getSoldAssetCode() {
        return soldAssetCode;
    }

    public String getSoldAssetIssuer() {
        return soldAssetIssuer;
    }

    public KeyPair getBuyer() {
        return buyer;
    }

    public String getBoughtAmount() {
        return boughtAmount;
    }

    public String getBoughtAssetType() {
        return boughtAssetType;
    }

    public String getBoughtAssetCode() {
        return boughtAssetCode;
    }

    public String getBoughtAssetIssuer() {
        return boughtAssetIssuer;
    }

    public Links getLinks() {
        return links;
    }

    /**
     * Links connected to a trade.
     */
    public static class Links {
        @SerializedName("self")
        private final Link self;
        @SerializedName("seller")
        private final Link seller;
        @SerializedName("buyer")
        private final Link buyer;

        public Links(Link self, Link seller, Link buyer) {
            this.self = self;
            this.seller = seller;
            this.buyer = buyer;
        }

        public Link getSelf() {
            return self;
        }

        public Link getSeller() {
            return seller;
        }

        public Link getBuyer() {
            return buyer;
        }
    }
}
