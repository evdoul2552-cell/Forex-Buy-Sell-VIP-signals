package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.interfaces.TimeZoneAddedCallback;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.models.TimeZoneData;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.models.TimeZoneDataItem;
import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.pref.SPManager;

public final class TimeZoneAdapter extends RecyclerView.Adapter<TimeZoneAdapter.TimeZoneViewHolder> {
    private final TimeZoneAddedCallback callback;
    private final Context context;
    private final TimeZoneData list;
    private final String tag;

    public TimeZoneAdapter(Context context2, TimeZoneData timeZoneData, TimeZoneAddedCallback timeZoneAddedCallback) {
        this.context = context2;
        this.list = timeZoneData;
        this.callback = timeZoneAddedCallback;
        String name = getClass().getName();
        this.tag = name;
    }

    public static final class TimeZoneViewHolder extends RecyclerView.ViewHolder {
        private TextView textTimeZone;
        private TextView textTimeZoneName;

        public TimeZoneViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textTimeZoneName = itemView.findViewById(R.id.text_time_zone_name);
            this.textTimeZone = itemView.findViewById(R.id.text_time_zone);
        }


        public final TextView getTextTimeZoneName() {
            return this.textTimeZoneName;
        }

        public final void setTextTimeZoneName(TextView textView) {
            this.textTimeZoneName = textView;
        }

        public final TextView getTextTimeZone() {
            return this.textTimeZone;
        }

        public final void setTextTimeZone(TextView textView) {
            this.textTimeZone = textView;
        }
    }

    public TimeZoneViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_time_zone4, viewGroup, false);
        return new TimeZoneViewHolder(inflate);
    }

    public void onBindViewHolder(TimeZoneViewHolder timeZoneViewHolder, int i) {
        TimeZoneDataItem timeZoneDataItem = this.list.get(i);
        timeZoneViewHolder.getTextTimeZoneName().setText(timeZoneDataItem.getValue());
        timeZoneViewHolder.getTextTimeZone().setText(timeZoneDataItem.getText());
        timeZoneViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPManager.INSTANCE.init(context);
                SPManager.INSTANCE.saveSPFloat(SPManager.SP_timeZone, timeZoneDataItem.getOffset());
                Toast.makeText(context, timeZoneDataItem.getText() + " set as default timezone", Toast.LENGTH_LONG).show();
                callback.timeZoneAdded();
            }
        });
    }

    public int getItemCount() {
        return this.list.size();
    }
}
