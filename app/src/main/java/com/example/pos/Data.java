package com.example.pos;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "DataTable")
public class Data implements Serializable {  //DB내의 Table
    @PrimaryKey(autoGenerate = true) public int uid;
    public String name;
    public String category;
    public int price;
    public String imgUri;

    String print(){
        return String.format("[%s] %s %d원",category, name,price);
    }

    boolean find(String name){
        if(name.contentEquals(this.name))
            return true;
        return false;
    }
}

