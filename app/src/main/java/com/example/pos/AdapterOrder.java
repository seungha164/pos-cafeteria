package com.example.pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.OrderViewHolder>{

    Context context;
    List<Order> orderList;
    ODataDao oDataDao;
    public AdapterOrder(Context context, List<Order> orderList, ODataDao orderDao) {
        this.context = context;
        this.orderList = orderList;
        this.oDataDao = orderDao;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_order,parent,false);
        AdapterOrder.OrderViewHolder holder = new AdapterOrder.OrderViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.onBind(orderList.get(position));

    }
    @Override
    public int getItemCount() {
        return (null!=orderList?orderList.size():0);
    }
    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView time,amount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tvDataOrder_time);
            amount = itemView.findViewById(R.id.tvDataOrder_amount);
        }
        public void onBind(Order order) {
            time.setText(order.payDate.substring(11));
            amount.setText(String.format("%d원",order.totalpay));
        }
    }
}
