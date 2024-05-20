package com.example.appvuatiengviet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CauHoiAdapter extends RecyclerView.Adapter<CauHoiAdapter.ViewHolder> {

    @NonNull
    @Override
    public CauHoiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.dongcauhoi,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CauHoiAdapter.ViewHolder holder, int i) {
        if("".equalsIgnoreCase(list.get(i).toUpperCase().trim())){
            holder.kytu.setBackgroundResource(R.drawable.nen_cauhoi);
            holder.kytu.setText("");
        }
        else {
            holder.kytu.setBackgroundResource(R.drawable.nen_dapan);
            holder.kytu.setText(list.get(i));
        }

        holder.kytu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.CauHoiClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kytu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kytu = itemView.findViewById(R.id.kytu);
        }
    }
    private Context context;
    private ItemCauHoiClick mclick;
    public CauHoiAdapter(Context context, ArrayList<String> list,ItemCauHoiClick mclick) {
        this.context = context;
        this.list = list;
        this.mclick=mclick;
    }
    ArrayList<String> list;
}
