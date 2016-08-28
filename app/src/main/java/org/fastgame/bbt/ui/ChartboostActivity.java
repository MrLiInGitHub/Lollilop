package org.fastgame.bbt.ui;

import android.os.Bundle;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import org.fastgame.bbt.R;
import org.fastgame.bbt.constant.AdMediatorAccounts;

/**
 * Integrate Chartboost Advertisement.
 *
 * @Author MrLi
 * @Since 08/25/2016
 */
public class ChartboostActivity extends AdActivity {

    private static final String APP_ID = AdMediatorAccounts.CHARTBOOST_APP_ID;
    private static final String APP_SIGNATURE = AdMediatorAccounts.CHARTBOOST_APP_SIGNATURE;
    private String location = CBLocation.LOCATION_MAIN_MENU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartboost);

        Chartboost.startWithAppId(this, APP_ID, APP_SIGNATURE);
        Chartboost.onCreate(this);

        Chartboost.cacheRewardedVideo(location);

        if (Chartboost.hasRewardedVideo(location)) {
            Chartboost.showRewardedVideo(location);
        } else {
            onNoAvailableAds();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        if (Chartboost.onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }
}
