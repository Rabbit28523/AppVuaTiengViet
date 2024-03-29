package com.example.appvuatiengviet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManChoi extends AppCompatActivity implements ItemCauHoiClick, ItemCauTraLoiClick {
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
    ImageView chiase,back,help;
    LinearLayout lele;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_choi);
        cauhoi = findViewById(R.id.cauhoi);
        ochu = findViewById(R.id.ochu);
        cautraloi = findViewById(R.id.cautraloi);
        csdl = new CSDL(ManChoi.this);
        cauHoi = csdl.HienCSDL(ManChoi.this);
        cauhoi.setText(cauHoi.getTu());
        ruby=findViewById(R.id.ruby);
        sttcauhoi=findViewById(R.id.sttcauhoi);
        lele=findViewById(R.id.lele);
        chiase=findViewById(R.id.chiase);
        back=findViewById(R.id.back);
        help=findViewById(R.id.trogiup);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManChoi.this.finish();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(csdl.HienRuby(ManChoi.this)>=50){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManChoi.this);
                    builder.setTitle("Xác nhận");
                    builder.setMessage("Gợi ý 1 ô chữ cần 50 kim cương, bạn có chắc chắn không?");
                    builder.setCancelable(false);
                    // Nút xác nhận
                    builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấn nút xác nhận
                            // Thêm code xử lý ở đây
                            String dapan1="";
                            for (int i = 0; i < cauHoi.getDapAn().length(); i++) {
                                char currentChar = cauHoi.getDapAn().charAt(i);
                                if (currentChar != ' ') {
                                    dapan1+=String.valueOf(currentChar);

                                }
                            }
                            System.out.println(dapan1);
                            for(int i=0;i<dapan1.length();i++){
                                if(String.valueOf(dapan1.charAt(i)).equalsIgnoreCase(String.valueOf(myAnswer.get(i)))
                                        && !String.valueOf(myAnswer.get(i)).trim().equals(""))
                                {
                                    trogiup.set(i, 1);
                                }

                                else if(vitrioDapAn.get(i)!=-1){

                                    listdapan.set(vitrioDapAn.get(i),myAnswer.get(i));
                                    myAnswer.set(i,"");
                                    vitrioDapAn.set(i,-1);
                                    ochu.setAdapter( new CauHoiAdapter(ManChoi.this,myAnswer,ManChoi.this));
                                    cautraloi.setAdapter( new CauTraLoiAdapter(ManChoi.this,listdapan,ManChoi.this));
                                }

                            }
                            HienTroGiup();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    Toast.makeText(ManChoi.this, "Bạn không đủ 50 kim cương", Toast.LENGTH_SHORT).show();
                }

            }
        });
        chiase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap b=takescreenshotOfRootView(lele);
                File savedFile = saveBitmapToFile(b);
                if (savedFile != null) {
                    Toast.makeText(ManChoi.this, "Đã chụp màn hình để chia sẻ", Toast.LENGTH_SHORT).show();
                    try {
                        savedFile.setReadable(true,false);
                        final Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri= FileProvider.getUriForFile(getApplicationContext(),getApplication().getPackageName()+".provider",savedFile);
                        intent.putExtra(Intent.EXTRA_STREAM,uri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("image/*");

                        startActivity(Intent.createChooser(intent,"Share to..."));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Toast.makeText(ManChoi.this, "Failed to save screenshot", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LoadCauHoi();
    }
    public int KiemTraViTri_trogiup(){
        int position=-1;
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
        for(int i=0;i<listdapan.size();i++){
            if (listdapan.get(i).equalsIgnoreCase(ktu)){
                position=i;
                break;
            }
        }
        return position;
    }
    private void HienTroGiup(){

        if (KiemTraViTri_trogiup()>-1 ){
            String dapan1="";
            for (int i = 0; i < cauHoi.getDapAn().length(); i++) {
                char currentChar = cauHoi.getDapAn().charAt(i);
                if (currentChar != ' ') {
                    dapan1+=String.valueOf(currentChar);

                }
            }
            String ktu= String.valueOf(dapan1.charAt( KiemTraViTri_trogiup()));
            int vitrioda=KiemTraViTri_trogiup();
            myAnswer.set(vitrioda,ktu);
            vitrioDapAn.set(KiemTraViTri_trogiup(),vitrioda);
            trogiup.set(KiemTraViTri_trogiup(),1);
            int vittriDA=KiemTraViTri_dapAn(ktu);
            System.out.println(ktu);
//            Toast.makeText(this, "vị trí: "+vittriDA, Toast.LENGTH_SHORT).show();
            listdapan.set(vittriDA,"");
            cautraloi.setAdapter( new CauTraLoiAdapter(ManChoi.this,listdapan,ManChoi.this));
            ochu.setAdapter( new CauHoiAdapter(ManChoi.this,myAnswer,ManChoi.this));
            csdl.UpdateRuby(ManChoi.this,-50);
            ruby.setText(String.valueOf(csdl.HienRuby(ManChoi.this)));
            KiemTraDapAn();

        }
        else {
            Toast.makeText(ManChoi.this, "Ô chữ đã được hoành thành", Toast.LENGTH_SHORT).show();
        }
    }
    public static  Bitmap takescreenshot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b=Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }
    private static Bitmap takescreenshotOfRootView(View v){
        return takescreenshot(v.getRootView());
    }
    public File saveBitmapToFile(Bitmap bitmap) {
        String savedImagePath = null;
        String imageFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
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
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            // Update MediaStore to display the image in the Gallery
            MediaStore.Images.Media.insertImage(getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), imageFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
            savedImagePath = null;
        }
        return imageFile;
    }

    private void LoadCauHoi() {
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

    @Override
    public void CauHoiClick(int position) {
        String s=myAnswer.get(position).toString();
        if(s.trim().length()>0 &&s!=""&&s!=null && s!="1"){
            myAnswer.set(position,"");

            myAnswer.set(position,"");
            listdapan.set(vitrioDapAn.get(position),s);

            vitrioDapAn.set(position,-1);



//            vitrioDapAn.add(position);
            ochu.setAdapter( new CauHoiAdapter(this,myAnswer,this));
//            listcauhoi.setLayoutManager(layoutManager);
            cautraloi.setAdapter( new CauTraLoiAdapter(this,listdapan,this));
        }
    }

    @Override
    public void CauTraLoiClick(int position) {
        int dem=0;
        for(int i=0;i<myAnswer.size();i++){
            if(myAnswer.get(i).toUpperCase()!="" && !myAnswer.get(i).trim().isEmpty()){
                dem++;
            }
        }
        String s=listdapan.get(position).toString();

        if(s.trim().length()>0 &&s!=""&&s!=null && dem<listcauhoi.size()){
            listdapan.set(position," ");
            boolean foundNegativeIndex = false;
            int vitri=-1;// Biến đánh dấu để chỉ thực hiện cập nhật arr một lần
            for (int j = 0; j < vitrioDapAn.size(); j++) {
                if (!foundNegativeIndex && vitrioDapAn.get(j) < 0) {
                    // Nếu chưa tìm thấy phần tử âm và vitrioDapAn[j] nhỏ hơn 0, cập nhật arr và đánh dấu đã tìm thấy
                    if (vitrioDapAn.get(j) == -1 ) {
                        vitrioDapAn.set(j, position);
                        foundNegativeIndex = true;
                        myAnswer.set(j, s);


                    }

                }
            }

            adapter.notifyDataSetChanged();
            ochu.setAdapter( new CauHoiAdapter(this,myAnswer,this));
            cautraloi.setAdapter( new CauTraLoiAdapter(this,listdapan,this));
            KiemTraDapAn();
        }
    }
    private void KiemTraDapAn(){
        if(vitrioDapAn.get(vitrioDapAn.size()-1)>=0){
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
            if(dapan1.equalsIgnoreCase(dapan2)){

                ShowQuaMan();
            }
            else {

                Toast.makeText(this, "lew lew gà", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void ShowQuaMan(){
//        LoadCauHoi();
        Dialog dialog = new Dialog(ManChoi.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.quaman);
        Button qua=dialog.findViewById(R.id.tieptuc);
        TextView da=dialog.findViewById(R.id.dapan);
        da.setText(cauHoi.getDapAn());
        qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                Toast.makeText(ManChoi.this, "mày giỏi đúng r đó con chóa", Toast.LENGTH_SHORT).show();
                csdl.Update(ManChoi.this,cauHoi.getId());
                csdl.UpdateRuby(ManChoi.this,25);
                recreate();
//                LoadCauHoi();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Thực hiện cập nhật CSDL và tải lại trang
                csdl.Update(ManChoi.this,cauHoi.getId());
                csdl.UpdateRuby(ManChoi.this,25);
                recreate();
//                LoadCauHoi();
            }
        });

        dialog.show();
    }
}