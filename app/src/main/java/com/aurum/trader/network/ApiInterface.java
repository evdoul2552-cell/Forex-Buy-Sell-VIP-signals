package com.aurum.trader.network;

import com.aurum.trader.models.TimeZoneData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/dmfilipenko/timezones.json/master/timezones.json")
    Call<TimeZoneData> timeZoneApi();
}
