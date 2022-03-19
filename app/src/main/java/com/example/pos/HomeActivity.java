package com.example.pos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnPos, btnCal, btnAdd;
    Frag1 frag1;
    Frag2 frag2;
    Frag3 frag3;
    Vibrator vibrator;
    DataDB database;    // db

    // 카테고리s
    public List<String> categorys;
    public List<String> allCategorys;
    // 전체 데이터s
    ArrayList<Data> dataList = new ArrayList<>();
    // <카테고리-datas> 맵
    HashMap<String,List<Data>> dataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_home);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // 1. db setting -> database 연결 및 받아오기
        DB_setting();

        btnPos = findViewById(R.id.ibtnHome_shopper);
        btnCal = findViewById(R.id.ibtnHome_calc);
        btnAdd = findViewById(R.id.ibtnHome_postadd);
        frag1 = new Frag1(this);
        frag2 = new Frag2(this);
        frag3 = new Frag3(this);

        getSupportFragmentManager().beginTransaction().add(R.id.MainSpace,frag1).commit();
        btnPos.setOnClickListener(this);
        btnCal.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }
    private void DB_setting() {
        database = DataDB.getInstance(this);
        // - cTable 불러오기
        allCategorys = database.cdataDao().CgetName();   // 전체 카테고리
        Collections.reverse(allCategorys);
        // - dataTable 불러오기
        // 1. 카테고리
        categorys = database.dataDao().getCategory();
        for(String category:categorys){
            List<Data> tmp = database.dataDao().getOrderbyCategory(category);
            dataMap.put(category,tmp);
        }
        dataList = (ArrayList<Data>) database.dataDao().getAll();
    }
    boolean findCategory(String kwd){
        for(String cate:allCategorys)
            if(cate.contentEquals(kwd))
                return true;
        return false;
    }
    boolean findData(String name,boolean isHot){
        for(Data d:dataList)
            if(d.find(name,isHot))
                return true;
        return false;
    }
    @Override
    public void onClick(View view) {
        vibrator.vibrate(70);
        switch (view.getId()){
            case R.id.ibtnHome_shopper:
                getSupportFragmentManager().beginTransaction().replace(R.id.MainSpace,frag1).commit();
                break;
            case R.id.ibtnHome_calc:
                getSupportFragmentManager().beginTransaction().replace(R.id.MainSpace,frag2).commit();
                break;
            case R.id.ibtnHome_postadd:
                Bundle bundle = new Bundle(1);
                bundle.putSerializable("all",dataList);
                frag3.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.MainSpace,frag3).commit();
                break;
        }
    }
    public void toast(String s){
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
    }
}