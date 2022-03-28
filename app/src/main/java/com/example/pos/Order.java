package com.example.pos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity(tableName = "OTable")
public class Order {
    @PrimaryKey(autoGenerate = true) public int oid;
    @ColumnInfo(name = "oDate") public String payDate;  // "2022-03-28|11:20"
    @ColumnInfo(name = "oCoupon") public int coupon;
    @ColumnInfo(name = "oTotal") public int totalpay;
    @ColumnInfo(name = "opayMethod") public int payMethod;  // 0:cash, 1:kakao
    @ColumnInfo(name = "oList") public String oList;

    @Ignore List<Data> items;
    @Ignore Map<Data,Integer> countingMap;

    public Order(){
        items = new ArrayList<>();
        countingMap = new HashMap<>();
        coupon=0;
        totalpay=0;
    }
    boolean findItem(Data item){     // 이 오더 속 item 찾기
        for(Data d : items){
            if(d.find(item))
                return true;
        }
        return false;
    }
    public void newAdd(Data item) { // 새로 추가
        items.add(item);
        countingMap.put(item,1);
    }
    public void reNewal(Data item,int num) {    // 리뉴얼
        int tmp = countingMap.get(item);
        countingMap.put(item,tmp+num);
    }
    int calPay(){
        int sum = 0;
        for(Data d:items)
            sum+=countingMap.get(d)*d.price;
        return sum;
    }
    void clear(){
        items.clear();
        countingMap.clear();
    }

    public void deleteData(Data d) {
        items.remove(d);
        countingMap.remove(d);
    }

    void ListConverter(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Data d:items){
            stringBuilder.append(String.format("%s(%s)-%d?",d.name,(d.isHot?"HOT":"ICE"),countingMap.get(d)));
        }
        oList = stringBuilder.toString();
    }
}
