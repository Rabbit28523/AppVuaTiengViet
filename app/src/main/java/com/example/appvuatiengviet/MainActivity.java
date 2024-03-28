package com.example.appvuatiengviet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CSDL csdl;
    Button choiNgay,bangXh,thoat;
    ImageView amLuong,nhacNen,chiaSe;
    TextView tien;
    MediaPlayer mediaPlayer,mediaPlayer2;
    boolean NhacNen =true ,AmLuong =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Ẩn thanh công cụ (navigation bar)
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        choiNgay = findViewById(R.id.choingay);
        bangXh = findViewById(R.id.bxh);
        thoat = findViewById(R.id.thoat);
        amLuong = findViewById(R.id.amluong);
        nhacNen = findViewById(R.id.nhacnen);
        chiaSe = findViewById(R.id.chiase);
        tien = findViewById(R.id.tien);
        csdl = new CSDL(MainActivity.this);
        csdl.TaoCSDL(MainActivity.this);
        int ruby = csdl.HienRuby(MainActivity.this);
        tien.setText(ruby+"");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.quizstarts);
        mediaPlayer.start();
        mediaPlayer2 = new MediaPlayer();
        mediaPlayer2 = MediaPlayer.create(MainActivity.this,R.raw.click_003);
        choiNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer2.start();
                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        startActivity(new Intent(MainActivity.this,ManChoi.class));
                    }
                });
            }
        });
        thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer2.start();
                mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        MainActivity.this.finish();
                    }
                });
            }
        });
        nhacNen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NhacNen){
                    mediaPlayer.setVolume(0f,0f);
                    nhacNen.setImageResource(R.drawable.matnhacnen);
                    NhacNen = !NhacNen;
                    Toast.makeText(MainActivity.this, "Đã tắt nhạc nền!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mediaPlayer.setVolume(1f,1f);
                    nhacNen.setImageResource(R.drawable.nhacnen);
                    NhacNen = !NhacNen;
                    Toast.makeText(MainActivity.this, "Đã bật nhạc nền!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        amLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AmLuong){
                    mediaPlayer2.setVolume(0f,0f);
                    amLuong.setImageResource(R.drawable.matamthanh);
                    AmLuong = !AmLuong;
                    Toast.makeText(MainActivity.this, "Đã tắt âm thanh!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mediaPlayer2.setVolume(1f,1f);
                    amLuong.setImageResource(R.drawable.volume);
                    AmLuong = !AmLuong;
                    Toast.makeText(MainActivity.this, "Đã bật âm thanh!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Dừng nhạc nếu người dùng ẩn ra khỏi trò chơi
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            };
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Tiếp tục phát nhạc nếu người dùng ẩn ra khỏi trò chơi
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }
}