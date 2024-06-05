package com.vtv.vuatiengviet;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.rewarded.RewardItem;


public interface ItemCauHoiClick {

    void onUserEarnedReward(@NonNull RewardItem rewardItem);

    void CauHoiClick (int position);
}
