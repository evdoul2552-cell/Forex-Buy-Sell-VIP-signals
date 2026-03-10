package forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.candlestick;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import forex.school.trading.app.buy.sell.signals.forexvipbuysellsignals.R;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    TypedArray img, names, description;
    int[] candle_images = {R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt, R.drawable.profitt};

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        img = context.getResources().obtainTypedArray(R.array.candle_img);
        names = context.getResources().obtainTypedArray(R.array.candle_name);
        description = context.getResources().obtainTypedArray(R.array.candle_desc);
        holder.name.setText(names.getText(position));

        holder.desc.setText(description.getText(position));

        Glide.with(context).load(img.getString(position)).into(holder.image);
        String path = img.getString(position);
//        holder.image.setImageResource(img[position]);
        holder.image.buildDrawingCache();
        Bitmap bitmap = holder.image.getDrawingCache();
        holder.applyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("pos", position);
                intent.putExtra("bitmap", path);
                intent.putExtra("name", holder.name.getText());
                intent.putExtra("desc", description.getText(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return candle_images.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, desc;
        ImageView image;
        CardView card;
        LinearLayout applyNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.iamge);
            card = itemView.findViewById(R.id.card);
            desc = itemView.findViewById(R.id.desc);
            applyNow = itemView.findViewById(R.id.applyNow);
        }
    }

}
