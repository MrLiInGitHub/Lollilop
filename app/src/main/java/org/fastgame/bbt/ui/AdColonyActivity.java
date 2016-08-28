package org.fastgame.bbt.ui;

import android.os.Bundle;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyVideoAd;

import org.fastgame.bbt.R;
import org.fastgame.bbt.constant.AdMediatorAccounts;
import org.fastgame.bbt.utility.UIUtils;

/**
 * Integrate AdColony Advertisement.
 *
 * @Author MrLi
 * @Since 08/24/2016
 */
public class AdColonyActivity extends AdActivity implements AdColonyAdAvailabilityListener, AdColonyAdListener {

    private static final String ZONE_ID = AdMediatorAccounts.ADCOLONY_ZONE_ID;
    private static final String ADCOLONY_APP_ID = AdMediatorAccounts.ADCOLONY_APP_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adcolony);
        AdColony.configure(this, "version:1.0,store:google", ADCOLONY_APP_ID, ZONE_ID);

        AdColony.addAdAvailabilityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AdColony.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdColony.resume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getAndShowAd() {
        AdColonyVideoAd ad = new AdColonyVideoAd(ZONE_ID).withListener(AdColonyActivity.this);
        ad.show();
    }

    @Override
    public void onAdColonyAdAvailabilityChange(final boolean b, String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    getAndShowAd();
                } else {
                    onNoAvailableAds();
                }
            }
        });
    }

    @Override
    public void onAdColonyAdAttemptFinished(AdColonyAd adColonyAd) {
        onAdsPlayFinished();
    }

    @Override
    public void onAdColonyAdStarted(AdColonyAd adColonyAd) {
        onAdsPlayStart();
    }
}
