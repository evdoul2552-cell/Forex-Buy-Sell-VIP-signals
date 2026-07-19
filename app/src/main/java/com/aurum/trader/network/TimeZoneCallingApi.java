package com.aurum.trader.network;

import android.content.Context;
import android.widget.Toast;

import kotlin.jvm.internal.DefaultConstructorMarker;
import com.aurum.trader.R;
import com.aurum.trader.interfaces.TimeZoneListCallback;
import com.aurum.trader.models.TimeZoneData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class TimeZoneCallingApi {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void myTimeZones(Context context, TimeZoneListCallback timeZoneListCallback) {
            ApiInterface api;
            RetrofitClient instance = RetrofitClient.Companion.getInstance();
            Call<TimeZoneData> timeZoneApi = (instance == null || (api = instance.getApi()) == null) ? null : api.timeZoneApi();
            if (timeZoneApi != null) {
                timeZoneApi.enqueue(new Callback<TimeZoneData>() {
                    @Override
                    public void onResponse(Call<TimeZoneData> call, Response<TimeZoneData> response) {
                        if (response.isSuccessful()) {
                            TimeZoneData body = response.body();
                            timeZoneListCallback.myTimeZoneList(body);
                            return;
                        }
                        Toast.makeText(context, context.getString(R.string.try_again), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<TimeZoneData> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
