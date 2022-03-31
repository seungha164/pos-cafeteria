package com.example.pos;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {
    private Context context;
    private List<Data> dataList;
    Frag1 frag1;
    public GridViewAdapter(Context context, ArrayList<Data> dataList,Frag1 parent) {
        this.context = context;
        this.dataList = dataList;
        this.frag1 = parent;
    }
    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_pos,parent,false);
        GridViewHolder holder = new GridViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.onBind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return (null!=dataList?dataList.size():0);
    }
    public class GridViewHolder extends RecyclerView.ViewHolder {
        Data item;
        ImageView img;
        TextView name;
        public GridViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvDataPos_Name);
            img = view.findViewById(R.id.imgDataPos);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    frag1.addBasket(item);
                }
            });
        }
        void onBind(Data d){
            name.setText(d.name);
            item = d;
            System.out.println(d.imgUri);
            try{
                Glide.with(itemView.getContext()).load(Uri.parse(d.imgUri)).into(img);
            }
            catch (Exception e){
                System.out.println(e.toString());
            }
            /*
            try{
                InputStream in = itemView.getContext().getContentResolver().openInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

             */
        }
    }
}
