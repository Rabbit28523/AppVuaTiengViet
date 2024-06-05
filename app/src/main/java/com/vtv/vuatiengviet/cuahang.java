package com.vtv.vuatiengviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.material.tabs.TabLayout;
import com.vtv.vuatiengviet.R;

import java.util.ArrayList;

public class cuahang extends AppCompatActivity implements ItemCauHoiClick,ItemCauTraLoiClick{

    static CSDL csdl;
    SanPham_avt_Adapter adapter_avt;
    SanPham_khung_Adapter adapter_khung;
    ArrayList<SanPham> dsAVT,dsKhung;
    RecyclerView recyclerView_avt,recyclerView_khung;
    static ImageView avt;
    static TextView hightcore;
    static TextView name;
    static TextView ruby;
    Button doiten;
    static ThongTinNguoiChoi tt;
    private CategoryPagerAdapter adapter;
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
        setContentView(R.layout.activity_cuahang);


        hightcore=findViewById(R.id.hightCore);
        name=findViewById(R.id.tvnameNG);
        avt=findViewById(R.id.avt1);
        ruby=findViewById(R.id.ruby);
        doiten=findViewById(R.id.btndoiten);
        ViewPager viewPager = findViewById(R.id.vp_shop_viewpager);
        TabLayout tabLayout = findViewById(R.id.tl_shop_tablayout);
        tabLayout.setTabTextColors(Color.GRAY, Color.BLACK);
        adapter = new CategoryPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentAvatar(), "Avatar");
        adapter.addFragment(new FragmentKhung(), "Khung");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        csdl=new CSDL(this);
        tt=csdl.HienThongTinNhanVat();
        CapNhatDuLieu(cuahang.this);
//        hightcore.setText("Hight core: "+ tt.getLevel());
//        name.setText("Name: "+tt.getName());

        doiten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogDoiTen();
            }
        });
//        csdl.TaoCSDL(this);



    }
    private void ShowDialogDoiTen() {
        Dialog dialog = new Dialog(cuahang.this,android.R.style.Theme_Dialog );

        dialog.setContentView(R.layout.dialog_doiten);
        EditText tvname=dialog.findViewById(R.id.tvname);
        Button btnXN=dialog.findViewById(R.id.btnname);
        TextView cham=dialog.findViewById(R.id.cham);
        Animation blinkk= AnimationUtils.loadAnimation(this,R.anim.blink2);
        cham.setAnimation(blinkk);
//        dialog.setCancelable(false);
        cham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
//        cham.setVisibility(View.GONE);
        btnXN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvname.getText().toString().trim().equals("")){
                    Toast.makeText(cuahang.this, "Tên nhân vật vẫn giữ nguyên", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(csdl.HienThongTinNhanVat().getRuby()>=10){
                        csdl.SuaThongTinNhanVat(tvname.getText().toString(),csdl.HienThongTinNhanVat().getAvt_id(),csdl.HienThongTinNhanVat().getKhung_id());
                        csdl.UpdateRuby(-10);
                        CapNhatDuLieu(cuahang.this);
                        Toast.makeText(cuahang.this, "Bạn đã đổi tên thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(cuahang.this, "Bạn không đủ ruby để đổi tên", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.show();
    }


    public static void CapNhatDuLieu(Context context) {
        tt=csdl.HienThongTinNhanVat();
        hightcore.setText("High score: "+ tt.getLevel());
        name.setText("Name: "+tt.getName());
        ruby.setText("Ruby: "+csdl.HienThongTinNhanVat().getRuby());
        String fileAvt = "avt"+String.valueOf(tt.getAvt_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
        int resId = context.getResources().getIdentifier(fileAvt, "drawable", context.getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
        String fileKhung = "khung"+String.valueOf(tt.getKhung_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
        int resId2 = context.getResources().getIdentifier(fileKhung, "drawable", context.getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh

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

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }

    @Override
    public void CauHoiClick(int position) {
        if(dsAVT.get(position).getTinhtrang()==1){
            csdl.SuaThongTinNhanVat(csdl.HienThongTinNhanVat().getName(),dsAVT.get(position).getId(),csdl.HienThongTinNhanVat().getKhung_id());
            CapNhatDuLieu(cuahang.this);
        }
        if(dsAVT.get(position).getTinhtrang()==0){
            if(csdl.HienThongTinNhanVat().getRuby()>=dsAVT.get(position).getPrice()){
                csdl.UpdateSanPham("avt",dsAVT.get(position).getId());
                csdl.UpdateRuby(-dsAVT.get(position).getPrice());
                CapNhatDuLieu(cuahang.this);
            }
            else {
                Toast.makeText(this, "Bạn không đủ ruby để mua vật phẩm này", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void CauTraLoiClick(int position) {
        if(dsKhung.get(position).getTinhtrang()==1){
            csdl.SuaThongTinNhanVat(csdl.HienThongTinNhanVat().getName(),csdl.HienThongTinNhanVat().getAvt_id(),dsKhung.get(position).getId());
            CapNhatDuLieu(cuahang.this);
        }
        if(dsKhung.get(position).getTinhtrang()==0){
            if(csdl.HienThongTinNhanVat().getRuby()>=dsKhung.get(position).getPrice()){
                csdl.UpdateSanPham("khung",dsKhung.get(position).getId());
                csdl.UpdateRuby(-dsKhung.get(position).getPrice());
                CapNhatDuLieu(cuahang.this);
            }
            else {
                Toast.makeText(this, "Bạn không đủ ruby để mua vật phẩm này", Toast.LENGTH_SHORT).show();
            }
        }
    }
}