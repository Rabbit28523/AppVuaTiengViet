package com.vtv.appvuatiengviet;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginGG extends AppCompatActivity {
    private CSDL database;
    public static int RC_DEFAULT_SIGN_IN = 100;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;
    Button btnSignIn, btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ẩn thanh tiêu đề
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Đặt cờ cho cửa sổ
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar();
        setContentView(R.layout.activity_login_gg);
        database = new CSDL(this);

        // Ánh xạ các thành phần trong layout
        btnSignIn = findViewById(R.id.login);
        btnSignOut = findViewById(R.id.dangxuat);
        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        gsc=GoogleSignIn.getClient(this,gso);

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
        if (currentUser != null) {
            // Người dùng đã đăng nhập
            Toast.makeText(this, "UserId: " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            // Người dùng chưa đăng nhập
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.TaoCSDL(LoginGG.this);
                database.TaoNhanVat("Foxie");
                signIn1();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
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
                startActivity(new Intent(LoginGG.this,LeaderboardActivity.class));
                btnSignIn.setVisibility(View.GONE);
                btnSignOut.setVisibility(View.VISIBLE);
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth1(String idToken) {
        AuthCredential credential=GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user =auth.getCurrentUser();
                            ThongTinNguoiChoi thongTinNguoiChoi = database.HienThongTinNhanVat();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("name", thongTinNguoiChoi.getName());
                            map.put("ruby", thongTinNguoiChoi.getRuby());
                            map.put("level", thongTinNguoiChoi.getLevel());
                            map.put("avt_id", thongTinNguoiChoi.getAvt_id());
                            map.put("khung_id", thongTinNguoiChoi.getKhung_id());

                            // Kiểm tra xem thông tin người dùng đã tồn tại chưa
                            firebaseDatabase.getReference().child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(LoginGG.this, "Old player", Toast.LENGTH_SHORT).show();
                                        for (int i = 0; i < 20; i++) {
                                            firebaseDatabase.getReference().child("users").child(user.getUid() + i)
                                                    .setValue(map);
                                        }
                                    } else {
                                        Toast.makeText(LoginGG.this, "New player", Toast.LENGTH_SHORT).show();
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
    }

    // Hàm ẩn thanh công cụ navigation bar
    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }



    // Hàm xác thực Firebase Authentication bằng token được cung cấp bởi Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginGG.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    // Hàm cập nhật giao diện người dùng sau khi đăng nhập
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            ThongTinNguoiChoi thongTinNguoiChoi = database.HienThongTinNhanVat();
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", user.getUid());
            map.put("name", thongTinNguoiChoi.getName());
            map.put("ruby", thongTinNguoiChoi.getRuby());
            map.put("level", thongTinNguoiChoi.getLevel());
            map.put("avt_id", thongTinNguoiChoi.getAvt_id());
            map.put("khung_id", thongTinNguoiChoi.getKhung_id());

            // Kiểm tra xem thông tin người dùng đã tồn tại chưa
            firebaseDatabase.getReference().child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(LoginGG.this, "Old player", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginGG.this, "New player", Toast.LENGTH_SHORT).show();
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
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }
    }

    //     Hàm đăng xuất
    private void signOut() {
        auth.signOut();
        gsc.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(LoginGG.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        });
        // Xóa dữ liệu của csdl và tạo lại
//        database.recreateDatabase();
    }
}