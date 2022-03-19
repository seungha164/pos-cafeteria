package com.example.pos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
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
    List<String> categorys;
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

        System.out.println("=>"+dataList.size());
        for(Data i:dataList){
            System.out.println(i.print());
        }

        btnPos = findViewById(R.id.ibtnHome_shopper);
        btnCal = findViewById(R.id.ibtnHome_calc);
        btnAdd = findViewById(R.id.ibtnHome_postadd);
        frag1 = new Frag1();
        frag2 = new Frag2(this);
        frag3 = new Frag3(this);

        getSupportFragmentManager().beginTransaction().add(R.id.MainSpace,frag1).commit();
        btnPos.setOnClickListener(this);
        btnCal.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }


    private void DB_setting() {
        database = DataDB.getInstance(this);
        // 1. 카테고리
        categorys = database.dataDao().getCategory();
        for(String category:categorys){
            List<Data> tmp = database.dataDao().getO(category);
            dataMap.put(category,tmp);
        }
        dataList = (ArrayList<Data>) database.dataDao().getAll();
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