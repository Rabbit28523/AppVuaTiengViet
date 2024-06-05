package com.vtv.vuatiengviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameShowRound2 extends AppCompatActivity implements ItemCauHoiClick,ItemCauTraLoiClick{
    String[] caDaoTucNgu = {
            "công", "cha", "như", "núi", "Thái", "Sơn", "nghĩa", "mẹ", "như", "nước",
            "trong", "nguồn", "chảy", "ra", "một", "cây", "làm", "chẳng", "nên", "non",
            "ba", "cây", "chụm", "lại", "nên", "hòn", "núi", "cao", "bầu", "ơi",
            "thương", "lấy", "bí", "cùng", "tuy", "rằng", "khác", "giống", "nhưng", "chung",
            "một", "giàn", "ăn", "quả", "nhớ", "kẻ", "trồng", "cây", "uống", "nước",
            "nhớ", "nguồn", "tốt", "gỗ", "hơn", "tốt", "nước", "sơn", "gần", "mực",
            "thì", "đen", "gần", "đèn", "thì", "sáng", "có", "công", "mài", "sắt",
            "có", "ngày", "nên", "kim", "chim", "khôn", "kêu", "tiếng", "rảnh", "ranh",
            "người", "khôn", "nói", "tiếng", "dịu", "dàng", "đói", "cho", "sạch", "rách",
            "cho", "thơm", "đêm", "đến", "gió", "ngày", "trưa", "sự", "học", "một",
            "khác", "chi", "thuyền", "trôi", "biển", "khơi", "nhớ", "mẹ", "ruột", "nghèo",
            "mà", "thắm", "người", "trên", "kính", "dưới", "nhường", "công", "danh", "phú",
            "quý", "làm", "chi", "dạ", "đã", "lạnh", "thì", "lòng", "chẳng", "ấm",
            "ông", "bà", "cha", "mẹ", "công", "cha", "nghĩa", "mẹ", "chẳng", "quên",
            "ai", "có", "đức", "mới", "thành", "công", "đời", "cây", "đắng", "kết",
            "quả", "ngọt", "trời", "cho", "chẳng", "bằng", "cha", "mẹ", "dạy", "giàu",
            "vì", "bạn", "sang", "vì", "vợ", "một", "nắm", "gạo", "cơm", "một", "đốt",
            "đèn", "cũng", "sáng", "nhà", "dưới", "mái", "chèo", "lên", "đời", "mẹ",
            "cha", "công", "ơn", "sinh", "dưỡng", "nhất", "tự", "vi", "sư", "bán",
            "tự", "vi", "sư", "bách", "chiến", "bách", "thắng", "không", "bằng", "một",
            "lần", "biết", "cách", "cư", "xử"
    };
    List<CaDao> list_cadao;
    List<LoiChinhTa> list_cauhoi;
    CSDL csdl;
    TextView tym,backr2;
    ImageView layout_2;
    AppCompatButton h1,h2,h3,h4,h5,h6;
    RecyclerView dapan, cautraloi;
    MediaPlayer mp, mp1;
    static boolean nhacXB ;
    static boolean nhacback;
    static float volumn1,volumn2;
    static SharedPreferences prefs;
    Round2_dapanAdapter dapanAdapter;
    Round2_cauhoiAdapter cauhoiAdapter;
    static boolean[] traloidung={false,false,false,false,false,false};
    static boolean[] datraloi={false,false,false,false,false,false};
    CaDao cau_cuoi;
    String[] list_DapAn;
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
        setContentView(R.layout.activity_game_show_round2);
        csdl=new CSDL(GameShowRound2.this);
        csdl.taoCSDLround2CaDao();
        csdl.taoCSDLround2ChinhTa();
        list_cadao=csdl.getCauHoiRound2(1);
        list_cauhoi=csdl.getLoiCTRound2(6);
        tym=findViewById(R.id.tym);
        backr2=findViewById(R.id.backr2);
        h1=findViewById(R.id.smallImage1);
        h2=findViewById(R.id.smallImage2);
        h3=findViewById(R.id.smallImage3);
        h4=findViewById(R.id.smallImage4);
        h5=findViewById(R.id.smallImage5);
        h6=findViewById(R.id.smallImage6);
        cautraloi=findViewById(R.id.listcauhoi);
        layout_2=findViewById(R.id.layout_2);
        dapan=findViewById(R.id.dapan);
        backr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBack();
            }
        });

        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCau(list_cauhoi.get(0),1);
            }
        });
        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCau(list_cauhoi.get(2),3);
            }
        });
        h5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCau(list_cauhoi.get(4),5);
            }
        });

        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCau(list_cauhoi.get(1),2);
            }
        });
        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCau(list_cauhoi.get(3),4);
            }
        });
        h6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCau(list_cauhoi.get(5),6);
            }
        });
        prefs= getSharedPreferences("game", MODE_PRIVATE);
        nhacback = prefs.getBoolean("isMute", true);
        nhacXB = prefs.getBoolean("isXB", true);
        volumn1=prefs.getFloat("volumnBack",1);
        volumn2=prefs.getFloat("volumnXB",1);
        loadCaDao();
    }
    private void showDialogCau(LoiChinhTa cauHoi,int id) {
        round2_loichinhta.cauHoi=cauHoi;
        round2_loichinhta.id=id;
        if(traloidung[id-1]){
            Toast.makeText(this, "Bạn đã trả lời câu này", Toast.LENGTH_SHORT).show();
        } else if (datraloi[id-1]) {
            Toast.makeText(this, "Bạn đã trả lời sai câu này", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(GameShowRound2.this, round2_loichinhta.class));
        }
    }



    private void showDialogBack() {
        Dialog dialog = new Dialog(GameShowRound2.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_dunggame);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        Button chapnhan=dialog.findViewById(R.id.chapnhan);
        Button tuchoi=dialog.findViewById(R.id.tuchoi);
        prefs= getSharedPreferences("game", MODE_PRIVATE);
        nhacXB = prefs.getBoolean("isXB", true);
        volumn1=prefs.getFloat("volumnBack",1);
        volumn2=prefs.getFloat("volumnXB",1);
        mp=new MediaPlayer();
        loadCaDao();
        ktraAmthanh();
        chapnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                GameShowRound2.this.finish();
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
    //chơi với ca dao
    //chọn 4 câu ca dao tục ngữ ở mảnh ghép 1,2,3 và cuối cùng
    // chọn 3 câu ở ghép từ bình thường

    // hiện câu ca dao cuối

    String DapAn="";
    String[] list_tu,list_cautraloi;
    FlexboxLayoutManager layoutManager,layoutManager2;
    private void ktraAmthanh() {
        mp=MediaPlayer.create(GameShowRound2.this,R.raw.quizstarts);
        mp.setVolume(MainActivity.volumn1,MainActivity.volumn1);
        mp.start();

    }
    public void loadCaDao(){
        String fileName = list_cadao.get(0).getHinhAnh().toString();
        int resId = getResources().getIdentifier(fileName, "drawable", getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
        if (resId != 0) {
            layout_2.setImageResource(resId);
            layout_2.setBackgroundColor(Color.WHITE);// Thiết lập hình ảnh cho ImageView
        } else {
            // Xử lý trường hợp không tìm thấy tệp ảnh
        }
        DapAn= list_cadao.get(0).getDapAn();

        list_DapAn = DapAn.split(" ");
        list_cautraloi=new String[list_DapAn.length];
        Arrays.fill(list_cautraloi,"1");
        vitrioDapAn=new int[list_DapAn.length];
        Arrays.fill(vitrioDapAn, -1);
        // Sử dụng Set để đảm bảo không có từ nào bị trùng
        Set<String> randomWordsSet = new HashSet<>();
        Random random = new Random();

        // Lấy ngẫu nhiên 5 từ từ mảng caDaoTucNgu
        while (randomWordsSet.size() < 5) {
            int randomIndex = random.nextInt(caDaoTucNgu.length);
            randomWordsSet.add(caDaoTucNgu[randomIndex]);
        }
        // Chuyển Set thành mảng
        String[] randomWordsArray = randomWordsSet.toArray(new String[0]);
        // Tạo mảng mới với kích thước đủ lớn để chứa cả list_DapAn và randomWordsArray
        list_tu= new String[list_DapAn.length + randomWordsArray.length];
        // Sao chép các từ từ list_DapAn vào mảng mới
        System.arraycopy(list_DapAn, 0, list_tu, 0, list_DapAn.length);
        // Sao chép các từ ngẫu nhiên vào mảng mới
        System.arraycopy(randomWordsArray, 0, list_tu, list_DapAn.length, randomWordsArray.length);
        List<String> list = Arrays.asList(list_tu);

        // Xáo trộn danh sách
        Collections.shuffle(list);

        // Chuyển danh sách trở lại mảng
        list.toArray(list_tu);

        cauhoiAdapter = new Round2_cauhoiAdapter(this, list_cautraloi,this);
        dapanAdapter = new Round2_dapanAdapter(this, list_tu,this);
        layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        layoutManager2 = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);


        cautraloi.setLayoutManager(layoutManager);
        dapan.setLayoutManager(layoutManager2);
        cautraloi.setAdapter(cauhoiAdapter);
        dapan.setAdapter(dapanAdapter);

    }
    MediaPlayer mp3;
    @Override
    public void CauHoiClick(int position) {
        mp3=new MediaPlayer();
        mp3=MediaPlayer.create(GameShowRound2.this,R.raw.loaded);
        mp3.setVolume(MainActivity.volumn2,MainActivity.volumn2);
        mp3.start();
        String s = list_cautraloi[position].toUpperCase();
        if(s.trim().length()>0 &&s!=""&&s!=null && s!="1"){
            // set vị trí đó trở thành ""
            list_cautraloi[position]="1";

            // set lại vị trí cữ của từ đó vào arr2
            // vị trí cũ đã được lưu ở mảng vitriodapan
//            c[position]="";
            list_tu[vitrioDapAn[position]]=s;
            // vị trí đó ở mảng vitriodapan -> =1 (-1: chưa có ký tự; >-1: đã có ký tự)
            vitrioDapAn[position]=-1;

            //set lại adapter
            cautraloi.setAdapter(new Round2_cauhoiAdapter(this, list_cautraloi, this));
            dapan.setAdapter(new Round2_dapanAdapter(this, list_tu, this));
        }
    }
    int[] vitrioDapAn;
    @Override
    public void CauTraLoiClick(int position) {
        mp3 = new MediaPlayer();
        mp3=MediaPlayer.create(GameShowRound2.this,R.raw.loaded);
        mp3.setVolume(MainActivity.volumn2,MainActivity.volumn2);
        mp3.start();
        int dem = 0;
        // kiểm tra xem đã có bao nhiêu ký tự trong câu trả lời của người chơi
        for (int i = 0; i < list_cautraloi.length; i++) {
            if (list_cautraloi[i].toUpperCase() != "1" && !list_cautraloi[i].trim().isEmpty()) {
                dem++;
            }
        }

        String s = list_tu[position].toUpperCase();
        // nếu chọn ký tự trong listdapan !="" và biến dem < listcauhoi.size()
        if (s.trim().length() > 0 && s != "" && s != null && dem < vitrioDapAn.length) {

            //set vị trí đó trong arr2 => ""
            list_tu[position] = "";
            // biến dùng để chỉ set myAnswer 1 lần
            boolean foundNegativeIndex = false;
            //lập vòng for vitriodapan
            for (int j = 0; j < vitrioDapAn.length; j++) {

                // nếu foundNegativeIndex=false và có ký tự "" trong cautraloi
                if (!foundNegativeIndex && vitrioDapAn[j] < 0) {
                    // Nếu chưa tìm thấy phần tử âm và vitrioDapAn[j] nhỏ hơn 0, cập nhật arr và đánh dấu đã tìm thấy


                    if (vitrioDapAn[j] == -1) {
                        vitrioDapAn[j] = position;
                        foundNegativeIndex = true;
                        list_cautraloi[j] = s;


                    }


                }


            }
            KiemTraDapAn();


//            adap.notifyDataSetChanged();
//            adap.notifyDataSetChanged();
//            listcauhoi.setLayoutManager(layoutManager);
            cautraloi.setAdapter(new Round2_cauhoiAdapter(this, list_cautraloi, this));
            dapan.setAdapter(new Round2_dapanAdapter(this, list_tu, this));
        }
    }
    private void KiemTraDapAn(){
        int dem=0;
        // kiểm tra xem đã có bao nhiêu ký tự trong câu trả lời của người chơi
        for(int i=0;i<list_cautraloi.length;i++){
            if(list_cautraloi[i].toUpperCase()!="1" && !list_cautraloi[i].trim().isEmpty()){
                dem++;
            }
        }
        if(dem>=list_DapAn.length){
            boolean areEqual1 = arraysEqualIgnoreCase(list_DapAn, list_cautraloi);
            if(areEqual1){
                if(nhacXB){

                    mp1=MediaPlayer.create(GameShowRound2.this,R.raw.congrates_3);
                    mp1.setVolume(volumn2, volumn2);
                    mp1.start();


                    mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            showDialogEndRoud2();

                        }
                    });


                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GameShowRound2.this, "Đáp án hoàn toàn chính xác", Toast.LENGTH_SHORT).show();
                            showDialogEndRoud2();

                        }
                    }, 1000);
                }
            }
            else {
                tymm-=1;
                if(nhacXB){

                    mp1=MediaPlayer.create(GameShowRound2.this,R.raw.mixkitclickerror1110);
                    mp1.setVolume(volumn2, volumn2);
                    mp1.start();


                    mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            tym.setText((tymm) + " ❤");

                        }
                    });

                }
                else {
                    tym.setText((tymm) + " ❤");

                    Toast.makeText(this, "Đáp án chưa chính xác", Toast.LENGTH_SHORT).show();


                }
            }
        }
        if(tymm==0){
            showDialogEndRoud2();
        }
    }
    static int diemR2=0;
    private void showDialogEndRoud2() {
        Dialog dialog = new Dialog(GameShowRound2.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_game_end_round1);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        ImageView ketthuc=dialog.findViewById(R.id.lydoend);
        ImageView round=dialog.findViewById(R.id.round);
        round.setImageResource(R.drawable.tvround2);
        ImageView choi=dialog.findViewById(R.id.choi);
        TextView diemtv=dialog.findViewById(R.id.diem);
        Button btnHuy=dialog.findViewById(R.id.choilai);
        Button btnchoitiep=dialog.findViewById(R.id.choitiep);
        btnHuy.setBackgroundResource(R.drawable.butthoatlick);
        ketthuc.setImageResource(R.drawable.ketthuc);
        diemtv.setText("Dừng cuộc chơi + 15 ruby");
        if(tymm>0){
            choi.setImageResource(R.drawable.bn_tiep);
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    GameShowRound2.this.finish();
                    csdl.UpdateRuby(15);
                }
            });
            btnchoitiep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    diemR2=10;
                    startActivity(new Intent(GameShowRound2.this, GameShowRound3.class));
                    GameShowRound2.this.finish();
                }
            });
        }
        else {
            choi.setImageResource(R.drawable.tvtiep);
            btnHuy.setBackgroundResource(R.drawable.butthoatlick);
            btnchoitiep.setBackgroundResource(R.drawable.choingayclick);
            diemtv.setText("Chơi lại + 5 ruby");
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    GameShowRound2.this.finish();
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

        dialog.show();
    }

    int tymm=2;
    public static boolean arraysEqualIgnoreCase(String[] array1, String[] array2) {
        // Kiểm tra nếu hai mảng có chiều dài khác nhau
        if (array1.length != array2.length) {
            return false;
        }

        // So sánh từng phần tử không phân biệt chữ hoa và chữ thường
        for (int i = 0; i < array1.length; i++) {
            if (!array1[i].equalsIgnoreCase(array2[i])) {
                return false;
            }
        }

        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mp != null && !mp.isPlaying() && nhacback) {
            mp.start();
        }
        if (mp1 != null && !mp1.isPlaying() && nhacXB) {
            mp1.start();

        }
        List<AppCompatButton> buttonList = new ArrayList<>();
        buttonList.add(h1);
        buttonList.add(h2);
        buttonList.add(h3);
        buttonList.add(h4);
        buttonList.add(h5);
        buttonList.add(h6);
        for(int i=0;i<traloidung.length;i++){
            if(traloidung[i]){
                buttonList.get(i).setBackgroundResource(R.drawable.transparent_color);
                buttonList.get(i).setText("");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Kiểm tra nếu audio đang phát
        if (mp != null && mp.isPlaying()&& nhacback) {
            // Tạm dừng audio
            mp.pause();
        }
        if (mp1 != null && mp1.isPlaying() && nhacXB) {
            // Tạm dừng audio
            mp1.pause();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Kiểm tra nếu audio đang phát
        if (mp != null && mp.isPlaying()&& nhacback) {
            // Tạm dừng audio
            mp.pause();
        }
        if (mp1 != null && mp1.isPlaying() && nhacXB) {
            // Tạm dừng audio
            mp1.pause();
        }
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }
}
