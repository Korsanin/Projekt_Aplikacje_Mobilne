package com.example.projekt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Integer> imgs;
    private List<String>  titles;
    private List<Integer>  prices;
    private List<Integer>  ids;

    public RecyclerViewAdapter(Context context, List<Integer> imgs, List<String> titles, List<Integer> prices, List<Integer> ids) {
        this.context = context;
        this.imgs = imgs;
        this.titles = titles;
        this.prices = prices;
        this.ids = ids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(imgs.get(position));
        holder.title.setText(titles.get(position));
        holder.price.setText(prices.get(position)+"zł");
        holder.checkBox.setText(ids.get(position)+"");
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView title;
        private TextView price;
        private CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewRecyclerItem);
            title = itemView.findViewById(R.id.titleRecyclerItem);
            price = itemView.findViewById(R.id.priceRecyclerItem);
            checkBox = itemView.findViewById(R.id.checkboxRecyclerView);
        }
    }
}
