package com.vtv.vuatiengviet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vtv.vuatiengviet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentLeaderboard extends Fragment {
    public String infoName;
    private CSDL database;
    private RecyclerView recyclerView;
    private PlayerInfoAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private List<ThongTinNguoiChoi> playerInfoList;
    public FragmentLeaderboard(String infoName) {
        this.infoName = infoName;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.rv_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        playerInfoList = new ArrayList<>();
        adapter = new PlayerInfoAdapter(getContext(), playerInfoList, infoName);
        recyclerView.setAdapter(adapter);

        database = new CSDL(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();

        getLeaderboardData();
        return view;
    }
    private void getLeaderboardData() {
        DatabaseReference usersRef = firebaseDatabase.getReference().child("users");
        usersRef.orderByChild(infoName).limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerInfoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ThongTinNguoiChoi playerInfo = snapshot.getValue(ThongTinNguoiChoi.class);
                    if (playerInfo != null) {
                        playerInfoList.add(playerInfo);
                    } else {
                        Log.w("TAG", "Null PlayerInfo received from database");
                    }
                }
                // Sắp xếp ngược lại vì `limitToLast` lấy từ thấp đến cao
                Collections.reverse(playerInfoList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadLeaderboard:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load leaderboard data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}