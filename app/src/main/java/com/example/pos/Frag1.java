package com.example.pos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frag1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag1 extends Fragment {

    TabLayout tabs;
    RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String cCategory;
    HomeActivity parent;
    public Frag1(HomeActivity parent) {
        this.parent = parent;
    }
    public static Frag1 newInstance(String param1, String param2) {
        Frag1 fragment = new Frag1(new HomeActivity());
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        tabs = view.findViewById(R.id.tabsF1);
        rv = view.findViewById(R.id.rvF1);
        tabSetting();
        rvSetting();
        return view;
    }
    void rvSetting(){
        adapter = new GridViewAdapter(getContext(),parent.dataList,this);
        layoutManager = new GridLayoutManager(getContext(),4);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }
    void tabSetting(){
        cCategory = "All";
        for(String cName:parent.allCategorys)
            tabs.addTab(tabs.newTab().setText(cName));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {  // tab상태가 선택 상태로 변경
                cCategory = tab.getText().toString();
                classify(cCategory);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {    // tab의 상태가 선택되지 않음으로 변경
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {    // 이미 선택된 tab이 다시시
            }
        });
    }
    void addBasket(Data d){
        parent.toast(d.print());

    }
    void classify(String condition){
        List<Data> tmp;
        if(condition.contentEquals("All"))
            tmp = parent.dataList;
        else
            tmp = parent.database.dataDao().getOrderbyCategory(condition);

        adapter = new GridViewAdapter(getContext(),(ArrayList<Data>)tmp,this);
        rv.setAdapter(adapter);
    }
}