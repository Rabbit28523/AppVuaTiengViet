package com.vtv.vuatiengviet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vtv.vuatiengviet.R;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    CSDL csdl;
    Button choiNgay,bangXh,thoat,choilai;
    ImageView setting,chiaSe;
    TextView tien;
    MediaPlayer mediaPlayer,mediaPlayer2;
    static boolean NhacNen ,AmLuong;
    SharedPreferences prefs;
    ImageView avt,shop;
    TextView hightcore,name;
    static float volumn1,volumn2;
    AppCompatButton login;
    public static int RC_DEFAULT_SIGN_IN = 100;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    ThongTinNguoiChoi tt;
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
        avt=findViewById(R.id.avt1);
        hightcore=findViewById(R.id.hightCore);
        name=findViewById(R.id.tvnameNG);
        shop=findViewById(R.id.shop);
        choilai=findViewById(R.id.choilai);
        login = findViewById(R.id.login);
        csdl = new CSDL(MainActivity.this);
        csdl.TaoCSDL();
        csdl.insertNewAvt();
        csdl.insertNewKhung();
        if(csdl.KiemTraNhanVat(this)){
            showDialogDatTen();
        }
        else {
            int ruby = csdl.HienThongTinNhanVat().getRuby();
            tien.setText(ruby+"");
            hightcore.setText("Câu hỏi: "+ csdl.HienThongTinNhanVat().getLevel());
            name.setText(csdl.HienThongTinNhanVat().getName());
            String fileAvt = "avt"+String.valueOf(csdl.HienThongTinNhanVat().getAvt_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
            int resId = getResources().getIdentifier(fileAvt, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
            String fileKhung = "khung"+String.valueOf(csdl.HienThongTinNhanVat().getKhung_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
            int resId2 = getResources().getIdentifier(fileKhung, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh

            if (resId != 0) {
                avt.setImageResource(resId); // Thiết lập hình ảnh cho ImageView
            } else {
                // Xử lý trường hợp không tìm thấy tệp ảnh
            }
            if (resId2 != 0) {
                avt.setBackgroundResource(resId2); // Thiết lập hình ảnh cho ImageView
            } else {
                // Xử lý trường hợp không tìm thấy tệp ảnh
            }
        }
        
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
                        startActivity(new Intent(MainActivity.this,ChonCheDoChoi.class));
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
                startActivity(new Intent(MainActivity.this, ChallengeActivity.class));
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

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,cuahang.class));

            }
        });
        bangXh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LeaderboardActivity.class));

            }
        });
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        gsc= GoogleSignIn.getClient(this,gso);

        // Khởi tạo đối tượng Firebase Auth để thực hiện việc xác thực người dùng
        auth = FirebaseAuth.getInstance();
        // Khởi tạo đối tượng FirebaseDatabase để thực hiện lưu trữ thông tin người dùng
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Khởi tạo đối tượng GoogleSignInOptions để yêu cầu việc xác thực bằng tài khoản Google
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        // Kiểm tra trạng thái đăng nhập của người dùng
        FirebaseUser currentUser = auth.getCurrentUser();
        if (auth.getCurrentUser() != null) {
            login.setBackgroundResource(R.drawable.icon_tatloa);
            // Người dùng đã đăng nhậpic
//            Toast.makeText(this, "UserId: " + currentUser.getUid(), Toast.LENGTH_SHORT).show();

        } else {
            // Người dùng chưa đăng nhập
            login.setBackgroundResource(R.drawable.icon_loa);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null) {
                    signOut();
                } else {
                    signIn1();
                }
            }
        });
    }
    private void signOut() {
        auth.signOut();
        gsc.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

        });
        // Xóa dữ liệu của csdl và tạo lại
        csdl.recreateDatabase();
        login.setBackgroundResource(R.drawable.icon_loa);
        updatedl();
    }
    private void signIn1() {

        Intent intent=gsc.getSignInIntent();
        startActivityForResult(intent, RC_DEFAULT_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_DEFAULT_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuth1(account.getIdToken());

                login.setBackgroundResource(R.drawable.icon_tatloa);
//                updatedl();

            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void updatedl(){
        tt=csdl.HienThongTinNhanVat2();
        hightcore.setText("High score: "+ tt.getLevel());
        name.setText(tt.getName());
        tien.setText(String.valueOf(csdl.HienThongTinNhanVat().getRuby()));
        String fileAvt = "avt"+String.valueOf(tt.getAvt_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
        int resId = getResources().getIdentifier(fileAvt, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
        String fileKhung = "khung"+String.valueOf(tt.getKhung_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
        int resId2 = getResources().getIdentifier(fileKhung, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh

        if (resId != 0) {
            avt.setImageResource(resId); // Thiết lập hình ảnh cho ImageView
        } else {
            // Xử lý trường hợp không tìm thấy tệp ảnh
        }
        if (resId2 != 0) {
            avt.setBackgroundResource(resId2); // Thiết lập hình ảnh cho ImageView
        } else {
            // Xử lý trường hợp không tìm thấy tệp ảnh
        }
    }

    private void firebaseAuth1(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user =auth.getCurrentUser();
                            ThongTinNguoiChoi thongTinNguoiChoi = csdl.HienThongTinNhanVat2();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("name", thongTinNguoiChoi.getName());
                            map.put("ruby", thongTinNguoiChoi.getRuby());
                            map.put("level", thongTinNguoiChoi.getLevel());
                            map.put("avt_id", thongTinNguoiChoi.getAvt_id());
                            map.put("khung_id", thongTinNguoiChoi.getKhung_id());map.put("avt_id", thongTinNguoiChoi.getAvt_id());
                            map.put("damua_khung", thongTinNguoiChoi.getDamua_khung());
                            map.put("damua_avt", thongTinNguoiChoi.getDamua_avt());


                            // Kiểm tra xem thông tin người dùng đã tồn tại chưa
                            firebaseDatabase.getReference().child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        csdl.getPlayerInfoFromFirebase();
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        FirebaseUser user = auth.getCurrentUser();
                                        if (user != null) {
                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            DatabaseReference userRef = firebaseDatabase.getReference().child("users").child(user.getUid());

                                            // Lấy thông tin người chơi từ Firebase
                                            userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    DataSnapshot dataSnapshot = task.getResult();
                                                    // Kiểm tra xem dataSnapshot có tồn tại không
                                                    if (dataSnapshot.exists()) {
                                                        ThongTinNguoiChoi thongTinNguoiChoi = dataSnapshot.getValue(ThongTinNguoiChoi.class);
                                                        if (thongTinNguoiChoi != null) {

                                                            hightcore.setText("Câu hỏi: "+ thongTinNguoiChoi.getLevel());
                                                            name.setText(thongTinNguoiChoi.getName());
                                                            tien.setText(String.valueOf(thongTinNguoiChoi.getRuby()));
                                                            String fileAvt = "avt"+String.valueOf(thongTinNguoiChoi.getAvt_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
                                                            int resId = getResources().getIdentifier(fileAvt, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
                                                            String fileKhung = "khung"+String.valueOf(tt.getKhung_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
                                                            int resId2 = getResources().getIdentifier(fileKhung, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh

                                                            if (resId != 0) {
                                                                avt.setImageResource(resId); // Thiết lập hình ảnh cho ImageView
                                                            } else {
                                                                // Xử lý trường hợp không tìm thấy tệp ảnh
                                                            }
                                                            if (resId2 != 0) {
                                                                avt.setBackgroundResource(resId2); // Thiết lập hình ảnh cho ImageView
                                                            } else {
                                                                // Xử lý trường hợp không tìm thấy tệp ảnh
                                                            }
                                                        }
                                                    } else {
                                                        System.out.println("User does not exist.");
                                                    }
                                                }
                                            });
                                        } else {
                                            System.out.println("User not logged in.");
                                        }
                                        Toast.makeText(MainActivity.this, "Old player", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "New player", Toast.LENGTH_SHORT).show();
                                        // Lưu thông tin người dùng vào Realtime Database
                                        firebaseDatabase.getReference().child("users").child(user.getUid())
                                                .setValue(map);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w("TAG", "loadPost:onCancelled", error.toException());
                                }
                            });

                        }
                    }
                });
        updatedl();
    }
    private void showDialogDatTen() {
        Dialog dialog = new Dialog(MainActivity.this,android.R.style.Theme_Dialog );

        dialog.setContentView(R.layout.dialog_doiten);
        EditText tvname=dialog.findViewById(R.id.tvname);
        Button btnXN=dialog.findViewById(R.id.btnname);
        TextView cham=dialog.findViewById(R.id.cham);
        RelativeLayout chuy=dialog.findViewById(R.id.tieude2);
        chuy.setVisibility(View.GONE);
        Animation blinkk=AnimationUtils.loadAnimation(this,R.anim.blink2);
        cham.setAnimation(blinkk);
        dialog.setCancelable(false);
        cham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cham.setVisibility(View.GONE);
        btnXN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvname.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Bạn phải nhập tên người dùng", Toast.LENGTH_SHORT).show();
                }
                else {
                    csdl.TaoNhanVat(tvname.getText().toString());
                    int ruby = csdl.HienThongTinNhanVat().getRuby();
                    tien.setText(ruby+"");
                    hightcore.setText("Câu hỏi: "+ csdl.HienThongTinNhanVat().getLevel());
                    name.setText(csdl.HienThongTinNhanVat().getName());
                    String fileAvt = "avt"+String.valueOf(csdl.HienThongTinNhanVat().getAvt_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
                    int resId = getResources().getIdentifier(fileAvt, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
                    String fileKhung = "khung"+String.valueOf(csdl.HienThongTinNhanVat().getKhung_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
                    int resId2 = getResources().getIdentifier(fileKhung, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh

                    if (resId != 0) {
                        avt.setImageResource(resId); // Thiết lập hình ảnh cho ImageView
                    } else {
                        // Xử lý trường hợp không tìm thấy tệp ảnh
                    }
                    if (resId2 != 0) {
                        avt.setBackgroundResource(resId2); // Thiết lập hình ảnh cho ImageView
                    } else {
                        // Xử lý trường hợp không tìm thấy tệp ảnh
                    }
                    dialog.dismiss();
                    startActivity(new Intent(MainActivity.this, ManChoi.class));
                }
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.show();
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
        if(!csdl.KiemTraNhanVat(this)){
            int ruby = csdl.HienThongTinNhanVat().getRuby();
            tien.setText(ruby+"");
            hightcore.setText("Câu hỏi: "+ csdl.HienThongTinNhanVat().getLevel());
            name.setText(csdl.HienThongTinNhanVat().getName());
            String fileAvt = "avt"+String.valueOf(csdl.HienThongTinNhanVat().getAvt_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
            int resId = getResources().getIdentifier(fileAvt, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
            String fileKhung = "khung"+String.valueOf(csdl.HienThongTinNhanVat().getKhung_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
            int resId2 = getResources().getIdentifier(fileKhung, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh

            if (resId != 0) {
                avt.setImageResource(resId); // Thiết lập hình ảnh cho ImageView
            } else {
                // Xử lý trường hợp không tìm thấy tệp ảnh
            }
            if (resId2 != 0) {
                avt.setBackgroundResource(resId2); // Thiết lập hình ảnh cho ImageView
            } else {
                // Xử lý trường hợp không tìm thấy tệp ảnh
            }
        }

    }
}