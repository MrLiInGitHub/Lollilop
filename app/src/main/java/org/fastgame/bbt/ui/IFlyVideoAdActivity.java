package org.fastgame.bbt.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYVideoAd;
import com.iflytek.voiceads.IFLYVideoAdListener;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.R;
import org.fastgame.bbt.constant.AdMediatorAccounts;

/**
 *  Intergrate IFly Advertisement.
 *
 * @Author MrLi
 * @Since 08/21/2016
 */
public class IFlyVideoAdActivity extends AdActivity implements IFLYVideoAdListener {

    private static final String AD_UNIT_ID = AdMediatorAccounts.IFLY_AD_UNIT_ID;
    private final Context mContext = BBT.getAppContext();
    private IFLYVideoAd mIflyVideoAd;
    RelativeLayout mAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ifly_video_ad);

        initializeVideoAd();
    }

    @Override
    protected void onPause() {
        if (mIflyVideoAd != null) {
            mIflyVideoAd.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mIflyVideoAd != null) {
            mIflyVideoAd.onResume();
        }
        super.onResume();
    }

    private void initializeVideoAd() {
        mIflyVideoAd = IFLYVideoAd.createVideoAd(this, AD_UNIT_ID, this, true);

        if (mIflyVideoAd == null) {
            onCanceledResult();
            return;
        }

        mIflyVideoAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        mAdContainer = (RelativeLayout) findViewById(R.id.ad_zone);
        mAdContainer.removeAllViews();
        mAdContainer.addView(mIflyVideoAd);
        mIflyVideoAd.loadAd();
    }

    @Override
    public void onAdStartPlay() {
        onAdStartPlay();
    }

    @Override
    public void onAdPlayComplete() {
        onAdsPlayFinished();
    }

    @Override
    public void onAdPlayProgress(int i, int i1) {
        Toast.makeText(mContext, "Ad Play " + i + "," + i1, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdReceive() {
        mIflyVideoAd.showAd();

        Toast.makeText(mContext, "on Ad Receive", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdFailed(AdError adError) {
        onCanceledResult();
        Toast.makeText(mContext, "on Ad Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdClick() {
        Toast.makeText(mContext, "on Ad Click", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdClose() {
        onCanceledResult();
        Toast.makeText(mContext, "on Ad Close", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdExposure() {
        Toast.makeText(mContext, "on Ad Exposure", Toast.LENGTH_LONG).show();
    }
}
