package com.aurum.trader.network;

import kotlin.jvm.internal.DefaultConstructorMarker;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    public static final Companion Companion = new Companion(null);
    private static RetrofitClient retrofitClient;
    private String baseUrl = "https://raw.githubusercontent.com/";
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(this.baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final synchronized RetrofitClient getInstance() {
            if (RetrofitClient.retrofitClient == null) {
                RetrofitClient.retrofitClient = new RetrofitClient();
            }
            return RetrofitClient.retrofitClient;
        }
    }

    public final ApiInterface getApi() {
        Retrofit retrofit3 = this.retrofit;
        if (retrofit3 != null) {
            return (ApiInterface) retrofit3.create(ApiInterface.class);
        }
        return null;
    }
}
