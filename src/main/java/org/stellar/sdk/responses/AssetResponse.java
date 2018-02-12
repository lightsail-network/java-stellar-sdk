package org.stellar.sdk.responses;

import com.google.gson.annotations.SerializedName;
import org.stellar.sdk.Asset;

public class AssetResponse extends Response {
    @SerializedName("asset_type")
    private final String assetType;
    @SerializedName("asset_code")
    private final String assetCode;
    @SerializedName("asset_issuer")
    private final String assetIssuer;
    @SerializedName("paging_token")
    private final String pagingToken;
    @SerializedName("amount")
    private final String amount;
    @SerializedName("num_accounts")
    private final int numAccounts;
    @SerializedName("flags")
    private final AssetResponse.Flags flags;
    @SerializedName("_links")
    private final AssetResponse.Links links;

    public AssetResponse(String assetType, String assetCode, String assetIssuer, String pagingToken, String amount, int numAccounts, Flags flags, Links links) {
        this.assetType = assetType;
        this.assetCode = assetCode;
        this.assetIssuer = assetIssuer;
        this.pagingToken = pagingToken;
        this.amount = amount;
        this.numAccounts = numAccounts;
        this.flags = flags;
        this.links = links;
    }

    public String getAssetType() {
        return assetType;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public String getAssetIssuer() {
        return assetIssuer;
    }

    public Asset getAsset() {
        return Asset.create(this.assetType, this.assetCode, this.assetIssuer);
    }

    public String getPagingToken() {
        return pagingToken;
    }

    public String getAmount() {
        return amount;
    }

    public int getNumAccounts() {
        return numAccounts;
    }

    public Flags getFlags() {
        return flags;
    }

    public Links getLinks() {
        return links;
    }

    /**
     * Flags describe asset flags.
     */
    public static class Flags {
        @SerializedName("auth_required")
        private final boolean authRequired;
        @SerializedName("auth_revocable")
        private final boolean authRevocable;

        public Flags(boolean authRequired, boolean authRevocable) {
            this.authRequired = authRequired;
            this.authRevocable = authRevocable;
        }

        public boolean isAuthRequired() {
            return authRequired;
        }

        public boolean isAuthRevocable() {
            return authRevocable;
        }
    }

    /**
     * Links connected to asset.
     */
    public static class Links {
        @SerializedName("toml")
        private final Link toml;

        public Links(Link toml) {
            this.toml = toml;
        }

        public Link getToml() {
            return toml;
        }
    }
}
