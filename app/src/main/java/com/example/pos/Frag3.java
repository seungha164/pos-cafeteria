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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frag3} factory method to
 * create an instance of this fragment.
 */
public class Frag3 extends Fragment{

    TextView tvDate;
    String date;
    ODataDao orderDao;
    List<Order> orderList;

    RecyclerView rv;
    RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String mParam1;
    private String mParam2;
    public Frag3(LocalDate date, ODataDao oDataDao) {
        this.date=date+"";
        this.orderDao = oDataDao;
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
        rv = view.findViewById(R.id.rvF3);
        tvDate = view.findViewById(R.id.tvF3_date);
        tvDate.setText(date);
        System.out.println(date);
        orderList = orderDao.getOrderbyDate(date+"%");
        System.out.println(orderList.size()+"개");
        for(Order o:orderList){
            System.out.println(o.payDate+"");
        }
        AdapterSet();
        return view;
    }
    private void AdapterSet() {
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        adapter = new AdapterOrder(getContext(),orderList,orderDao);
        rv.setAdapter(adapter);
    }

}