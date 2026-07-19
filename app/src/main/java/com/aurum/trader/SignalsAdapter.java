package com.aurum.trader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.measurement.api.AppMeasurementSdk;

import java.util.ArrayList;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;
import com.aurum.trader.helpers.GlobalMethods;
import com.aurum.trader.models.SignalModel;

public final class SignalsAdapter extends RecyclerView.Adapter<SignalsAdapter.SignalsViewHolder> {
    private final String activeAnim;
    private final Context context;
    private int lastPosition = -1;
    private final ArrayList<SignalModel> list;
    private final String tag;

    public SignalsAdapter(Context context2, ArrayList<SignalModel> arrayList, String str) {
        this.context = context2;
        this.list = arrayList;
        this.activeAnim = str;
        String name = getClass().getName();
        this.tag = name;
    }

    public static final class SignalsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageDotAnim;
        private ImageView imageFlag;
        private TextView textAction;
        private TextView textStatus;
        private TextView textTradeResult;
        TextView name, date, timeframe, sl, tp;

        public SignalsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textStatus = itemView.findViewById(R.id.text_status);
            this.textTradeResult = itemView.findViewById(R.id.text_trade_result);
            this.textAction = itemView.findViewById(R.id.text_action);
            this.imageFlag = itemView.findViewById(R.id.image_flag);
            this.imageDotAnim = itemView.findViewById(R.id.image_dot_anim);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            timeframe = itemView.findViewById(R.id.timeframe);
            sl = itemView.findViewById(R.id.sl);
            tp = itemView.findViewById(R.id.tp);
        }

        public final TextView getTextStatus() {
            return this.textStatus;
        }

        public final void setTextStatus(TextView textView) {
            this.textStatus = textView;
        }

        public final TextView getTextTradeResult() {
            return this.textTradeResult;
        }

        public final void setTextTradeResult(TextView textView) {
            this.textTradeResult = textView;
        }

        public final TextView getTextAction() {
            return this.textAction;
        }

        public final void setTextAction(TextView textView) {
            this.textAction = textView;
        }

        public final ImageView getImageFlag() {
            return this.imageFlag;
        }

        public final void setImageFlag(ImageView imageView) {
            this.imageFlag = imageView;
        }

        public final ImageView getImageDotAnim() {
            return this.imageDotAnim;
        }

        public final void setImageDotAnim(ImageView imageView) {
            this.imageDotAnim = imageView;
        }
    }

    public SignalsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_signal, viewGroup, false);
        return new SignalsViewHolder(inflate);
    }

    public void onBindViewHolder(SignalsViewHolder signalsViewHolder, int i) {
        String str;
        SignalModel signalModel = this.list.get(i);
        TextView textStatus = signalsViewHolder.getTextStatus();
        StringBuilder sb = new StringBuilder();
        String substring = this.list.get(i).getStatus().substring(0, 1);
        String upperCase = substring.toUpperCase(Locale.ROOT);
        sb.append(upperCase);
        String substring2 = this.list.get(i).getStatus().substring(1);
        String lowerCase = substring2.toLowerCase(Locale.ROOT);
        sb.append(lowerCase);
        textStatus.setText(sb.toString());
        signalsViewHolder.getTextAction().setText(this.list.get(i).getAction());
        TextView textTradeResult = signalsViewHolder.getTextTradeResult();
        if (Intrinsics.areEqual(this.list.get(i).getTrade_Result(), "TP-1") || Intrinsics.areEqual(this.list.get(i).getTrade_Result(), "TP-2")) {
            str = this.list.get(i).getTrade_Result();
        } else {
            StringBuilder sb2 = new StringBuilder();
            String substring3 = this.list.get(i).getTrade_Result().substring(0, 1);
            String upperCase2 = substring3.toUpperCase(Locale.ROOT);
            sb2.append(upperCase2);
            String substring4 = this.list.get(i).getTrade_Result().substring(1);
            String lowerCase2 = substring4.toLowerCase(Locale.ROOT);
            sb2.append(lowerCase2);
            str = sb2.toString();
        }
        textTradeResult.setText(str);
        String curr = signalModel.getCurrency().toString();
        String remove1 = " ";
        signalsViewHolder.name.setText(removeWords(curr, remove1));

        GlobalMethods globalMethods = GlobalMethods.INSTANCE;
        signalsViewHolder.date.setText(globalMethods.getNewDate(signalModel.getTrade_Opening_Time(), "details"));

        String timefram = signalModel.getTimeFrame().toString();
        String remove = "-";
        signalsViewHolder.timeframe.setText(removeWords(timefram, remove));

        signalsViewHolder.sl.setText(signalModel.getStop_Loss());
        signalsViewHolder.tp.setText(signalModel.getTake_profit());

        String lowerCase3 = this.list.get(i).getStatus().toLowerCase(Locale.ROOT);
        if (Intrinsics.areEqual(lowerCase3, "active")) {
            signalsViewHolder.getImageDotAnim().setVisibility(View.VISIBLE);
            String lowerCase4 = this.activeAnim.toLowerCase(Locale.ROOT);
            if (Intrinsics.areEqual(lowerCase3, "active")) {
                Glide.with(this.context).load(Integer.valueOf((int) R.drawable.dot_anim)).into(signalsViewHolder.getImageDotAnim());
            } else {
                signalsViewHolder.getImageDotAnim().setImageResource(R.drawable.dot_static);
            }
        } else {
            signalsViewHolder.getImageDotAnim().setVisibility(View.GONE);
        }
        String lowerCase5 = this.list.get(i).getAction().toLowerCase(Locale.ROOT);
        if (Intrinsics.areEqual(lowerCase5, "buy")) {
            signalsViewHolder.getTextAction().setTextColor(ContextCompat.getColor(this.context, R.color.green_light));
        } else {
            signalsViewHolder.getTextAction().setTextColor(ContextCompat.getColor(this.context, R.color.red));
        }
        String lowerCase6 = this.list.get(i).getTrade_Result().toLowerCase(Locale.ROOT);
        switch (lowerCase6.hashCode()) {
            case 3564832:
                if (lowerCase6.equals("tp-1")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.black));
                    break;
                }
                String str2 = this.tag;
                Log.d(str2, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 3564833:
                if (lowerCase6.equals("tp-2")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.black));
                    break;
                }
                String str22 = this.tag;
                Log.d(str22, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 149380223:
                if (lowerCase6.equals(" trade loss")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.red));
                    break;
                }
                String str222 = this.tag;
                Log.d(str222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 327414812:
                if (lowerCase6.equals(" trade close")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.red));
                    break;
                }
                String str2222 = this.tag;
                Log.d(str2222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 491735100:
                if (lowerCase6.equals("trade close")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.green_light));
                    break;
                }
                String str22222 = this.tag;
                Log.d(str22222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 1116313165:
                if (lowerCase6.equals("waiting")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                    break;
                }
                String str222222 = this.tag;
                Log.d(str222222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 1571709057:
                if (lowerCase6.equals("stop loss")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.red));
                    break;
                }
                String str2222222 = this.tag;
                Log.d(str2222222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 1581685757:
                if (lowerCase6.equals("take profit")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.flat_blue_2));
                    break;
                }
                String str22222222 = this.tag;
                Log.d(str22222222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            case 1817248863:
                if (lowerCase6.equals("trade loss")) {
                    signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.green_light));
                    break;
                }
                String str222222222 = this.tag;
                Log.d(str222222222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
            default:
                String str2222222222 = this.tag;
                Log.d(str2222222222, "onBindViewHolder: wrong trade-result: " + this.list.get(i).getTrade_Result());
                signalsViewHolder.getTextTradeResult().setText(this.context.getString(R.string.waiting));
                signalsViewHolder.getTextTradeResult().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
                break;
        }
        String lowerCase7 = this.list.get(i).getStatus().toLowerCase(Locale.ROOT);
        if (Intrinsics.areEqual(lowerCase7, "expired")) {
            signalsViewHolder.getTextStatus().setTextColor(ContextCompat.getColor(this.context, R.color.text_color_grey));
        } else {
            String lowerCase8 = this.list.get(i).getStatus().toLowerCase(Locale.ROOT);
            if (Intrinsics.areEqual(lowerCase8, "pending")) {
                signalsViewHolder.getTextStatus().setTextColor(ContextCompat.getColor(this.context, R.color.yellow));
            } else {
                String lowerCase9 = this.list.get(i).getStatus().toLowerCase(Locale.ROOT);
                if (Intrinsics.areEqual(lowerCase9, AppMeasurementSdk.ConditionalUserProperty.ACTIVE)) {
                    signalsViewHolder.getTextStatus().setTextColor(ContextCompat.getColor(this.context, R.color.green_light));
                } else {
                    String str3 = this.tag;
                    Log.d(str3, "onBindViewHolder: wrong status: " + this.list.get(i).getStatus());
                    signalsViewHolder.getTextStatus().setText(this.context.getString(R.string.active));
                    signalsViewHolder.getTextStatus().setTextColor(ContextCompat.getColor(this.context, R.color.green_light));
                }
            }
        }
        selectFlag(signalsViewHolder, signalModel);
        signalsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignalDetailsActivity.class);
                intent.putExtra("object", signalModel);
                context.startActivity(intent);
            }
        });
    }


    public static String removeWords(String word, String remove) {
        return word.replace(remove, "");
    }


    private final void selectFlag(SignalsViewHolder signalsViewHolder, SignalModel signalModel) {
        String lowerCase = signalModel.getCurrency().toLowerCase(Locale.ROOT);
        switch (lowerCase.hashCode()) {
            case -1867650114:
                if (lowerCase.equals("nzd cad")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.nzd_cad);
                    return;
                }
                return;
            case -1867649895:
                if (lowerCase.equals("nzd chf")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.nzd_chf);
                    return;
                }
                return;
            case -1867642901:
                if (lowerCase.equals("nzd jpy")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.nzd_jpy);
                    return;
                }
                return;
            case -1867632258:
                if (lowerCase.equals("nzd usd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.nzd_usd);
                    return;
                }
                return;
            case -1395466414:
                if (lowerCase.equals("eur aud")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.eur_aud);
                    return;
                }
                return;
            case -1395465112:
                if (lowerCase.equals("eur cad")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.eur_cad);
                    return;
                }
                return;
            case -1395464893:
                if (lowerCase.equals("eur chf")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.eur_chf);
                    return;
                }
                return;
            case -1395461225:
                if (lowerCase.equals("eur gbp")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.eur_gbp);
                    return;
                }
                return;
            case -1395457899:
                if (lowerCase.equals("eur jpy")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.eur_jpy);
                    return;
                }
                return;
            case -1395453766:
                if (lowerCase.equals("eur nzd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.eur_nzd);
                    return;
                }
                return;
            case -1395447256:
                if (lowerCase.equals("eur usd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.eur_usd);
                    return;
                }
                return;
            case -663441834:
                if (lowerCase.equals("aud cad")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.aud_cad);
                    return;
                }
                return;
            case -663441615:
                if (lowerCase.equals("aud chf")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.aud_chf);
                    return;
                }
                return;
            case -663434621:
                if (lowerCase.equals("aud jpy")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.aud_jpy);
                    return;
                }
                return;
            case -663430488:
                if (lowerCase.equals("aud nzd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.aud_nzd);
                    return;
                }
                return;
            case -663423978:
                if (lowerCase.equals("aud usd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.aud_usd);
                    return;
                }
                return;
            case -166259963:
                if (lowerCase.equals("gbp aud")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.gbp_aud);
                    return;
                }
                return;
            case -166258661:
                if (lowerCase.equals("gbp cad")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.gbp_cad);
                    return;
                }
                return;
            case -166258442:
                if (lowerCase.equals("gbp chf")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.gbp_chf);
                    return;
                }
                return;
            case -166251448:
                if (lowerCase.equals("gbp jpy")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.gbp_jpy);
                    return;
                }
                return;
            case -166247315:
                if (lowerCase.equals("gbp nzd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.gbp_nzd);
                    return;
                }
                return;
            case -166240805:
                if (lowerCase.equals("gbp usd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.gbp_usd);
                    return;
                }
                return;
            case -150495700:
                if (lowerCase.equals("usd cad")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.usd_cad);
                    return;
                }
                return;
            case -150495481:
                if (lowerCase.equals("usd chf")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.usd_chf);
                    return;
                }
                return;
            case -150488487:
                if (lowerCase.equals("usd jpy")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.usd_jpy);
                    return;
                }
                return;
            case 538982727:
                if (lowerCase.equals("cad chf")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.cad_chf);
                    return;
                }
                return;
            case 538989721:
                if (lowerCase.equals("cad jpy")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.cad_jpy);
                    return;
                }
                return;
            case 741240820:
                if (lowerCase.equals("chf jpy")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.chf_jpy);
                    return;
                }
                return;
            case 2012408338:
                if (lowerCase.equals("xau usd")) {
                    signalsViewHolder.getImageFlag().setImageResource(R.drawable.xau_usd);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void onViewDetachedFromWindow(SignalsViewHolder signalsViewHolder) {
        super.onViewDetachedFromWindow(signalsViewHolder);
        signalsViewHolder.itemView.clearAnimation();
    }
}
