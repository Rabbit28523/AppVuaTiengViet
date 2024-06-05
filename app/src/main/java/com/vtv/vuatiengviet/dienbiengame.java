package com.vtv.vuatiengviet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class dienbiengame extends AppCompatActivity {

    List<TextView> tvPlayer1=new ArrayList<>();
    List<TextView> tvPlayer2=new ArrayList<>();
    TextView ot5, ot4a, ot4b, ot3a, ot3b, ot2a, ot2b, ot1a, ot1b;
    Button tieptuc;
    List<CauHoi> list;
    CSDL csdl;
    String idRoom;
    String UserID;
    private DatabaseReference mDatabase;
    
    static int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dienbiengame);
        Intent intent=getIntent();
        idRoom= intent.getStringExtra("roomId");
        UserID=intent.getStringExtra("userId");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        Toast.makeText(this, String.valueOf(idRoom), Toast.LENGTH_SHORT).show();
        listenToDataChanges("rooms/"+idRoom);
        tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(dienbiengame.this, cauhoionl5.class);
                intent1.putExtra("idRoom",idRoom);
                intent1.putExtra("userID",UserID);
                startActivity(intent1);

            }
        });
    }

    private void AnhXa() {
        csdl=new CSDL(dienbiengame.this);
        list=csdl.getCauHoiRound1(5);
        ot5 = findViewById(R.id.ot5);
        ot4a = findViewById(R.id.ot4a);
        ot4b = findViewById(R.id.ot4b);
        ot3a = findViewById(R.id.ot3a);
        ot3b = findViewById(R.id.ot3b);
        ot2a = findViewById(R.id.ot2a);
        ot2b = findViewById(R.id.ot2b);
        ot1a = findViewById(R.id.ot1a);
        ot1b = findViewById(R.id.ot1b);
        tieptuc = findViewById(R.id.tieptuc);

        tvPlayer1.add((TextView) findViewById(R.id.ot1a));
        tvPlayer1.add((TextView) findViewById(R.id.ot2a));
        tvPlayer1.add((TextView) findViewById(R.id.ot3a));
        tvPlayer1.add((TextView) findViewById(R.id.ot4a));

        tvPlayer2.add((TextView) findViewById(R.id.ot1b));
        tvPlayer2.add((TextView) findViewById(R.id.ot2b));
        tvPlayer2.add((TextView) findViewById(R.id.ot3b));
        tvPlayer2.add((TextView) findViewById(R.id.ot4b));
    }

    private void readDataOnce(String path) {
        mDatabase.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
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
                        Log.d("Firebase", "index: " + player2CorrectAnswers);
                        if(UserID.equalsIgnoreCase(player1Id)){
                            index=player1CorrectAnswers;
                        }
                        else {
                            index=player2CorrectAnswers;
                        }
                        if (player1CorrectAnswers != null) {
                            Log.d("Firebase", "Player 1 Correct Answers: " + player1CorrectAnswers);
                            for (int i = 0; i < player1CorrectAnswers; i++) {
                                if (i < tvPlayer1.size()) {
                                    tvPlayer1.get(i).setBackgroundResource(R.drawable.otraloir);
                                }
                            }
                        } else {
                            Log.d("Firebase", "Player 1 Correct Answers not found or is null");
                        }

                        if (player2CorrectAnswers != null) {
                            Log.d("Firebase", "Player 2 Correct Answers: " + player2CorrectAnswers);
                            for (int i = 0; i < player2CorrectAnswers; i++) {
                                if (i < tvPlayer2.size()) { // Assuming you have a list for Player 2 TextViews
                                    tvPlayer2.get(i).setBackgroundResource(R.drawable.otraloir);
                                }
                            }
                        } else {
                            Log.d("Firebase", "Player 2 Correct Answers not found or is null");
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
    private void listenToDataChanges(String path) {
        mDatabase.child(path).addValueEventListener(new ValueEventListener() {
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
                    if (player2CorrectAnswers != null && player2CorrectAnswers == 5) {
                        if (UserID.equalsIgnoreCase(player1Id)) {
                            showDialogLose();
                        } else {
                            showDialogWin();
                        }
                    }
                    if (player1CorrectAnswers != null && player1CorrectAnswers == 5) {
                        if (UserID.equalsIgnoreCase(player2Id)) {
                            showDialogLose();
                        } else {
                            showDialogWin();
                        }
                    }
                    if (player1CorrectAnswers != null) {
                        Log.d("Firebase", "Player 1 Correct Answers: " + player1CorrectAnswers);
                        for (int i = 0; i < player1CorrectAnswers; i++) {
                            if (i < tvPlayer1.size()) {
                                tvPlayer1.get(i).setBackgroundResource(R.drawable.otraloir);
                            }
                            if (player1CorrectAnswers >= 5) {
                                ot5.setBackgroundResource(R.drawable.otraloir);
                            }
                        }

                    } else {
                        Log.d("Firebase", "Player 1 Correct Answers not found or is null");
                    }

                    if (player2CorrectAnswers != null) {
                        Log.d("Firebase", "Player 2 Correct Answers: " + player2CorrectAnswers);
                        for (int i = 0; i < player2CorrectAnswers; i++) {
                            if (i < tvPlayer2.size()) { // Assuming you have a list for Player 2 TextViews
                                tvPlayer2.get(i).setBackgroundResource(R.drawable.otraloir);
                            }
                            if (player2CorrectAnswers >= 5) {
                                ot5.setBackgroundResource(R.drawable.otraloir);
                            }
                        }

                    } else {
                        Log.d("Firebase", "Player 2 Correct Answers not found or is null");
                    }
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

    private void showDialogWin() {
        Dialog dialog = new Dialog(dienbiengame.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialogwin);
        Button qua=dialog.findViewById(R.id.tieptuc);
        ImageView as=dialog.findViewById(R.id.anhsang);
        Animation rotate= AnimationUtils.loadAnimation(dienbiengame.this,R.anim.rotate);
        Animation blink=AnimationUtils.loadAnimation(dienbiengame.this,R.anim.blink2);
        as.setAnimation(blink);
        as.setAnimation(rotate);
        qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
                dialog.dismiss();
                csdl.UpdateRuby(25);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 2000);
    }

    private void showDialogLose() {
        Dialog dialog = new Dialog(dienbiengame.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialoglose);
        Button qua=dialog.findViewById(R.id.tieptuc);
        ImageView as=dialog.findViewById(R.id.anhsang);
        Animation rotate= AnimationUtils.loadAnimation(dienbiengame.this,R.anim.rotate);
        Animation blink=AnimationUtils.loadAnimation(dienbiengame.this,R.anim.blink2);
        as.setAnimation(blink);
        as.setAnimation(rotate);
        qua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
                dialog.dismiss();
                csdl.UpdateRuby(5);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 2000);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        readDataOnce("rooms/"+idRoom);
    }
}