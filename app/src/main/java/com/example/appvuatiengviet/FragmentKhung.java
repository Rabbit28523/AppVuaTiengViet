package com.example.appvuatiengviet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.rewarded.RewardItem;

import java.util.ArrayList;

public class FragmentKhung extends Fragment implements ItemCauHoiClick {
    CSDL csdl;
    SanPham_khung_Adapter adapter_khung;
    ArrayList<SanPham> dsKhung;
    RecyclerView recyclerView_khung;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_khung, container, false);
        recyclerView_khung = view.findViewById(R.id.rv_khung);
        csdl=new CSDL(getContext());
        CapNhatDuLieu();
        return view;
    }
    private void CapNhatDuLieu() {
        cuahang.CapNhatDuLieu(getContext());
        dsKhung=csdl.HienDS_Khung();
        adapter_khung = new SanPham_khung_Adapter(getContext(),dsKhung,this);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);

        recyclerView_khung.setLayoutManager(layoutManager1);
        recyclerView_khung.setAdapter(adapter_khung);

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
                csdl.UpdateRuby(getContext(),-dsKhung.get(position).getPrice());
                CapNhatDuLieu();
            }
            else {
                Toast.makeText(getContext(), "Bạn không đủ ruby để mua vật phẩm này", Toast.LENGTH_SHORT).show();
            }
        }
    }
}