package com.example.pos;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frag1 extends Fragment implements View.OnClickListener {

    Button btnPay;
    Chip chipCoupon;
    TextView tvTotal,tvcoupon;
    ImageButton ibtnResetBasket,ibtnResetCoupon;
    TabLayout tabs;
    RecyclerView rv,rvB;
    private RecyclerView.Adapter adapter, adapterB;
    private RecyclerView.LayoutManager layoutManager,layoutManagerB;

    Order Basket;   // 현재 장바구니
    LocalDateTime now;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    String cCategory;
    DataDao dataDao;
    ODataDao oDao;
    List<String> categorys;
    ArrayList<Data> dataList;

    public Frag1(DataDao dao,ODataDao oDataDao,List<String> ctcrys,List<Data> dataList) {
        this.dataDao = dao;
        this.oDao =oDataDao;
        Basket = new Order();
        this.categorys = ctcrys;
        this.dataList= (ArrayList<Data>) dataList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_frag1, container, false);
        tvTotal = view.findViewById(R.id.tvFrag1_BTotal);
        ibtnResetBasket = view.findViewById(R.id.ibtnFrag1_basketReset);
        tabs = view.findViewById(R.id.tabsF1);
        rv = view.findViewById(R.id.rvF1);
        rvB = view.findViewById(R.id.rvF1_basket);
        chipCoupon = view.findViewById(R.id.chipF1_C);
        ibtnResetCoupon = view.findViewById(R.id.ibtnF1_Creset);
        tvcoupon = view.findViewById(R.id.tvF1_Ctotal);
        btnPay = view.findViewById(R.id.btnF1_pay);
        tabSetting();
        rvSetting();
        setTotal();
        ibtnResetCoupon.setOnClickListener(this);
        ibtnResetBasket.setOnClickListener(this);
        chipCoupon.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        return view;
    }
    public void setTotal(){
        tvTotal.setText(String.format("%d원",calTotal()));
    }
    private int calTotal() {
        int sum = Basket.calPay();      // 1. basket의 calc
        sum = (sum- Basket.coupon<0?0:sum-Basket.coupon);    // 2. coupon
        return sum;
    }
    void tabSetting(){
        for(String cName:categorys)
            tabs.addTab(tabs.newTab().setText(cName));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {  // tab상태가 선택 상태로 변경
                cCategory = tab.getText().toString();
                classify(cCategory);
            }
            @Override   // tab의 상태가 선택되지 않음으로 변경
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override // 이미 선택된 tab이 다시시
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
    void classify(String condition){
        ArrayList<Data> tmp;
        if(condition.contentEquals("All"))
            tmp = dataList;
        else
            tmp = (ArrayList<Data>) dataDao.getOrderbyCategory(condition);

        adapter = new GridViewAdapter(getContext(),tmp,this);
        rv.setAdapter(adapter);
    }
    void rvSetting(){
        adapter = new GridViewAdapter(getContext(),dataList,this);
        layoutManager = new GridLayoutManager(getContext(),4);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        rvB.setHasFixedSize(true);
        adapterB = new BasketAdapter(getContext(),Basket,this);
        layoutManagerB = new LinearLayoutManager(getContext());
        rvB.setLayoutManager(layoutManagerB);
        rvB.setAdapter(adapterB);
    }
    void addBasket(Data item){
        if(Basket.findItem(item)){  // basket에 이미 item 존재시,
            Basket.reNewal(item,1);   // map갱신
        } else{
            Basket.newAdd(item);   // 새로 item 추가
        }
        adapterB.notifyDataSetChanged();
        setTotal();
    }
    private void setCoupon(int mode){
        if(mode==0)
            Basket.coupon=0;
        else
            Basket.coupon+=1500;
        tvcoupon.setText(String.format("%d원",Basket.coupon));
    }
    void payment(){
        if(Basket.items.size()==0){
            toast("주문이 없습니다.");
            return;
        }
        View dlgView = getLayoutInflater().inflate(R.layout.dialogf1_payment,null);
        AlertDialog alertDialog = newDialog(dlgView);

        Button btnCash = dlgView.findViewById(R.id.btnDlgPay_cash);
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.payMethod= 0;
                alertDialog.dismiss();
                receipe();
            }
        });
        Button btnKakao = dlgView.findViewById(R.id.btnDlgPay_kakaopay);
        btnKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.payMethod= 1;
                alertDialog.dismiss();
                receipe();
            }
        });
    }
    void receipe(){
        TextView pay1,pay2,method,coupon;
        View dlgView = getLayoutInflater().inflate(R.layout.receipt,null);
        AlertDialog alertDialog = newDialog(dlgView);
        alertDialog.setCancelable(false);
        ListView lv = dlgView.findViewById(R.id.lvReceipe);
        method = dlgView.findViewById(R.id.tvReceipt_method);
        pay1 = dlgView.findViewById(R.id.tvReceipt_pay1);
        pay2 = dlgView.findViewById(R.id.tvReceipt_pay2);
        coupon = dlgView.findViewById(R.id.tvReceipt_coupon);
        method.setText(String.format("결제수단 : %s",Basket.payMethod==0?"현금":"카카오페이"));
        pay1.setText(Basket.calPay()+"원");
        coupon.setText(String.format("할인금액 :%20d원",Basket.coupon));
        Basket.totalpay = calTotal();
        pay2.setText(Basket.totalpay+"원");

        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        for(Data d : Basket.items){
            HashMap<String,String> tmp = new HashMap<>();
            tmp.put("name",String.format("%10s (%s)",d.name,d.isHot?"HOT":"ICE"));
            tmp.put("num","x "+Basket.countingMap.get(d));
            list.add(tmp);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(dlgView.getContext(),list,R.layout.receipt_lv,
                new String[]{"name","num"},
                new int[]{R.id.lv_name,R.id.lv_num});
        lv.setAdapter(simpleAdapter);
        dlgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // order 데베에 넣기
                Basket.ListConverter();
                now = LocalDateTime.now();
                Basket.payDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm"));
                oDao.insert(Basket);
                Basket = new Order();   // 새로 만들기
                adapterB.notifyDataSetChanged();
                setTotal();
                setCoupon(0);

                toast("주문 완료");
                alertDialog.dismiss();
            }
        });
    }
    private AlertDialog newDialog(View dlgView){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dlgView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtnFrag1_basketReset:
                Basket.clear();
                adapterB.notifyDataSetChanged();
                setTotal();
                break;
            case R.id.ibtnF1_Creset:
                setCoupon(0);
                setTotal();
                break;
            case R.id.chipF1_C:
                setCoupon(1);
                setTotal();
                break;
            case R.id.btnF1_pay:
                payment();
                break;
        }
    }
    private void toast(String txt){
        Toast.makeText(getContext(),txt, Toast.LENGTH_SHORT).show();
    }
}