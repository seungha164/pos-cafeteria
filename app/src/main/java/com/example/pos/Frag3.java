package com.example.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frag3} factory method to
 * create an instance of this fragment.
 */
public class Frag3 extends Fragment implements View.OnClickListener {
    FloatingActionButton fabAdd,fabCate, fabMenu;
    Spinner spinner;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close;
    RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayAdapter spinnerAdapter;
    private String mParam1;
    private String mParam2;



    HomeActivity parent;
    public Frag3(Activity parent) {
        this.parent = (HomeActivity) parent;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_frag3, container, false);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fabAdd = view.findViewById(R.id.fabF3_add);
        fabCate = view.findViewById(R.id.fabF3_category);
        fabMenu = view.findViewById(R.id.fabF3_menu);
        spinner = view.findViewById(R.id.spinF3);
        rv = view.findViewById(R.id.rvF3);

        fabAdd.setOnClickListener(this);
        fabCate.setOnClickListener(this);
        fabMenu.setOnClickListener(this);

        AdapterSet();
        SpinnerSet();

        return view;
    }
    private void SpinnerSet() {
        spinnerAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,parent.allCategorys);
        spinner.setAdapter(spinnerAdapter);
    }
    private void AdapterSet() {
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(getContext(),parent.dataList);
        rv.setAdapter(adapter);
        System.out.println("@ adapterSet() end");
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
                    if(parent.findCategory(tmpName))
                       parent.toast("이미 존재하는 카테고리입니다.");
                    else{
                        CData newC = new CData();
                        newC.cname = et.getText().toString().trim();
                        parent.database.cdataDao().insert(newC);
                        parent.allCategorys.clear();
                        parent.allCategorys.addAll(parent.database.cdataDao().CgetName());
                        spinnerAdapter.notifyDataSetChanged();
                        parent.toast("추가 완료");
                        alertDialog.dismiss();
                    }
                }
                else
                    parent.toast("추가할 내용을 입력하세요");
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
        for(String category: parent.allCategorys){
            Chip chip = new Chip(getContext());
            chip.setText(category);
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
                    if(parent.findData(sName, mode[0] ==0?true:false))
                        parent.toast("이미 존재하는 메뉴입니다.");
                    else{
                        Data data = setData(sName, mode[0] ==0?true:false,Integer.valueOf(etPay.getText().toString()), chipCategory[0]);
                        parent.database.dataDao().insert(data);
                        alertDialog.dismiss();
                        parent.dataList.clear();
                        parent.dataList.addAll(parent.database.dataDao().getAll());
                        adapter.notifyDataSetChanged();
                        parent.toast("추가 완료");
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
    public void anim() {
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
}