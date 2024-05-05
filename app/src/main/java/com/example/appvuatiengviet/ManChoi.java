package com.example.appvuatiengviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManChoi extends AppCompatActivity implements ItemCauHoiClick, ItemCauTraLoiClick, OnUserEarnedRewardListener {
    ArrayList<String> listcauhoi;
    ArrayList<String> listdapan;
    ArrayList<String> myAnswer;
    List<Integer> trogiup;
    ArrayList<Integer> vitrioDapAn;
    CauHoiAdapter cauHoiAdapter;
    CauTraLoiAdapter adapter;
    CSDL csdl;
    TextView cauhoi;
    RecyclerView ochu,cautraloi;
    CauHoi cauHoi;
    TextView ruby,sttcauhoi;
    ImageView shop,back,help;
    LinearLayout lele;
    MediaPlayer mediaPlayer,mediaPlayer2;

    LinearLayout adContainerView;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private InterstitialAd mInterstitialAd;
    private String TAG = "ManChoi";
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
        setContentView(R.layout.activity_man_choi);
        cauhoi = findViewById(R.id.cauhoi);
        ochu = findViewById(R.id.ochu);
        cautraloi = findViewById(R.id.cautraloi);
        csdl = new CSDL(ManChoi.this);
        lele=findViewById(R.id.lele);
        shop=findViewById(R.id.shop);
        help=findViewById(R.id.trogiup);
        adContainerView = findViewById(R.id.layoutAd);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadAd();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });


        loadBanner();

        mediaPlayer=new MediaPlayer();
        mediaPlayer2=new MediaPlayer();
        mediaPlayer2=MediaPlayer.create(ManChoi.this,R.raw.click_003);
        mediaPlayer2.setVolume(MainActivity.volumn2,MainActivity.volumn2);
        if(MainActivity.NhacNen){
            mediaPlayer=MediaPlayer.create(ManChoi.this,R.raw.quizstarts);
            mediaPlayer.setVolume(MainActivity.volumn1,MainActivity.volumn1);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
                showDialogTroGiup();
                //nếu ng chơi có ít nhất 50 kim cương


            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
                showDialogShop();
            }
        });
        LoadCauHoi();
    }
    TextView tien;
    private void showDialogShop() {
        Dialog dialog=new Dialog(ManChoi.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.cuahang);
        tien=dialog.findViewById(R.id.tien);
        ImageView video_ruby=dialog.findViewById(R.id.video_ruby);
        Button ruby_20k=dialog.findViewById(R.id.ruby_20k);
        Button ruby_50k=dialog.findViewById(R.id.ruby_50k);
        Button ruby_100k=dialog.findViewById(R.id.ruby_100k);
        TextView cham=dialog.findViewById(R.id.cham);
        Animation blink=AnimationUtils.loadAnimation(ManChoi.this,R.anim.blink2);
        cham.setAnimation(blink);
        cham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tien.setText(String.valueOf(csdl.HienRuby(ManChoi.this)));
        video_ruby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rewardedInterstitialAd != null) {
                    rewardedInterstitialAd.show((Activity) ManChoi.this,ManChoi.this);
                }

            }
        });

        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }

    private void showDialogTroGiup() {
        Dialog dialog=new Dialog(ManChoi.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_trogiup);
        Button xacnhan=dialog.findViewById(R.id.xacnhan);
        Button tuchoi=dialog.findViewById(R.id.huy);
        RelativeLayout nenchiase=dialog.findViewById(R.id.nenchiase);
        TextView cham=dialog.findViewById(R.id.cham);
        Animation blink= AnimationUtils.loadAnimation(ManChoi.this,R.anim.blink2);
        cham.setAnimation(blink);

        cham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        nenchiase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();
                Bitmap b=takescreenshotOfRootView(lele);
                File savedFile = saveBitmapToFile(b);
                if (savedFile != null) {
                    Toast.makeText(ManChoi.this, "Đã chụp màn hình để chia sẻ", Toast.LENGTH_SHORT).show();
                    try {
                        savedFile.setReadable(true,false);

                        //Đây là hành động của Intent được sử dụng để chia sẻ nội dung.
                        // Trong trường hợp này, nó được sử dụng để chia sẻ một file ảnh.
                        final Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        //Đây là cách để nhận một Uri cho một file từ một FileProvider.
                        // Điều này cần thiết khi chia sẻ file với ứng dụng khác trên Android Nougat (API level 24) trở lên.
                        Uri uri= FileProvider.getUriForFile(getApplicationContext(),getApplication().getPackageName()+".provider",savedFile);
                        intent.putExtra(Intent.EXTRA_STREAM,uri);

                        //ây là cờ được sử dụng để cho phép ứng dụng mà Intent được gửi tới đọc dữ liệu từ Uri đã được cung cấp.
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("image/*");
                        dialog.dismiss();

                        startActivity(Intent.createChooser(intent,"Share to..."));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(ManChoi.this, "Failed to save screenshot", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tuchoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(csdl.HienRuby(ManChoi.this)>=50){


                            // Xử lý khi người dùng nhấn nút xác nhận
                            // Thêm code xử lý ở đây
                            String dapan1="";
                            // đáp án 1 là đáp án của câu hỏi nhưng mất dấu " "
                            for (int i = 0; i < cauHoi.getDapAn().length(); i++) {
                                char currentChar = cauHoi.getDapAn().charAt(i);
                                if (currentChar != ' ') {
                                    dapan1+=String.valueOf(currentChar);

                                }
                            }
                            System.out.println(dapan1);
                            // lập vòng for các từ trong đáp án (dapan1.lenght())

                            for(int i=0;i<dapan1.length();i++){
                                // nếu các từ trong đáp án của ng chơi trùng với đáp án của câu hỏi
                                // set vị trí đó trong mảng trợ giúp thành 1 (đã trợ giúp)
                                if(String.valueOf(dapan1.charAt(i)).equalsIgnoreCase(String.valueOf(myAnswer.get(i)))
                                        && !String.valueOf(myAnswer.get(i)).trim().equals(""))
                                {
                                    trogiup.set(i, 1);
                                }
                                // nếu các từ trong đáp án của người chơi không trùng đáp án câu hỏi
                                // thì chuyển các ký tự sai xuống danh sách các từ trong listdapan
                                // các vị trí sai trong ô chữ  -> ""
                                // set lại adapter
                                else if(vitrioDapAn.get(i)!=-1){

                                    listdapan.set(vitrioDapAn.get(i),myAnswer.get(i));
                                    myAnswer.set(i,"");
                                    vitrioDapAn.set(i,-1);
                                    ochu.setAdapter( new CauHoiAdapter(ManChoi.this,myAnswer,ManChoi.this));
                                    cautraloi.setAdapter( new CauTraLoiAdapter(ManChoi.this,listdapan,ManChoi.this));
                                }

                            }
                            dialog.dismiss();
                            HienTroGiup();

                }
                else {
                    Toast.makeText(ManChoi.this, "Bạn không đủ 50 kim cương", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();

    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        Toast.makeText(this, "Nhận thưởng thành công", Toast.LENGTH_SHORT).show();
        csdl.UpdateRuby(ManChoi.this,10);
        loadAd();
        tien.setText(String.valueOf(csdl.HienRuby(ManChoi.this)));
        ruby.setText(String.valueOf(String.valueOf(csdl.HienRuby(ManChoi.this))));
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadBanner() {

        // Create a new ad view.
        AdView adView = new AdView(this);
        adView.setAdSize(getAdSize());
        adView.setAdUnitId("ca-app-pub-3940256099942544/9214589741");

        // Replace ad container with new ad view.
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    public void loadAd() {
        // Use the test ad unit ID to load an ad.
        rewardedInterstitialAd.load(ManChoi.this, "ca-app-pub-3940256099942544/5354046379",
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        Log.d(TAG, "Ad was loaded.");
                        rewardedInterstitialAd = ad;
                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        rewardedInterstitialAd = null;
                    }
                });
    }
    private void LoadCauHoi() {
        cauHoi = csdl.HienCSDL(ManChoi.this);
        cauhoi.setText(cauHoi.getTu());
        ruby=findViewById(R.id.ruby);
        sttcauhoi=findViewById(R.id.sttcauhoi);
        ruby.setText(String.valueOf(csdl.HienRuby(ManChoi.this)));
        if(cauHoi.getId()==-1){
            lele.setVisibility(View.GONE);
            Toast.makeText(this, "lll", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận");
            builder.setMessage("Bạn đã chơi hết các level, bạn có muốn chơi lại không?");
            builder.setCancelable(false);
            // Nút xác nhận
            builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Xử lý khi người dùng nhấn nút xác nhận
                    // Thêm code xử lý ở đây
                    csdl.ChoiLai(ManChoi.this);
                    lele.setVisibility(View.VISIBLE);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            sttcauhoi.setText(String.valueOf(cauHoi.getId()));
            listcauhoi = new ArrayList<>();
            listdapan = new ArrayList<>();
            myAnswer=new ArrayList<>();
            trogiup=new ArrayList<>();
            vitrioDapAn=new ArrayList<>();
            String trimmedString = cauHoi.getDapAn().trim();
            for (int i = 0; i < trimmedString.length(); i++) {
                char currentChar = trimmedString.charAt(i);
                if (currentChar != ' ') {
                    listcauhoi.add(String.valueOf(""));
                    listdapan.add(String.valueOf(currentChar));
                    trogiup.add(0);
                    myAnswer.add("");
                    vitrioDapAn.add(-1);
                }
            }
            Collections.shuffle(listdapan);
            adapter = new CauTraLoiAdapter(ManChoi.this,listdapan,this);
            FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(getApplicationContext());
            layoutManager1.setFlexDirection(FlexDirection.ROW);
            layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
            cautraloi.setLayoutManager(layoutManager1);
            cautraloi.setAdapter(adapter);
            cauHoiAdapter = new CauHoiAdapter(ManChoi.this,listcauhoi,this);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            ochu.setLayoutManager(layoutManager);
            ochu.setAdapter(cauHoiAdapter);

        }

    }
    public int KiemTraViTri_trogiup(){
        int position=-1;
        //lập vòng for trong mảng trợ giúp
        //nếu tìm thấy phần tử có giá trị =0 thì break và return ra vị trí của phần tử ấy
        for(int i=0;i<trogiup.size();i++){
            if (trogiup.get(i)==0){
                position=i;
                break;
            }
        }
        return position;
    }
    public int KiemTraViTri_dapAn(String ktu){
        int position=-1;
        // lập vòng for trong lisdapan
        // nếu tìm thấy phần tử có giá trị = ký tự thì break và return vị trí phần tử ấy
        for(int i=0;i<listdapan.size();i++){
            if (listdapan.get(i).equalsIgnoreCase(ktu)){
                position=i;
                break;
            }
        }
        return position;
    }
    private void HienTroGiup(){

        // kiểm tra vị trí trợ giúp
        //nếu KiemTraViTri_trogiup()>-1 (còn phần tử cần trợ giúp)
        if (KiemTraViTri_trogiup()>-1 ){
            // đáp án 1 là đáp án của câu hỏi nhưng mất dấu " "
            String dapan1="";
            for (int i = 0; i < cauHoi.getDapAn().length(); i++) {
                char currentChar = cauHoi.getDapAn().charAt(i);
                if (currentChar != ' ') {
                    dapan1+=String.valueOf(currentChar);

                }
            }
            // ktu là từ cần hiện khi nhấn trợ giúp
            String ktu= String.valueOf(dapan1.charAt( KiemTraViTri_trogiup()));

            // tìm vị trí của kyu ở trong listdapan
            int vitrioda=KiemTraViTri_trogiup();

            // set ktu đó vào đáp án của người chơi
            myAnswer.set(vitrioda,ktu);

            // set vị trí vừa trợ giúp trong mảng trợ giúp thành 1 (0- chưa trợ giúp; 1- đã trợ giúp)
            trogiup.set(vitrioda,1);

            // tìm vị trí của ktu trong listdapan
            int vittriDA=KiemTraViTri_dapAn(ktu);

            // set vị trí của từ đó trong list đáp án  vào mảng vị trí ở đáp án
            vitrioDapAn.set(vitrioda,vittriDA);

            System.out.println(ktu);
//            Toast.makeText(this, "vị trí: "+vittriDA, Toast.LENGTH_SHORT).show();

            //set vị trí ở trong listdapan =""
            listdapan.set(vittriDA,"");

            //set lại adapter
            cautraloi.setAdapter( new CauTraLoiAdapter(ManChoi.this,listdapan,ManChoi.this));
            ochu.setAdapter( new CauHoiAdapter(ManChoi.this,myAnswer,ManChoi.this));

            // trừ 50 kim cương và reload lại
            csdl.UpdateRuby(ManChoi.this,-50);
            ruby.setText(String.valueOf(csdl.HienRuby(ManChoi.this)));
            KiemTraDapAn();

        }
        else {
            Toast.makeText(ManChoi.this, "Ô chữ đã được hoành thành", Toast.LENGTH_SHORT).show();
        }
    }

    public static  Bitmap takescreenshot(View v){
        //Bật bộ đệm vẽ của View.
        // Khi được bật, View sẽ giữ một bản sao bitmap của nội dung hiện tại của nó trong bộ đệm vẽ.
        v.setDrawingCacheEnabled(true);

        //Xây dựng bộ đệm vẽ của View.
        // Điều này đảm bảo rằng bitmap được tạo ra sau đó sẽ là một bản sao của nội dung hiện tại của View.
        v.buildDrawingCache(true);

        // Tạo một bản sao của bitmap trong bộ đệm vẽ của View.
        Bitmap b=Bitmap.createBitmap(v.getDrawingCache());

        //Tắt bộ đệm vẽ của View, giải phóng bộ nhớ.
        v.setDrawingCacheEnabled(false);
        return b;
    }
    private static Bitmap takescreenshotOfRootView(View v){
        //Phương thức này nhận một View làm đối số và chụp ảnh của nó.
        return takescreenshot(v.getRootView());
    }
    public File saveBitmapToFile(Bitmap bitmap) {
        //khởi tạo biến để lưu đường dẫn ảnh
        String savedImagePath = null;
        //Tạo tên file ảnh mới dựa trên thời gian hiện tại, để đảm bảo tính duy nhất của tên file.
        String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        //Xác định thư mục lưu trữ bên ngoài để lưu file ảnh
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/YourAppName");

        // Create directory if it doesn't exist
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // Create the image file
        File imageFile = new File(storageDir, imageFileName);
        savedImagePath = imageFile.getAbsolutePath();

        try {
            // Save the image to storage
            //Mở một luồng ghi vào file ảnh
            FileOutputStream fos = new FileOutputStream(imageFile);

            //Nén bitmap và ghi dữ liệu nén vào luồng ghi.
            // Trong trường hợp này, định dạng JPEG được sử dụng với chất lượng 100 (tốt nhất).
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            //Đóng luồng ghi sau khi hoàn thành.
            fos.close();

            //Cập nhật cơ sở dữ liệu MediaStore để hiển thị ảnh mới trong Gallery của thiết bị.
            MediaStore.Images.Media.insertImage(getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), imageFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
            savedImagePath = null;
        }
        return imageFile;
    }



    @Override
    public void CauHoiClick(int position) {
        // lấy ra text của textview đã click
        String s=myAnswer.get(position).toString();
        // nếu s != ""
        if(s.trim().length()>0 &&s!=""&&s!=null ){

            // set vị trí đó trở thành ""
            myAnswer.set(position,"");

            // set lại vị trí cữ của từ đó vào lisdapan
            // vị trí cũ đã được lưu ở mảng vitriodapan
            listdapan.set(vitrioDapAn.get(position),s);

            // vị trí đó ở mảng vitriodapan -> =1 (-1: chưa có ký tự; >-1: đã có ký tự)
            vitrioDapAn.set(position,-1);
            mediaPlayer2.start();
            // set adapter
            ochu.setAdapter( new CauHoiAdapter(this,myAnswer,this));
            cautraloi.setAdapter( new CauTraLoiAdapter(this,listdapan,this));
        }
    }

    @Override
    public void CauTraLoiClick(int position) {
        int dem=0;
        // kiểm tra xem đã có bao nhiêu ký tự trong câu trả lời của người chơi
        for(int i=0;i<myAnswer.size();i++){
            if(myAnswer.get(i).toUpperCase()!="" && !myAnswer.get(i).trim().isEmpty()){
                dem++;
            }
        }
        String s=listdapan.get(position).toString();

        // nếu chọn ký tự trong listdapan !="" và biến dem < listcauhoi.size()
        if(s.trim().length()>0 &&s!=""&&s!=null && dem<listcauhoi.size()){

            //set vị trí đó trong listdapan => ""
            listdapan.set(position," ");

            // biến dùng để chỉ set myAnswer 1 lần
            boolean foundNegativeIndex = false;
            //lập vòng for vitriodapan
            for (int j = 0; j < vitrioDapAn.size(); j++) {
                // nếu foundNegativeIndex=false và có ký tự "" trong myAnswer
                if (!foundNegativeIndex && vitrioDapAn.get(j) < 0) {

                    if (vitrioDapAn.get(j) == -1 ) {
                        vitrioDapAn.set(j, position);
                        foundNegativeIndex = true;
                        myAnswer.set(j, s);


                    }

                }
            }
            mediaPlayer2.start();

            adapter.notifyDataSetChanged();
            // set lại adapter
            ochu.setAdapter( new CauHoiAdapter(this,myAnswer,this));
            cautraloi.setAdapter( new CauTraLoiAdapter(this,listdapan,this));
            KiemTraDapAn();
        }
    }
    private void KiemTraDapAn(){
        int dem=0;
        for(int i=0;i<myAnswer.size();i++){
            if(myAnswer.get(i).toUpperCase()!="" && !myAnswer.get(i).trim().isEmpty()){
                dem++;
            }
        }
        // nếu vị trí textview cuối cùng đã có ký tự
        if(dem>=listcauhoi.size()){
            String dapan1="";
            for (int i = 0; i < cauHoi.getDapAn().length(); i++) {
                char currentChar = cauHoi.getDapAn().charAt(i);
                if (currentChar != ' ') {
                    dapan1+=String.valueOf(currentChar);

                }
            }
            StringBuilder result = new StringBuilder();
            for (String item : myAnswer) {
                result.append(item);
            }
            String dapan2 = result.toString();
            System.out.println("da1: "+dapan1);
            System.out.println("da2: "+dapan2);
            //nếu đáp án câu hỏi và đáp án của người chơi trùng nhau
            if(dapan1.equalsIgnoreCase(dapan2)){
//                if (mInterstitialAd != null) {
//                    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//                    service.scheduleAtFixedRate(() -> {
//                        runOnUiThread(() -> {
//                            mInterstitialAd.show(ManChoi.this);
//                        });
//                    }, 1, 1, TimeUnit.SECONDS);
//                } else {
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
//                }
                ShowQuaMan();
            }
            else {

                Toast.makeText(this, "Sai đáp án!!!", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void loadInterstitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }
    private void ShowQuaMan(){
//        LoadCauHoi();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(ManChoi.this);

        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
        loadInterstitialAds();
        // show dialog
        Dialog dialog = new Dialog(ManChoi.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.quaman);
        Button qua=dialog.findViewById(R.id.tieptuc);
        TextView da=dialog.findViewById(R.id.dapan);
        ImageView as=dialog.findViewById(R.id.anhsang);
        Animation rotate=AnimationUtils.loadAnimation(ManChoi.this,R.anim.rotate);
        Animation blink=AnimationUtils.loadAnimation(ManChoi.this,R.anim.blink2);
        as.setAnimation(blink);
        as.setAnimation(rotate);
        da.setText(cauHoi.getDapAn());
        qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                 Thực hiện cập nhật CSDL và tải lại trang
                csdl.Update(ManChoi.this,cauHoi.getId());
                csdl.UpdateRuby(ManChoi.this,5);
                LoadCauHoi();
            }
        });

        dialog.show();
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}