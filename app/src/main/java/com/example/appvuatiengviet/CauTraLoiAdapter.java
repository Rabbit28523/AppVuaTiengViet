package com.example.appvuatiengviet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CauTraLoiAdapter  extends RecyclerView.Adapter<CauTraLoiAdapter.ViewHolder>{
    private Context context;
    private ItemCauTraLoiClick mclick;
    public CauTraLoiAdapter(Context context, ArrayList<String> list, ItemCauTraLoiClick mclick) {
        this.context = context;
        this.list = list;
        this.mclick=mclick;
    }
    ArrayList<String> list;
    @NonNull
    @Override
    public CauTraLoiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.dongcautraloi,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CauTraLoiAdapter.ViewHolder holder, int i) {
        holder.kytu.setText(list.get(i));

        holder.kytu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mclick.CauTraLoiClick(i);
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
            kytu = itemView.findViewById(R.id.kytucautl);
        }
    }
}
