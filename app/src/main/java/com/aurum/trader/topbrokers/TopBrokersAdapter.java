package com.aurum.trader.topbrokers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.aurum.trader.MyApplication;
import com.aurum.trader.R;


public class TopBrokersAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    static String TAG = TopBrokersAdapter.class.getName();
    public static MyApplication myApplication;
    private SparseArray<TopBrokerObject> brokers;
    Context con;
    private LayoutInflater li;
    private LongSparseArray<TopBrokerObject> oidToHistoryObjects = new LongSparseArray<>();
    private LongSparseArray<Integer> oidToPosition = new LongSparseArray<>();

    public boolean hasStableIds() {
        return false;
    }

    public TopBrokersAdapter(Context context, TopBrokerActivity topBrokersFragment, SparseArray<TopBrokerObject> sparseArray) {
        myApplication = MyApplication.getInstance();
        this.con = context;
        this.brokers = sparseArray;
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            TopBrokerObject valueAt = sparseArray.valueAt(i);
            this.oidToHistoryObjects.append((long) valueAt.oid, valueAt);
            this.oidToPosition.put((long) valueAt.oid, Integer.valueOf(i));
        }
        this.li = LayoutInflater.from(context);
    }


    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Intent intent = new Intent(this.con, BrokerDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("position", i);
        intent.putExtra("queries", asList(this.brokers));
        con.startActivity(intent);
    }

    public static <C> ArrayList<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) {
            return null;
        }
        ArrayList<C> arrayList = new ArrayList<>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }



    public int getCount() {
        return this.brokers.size();
    }

    public Object getItem(int i) {
        return this.brokers.valueAt(i);
    }

    public long getItemId(int i) {
        return (long) this.brokers.valueAt(i).oid;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        OrderGroupViewHolder orderGroupViewHolder;
        TopBrokerObject valueAt = this.brokers.valueAt(i);
        if (view == null) {
            con = con.getApplicationContext();
            orderGroupViewHolder = new OrderGroupViewHolder(con, con.getResources());
            view = this.li.inflate(R.layout.broker_row, viewGroup, false);
            orderGroupViewHolder.brokerImage = (ImageView) view.findViewById(R.id.brokerImage);
            orderGroupViewHolder.brokerName = (TextView) view.findViewById(R.id.brokerName);
            orderGroupViewHolder.brokerType = (TextView) view.findViewById(R.id.brokerType);
            orderGroupViewHolder.brokerRegulation = (TextView) view.findViewById(R.id.regulationBy);
            orderGroupViewHolder.applyNow = view.findViewById(R.id.applyNow);
            orderGroupViewHolder.recom = view.findViewById(R.id.recom);
            orderGroupViewHolder.card = view.findViewById(R.id.card);
            view.setTag(orderGroupViewHolder);
        } else {
            orderGroupViewHolder = (OrderGroupViewHolder) view.getTag();
        }
        orderGroupViewHolder.applyNow.setOnClickListener(new ApplyNowListener(!TextUtils.isEmpty(valueAt.mobileUrl) ? valueAt.mobileUrl : valueAt.url, valueAt.brokerName, valueAt.brokerOid));
        orderGroupViewHolder.update(valueAt);
        return view;
    }

    static class OrderGroupViewHolder {
        public View applyNow;
        public ImageView brokerImage;
        public TextView brokerName;
        public TextView brokerRegulation;
        CardView recom, card;
        public TextView brokerType;
        public Context con;
        public View view;

        public OrderGroupViewHolder(Context context, Resources resources) {
            this.con = context;
        }

        public void update(TopBrokerObject topBrokerObject) {
            Picasso.get().load(topBrokerObject.imageUrl.replaceAll(" ", "%20")).error(R.drawable.exness).into(this.brokerImage);
            Log.d("IMAGE", topBrokerObject.imageUrl.replaceAll(" ", "%20"));
            Log.d("PATH", topBrokerObject.imageUrl);

            this.brokerName.setText(topBrokerObject.brokerName);
            this.brokerType.setText(topBrokerObject.brokerType);
            this.brokerRegulation.setText(this.con.getString(R.string.brokerregulation, topBrokerObject.regulationType));
            if (topBrokerObject.brokerName.equals("Exness") || topBrokerObject.brokerName.equals("FXTM") || topBrokerObject.brokerName.equals("MultiBank Group")) {
                recom.setVisibility(View.VISIBLE);
                card.setCardBackgroundColor(con.getResources().getColor(R.color.yellow1));
            } else {
                recom.setVisibility(View.GONE);
                card.setCardBackgroundColor(con.getResources().getColor(R.color.white));
            }
        }
    }

    public static class ApplyNowListener implements View.OnClickListener {
        private HttpStatus httpStatus = new HttpStatus();
        String name;
        int oid;
        String url;

        public ApplyNowListener(String str, String str2, int i) {
            this.url = str;
            this.name = str2;
            this.oid = i;
        }

        public void onClick(View view) {
            try {
                TopBrokerActivity.updateBrokerStats(this.httpStatus, this.url, this.oid);
                openWebView(this.url);
            } catch (Exception e) {
                Log.e(TopBrokersAdapter.TAG, "error apply broker", e);
            }
        }
    }

    public static void openWebView(String str) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setFlags(268435456);
            MyApplication.getContext().startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
    }
}
