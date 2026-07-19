package com.aurum.trader.models;

import java.io.Serializable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class SignalModel implements Serializable {
    private static final String r2 = "-";
    private final String Action;
    private final String Open_Price;
    private String Probability;
    private final String Status;
    private final String Stop_Loss;
    private final String Take_Profit_3;
    private final String Take_profit;
    private final String Take_profit_2;
    private String TimeFrame;
    private final String Trade_Close;
    private final String Trade_Opening_Time;
    private final String Trade_Result;
    private final String currency;
    private final String last_update;
    private final String old_new;

    public SignalModel() {
        this((String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, 32767, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ SignalModel copy$default(SignalModel signalModel, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, int i, Object obj) {
        SignalModel signalModel2 = signalModel;
        int i2 = i;
        return signalModel.copy((i2 & 1) != 0 ? signalModel2.Action : str, (i2 & 2) != 0 ? signalModel2.currency : str2, (i2 & 4) != 0 ? signalModel2.last_update : str3, (i2 & 8) != 0 ? signalModel2.old_new : str4, (i2 & 16) != 0 ? signalModel2.Open_Price : str5, (i2 & 32) != 0 ? signalModel2.Status : str6, (i2 & 64) != 0 ? signalModel2.Stop_Loss : str7, (i2 & 128) != 0 ? signalModel2.Take_profit : str8, (i2 & 256) != 0 ? signalModel2.Take_profit_2 : str9, (i2 & 512) != 0 ? signalModel2.Take_Profit_3 : str10, (i2 & 1024) != 0 ? signalModel2.Trade_Close : str11, (i2 & 2048) != 0 ? signalModel2.Trade_Opening_Time : str12, (i2 & 4096) != 0 ? signalModel2.Trade_Result : str13, (i2 & 8192) != 0 ? signalModel2.TimeFrame : str14, (i2 & 16384) != 0 ? signalModel2.Probability : str15);
    }

    public final String component1() {
        return this.Action;
    }

    public final String component10() {
        return this.Take_Profit_3;
    }

    public final String component11() {
        return this.Trade_Close;
    }

    public final String component12() {
        return this.Trade_Opening_Time;
    }

    public final String component13() {
        return this.Trade_Result;
    }

    public final String component14() {
        return this.TimeFrame;
    }

    public final String component15() {
        return this.Probability;
    }

    public final String component2() {
        return this.currency;
    }

    public final String component3() {
        return this.last_update;
    }

    public final String component4() {
        return this.old_new;
    }

    public final String component5() {
        return this.Open_Price;
    }

    public final String component6() {
        return this.Status;
    }

    public final String component7() {
        return this.Stop_Loss;
    }

    public final String component8() {
        return this.Take_profit;
    }

    public final String component9() {
        return this.Take_profit_2;
    }

    public final SignalModel copy(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15) {
        String str16 = str;
        String str17 = str2;
        String str18 = str3;
        String str19 = str4;
        String str20 = str5;
        String str21 = str6;
        String str22 = str7;
        String str23 = str8;
        String str24 = str9;
        String str25 = str10;
        String str26 = str11;
        String str27 = str12;
        String str28 = str13;
        String str29 = str14;
        return new SignalModel(str16, str17, str18, str19, str20, str21, str22, str23, str24, str25, str26, str27, str28, str29, str15);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SignalModel)) {
            return false;
        }
        SignalModel signalModel = (SignalModel) obj;
        return Intrinsics.areEqual((Object) this.Action, (Object) signalModel.Action) && Intrinsics.areEqual((Object) this.currency, (Object) signalModel.currency) && Intrinsics.areEqual((Object) this.last_update, (Object) signalModel.last_update) && Intrinsics.areEqual((Object) this.old_new, (Object) signalModel.old_new) && Intrinsics.areEqual((Object) this.Open_Price, (Object) signalModel.Open_Price) && Intrinsics.areEqual((Object) this.Status, (Object) signalModel.Status) && Intrinsics.areEqual((Object) this.Stop_Loss, (Object) signalModel.Stop_Loss) && Intrinsics.areEqual((Object) this.Take_profit, (Object) signalModel.Take_profit) && Intrinsics.areEqual((Object) this.Take_profit_2, (Object) signalModel.Take_profit_2) && Intrinsics.areEqual((Object) this.Take_Profit_3, (Object) signalModel.Take_Profit_3) && Intrinsics.areEqual((Object) this.Trade_Close, (Object) signalModel.Trade_Close) && Intrinsics.areEqual((Object) this.Trade_Opening_Time, (Object) signalModel.Trade_Opening_Time) && Intrinsics.areEqual((Object) this.Trade_Result, (Object) signalModel.Trade_Result) && Intrinsics.areEqual((Object) this.TimeFrame, (Object) signalModel.TimeFrame) && Intrinsics.areEqual((Object) this.Probability, (Object) signalModel.Probability);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((this.Action.hashCode() * 31) + this.currency.hashCode()) * 31) + this.last_update.hashCode()) * 31) + this.old_new.hashCode()) * 31) + this.Open_Price.hashCode()) * 31) + this.Status.hashCode()) * 31) + this.Stop_Loss.hashCode()) * 31) + this.Take_profit.hashCode()) * 31) + this.Take_profit_2.hashCode()) * 31) + this.Take_Profit_3.hashCode()) * 31) + this.Trade_Close.hashCode()) * 31) + this.Trade_Opening_Time.hashCode()) * 31) + this.Trade_Result.hashCode()) * 31) + this.TimeFrame.hashCode()) * 31) + this.Probability.hashCode();
    }

    public String toString() {
        return "SignalModel(Action=" + this.Action + ", currency=" + this.currency + ", last_update=" + this.last_update + ", old_new=" + this.old_new + ", Open_Price=" + this.Open_Price + ", Status=" + this.Status + ", Stop_Loss=" + this.Stop_Loss + ", Take_profit=" + this.Take_profit + ", Take_profit_2=" + this.Take_profit_2 + ", Take_Profit_3=" + this.Take_Profit_3 + ", Trade_Close=" + this.Trade_Close + ", Trade_Opening_Time=" + this.Trade_Opening_Time + ", Trade_Result=" + this.Trade_Result + ", TimeFrame=" + this.TimeFrame + ", Probability=" + this.Probability + ')';
    }

    public SignalModel(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15) {
        String str16 = str;
        String str17 = str2;
        String str18 = str3;
        String str19 = str4;
        String str20 = str5;
        String str21 = str6;
        String str22 = str7;
        String str23 = str8;
        String str24 = str9;
        String str25 = str10;
        String str26 = str11;
        String str27 = str12;
        String str28 = str13;
        String str29 = str14;
        String str30 = str15;
        this.Action = str16;
        this.currency = str17;
        this.last_update = str18;
        this.old_new = str19;
        this.Open_Price = str20;
        this.Status = str21;
        this.Stop_Loss = str22;
        this.Take_profit = str23;
        this.Take_profit_2 = str24;
        this.Take_Profit_3 = str25;
        this.Trade_Close = str26;
        this.Trade_Opening_Time = str27;
        this.Trade_Result = str28;
        this.TimeFrame = str29;
        this.Probability = str30;
    }

    public /* synthetic */ SignalModel(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? r2 : str, (i & 2) != 0 ? r2 : str2, (i & 4) != 0 ? r2 : str3, (i & 8) != 0 ? r2 : str4, (i & 16) != 0 ? r2 : str5, (i & 32) != 0 ? r2 : str6, (i & 64) != 0 ? r2 : str7, (i & 128) != 0 ? r2 : str8, (i & 256) != 0 ? r2 : str9, (i & 512) != 0 ? r2 : str10, (i & 1024) != 0 ? r2 : str11, (i & 2048) != 0 ? r2 : str12, (i & 4096) != 0 ? r2 : str13, (i & 8192) != 0 ? r2 : str14, (i & 16384) == 0 ? str15 : r2);
        String str16 = "-";
    }

    public final String getAction() {
        return this.Action;
    }

    public final String getCurrency() {
        return this.currency;
    }

    public final String getLast_update() {
        return this.last_update;
    }

    public final String getOld_new() {
        return this.old_new;
    }

    public final String getOpen_Price() {
        return this.Open_Price;
    }

    public final String getStatus() {
        return this.Status;
    }

    public final String getStop_Loss() {
        return this.Stop_Loss;
    }

    public final String getTake_profit() {
        return this.Take_profit;
    }

    public final String getTake_profit_2() {
        return this.Take_profit_2;
    }

    public final String getTake_Profit_3() {
        return this.Take_Profit_3;
    }

    public final String getTrade_Close() {
        return this.Trade_Close;
    }

    public final String getTrade_Opening_Time() {
        return this.Trade_Opening_Time;
    }

    public final String getTrade_Result() {
        return this.Trade_Result;
    }

    public final String getTimeFrame() {
        return this.TimeFrame;
    }

    public final void setTimeFrame(String str) {
        this.TimeFrame = str;
    }

    public final String getProbability() {
        return this.Probability;
    }

    public final void setProbability(String str) {
        this.Probability = str;
    }
}
