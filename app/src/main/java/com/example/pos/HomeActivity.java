package com.example.pos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnPos, btnCal, btnAdd;
    Frag1 frag1;
    Frag2 frag2;
    Frag3 frag3;
    Vibrator vibrator;
    // db
    private DataDB database;
    CDataDao cDataDao;
    DataDao dDataDao;
    ODataDao oDataDao;
    List<String> categorys;     // 카테고리s
    ArrayList<Data> dataList = new ArrayList<>();   // 전체 데이터s
    // date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_home);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        DB_setting();

        btnPos = findViewById(R.id.ibtnHome_shopper);
        btnCal = findViewById(R.id.ibtnHome_calc);
        btnAdd = findViewById(R.id.ibtnHome_postadd);
        frag1 = new Frag1(dDataDao,oDataDao,categorys,dataList);
        frag2 = new Frag2(dDataDao,cDataDao,categorys,dataList);
        frag3 = new Frag3(LocalDate.now(),oDataDao);

        getSupportFragmentManager().beginTransaction().add(R.id.MainSpace,frag1).commit();
        btnPos.setOnClickListener(this);
        btnCal.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }
    private void DB_setting() {
        database = DataDB.getInstance(this);
        if(database==null){
            System.out.println("database NULL");
            return;
        }
        cDataDao = database.cdataDao();
        dDataDao = database.dataDao();
        oDataDao = database.odataDao();

        categorys = cDataDao.CgetName();   // category
        categorys.add("All");
        Collections.reverse(categorys);
        dataList = (ArrayList<Data>) dDataDao.getAll();   // data
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

}