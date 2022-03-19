package com.example.pos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "DataTable")
public class Data implements Serializable {  //DB내의 Table
    @PrimaryKey(autoGenerate = true) public int uid;
    @ColumnInfo(name = "name") public String name;
    @ColumnInfo(name = "isHot") public boolean isHot;
    @ColumnInfo(name = "category") public String category;
    @ColumnInfo(name = "price") public int price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    String print(){
        return String.format("[%s] %s (%s) %d원",category, name,(isHot==true?"HOT":"ICE"),price);
    }
    boolean find(String n, boolean isH){
        if(n.contentEquals(name) && isH == isHot)
            return true;
        return false;
    }
}

