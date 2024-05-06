package com.example.appvuatiengviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.rewarded.RewardItem;

import java.util.ArrayList;

public class cuahang extends AppCompatActivity implements ItemCauHoiClick,ItemCauTraLoiClick{

    CSDL csdl;
    SanPham_avt_Adapter adapter_avt;
    SanPham_khung_Adapter adapter_khung;
    ArrayList<SanPham> dsAVT,dsKhung;
    RecyclerView recyclerView_avt,recyclerView_khung;
    ImageView avt;
    TextView hightcore,name,ruby;
    ImageButton doiten;
    ThongTinNguoiChoi tt;
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
        ScrollView scrollView = findViewById(R.id.nestedScrollView);

        recyclerView_avt=findViewById(R.id.dsAVT);
        recyclerView_khung=findViewById(R.id.dsKhung);

        hightcore=findViewById(R.id.hightCore);
        name=findViewById(R.id.tvnameNG);
        avt=findViewById(R.id.avt1);
        ruby=findViewById(R.id.ruby);
        doiten=findViewById(R.id.btndoiten);

        // Scroll to top

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Avatar");
        tabSpec1.setIndicator(Html.fromHtml("<font color='#000000'>Avatar</font>"));
        tabSpec1.setContent(R.id.Avatar); // Liên kết với LinearLayout chứa ImageView "Avatar"

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Khung");
        tabSpec2.setIndicator(Html.fromHtml("<font color='#000000'>Khung</font>")); // Set text color to black
        tabSpec2.setContent(R.id.Khung); // Liên kết với LinearLayout chứa ImageView "Khung"
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                // Reset the text style for all tabs
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    TextView tabTextView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tabTextView.setTypeface(null, Typeface.NORMAL);
                }

                // Get the selected tab view and make its text bold
                TextView selectedTabTextView = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title);
                selectedTabTextView.setTypeface(null, Typeface.BOLD);

//                scrollView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 000);
            }
        });
        tabHost.addTab(tabSpec1);
        tabHost.addTab(tabSpec2);
        csdl=new CSDL(this);
        tt=csdl.HienThongTinNhanVat();
        CapNhatDuLieu();
//        hightcore.setText("Hight core: "+ tt.getLevel());
//        name.setText("Name: "+tt.getName());

        doiten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogDoiTen();
            }
        });
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }

    @Override
    public void CauHoiClick(int position) {
        if(dsKhung.get(position).getTinhtrang()==1){
            csdl.SuaThongTinNhanVat(csdl.HienThongTinNhanVat().getName(),csdl.HienThongTinNhanVat().getAvt_id(),dsKhung.get(position).getId());
            CapNhatDuLieu();
        }
        if(dsKhung.get(position).getTinhtrang()==0){
            if(csdl.HienThongTinNhanVat().getRuby()>=dsKhung.get(position).getPrice()){
                csdl.UpdateSanPham("khung",dsKhung.get(position).getId());
                csdl.UpdateRuby(cuahang.this,-dsKhung.get(position).getPrice());
                CapNhatDuLieu();
            }
            else {
                Toast.makeText(this, "Bạn không đủ ruby để mua vật phẩm này", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void ShowDialogDoiTen() {
        Dialog dialog = new Dialog(cuahang.this,android.R.style.Theme_Dialog );

        dialog.setContentView(R.layout.dialog_doiten);
        EditText tvname=dialog.findViewById(R.id.tvname);
        ImageButton btnXN=dialog.findViewById(R.id.btnname);

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
                        csdl.UpdateRuby(cuahang.this,-10);
                        CapNhatDuLieu();
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

    private void CapNhatDuLieu() {
        tt=csdl.HienThongTinNhanVat();
        hightcore.setText("High score: "+ tt.getLevel());
        name.setText("Name: "+tt.getName());
        ruby.setText("Ruby: "+csdl.HienThongTinNhanVat().getRuby());
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
        dsAVT=csdl.HienDS_AVT();
        dsKhung=csdl.HienDS_Khung();
        adapter_avt=new SanPham_avt_Adapter(this,dsAVT,this);
        adapter_khung=new SanPham_khung_Adapter(this,dsKhung,this);
        GridLayoutManager layoutManager1 = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        recyclerView_avt.setLayoutManager(layoutManager1);
        recyclerView_avt.setAdapter(adapter_avt);
        recyclerView_khung.setLayoutManager(layoutManager2);
        recyclerView_khung.setAdapter(adapter_khung);

    }
    @Override
    public void CauTraLoiClick(int position) {
        if(dsAVT.get(position).getTinhtrang()==1){
            csdl.SuaThongTinNhanVat(csdl.HienThongTinNhanVat().getName(),dsAVT.get(position).getId(),csdl.HienThongTinNhanVat().getKhung_id());
            CapNhatDuLieu();
        }
        if(dsAVT.get(position).getTinhtrang()==0){
            if(csdl.HienThongTinNhanVat().getRuby()>=dsAVT.get(position).getPrice()){
                csdl.UpdateSanPham("avt",dsAVT.get(position).getId());
                csdl.UpdateRuby(cuahang.this,-dsAVT.get(position).getPrice());
                CapNhatDuLieu();
            }
            else {
                Toast.makeText(this, "Bạn không đủ ruby để mua vật phẩm này", Toast.LENGTH_SHORT).show();
            }
        }
    }
}