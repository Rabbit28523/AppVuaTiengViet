package com.vtv.appvuatiengviet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class PlayerInfoAdapter extends RecyclerView.Adapter<PlayerInfoAdapter.PlayerInfoViewHolder> {
    private CSDL database;
    private List<ThongTinNguoiChoi> playerInfoList;
    private String infoName;
    private Context context;

    public PlayerInfoAdapter(Context context, List<ThongTinNguoiChoi> playerInfoList, String infoName) {
        this.context = context;
        this.playerInfoList = playerInfoList;
        this.infoName = infoName;
    }

    @NonNull
    @Override
    public PlayerInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_info_item, parent, false);
        database = new CSDL(context);
        return new PlayerInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerInfoViewHolder holder, int position) {
        ThongTinNguoiChoi playerInfo = playerInfoList.get(position);
        switch (position) {
            case(0):
                holder.ivFrame.setImageResource(R.drawable.khung_top1);
                break;
            case(1):
                holder.ivFrame.setImageResource(R.drawable.khung_top2);
                break;
            case(2):
                holder.ivFrame.setImageResource(R.drawable.khung_top3);
                break;
            default:
                holder.tvTop.setText(String.valueOf(position + 1));
                holder.tvTop.setVisibility(View.VISIBLE);
                break;
        }
        String fileAvt = "avt"+String.valueOf(playerInfo.getAvt_id()); // Lấy tên tệp ảnh từ đối tượng baiHat
        holder.tvName.setText(playerInfo.getName());
        // Kiểm tra xem id của hình ảnh có hợp lệ không
        int resourceId = context.getResources().getIdentifier(fileAvt, "drawable", context.getPackageName()); // Tìm ID tài nguyên dựa trên tên tệp ảnh
        if (resourceId != 0) {
            // Nếu id hợp lệ, đặt hình ảnh vào ImageView
            holder.ivAvatar.setImageResource(resourceId);
        } else {
            // Nếu không tìm thấy hình ảnh, xử lý theo nhu cầu của bạn, ví dụ đặt một hình ảnh mặc định
//                holder.ivImage.setImageResource(R.drawable.default_image);
        }
        switch (infoName) {
            case("ruby"):
                holder.tvInfo.setText(playerInfo.getRuby() + "");
                holder.ivImage.setImageResource(R.drawable.money);
                break;
            case("level"):
                holder.tvInfo.setText(playerInfo.getLevel() + "");
                holder.ivImage.setVisibility(View.GONE);
                break;
        }
        // Bind other views if necessary
    }

    @Override
    public int getItemCount() {
        return playerInfoList.size();
    }

    public class PlayerInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivFrame, ivImage;
        TextView tvTop, tvName, tvInfo;
        public PlayerInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_leaderboard_info_avatar);
            ivImage = itemView.findViewById(R.id.iv_leaderboard_info_image);
            ivFrame = itemView.findViewById(R.id.iv_leaderboard_info_frame);
            tvTop = itemView.findViewById(R.id.tv_leaderboard_info_top);
            tvName = itemView.findViewById(R.id.tv_leaderboard_info_name);
            tvInfo = itemView.findViewById(R.id.tv_leaderboard_info_info);
        }
    }
}
