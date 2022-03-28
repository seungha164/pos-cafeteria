package com.example.pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder>{
    private Order basket;
    Context context;
    Frag1 frag1;
    public BasketAdapter(Context context, Order orders,Frag1 parent){
        this.context = context;
        this.basket = orders;
        this.frag1 = parent;
    }
    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_basket,parent,false);
        BasketViewHolder holder = new BasketViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
        holder.onBind(basket.items.get(position));
    }

    @Override
    public int getItemCount() {
        return (null!=basket.items?basket.items.size():0);
    }

    public class BasketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Name,Num;
        ImageButton remove;
        Data myItem;
        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.tvBasket_name);
            Num = itemView.findViewById(R.id.tvBasket_num);
            remove = itemView.findViewById(R.id.ibtnBasket_remove);
            remove.setOnClickListener(this);
        }
        void onBind(Data data){
            Name.setText(String.format("%s (%s)",data.name,(data.isHot?"HOT":"ICE")));
            Num.setText(basket.countingMap.get(data)+"");
            myItem = data;
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ibtnBasket_remove:
                    basket.deleteData(myItem);
                    notifyDataSetChanged();
                    frag1.setTotal();
                    break;
            }
        }
    }
}
