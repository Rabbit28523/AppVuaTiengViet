package com.vtv.vuatiengviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.vtv.vuatiengviet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameShowRound1 extends AppCompatActivity implements ItemCauHoiClick,ItemCauTraLoiClick{
    TextView countdown;
    static int diemR1=0;
    CountDownTimer countDownTimer;
    long timeLeftInMillis = 300000;
    boolean isRunning = true;
    List<CauHoi> list;
    CSDL csdl;
    TextView level;
    TextView layout_2;
    MediaPlayer mediaPlayer, mediaPlayer2,mediaPlayer3;
    RecyclerView listcauhoi,dapan;
    SharedPreferences prefs;
    boolean nhacback;
    boolean nhacXB;
    float volumn1,volumn2;
    ImageView nextcau,backr1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Ẩn thanh công cụ (navigation bar)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_game_show_round1);

        countdown=findViewById(R.id.countdown);
        level=findViewById(R.id.level);
        layout_2=findViewById(R.id.layout_2);
        nextcau=findViewById(R.id.nextr1);
        backr1=findViewById(R.id.backr1);
        dapan=findViewById(R.id.dapan);
        listcauhoi=findViewById(R.id.listcauhoi);
        backr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBack();
            }
        });
        nextcau.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialogNext();
            }
        });
        prefs= getSharedPreferences("game", MODE_PRIVATE);
        nhacback = prefs.getBoolean("isMute", true);
        nhacXB = prefs.getBoolean("isXB", true);
        volumn1=prefs.getFloat("volumnBack",1);
        volumn2=prefs.getFloat("volumnXB",1);
        startTimer(timeLeftInMillis);

        csdl=new CSDL(GameShowRound1.this);
        list=csdl.getCauHoiRound1(10);

        mediaPlayer=new MediaPlayer();
        mediaPlayer2=new MediaPlayer();
        mediaPlayer3=new MediaPlayer();
        mediaPlayer2=MediaPlayer.create(GameShowRound1.this,R.raw.click_003);
        mediaPlayer2.setVolume(MainActivity.volumn2,MainActivity.volumn2);
        if(MainActivity.NhacNen){
            mediaPlayer=MediaPlayer.create(GameShowRound1.this,R.raw.quizstarts);
            mediaPlayer.setVolume(MainActivity.volumn1,MainActivity.volumn1);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
        loadTrang();
    }
    private void showDialogNext() {
        Dialog dialog = new Dialog(GameShowRound1.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_nextcau);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        Button chapnhan=dialog.findViewById(R.id.chapnhan);
        Button tuchoi=dialog.findViewById(R.id.tuchoi);
        chapnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTrang();
                dialog.dismiss();

            }
        });
        tuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void loadTrang() {
        if(index2>9){
            showDialogEndRound1();
        }
        else{
            layout_2.setText(list.get(index2).getTu());
            trogiup=new ArrayList<>();
            vitrioDapAn=new ArrayList<>();
            level.setText(String.valueOf(diem)+"/10");
            cautraloi = new ArrayList<>();
            arr = new ArrayList<>();
            arr2=new ArrayList<>();
            trogiup=new ArrayList<>();
            vitrioDapAn=new ArrayList<>();
            String trimmedString = list.get(index2).getDapAn().trim();
            for (int i = 0; i < trimmedString.length(); i++) {
                char currentChar = trimmedString.charAt(i);
                if (currentChar != ' ') {
                    arr.add(String.valueOf(""));
                    arr2.add(String.valueOf(currentChar));
                    trogiup.add(0);
                    cautraloi.add("");
                    vitrioDapAn.add(-1);
                }
            }
            Collections.shuffle(arr2);
            adap = new CauTraLoiAdapter(GameShowRound1.this,arr2,this);
            FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(getApplicationContext());
            layoutManager1.setFlexDirection(FlexDirection.ROW);
            layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
            dapan.setLayoutManager(layoutManager1);
            dapan.setAdapter(adap);
            adapter = new CauHoiAdapter(GameShowRound1.this,arr,this);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            listcauhoi.setLayoutManager(layoutManager);
            listcauhoi.setAdapter(adapter);
            mediaPlayer3=MediaPlayer.create(GameShowRound1.this,R.raw.loaded);
            mediaPlayer3.setVolume(MainActivity.volumn2,MainActivity.volumn2);
            mediaPlayer3.start();
            index2++;
        }




    }

    private void showDialogBack() {
        Dialog dialog = new Dialog(GameShowRound1.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_dunggame);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        Button chapnhan=dialog.findViewById(R.id.chapnhan);
        Button tuchoi=dialog.findViewById(R.id.tuchoi);
        chapnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                GameShowRound1.this.finish();
            }
        });
        tuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    List<Integer> vi_tri_dau_cach;
    List<Integer> trogiup;
    ArrayList<String> cautraloi;
    ArrayList<Integer> vitrioDapAn;
    ArrayList<String> arr2,arr;
    int index2=0;
    int diem=0;
    CauHoiAdapter adapter;
    CauTraLoiAdapter adap;

    String dapAn,dapAn2;
    FlexboxLayoutManager layoutManager,layoutManager2;
    private void showDialogEndRound1() {
        Dialog dialog = new Dialog(GameShowRound1.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_game_end_round1);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        ImageView ketthuc=dialog.findViewById(R.id.lydoend);
        ImageView choi=dialog.findViewById(R.id.choi);
        TextView diemtv=dialog.findViewById(R.id.diem);
        Button btnHuy=dialog.findViewById(R.id.choilai);
        Button btnchoitiep=dialog.findViewById(R.id.choitiep);
        if(isRunning){
            ketthuc.setImageResource(R.drawable.ketthuc);
            diemtv.setText("Điểm số: "+ diem +"/10");
            if(diem>=5){
                choi.setImageResource(R.drawable.tvtiep);
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        GameShowRound1.this.finish();
                        csdl.UpdateRuby(5);
                    }
                });
                btnchoitiep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diemR1=diem;
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        isActivityRunning = false;
                        startActivity(new Intent(GameShowRound1.this,GameShowRound2.class));
                        GameShowRound1.this.finish();
                    }
                });
            }
            else {
                choi.setImageResource(R.drawable.tvtiep);
                btnHuy.setBackgroundResource(R.drawable.butthoatlick);
                btnchoitiep.setBackgroundResource(R.drawable.choingayclick);

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        GameShowRound1.this.finish();
                    }
                });
                btnchoitiep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recreate();
                        dialog.dismiss();
                        csdl.UpdateRuby(5);
                    }
                });
            }
        }
        else {
            ketthuc.setImageResource(R.drawable.hetgio);
            diemtv.setText("Điểm của bạn: "+ diem +"/10");
            if(diem>=5){
                choi.setImageResource(R.drawable.tvtiep);
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        GameShowRound1.this.finish();
                        csdl.UpdateRuby(5);
                    }
                });
                btnchoitiep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diemR1=diem;
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        isActivityRunning = false;
                        startActivity(new Intent(GameShowRound1.this,GameShowRound2.class));
                        GameShowRound1.this.finish();
                    }
                });
            }
            else {
                choi.setImageResource(R.drawable.tvtiep);
                btnHuy.setBackgroundResource(R.drawable.butthoatlick);
                btnchoitiep.setBackgroundResource(R.drawable.choingayclick);

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        GameShowRound1.this.finish();
                    }
                });
                btnchoitiep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recreate();
                        dialog.dismiss();
                        csdl.UpdateRuby(5);
                    }
                });
            }
        }
        dialog.show();

    }
    private boolean isActivityRunning=true;
    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (!isActivityRunning) {
                    cancel();
                } else {
                    timeLeftInMillis = millisUntilFinished;
                    int minutes = (int) (millisUntilFinished / 1000) / 60;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                    countdown.setText(timeLeftFormatted);
                }

            }

            @Override
            public void onFinish() {
                if (isActivityRunning) {
                    countdown.setText("00:00");
                    isRunning = false;
                    Toast.makeText(GameShowRound1.this, "Trò chơi kết thúc", Toast.LENGTH_SHORT).show();
                    showDialogEndRound1();
                }
            }
        }.start();
//        isRunning = true;

    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }

    @Override
    public void CauHoiClick(int position) {
// lấy ra text của textview đã click
        String s=cautraloi.get(position).toString();
        // nếu s != ""
        if(s.trim().length()>0 &&s!=""&&s!=null ){

            // set vị trí đó trở thành ""
            cautraloi.set(position,"");

            // set lại vị trí cữ của từ đó vào lisdapan
            // vị trí cũ đã được lưu ở mảng vitriodapan
            arr2.set(vitrioDapAn.get(position),s);

            // vị trí đó ở mảng vitriodapan -> =1 (-1: chưa có ký tự; >-1: đã có ký tự)
            vitrioDapAn.set(position,-1);
            mediaPlayer2.start();
            // set adapter
            listcauhoi.setAdapter( new CauHoiAdapter(this,cautraloi,this));
            dapan.setAdapter( new CauTraLoiAdapter(this,arr2,this));
        }
    }

    @Override
    public void CauTraLoiClick(int position) {
        int dem=0;
        // kiểm tra xem đã có bao nhiêu ký tự trong câu trả lời của người chơi
        for(int i=0;i<cautraloi.size();i++){
            if(cautraloi.get(i).toUpperCase()!="" && !cautraloi.get(i).trim().isEmpty()){
                dem++;
            }
        }
        String s=arr2.get(position).toString();

        // nếu chọn ký tự trong listdapan !="" và biến dem < listcauhoi.size()
        if(s.trim().length()>0 &&s!=""&&s!=null && dem<arr.size()){

            //set vị trí đó trong listdapan => ""
            arr2.set(position," ");

            // biến dùng để chỉ set myAnswer 1 lần
            boolean foundNegativeIndex = false;
            //lập vòng for vitriodapan
            for (int j = 0; j < vitrioDapAn.size(); j++) {
                // nếu foundNegativeIndex=false và có ký tự "" trong myAnswer
                if (!foundNegativeIndex && vitrioDapAn.get(j) < 0) {

                    if (vitrioDapAn.get(j) == -1 ) {
                        vitrioDapAn.set(j, position);
                        foundNegativeIndex = true;
                        cautraloi.set(j, s);


                    }

                }
            }
            mediaPlayer2.start();

            adapter.notifyDataSetChanged();
            // set lại adapter
            listcauhoi.setAdapter( new CauHoiAdapter(this,cautraloi,this));
            dapan.setAdapter( new CauTraLoiAdapter(this,arr2,this));
            KiemTraDapAn();
        }
    }
    private void KiemTraDapAn(){
        int dem=0;
        for(int i=0;i<cautraloi.size();i++){
            if(cautraloi.get(i).toUpperCase()!="" && !cautraloi.get(i).trim().isEmpty()){
                dem++;
            }
        }
        // nếu vị trí textview cuối cùng đã có ký tự
        if(dem>=arr.size()){
            String dapan1="";
            for (int i = 0; i < list.get(index2-1).getDapAn().length(); i++) {
                char currentChar = list.get(index2-1).getDapAn().charAt(i);
                if (currentChar != ' ') {
                    dapan1+=String.valueOf(currentChar);

                }
            }
            StringBuilder result = new StringBuilder();
            for (String item : cautraloi) {
                result.append(item);
            }
            String dapan2 = result.toString();
            System.out.println("da1: "+dapan1);
            System.out.println("da2: "+dapan2);
            //nếu đáp án câu hỏi và đáp án của người chơi trùng nhau
            if(dapan1.equalsIgnoreCase(dapan2)){
                mediaPlayer3=MediaPlayer.create(GameShowRound1.this,R.raw.congrates_3);
                mediaPlayer3.setVolume(MainActivity.volumn2,MainActivity.volumn2);
                mediaPlayer3.start();
                mediaPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        diem++;
                        loadTrang();
                    }
                });
            }
            else {
                mediaPlayer3=MediaPlayer.create(GameShowRound1.this,R.raw.mixkitclickerror1110);
                mediaPlayer3.setVolume(MainActivity.volumn2,MainActivity.volumn2);
                mediaPlayer3.start();
                Toast.makeText(this, "Sai đáp án!!!", Toast.LENGTH_SHORT).show();
                loadTrang();
            }

        }
    }
}