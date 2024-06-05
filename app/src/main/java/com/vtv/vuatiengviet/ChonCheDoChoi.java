package com.vtv.vuatiengviet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vtv.vuatiengviet.R;


public class ChonCheDoChoi extends AppCompatActivity {
    static SharedPreferences prefs;
    static boolean AmLuong;
    static float volumn1;
    AppCompatButton h1,h2,h3,h4;
    MediaPlayer mediaPlayer2;
    private void AnhXa() {
        h1=findViewById(R.id.chedogameshow);
        h2=findViewById(R.id.chedocoban);
        h3=findViewById(R.id.btnback);
        h4=findViewById(R.id.btninfo);
    }
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
        setContentView(R.layout.activity_chon_che_do_choi);
        prefs= getSharedPreferences("game", MODE_PRIVATE);
        AmLuong = prefs.getBoolean("isXB", true);
        volumn1=prefs.getFloat("volumnXB",1);
        mediaPlayer2 = MediaPlayer.create(ChonCheDoChoi.this,R.raw.click_003);
        AnhXa();
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
                mediaPlayer2.setVolume(volumn1,volumn1);
                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    }
                });
                startActivity(new Intent(ChonCheDoChoi.this, GameShowRound1.class));
            }
        });
        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
                mediaPlayer2.setVolume(volumn1,volumn1);
                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    }
                });
                startActivity(new Intent(ChonCheDoChoi.this, ManChoi.class));
            }
        });
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
                mediaPlayer2.setVolume(volumn1,volumn1);
                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    }
                });

                ChonCheDoChoi.this.finish();
            }
        });
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
                mediaPlayer2.setVolume(volumn1,volumn1);
                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    }
                });
            }
        });
    }
}