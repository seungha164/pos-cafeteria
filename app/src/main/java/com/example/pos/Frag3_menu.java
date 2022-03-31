package com.example.pos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Frag3_menu extends Fragment {
    Spinner spinner;
    ArrayAdapter spinnerAdapter;
    RecyclerView rv;
    RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Frag2 frag2;
    public Frag3_menu(Frag2 frag2) {
        this.frag2 = frag2;
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_frag3_menu, container, false);
        spinner = view.findViewById(R.id.spinMenu);
        rv = view.findViewById(R.id.rvMenu);
        SpinnerSet();
        AdapterSet();

        return view;
    }
    private void SpinnerSet() {
        spinnerAdapter= new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,frag2.categorys);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classifyRv(frag2.categorys.get(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                classifyRv("All");
            }
        });
    }
    private void AdapterSet() {
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        classifyRv("All");
    }
    void classifyRv(String condition){
        if(condition.contentEquals("All"))
            adapter = new AdapterMenu(getContext(),frag2.dataList,frag2.dataDao);
        else
            adapter = new AdapterMenu(getContext(),frag2.dataDao.getOrderbyCategory(condition),frag2.dataDao);

        rv.setAdapter(adapter);
    }
}