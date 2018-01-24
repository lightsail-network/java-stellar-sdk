package org.stellar.sdk.requests;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

class HttpClientSingleton {
    private static OkHttpClient instance = null;

    private HttpClientSingleton() {
    }

    public static OkHttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClientSingleton.class) {
                if(instance == null) {
                    instance = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(false)
                            .build();
                }
            }

        }
        return instance;
    }

}
