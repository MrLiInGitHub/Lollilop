package org.fastgame.bbt.ui;

import android.os.Bundle;

import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;
import com.jirbo.adcolony.AdColonyV4VCAd;
import com.jirbo.adcolony.AdColonyV4VCListener;
import com.jirbo.adcolony.AdColonyV4VCReward;

import org.fastgame.bbt.R;
import org.fastgame.bbt.constant.AdMediatorAccounts;

/**
 * AdColony V4VC Advertisement.
 *
 * @Author MrLi
 * @Since 08/31/2016
 */
public class AdColonyV4VCActivity extends AdActivity implements AdColonyV4VCListener, AdColonyAdAvailabilityListener, AdColonyAdListener {

    private final String APP_ID = AdMediatorAccounts.ADCOLONY_APP_ID;
    private final String ZONE_ID = AdMediatorAccounts.ADCOLONY_ZONE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adcolony);

        AdColony.configure(this, "version:1.0,store:google", APP_ID, ZONE_ID);

        AdColony.addAdAvailabilityListener(this);
        AdColony.addV4VCListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdColony.resume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AdColony.pause();
    }

    @Override
    public void onAdColonyV4VCReward(AdColonyV4VCReward adColonyV4VCReward) {}

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

    private void getAndShowAd() {
        AdColonyV4VCAd ad = new AdColonyV4VCAd().withListener(this);
        ad.show();
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
