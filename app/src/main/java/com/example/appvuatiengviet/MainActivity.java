package com.example.appvuatiengviet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    CSDL csdl;
    Button choiNgay,bangXh,thoat,choilai;
    ImageView setting,chiaSe;
    TextView tien;
    MediaPlayer mediaPlayer,mediaPlayer2;
    static boolean NhacNen ,AmLuong;
    SharedPreferences prefs;
    static float volumn1,volumn2;
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
        setting = findViewById(R.id.setting);
        chiaSe = findViewById(R.id.chiase);
        tien = findViewById(R.id.tien);
        choilai=findViewById(R.id.choilai);
        csdl = new CSDL(MainActivity.this);
        csdl.TaoCSDL(MainActivity.this);
        int ruby = csdl.HienRuby(MainActivity.this);
        tien.setText(ruby+"");
        prefs= getSharedPreferences("game", MODE_PRIVATE);
        NhacNen = prefs.getBoolean("isMute", true);
        AmLuong = prefs.getBoolean("isXB", true);
        volumn1=prefs.getFloat("volumnBack",1);
        volumn2=prefs.getFloat("volumnXB",1);
        mediaPlayer = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();

        ktraAmthanh();

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
        chiaSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkApp();
            }
        });
        choilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mediaPlayer2.start();
                showDialogTroGiup();
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
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogSetting();
            }
        });

    }

    private void showDialogTroGiup() {
        Dialog dialog=new Dialog(MainActivity.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_choilai);
        Button xacnhan=dialog.findViewById(R.id.xacnhan);
        Button tuchoi=dialog.findViewById(R.id.huy);
        TextView cham=dialog.findViewById(R.id.cham);
        Animation blink=AnimationUtils.loadAnimation(MainActivity.this,R.anim.blink2);
        cham.setAnimation(blink);
        cham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                csdl.ChoiLai(MainActivity.this);
                Toast.makeText(MainActivity.this, "Bạn đã chọn chơi lại từ đầu", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                recreate();
            }
        });
        tuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }
    private SeekBar volumeSeekBar1,volumeSeekBar2;
    private void ktraAmthanh() {
        if (NhacNen) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getResources().openRawResourceFd(R.raw.quizstarts));
                mediaPlayer.setVolume(volumn1,volumn1);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private void ShowDialogSetting(){
        Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_settings);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView cham=dialog.findViewById(R.id.cham);
        ImageView nhacback12=dialog.findViewById(R.id.nhacback);
        ImageView nhacXB12=dialog.findViewById(R.id.nhacxb);

        Animation blinkk= AnimationUtils.loadAnimation(this,R.anim.blink2);
        cham.setAnimation(blinkk);
        cham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        volumeSeekBar1 = dialog.findViewById(R.id.seek1);
        volumeSeekBar2 = dialog.findViewById(R.id.seek2);
        volumeSeekBar1.setMax(100);
        int progress1 = (int) (100 - Math.pow(10, (1 - volumn1) * Math.log10(100)));
        volumeSeekBar1.setProgress(progress1); // Thiết lập mức âm lượng mặc định
        volumeSeekBar2.setMax(100);
        int progress2 = (int) (100 - Math.pow(10, (1 - volumn2) * Math.log10(100)));

        volumeSeekBar2.setProgress(progress2);
        // nhạc nền
        volumeSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0){
                    nhacback12.setImageResource(R.drawable.icon_loa);
                    NhacNen=true;
                }
                else {
                    nhacback12.setImageResource(R.drawable.icon_tatloa);
                    NhacNen=false;
                }
                volumn1 = (float) (1 - (Math.log(100 - progress) / Math.log(100)));
                mediaPlayer.setVolume(volumn1, volumn1); // Thiết lập âm lượng của MediaPlayer
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("volumnBack", volumn1);
                editor.putBoolean("isMute",NhacNen);
                editor.apply();
                ktraAmthanh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        volumeSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress!=0){
                    nhacXB12.setImageResource(R.drawable.icon_mic);
                    AmLuong=true;
                }
                else {
                    nhacXB12.setImageResource(R.drawable.icon_tatmic);
                    AmLuong=false;
                }
                volumn2 = (float) (1 - (Math.log(100 - progress) / Math.log(100)));
                mediaPlayer2.setVolume(volumn2, volumn2);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("volumnXB", volumn2);
                editor.putBoolean("isXB",AmLuong);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dialog.show();
    }
    private void ShareLinkApp(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Thay đổi thành URI của liên kết bạn muốn chia sẻ
        Uri uri = Uri.parse("https://facebook.com");

        // Đặt nội dung của Intent thành liên kết
        intent.putExtra(Intent.EXTRA_TEXT, uri.toString());

        // Thêm cờ cho phép ứng dụng đính kèm xử lý dữ liệu từ URI được cung cấp
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Đặt loại dữ liệu của Intent thành "text/plain"
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"Share to..."));
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
        int ruby = csdl.HienRuby(MainActivity.this);
        tien.setText(ruby+"");
    }
}