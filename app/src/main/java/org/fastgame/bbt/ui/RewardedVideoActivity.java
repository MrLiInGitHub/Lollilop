package org.fastgame.bbt.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jirbo.adcolony.AdColonyAdapter;
import com.jirbo.adcolony.AdColonyBundleBuilder;

import org.fastgame.bbt.R;
import org.fastgame.bbt.constant.AdMediatorAccounts;

/**
 * Integrate Google AdsMob.
 *
 * @Author MrLi
 * @Since 08/21/2016
 */
public class RewardedVideoActivity extends BaseActivity implements RewardedVideoAdListener {

//    private final Context mContext = BBT.getAppContext();
    private static final String AD_UNIT_ID = AdMediatorAccounts.ADMOB_AD_UNIT_ID;
    private static final String APP_ID = AdMediatorAccounts.ADMOB_APP_ID;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_video);

        MobileAds.initialize(this, APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setUserId("UserId");
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideo();
    }

    @Override
    protected void onRestart() {
        mRewardedVideoAd.resume(this);
        super.onRestart();
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private void loadRewardedVideo() {
        if (!mRewardedVideoAd.isLoaded()) {

            Bundle extras = new Bundle();
            extras.putBoolean("_noRefresh", true);

            mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdColonyAdapter.class, AdColonyBundleBuilder.build())
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build());
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }
}
