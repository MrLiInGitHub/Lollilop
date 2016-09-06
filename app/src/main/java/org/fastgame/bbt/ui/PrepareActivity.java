package org.fastgame.bbt.ui;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.R;
import org.fastgame.bbt.constant.AdMediatorAccounts;
import org.fastgame.bbt.utility.ActivityUtils;
import org.fastgame.bbt.utility.UIUtils;

/**
 * the launching Activity every time user open the app.
 *
 * @Author MrLi
 * @Since 08/20/2016
 */
public class PrepareActivity extends BaseActivity {

    private InterstitialAd mInterstitialAd;
    private Handler mHandler = new Handler();

    private AdListener mAdListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            jumpIntoApp();
        }

        @Override
        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
            jumpIntoApp();
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AdMediatorAccounts.ADMOB_STARTUP_AD_UNIT_ID);

        mInterstitialAd.setAdListener(mAdListener);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mInterstitialAd.loadAd(new AdRequest.Builder()
                        .build());
            }
        }, 3 * 1000);

    }

    private void jumpIntoApp() {
        ActivityUtils.startMainActivity(BBT.getAppContext());
    }
}
