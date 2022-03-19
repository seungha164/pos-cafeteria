package com.example.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{
    private Context context;
    private List<Data> dataList;
    DataDB db;

    public CustomAdapter(Context context, List<Data> dataList, DataDB database){
        this.context = context;
        this.dataList = dataList;
        this.db = database;
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
    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView idx,name,pay,isHot;
        ImageButton del,edit;
        Chip category;
        Data data;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            idx = itemView.findViewById(R.id.tvData_idx);
            isHot = itemView.findViewById(R.id.tvData_isH);
            name = itemView.findViewById(R.id.tvData_name);
            pay = itemView.findViewById(R.id.tvData_pay);
            del = itemView.findViewById(R.id.ibtnData_del);
            edit = itemView.findViewById(R.id.ibtnData_edit);
            category = itemView.findViewById(R.id.chipData);
        }
        void onBind(int i,Data d){
            idx.setText(""+i);
            isHot.setText(d.isHot()==true?"HOT":"ICE");
            pay.setText(String.format("%d원",d.price));
            name.setText(d.name);
            category.setText(d.category);
            data = d;
            del.setOnClickListener(this);
            edit.setOnClickListener(this);
        }
        void editDlg(){

        }
        void delDlg(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(String.format("%s (%s)",data.name,(data.isHot==true?"HOT":"ICE")))
                    .setMessage("해당 메뉴를 삭제하시겠습니까?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i){
                    db.dataDao().delete(data);
                    dataList.remove(data);
                    System.out.println(data.print());
                    notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("NOP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ibtnData_del:
                    delDlg();
                    break;
                case R.id.ibtnData_edit:
                    editDlg();
                    break;
            }
        }
    }
}
