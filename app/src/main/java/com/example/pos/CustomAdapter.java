package com.example.pos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{
    private Context context;
    private List<Data> dataList;

    public CustomAdapter(Context context, List<Data> dataList){
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.onBind(position,dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return (null!=dataList?dataList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView name,pay,isHot;
        Chip category;
        Data data;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            isHot = itemView.findViewById(R.id.tvData_isH);
            name = itemView.findViewById(R.id.tvData_name);
            pay = itemView.findViewById(R.id.tvData_pay);
            category = itemView.findViewById(R.id.chipData);
        }
        void onBind(int i,Data d){
            isHot.setText(d.isHot()==true?"HOT":"ICE");
            pay.setText(String.format("%d원",d.price));
            name.setText(d.name);
            category.setText(d.category);
            data = d;
        }
    }
}
