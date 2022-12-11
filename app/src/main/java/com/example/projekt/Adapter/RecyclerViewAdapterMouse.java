package com.example.projekt.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

public class RecyclerViewAdapterMouse extends RecyclerView.Adapter<RecyclerViewAdapterMouse.ViewHolder> {
    private Context context;
    private List<Integer> imgs;
    private List<String>  titles;
    private List<Integer>  prices;
    private List<Integer>  ids;
    private CheckBox lastSelected;


    public RecyclerViewAdapterMouse(Context context, List<Integer> imgs, List<String> titles, List<Integer> prices, List<Integer> ids) {
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

        if(position==0){
            lastSelected=null;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("ORDER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.v("UserOrder",holder.getAdapterPosition()+"");
        editor.putString("MOUSE", "0");
        editor.commit();

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()){
                    editor.putString("MOUSE", String.valueOf(ids.get(holder.getAdapterPosition())));
                    editor.commit();
                    if(lastSelected!=null){
                        lastSelected.setChecked(false);
                        lastSelected=holder.checkBox;
                    }
                    else{
                        lastSelected=holder.checkBox;
                    }
                }
            }
        });
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
            checkBox = itemView.findViewById(R.id.checkBoxRecyclerView);
        }
    }
}
