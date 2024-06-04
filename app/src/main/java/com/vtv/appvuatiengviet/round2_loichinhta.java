package com.vtv.appvuatiengviet;

import static com.vtv.appvuatiengviet.GameShowRound2.volumn2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;



import java.util.Random;

public class round2_loichinhta extends AppCompatActivity {
    static  LoiChinhTa cauHoi;
    static int id=0;
    TextView level;
    TextView countdown;
    TextView layout_2;
    TextView btnA,btnB;
    MediaPlayer mp1;
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
        setContentView(R.layout.activity_round2_loichinhta);
        level=findViewById(R.id.level);
        countdown=findViewById(R.id.countdown);
        layout_2=findViewById(R.id.layout_2);
        btnA=findViewById(R.id.dap1);
        btnB=findViewById(R.id.dap2);
        level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        countdown.setText("Mảnh "+id);
        layout_2.setText(cauHoi.getTu());
        Random random = new Random();
        boolean isBtnARight = random.nextBoolean();

        if (isBtnARight) {
            btnA.setText(cauHoi.getDapAnDung());
            btnB.setText(cauHoi.getDapAnSai());
        } else {
            btnA.setText(cauHoi.getDapAnSai());
            btnB.setText(cauHoi.getDapAnDung());
        }
        View.OnClickListener answerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView clickedButton = (TextView) v;
                String selectedAnswer = clickedButton.getText().toString();

                if (selectedAnswer.equals(cauHoi.getDapAnDung())) {
                    mp1= MediaPlayer.create(round2_loichinhta.this,R.raw.congrates_3);
                    mp1.setVolume(volumn2, volumn2);
                    mp1.start();
                    GameShowRound2.traloidung[id-1]=true;
                    GameShowRound2.datraloi[id-1]=true;
                    mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            finish();
                        }
                    });
                } else {
                    mp1= MediaPlayer.create(round2_loichinhta.this,R.raw.mixkitclickerror1110);
                    mp1.setVolume(volumn2, volumn2);
                    mp1.start();
                    mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            GameShowRound2.datraloi[id-1]=true;
                            finish();
                        }
                    });
                }
            }
        };
        btnA.setOnClickListener(answerClickListener);
        btnB.setOnClickListener(answerClickListener);
    }
}