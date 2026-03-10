package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;

import android.os.Parcel;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LastTickObject implements Serializable, Comparable<LastTickObject> {
    private static final String PARAM_CHANGE_PERCENT = "pe";
    private static final String PARAM_CHANGE_PIPS = "pi";
    private static final String PARAM_CLOSE = "cl";
    private static final String PARAM_DATE = "d";
    private static final String PARAM_HIGH_PRICE = "hp";
    private static final String PARAM_LOW_PRICE = "lp";
    private static final String PARAM_OPEN = "op";
    private static final String PARAM_PRICE = "p";
    private static final String PARAM_SYMBOL = "s";
    private static final String PARAM_TIMEFRAME = "p";
    @JsonProperty("pe")
    public double changePercent;
    @JsonProperty(PARAM_CHANGE_PIPS)
    public double changePips;
    public boolean clickOnNotification = false;
    @JsonProperty(PARAM_CLOSE)
    public double close;
    @JsonProperty("d")
    public long date;
    @JsonProperty(PARAM_HIGH_PRICE)
    public double highPrice;
    public long lastUpdateTime = 0;
    @JsonProperty(PARAM_LOW_PRICE)
    public double lowPrice;
    @JsonProperty(PARAM_OPEN)
    public double open;
    public int orderNumber = -1;
    public String symbolName;
    @JsonProperty(PARAM_SYMBOL)
    public int symbolOid;

    public LastTickObject() {
    }

    public LastTickObject(Parcel parcel) {
        this.symbolOid = parcel.readInt();
        this.symbolName = parcel.readString();
        this.close = parcel.readDouble();
    }

    public void update(LastTickObject lastTickObject) {
        this.date = lastTickObject.date;
        this.close = lastTickObject.close;
        this.highPrice = lastTickObject.highPrice;
        this.lowPrice = lastTickObject.lowPrice;
        this.open = lastTickObject.open;
        this.changePips = lastTickObject.changePips;
        this.changePercent = lastTickObject.changePercent;
    }

    public int compareTo(LastTickObject lastTickObject) {
        int i = this.orderNumber;
        int i2 = lastTickObject.orderNumber;
        if (i < i2) {
            return -1;
        }
        return i == i2 ? 0 : 1;
    }
}
