package com.example.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class Frag2 extends Fragment implements View.OnClickListener {
    FloatingActionButton fabAdd,fabCate, fabMenu;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close;

    private String mParam1;
    private String mParam2;

    DataDao dataDao;
    CDataDao cDataDao;
    ArrayList<String> categorys;
    ArrayList<Data> dataList;
    public Frag2(DataDao Ddao, CDataDao cDataDao, List<String> ctcrys, List<Data> dataList) {
        this.dataDao = Ddao;
        this.cDataDao =cDataDao;
        this.categorys = (ArrayList<String>) ctcrys;
        this.dataList= (ArrayList<Data>) dataList;
    }

    TabLayout tabs;
    Frag3_menu menu;
    Frag3_category category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_frag2, container, false);
        fabAdd = view.findViewById(R.id.fabF3_add);
        fabCate = view.findViewById(R.id.fabF3_category);
        fabMenu = view.findViewById(R.id.fabF3_menu);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        menu = new Frag3_menu(this);
        category = new Frag3_category();
        getChildFragmentManager().beginTransaction().add(R.id.vpFrag2,menu).commit();
        tabs = view.findViewById(R.id.tabsF2);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getChildFragmentManager().beginTransaction().replace(R.id.vpFrag2,menu).commit();
                        break;
                    case 1:
                        getChildFragmentManager().beginTransaction().replace(R.id.vpFrag2,category).commit();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
        fabAdd.setOnClickListener(this);
        fabCate.setOnClickListener(this);
        fabMenu.setOnClickListener(this);
        return view;
    }
    private void anim() {
        if (isFabOpen) {
            fabCate.startAnimation(fab_close);
            fabMenu.startAnimation(fab_close);
            fabCate.setClickable(false);
            fabMenu.setClickable(false);
            isFabOpen = false;
        } else {
            fabCate.startAnimation(fab_open);
            fabMenu.startAnimation(fab_open);
            fabCate.setClickable(true);
            fabMenu.setClickable(true);
            isFabOpen = true;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabF3_add:
                anim();
                break;
            case R.id.fabF3_menu:   // FAB_SUM1
                addMenu();
                anim();
                break;
            case R.id.fabF3_category:   // FAB_SUM2
                addCategory();
                anim();
                break;

        }
    }
    void addCategory(){
        View dlgView = getLayoutInflater().inflate(R.layout.dialogf3_fab2,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dlgView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Button submit = dlgView.findViewById(R.id.btnDlgfab2);
        ImageButton back = dlgView.findViewById(R.id.ibtnDlgfab2_B);
        EditText et = dlgView.findViewById(R.id.etDlgfab2_cafe);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText()!=null && et.getText().toString().trim()!=""){
                    String tmpName = et.getText().toString().trim();
                    if(findCategory(tmpName))
                        toast("이미 존재하는 카테고리입니다.");
                    else{
                        CData newC = new CData();
                        newC.cname = et.getText().toString().trim();
                        cDataDao.insert(newC);
                        categorys.add(newC.cname);
                        menu.spinnerAdapter.notifyDataSetChanged();
                        toast("추가 완료");
                        alertDialog.dismiss();
                    }
                }
                else
                    toast("추가할 내용을 입력하세요");
            }
        });
    }
    void addMenu(){
        // 초기화
        final int[] mode = {-1};
        final String[] chipCategory = new String[1];

        View dlgView = getLayoutInflater().inflate(R.layout.dialogf3_fab1,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dlgView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        // et & category setting
        ImageButton back = dlgView.findViewById(R.id.ibtnDlgF3_Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        EditText etName = dlgView.findViewById(R.id.etDlgF3_name);  // 1. name
        Button btnHot = dlgView.findViewById(R.id.btnDlgHot);       // 2. hot | oce
        btnHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode[0] = (mode[0] ==0?-1:0);
            }
        });
        Button btnIce = dlgView.findViewById(R.id.btnDlgIce);
        btnIce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode[0] = (mode[0] ==1?-1:1);
            }
        });
        EditText etPay = dlgView.findViewById(R.id.etDlgF3_pay);    // 3. price
        // 4. category
        ChipGroup chipGroup = dlgView.findViewById(R.id.chip_groupDlg);
        for(String category: categorys){
            if(category.contentEquals("All"))
                continue;
            Chip chip = new Chip(getContext());
            chip.setSelected(true);
            chip.setText(category);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                    if(ischecked)
                        toast("check");
                    else
                        toast("NOT checked");
                }
            });

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chipCategory[0] = category;
                }
            });
            chipGroup.addView(chip);
        }
        alertDialog.show();

        Button btnSubmit = dlgView.findViewById(R.id.btnDlgF3_submit);      // 5. btn
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText()==null || etPay.getText()==null || mode[0] ==-1
                        || chipCategory[0] =="" ||etName.getText().toString().trim().contentEquals(""))
                    Toast.makeText(getContext(), "모두 입력해주세요", Toast.LENGTH_SHORT).show();
                else{
                    String sName = etName.getText().toString().trim();
                    if(findData(sName, mode[0] ==0?true:false))
                        toast("이미 존재하는 메뉴입니다.");
                    else{
                        Data data = setData(sName, mode[0] ==0?true:false,Integer.valueOf(etPay.getText().toString()), chipCategory[0]);
                        dataDao.insert(data);
                        alertDialog.dismiss();
                        dataList.clear();
                        dataList.addAll(dataDao.getAll());
                        menu.adapter.notifyDataSetChanged();
                        toast("추가 완료");
                    }
                }
            }
        });
    }
    private Data setData(String n, boolean isH,int p,String c){
        Data d = new Data();
        d.setName(n);
        d.setHot(isH);
        d.setPrice(p);
        d.setCategory(c);
        Toast.makeText(getContext(), d.print(), Toast.LENGTH_SHORT).show();
        return d;
    }
    boolean findCategory(String kwd){
        for(String cate:categorys)
            if(cate.contentEquals(kwd))
                return true;
        return false;
    }
    boolean findData(String name,boolean isHot){
        for(Data d:dataList)
            if(d.name.contentEquals(name) && d.isHot==isHot)
                return true;
        return false;
    }
    private void toast(String txt){
        Toast.makeText(getContext(),txt, Toast.LENGTH_SHORT).show();
    }
}