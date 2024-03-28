package com.example.appvuatiengviet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Collections;

public class ManChoi extends AppCompatActivity implements ItemCauHoiClick {
    ArrayList<String> listcauhoi;
    ArrayList<String> listdapan;
    CauHoiAdapter cauHoiAdapter;
    CauHoiAdapter adapter;
    CSDL csdl;
    TextView cauhoi;
    RecyclerView ochu,cautraloi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_choi);
        cauhoi = findViewById(R.id.cauhoi);
        ochu = findViewById(R.id.ochu);
        cautraloi = findViewById(R.id.cautraloi);
        csdl = new CSDL(ManChoi.this);
        CauHoi cauHoi = csdl.HienCSDL(ManChoi.this);
        cauhoi.setText(cauHoi.getTu());
        listcauhoi = new ArrayList<>();
        listdapan = new ArrayList<>();
        String trimmedString = cauHoi.getDapAn().trim();
        for (int i = 0; i < trimmedString.length(); i++) {
            char currentChar = trimmedString.charAt(i);
            if (currentChar != ' ') {
                listcauhoi.add(String.valueOf(""));
                listdapan.add(String.valueOf(currentChar));
            }
        }
        Collections.shuffle(listdapan);
        adapter = new CauHoiAdapter(ManChoi.this,listdapan,this);
        FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(getApplicationContext());
        layoutManager1.setFlexDirection(FlexDirection.ROW);
        layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        cautraloi.setLayoutManager(layoutManager1);
        cautraloi.setAdapter(adapter);
       cauHoiAdapter = new CauHoiAdapter(ManChoi.this,listcauhoi,this);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
       layoutManager.setFlexDirection(FlexDirection.ROW);
       layoutManager.setJustifyContent(JustifyContent.FLEX_START);
       ochu.setLayoutManager(layoutManager);
       ochu.setAdapter(cauHoiAdapter);
    }
    @Override
    public void CauHoiClick(int position) {

    }
}