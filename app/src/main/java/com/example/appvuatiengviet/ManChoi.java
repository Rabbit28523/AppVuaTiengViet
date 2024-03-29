package com.example.appvuatiengviet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManChoi extends AppCompatActivity implements ItemCauHoiClick, ItemCauTraLoiClick {
    ArrayList<String> listcauhoi;
    ArrayList<String> listdapan;
    ArrayList<String> myAnswer;
    List<Integer> trogiup;
    ArrayList<Integer> vitrioDapAn;
    CauHoiAdapter cauHoiAdapter;
    CauTraLoiAdapter adapter;
    CSDL csdl;
    TextView cauhoi;
    RecyclerView ochu,cautraloi;
    CauHoi cauHoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_choi);
        cauhoi = findViewById(R.id.cauhoi);
        ochu = findViewById(R.id.ochu);
        cautraloi = findViewById(R.id.cautraloi);
        csdl = new CSDL(ManChoi.this);
        cauHoi = csdl.HienCSDL(ManChoi.this);
        cauhoi.setText(cauHoi.getTu());
        listcauhoi = new ArrayList<>();
        listdapan = new ArrayList<>();
        myAnswer=new ArrayList<>();
        trogiup=new ArrayList<>();
        vitrioDapAn=new ArrayList<>();
        String trimmedString = cauHoi.getDapAn().trim();
        for (int i = 0; i < trimmedString.length(); i++) {
            char currentChar = trimmedString.charAt(i);
            if (currentChar != ' ') {
                listcauhoi.add(String.valueOf(""));
                listdapan.add(String.valueOf(currentChar));
                trogiup.add(2);
                myAnswer.add("");
                vitrioDapAn.add(-1);
            }
        }
        Collections.shuffle(listdapan);
        adapter = new CauTraLoiAdapter(ManChoi.this,listdapan,this);
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
        String s=myAnswer.get(position).toString();
        if(s.trim().length()>0 &&s!=""&&s!=null && s!="1"){
            myAnswer.set(position,"");

            myAnswer.set(position,"");
            listdapan.set(vitrioDapAn.get(position),s);

            vitrioDapAn.set(position,-1);



//            vitrioDapAn.add(position);
            ochu.setAdapter( new CauHoiAdapter(this,myAnswer,this));
//            listcauhoi.setLayoutManager(layoutManager);
            cautraloi.setAdapter( new CauTraLoiAdapter(this,listdapan,this));
        }
    }

    @Override
    public void CauTraLoiClick(int position) {
        int dem=0;
        for(int i=0;i<myAnswer.size();i++){
            if(myAnswer.get(i).toUpperCase()!="" && !myAnswer.get(i).trim().isEmpty()){
                dem++;
            }
        }
        String s=listdapan.get(position).toString();

        if(s.trim().length()>0 &&s!=""&&s!=null && dem<listcauhoi.size()){
            listdapan.set(position," ");
            boolean foundNegativeIndex = false;
            int vitri=-1;// Biến đánh dấu để chỉ thực hiện cập nhật arr một lần
            for (int j = 0; j < vitrioDapAn.size(); j++) {
                if (!foundNegativeIndex && vitrioDapAn.get(j) < 0) {
                    // Nếu chưa tìm thấy phần tử âm và vitrioDapAn[j] nhỏ hơn 0, cập nhật arr và đánh dấu đã tìm thấy
                    if (vitrioDapAn.get(j) == -1 ) {


                        vitrioDapAn.set(j, position);
                        foundNegativeIndex = true;
                        myAnswer.set(j, s);


                    }

                }


            }
            KiemTraDapAn();



            adapter.notifyDataSetChanged();
            ochu.setAdapter( new CauHoiAdapter(this,myAnswer,this));
            cautraloi.setAdapter( new CauTraLoiAdapter(this,listdapan,this));
        }
    }
    private void KiemTraDapAn(){
        if(vitrioDapAn.get(vitrioDapAn.size()-1)>=0){
            String dapan1="";
            for (int i = 0; i < cauHoi.getDapAn().length(); i++) {
                char currentChar = cauHoi.getDapAn().charAt(i);
                if (currentChar != ' ') {
                    dapan1+=String.valueOf(currentChar);

                }
            }
            StringBuilder result = new StringBuilder();
            for (String item : myAnswer) {
                result.append(item);
            }
            String dapan2 = result.toString();
            System.out.println("da1: "+dapan1);
            System.out.println("da2: "+dapan2);
            if(dapan1.equalsIgnoreCase(dapan2)){

                  Toast.makeText(this, "mày giỏi đúng r đó con chóa", Toast.LENGTH_SHORT).show();

            }
            else {

                Toast.makeText(this, "lew lew gà", Toast.LENGTH_SHORT).show();
            }

        }
    }
}