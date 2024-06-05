package com.vtv.vuatiengviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class cauhoionl5 extends AppCompatActivity implements ItemCauHoiClick,ItemCauTraLoiClick{
    CauHoi cauHoi;
    CSDL csdl;
    String userID;
    String idRoom;
    TextView level;
    RecyclerView listcauhoi,dapan;

    ImageView nextcau,backr1;
    TextView layout_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cauhoionl5);
        Intent intent=getIntent();
        idRoom=intent.getStringExtra("idRoom");
        userID=intent.getStringExtra("userID");
        csdl=new CSDL(this);
        Random random = new Random();
        int randomNum = random.nextInt(40) + 1; // Generate a random number between 1 and 40
        cauHoi=csdl.getCauHoi(randomNum);
        Toast.makeText(this, cauHoi.getDapAn(), Toast.LENGTH_SHORT).show();
        mediaPlayer2=MediaPlayer.create(cauhoionl5.this,R.raw.click_003);
        mediaPlayer2.setVolume(MainActivity.volumn2,MainActivity.volumn2);
        level=findViewById(R.id.level);
        layout_2=findViewById(R.id.layout_2);
        nextcau=findViewById(R.id.nextr1);
        backr1=findViewById(R.id.backr1);
        dapan=findViewById(R.id.dapan);
        listcauhoi=findViewById(R.id.listcauhoi);
        loadTrang();
        backr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBack();
            }
        });
        nextcau.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialogNext();
            }
        });
        listenToDataChanges("rooms/"+idRoom);
    }
    private void showDialogNext() {
        Dialog dialog = new Dialog(cauhoionl5.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_nextcau);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        Button chapnhan=dialog.findViewById(R.id.chapnhan);
        Button tuchoi=dialog.findViewById(R.id.tuchoi);
        chapnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random1 = new Random();
                int randomNum = random1.nextInt(40) + 1; // Generate a random number between 1 and 40
                cauHoi=csdl.getCauHoi(randomNum);
                loadTrang();
                dialog.dismiss();

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
    List<Integer> trogiup;
    ArrayList<String> cautraloi;
    ArrayList<Integer> vitrioDapAn;
    ArrayList<String> arr2,arr;
    int index2=0;
    int diem=0;
    CauHoiAdapter adapter;
    CauTraLoiAdapter adap;

    private void loadTrang() {

            layout_2.setText(cauHoi.getTu());
            trogiup=new ArrayList<>();
            vitrioDapAn=new ArrayList<>();
            level.setText(String.valueOf(diem)+"/10");
            cautraloi = new ArrayList<>();
            arr = new ArrayList<>();
            arr2=new ArrayList<>();
            trogiup=new ArrayList<>();
            vitrioDapAn=new ArrayList<>();
            String trimmedString = cauHoi.getDapAn().trim();
            for (int i = 0; i < trimmedString.length(); i++) {
                char currentChar = trimmedString.charAt(i);
                if (currentChar != ' ') {
                    arr.add(String.valueOf(""));
                    arr2.add(String.valueOf(currentChar));
                    trogiup.add(0);
                    cautraloi.add("");
                    vitrioDapAn.add(-1);
                }
            }
            Collections.shuffle(arr2);
            adap = new CauTraLoiAdapter(cauhoionl5.this,arr2,this);
            FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(getApplicationContext());
            layoutManager1.setFlexDirection(FlexDirection.ROW);
            layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
            dapan.setLayoutManager(layoutManager1);
            dapan.setAdapter(adap);
            adapter = new CauHoiAdapter(cauhoionl5.this,arr,this);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            listcauhoi.setLayoutManager(layoutManager);
            listcauhoi.setAdapter(adapter);





    }

    private void showDialogBack() {
        Dialog dialog = new Dialog(cauhoionl5.this, android.R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_dunggame);
        dialog.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        Button chapnhan=dialog.findViewById(R.id.chapnhan);
        Button tuchoi=dialog.findViewById(R.id.tuchoi);
        chapnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                EndGame("rooms/"+idRoom);
                finish();
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
    private DatabaseReference mDatabase2;
    private void EndGame(String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase2 = database.getReference();
        mDatabase.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        String player1Id = dataSnapshot.child("player1Id").getValue(String.class);
                        String player2Id = dataSnapshot.child("player2Id").getValue(String.class);

                        if (userID.equalsIgnoreCase(player1Id)) {
                            // Set player2CorrectAnswers to 5
                            mDatabase.child(path).child("player2CorrectAnswers").setValue(5);
                        } else if (userID.equalsIgnoreCase(player2Id)) {
                            // Set player1CorrectAnswers to 5
                            mDatabase.child(path).child("player1CorrectAnswers").setValue(5);
                        } else {
                            Log.d("Firebase", "UserID does not match either player1Id or player2Id");
                        }
                    } else {
                        Log.d("Firebase", "No data found");
                    }
                } else {
                    Log.e("Firebase", "Error getting data", task.getException());
                }
            }
        });
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

    }

    @Override
    public void CauHoiClick(int position) {
// lấy ra text của textview đã click
        String s=cautraloi.get(position).toString();
        // nếu s != ""
        if(s.trim().length()>0 &&s!=""&&s!=null ){

            // set vị trí đó trở thành ""
            cautraloi.set(position,"");

            // set lại vị trí cữ của từ đó vào lisdapan
            // vị trí cũ đã được lưu ở mảng vitriodapan
            arr2.set(vitrioDapAn.get(position),s);

            // vị trí đó ở mảng vitriodapan -> =1 (-1: chưa có ký tự; >-1: đã có ký tự)
            vitrioDapAn.set(position,-1);
            mediaPlayer2.start();
            // set adapter
            listcauhoi.setAdapter( new CauHoiAdapter(this,cautraloi,this));
            dapan.setAdapter( new CauTraLoiAdapter(this,arr2,this));
        }
    }

    @Override
    public void CauTraLoiClick(int position) {
        int dem=0;
        // kiểm tra xem đã có bao nhiêu ký tự trong câu trả lời của người chơi
        for(int i=0;i<cautraloi.size();i++){
            if(cautraloi.get(i).toUpperCase()!="" && !cautraloi.get(i).trim().isEmpty()){
                dem++;
            }
        }
        String s=arr2.get(position).toString();

        // nếu chọn ký tự trong listdapan !="" và biến dem < listcauhoi.size()
        if(s.trim().length()>0 &&s!=""&&s!=null && dem<arr.size()){

            //set vị trí đó trong listdapan => ""
            arr2.set(position," ");

            // biến dùng để chỉ set myAnswer 1 lần
            boolean foundNegativeIndex = false;
            //lập vòng for vitriodapan
            for (int j = 0; j < vitrioDapAn.size(); j++) {
                // nếu foundNegativeIndex=false và có ký tự "" trong myAnswer
                if (!foundNegativeIndex && vitrioDapAn.get(j) < 0) {

                    if (vitrioDapAn.get(j) == -1 ) {
                        vitrioDapAn.set(j, position);
                        foundNegativeIndex = true;
                        cautraloi.set(j, s);


                    }

                }
            }
            mediaPlayer2.start();

            adapter.notifyDataSetChanged();
            // set lại adapter
            listcauhoi.setAdapter( new CauHoiAdapter(this,cautraloi,this));
            dapan.setAdapter( new CauTraLoiAdapter(this,arr2,this));
            KiemTraDapAn();
        }
    }
    MediaPlayer mediaPlayer, mediaPlayer2,mediaPlayer3;
    private void KiemTraDapAn(){
        int dem=0;
        for(int i=0;i<cautraloi.size();i++){
            if(cautraloi.get(i).toUpperCase()!="" && !cautraloi.get(i).trim().isEmpty()){
                dem++;
            }
        }
        // nếu vị trí textview cuối cùng đã có ký tự
        if(dem>=arr.size()){
            String dapan1="";
            for (int i = 0; i < cauHoi.getDapAn().length(); i++) {
                char currentChar = cauHoi.getDapAn().charAt(i);
                if (currentChar != ' ') {
                    dapan1+=String.valueOf(currentChar);

                }
            }
            StringBuilder result = new StringBuilder();
            for (String item : cautraloi) {
                result.append(item);
            }
            String dapan2 = result.toString();
            System.out.println("da1: "+dapan1);
            System.out.println("da2: "+dapan2);
            //nếu đáp án câu hỏi và đáp án của người chơi trùng nhau
            if(dapan1.equalsIgnoreCase(dapan2)){
                mediaPlayer3=MediaPlayer.create(cauhoionl5.this,R.raw.congrates_3);
                mediaPlayer3.setVolume(MainActivity.volumn2,MainActivity.volumn2);
                mediaPlayer3.start();
                mediaPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        UpdateDLRoom();
                        finish();
                    }
                });
            }
            else {
                mediaPlayer3=MediaPlayer.create(cauhoionl5.this,R.raw.mixkitclickerror1110);
                mediaPlayer3.setVolume(MainActivity.volumn2,MainActivity.volumn2);
                mediaPlayer3.start();
                Toast.makeText(this, "Sai đáp án!!!", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private DatabaseReference mDatabase;
    private void UpdateDLRoom() {
        dienbiengame.index++;
        mDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(idRoom);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        String player1Id = dataSnapshot.child("player1Id").getValue(String.class);
                        String player2Id = dataSnapshot.child("player2Id").getValue(String.class);
                        Integer player1CorrectAnwers=dataSnapshot.child("player1CorrectAnswers").getValue(Integer.class);
                        Integer player2CorrectAnwers=dataSnapshot.child("player2CorrectAnswers").getValue(Integer.class);

                        if (player1Id != null && player1Id.equals(userID)) {
                            if(player1CorrectAnwers<5 && player2CorrectAnwers<5){
                                mDatabase.child("player1CorrectAnswers").setValue(dienbiengame.index);
                                Log.d("Firebase", "Updated player1CorrectAnswers to " + dienbiengame.index);
                            }
                            else {
                                finish();
                            }

                        } else if (player2Id != null && player2Id.equals(userID)) {
                            if(player1CorrectAnwers<5 && player2CorrectAnwers<5){
                                mDatabase.child("player2CorrectAnswers").setValue(dienbiengame.index);
                                Log.d("Firebase", "Updated player2CorrectAnswers to " + dienbiengame.index);
                            }
                            else {
                                finish();
                            }

                        } else {
                            Log.d("Firebase", "User ID does not match any player");
                        }
                    } else {
                        Log.d("Firebase", "No data found for room: " + idRoom);
                    }
                } else {
                    Log.e("Firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    private DatabaseReference mDatabase1;
    private void listenToDataChanges(String path) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase1 = database.getReference();
        mDatabase1.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Handle the data here
                    String status = dataSnapshot.child("status").getValue(String.class);
                    String turn = dataSnapshot.child("turn").getValue(String.class);
                    String player1Id = dataSnapshot.child("player1Id").getValue(String.class);
                    String player2Id = dataSnapshot.child("player2Id").getValue(String.class);
                    Log.d("Firebase", "Status: " + status);
                    Log.d("Firebase", "Turn: " + turn);
                    Integer player1CorrectAnswers = dataSnapshot.child("player1CorrectAnswers").getValue(Integer.class);
                    Integer player2CorrectAnswers = dataSnapshot.child("player2CorrectAnswers").getValue(Integer.class);
                    if(player1CorrectAnswers==5 ||player2CorrectAnswers==5){
                        finish();
                    }
                   
                    

                    // Update UI for player1CorrectAnswers
                    
                } else {
                    Log.d("Firebase", "No data found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error getting data", databaseError.toException());
            }
        });
    }

}