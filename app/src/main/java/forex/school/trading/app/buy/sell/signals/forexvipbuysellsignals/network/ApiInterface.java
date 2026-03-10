package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.network;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.models.TimeZoneData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("/dmfilipenko/timezones.json/master/timezones.json")
    Call<TimeZoneData> timeZoneApi();
}
