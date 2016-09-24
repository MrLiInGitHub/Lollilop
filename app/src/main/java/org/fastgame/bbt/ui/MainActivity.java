package org.fastgame.bbt.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.gson.Gson;
import com.iflytek.autoupdate.IFlytekUpdate;
import com.iflytek.autoupdate.IFlytekUpdateListener;
import com.iflytek.autoupdate.UpdateConstants;
import com.iflytek.autoupdate.UpdateErrorCode;
import com.iflytek.autoupdate.UpdateInfo;
import com.iflytek.autoupdate.UpdateType;
import com.umeng.analytics.MobclickAgent;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.R;
import org.fastgame.bbt.connectivity.HttpRequestHelper;
import org.fastgame.bbt.connectivity.WebSocketLauncher;
import org.fastgame.bbt.constant.AdMediatorAccounts;
import org.fastgame.bbt.constant.RequestCode;
import org.fastgame.bbt.entity.MsgFromServer;
import org.fastgame.bbt.entity.MsgToSubmit;
import org.fastgame.bbt.event.SocketEvent;
import org.fastgame.bbt.event.UmengEvent;
import org.fastgame.bbt.sp.AddressHistoryPreference;
import org.fastgame.bbt.utility.ActivityUtils;
import org.fastgame.bbt.utility.NetworkUtils;
import org.fastgame.bbt.utility.SystemUtils;
import org.fastgame.bbt.utility.UIUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.sharesdk.integhelper.Main;

/**
 * The main Activity is for user operation to interact.
 *
 * @Author MrLi
 * @Since 08/20/2016
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, RewardedVideoAdListener {

    private static final int MSG_POLL_DURATION = 2000;
    private static final int MSG_WHAT_REQUEST_RESULT = 1;

    private final Context mContext = BBT.getAppContext();

    private AutoCompleteTextView mAddressView;
    private Button mFreeSubmitBtn;
    private Button mAdSubmitBtn;
    private TextView mTipView;
    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;
    private View mClearAddressBtn;
    private RewardedVideoAd mRewardedVideoAd;
    private Toolbar mToolbar;

    private IFlytekUpdate mUpdateManager;
    private boolean isRequestSuccessful;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT_REQUEST_RESULT:
                    if (!isRequestSuccessful) {
                        sendRequest((Boolean) msg.obj);
                    } else {
                        isRequestSuccessful = false;
                    }
                    break;
            }
        }
    };

    private IFlytekUpdateListener mIFlytekUpdateListener = new IFlytekUpdateListener() {
        @Override
        public void onResult(int i, UpdateInfo updateInfo) {
            if (i == UpdateErrorCode.OK && updateInfo != null) {
                if (updateInfo.getUpdateType() == UpdateType.NoNeed) {
                    UIUtils.showLongToast(UIUtils.getString(R.string.no_need_to_update));
                    return;
                }
                mUpdateManager.showUpdateInfo(MainActivity.this, updateInfo);
            } else {
                UIUtils.showLongToast(UIUtils.getString(R.string.requesting_update_error, i));
            }
        }
    };

    private void showAlertDialog(CharSequence message, CharSequence confirmBtnText, DialogInterface.OnClickListener confirmBtnListener,
                                 CharSequence cancelBtnText, DialogInterface.OnClickListener cancelBtnListener) {
        showAlertDialog(UIUtils.showAlertDialog(this, message, confirmBtnText, confirmBtnListener, cancelBtnText, cancelBtnListener));
    }

    private void showAlertDialog(CharSequence message) {
        showAlertDialog(UIUtils.showAlertDialog(this, message));
    }

    private void showAlertDialog(AlertDialog alertDialog) {

        dismissProgressDialog();
        dismissAlertDialog();

        mAlertDialog = alertDialog;

        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    private void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    private void showProgressDialog(CharSequence message) {

        if (mProgressDialog == null) {
            mProgressDialog = UIUtils.showProgressDialog(this, message);
        }

        mProgressDialog.setMessage(message);

        dismissAlertDialog();

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolbar();
        checkUpdate();

        initMobileAd();

        EventBus.getDefault().register(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            EventBus.getDefault().unregister(MainActivity.this);
        } catch (Exception e) {}

        dismissProgressDialog();
        dismissAlertDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menuItemId = item.getItemId();

        switch (menuItemId) {
            case R.id.action_share:
                shareApp();
                break;
            case R.id.action_info:
                showAboutInfo();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mAddressView = (AutoCompleteTextView) findViewById(R.id.address_text);
        mFreeSubmitBtn = (Button) findViewById(R.id.free_submit_btn);
        mAdSubmitBtn = (Button) findViewById(R.id.ad_submit_btn);
        mTipView = (TextView) findViewById(R.id.tip);
        mClearAddressBtn = findViewById(R.id.clear_address_btn);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mAdSubmitBtn.setOnClickListener(this);
        mFreeSubmitBtn.setOnClickListener(this);
        mClearAddressBtn.setOnClickListener(this);

        mTipView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/cuhuoyijianti.ttf"));

        initAddressAutoComplete();
        initToolbar();
    }

    private void checkUpdate() {
        mUpdateManager = IFlytekUpdate.getInstance(mContext);
        mUpdateManager.setDebugMode(true);
        mUpdateManager.setParameter(UpdateConstants.EXTRA_WIFIONLY, "true");
        mUpdateManager.setParameter(UpdateConstants.EXTRA_NOTI_ICON, "true");
        mUpdateManager.setParameter(UpdateConstants.EXTRA_STYLE, UpdateConstants.UPDATE_UI_DIALOG);

        mUpdateManager.autoUpdate(mContext, mIFlytekUpdateListener);
    }

    private void initMobileAd() {
        MobileAds.initialize(this, AdMediatorAccounts.ADMOB_APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
    }

    private void initAddressAutoComplete() {
        if (mAddressView == null) {
            return;
        }

        mAddressView.setAdapter(new AddressHistoryPreference().getAllAddressHistory());

        mAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                view.showDropDown();
            }
        });
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(Color.WHITE);

    }

    private void saveAddressHistory(String address) {
        new AddressHistoryPreference().addAddressHistory(address);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SocketEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getType()) {
            case SocketEvent.SOCKET_ON_CONNECTED:
                WebSocketLauncher.getInstance().sendAllWaitingMessage(event.getWebSocket());
                break;
            case SocketEvent.SOCKET_ON_TEXT_MESSAGE:
                MsgFromServer msg = new Gson().fromJson(event.getText(), MsgFromServer.class);

                if (msg != null && !TextUtils.isEmpty(msg.line0) && msg.line0.equals("success")) {
                    showAlertDialog(processMsgFromServer(msg));
                    isRequestSuccessful = true;
                    mHandler.removeMessages(MSG_WHAT_REQUEST_RESULT);
                } else {
                    showAlertDialog(processMsgFromServer(msg));
                }
                break;
        }
    }

    private String processMsgFromServer(MsgFromServer msg) {

        if (msg == null) {
            return "";
        }
        StringBuffer msgToShow = new StringBuffer(50);
        msgToShow.append(msg.line1).append(TextUtils.isEmpty(msg.line1) ? "" : "\n")
                .append(msg.line2).append(TextUtils.isEmpty(msg.line2) ? "" : "\n");

        return msgToShow.toString();
    }

    private String getContentFromAddressView() {
        return mAddressView.getText().toString();
    }

    private boolean checkAddressContent() {
        String text = getContentFromAddressView();
        if (TextUtils.isEmpty(text)) {
            showAlertDialog(UIUtils.getString(R.string.address_cannot_be_empty));
            return false;
        }
        return true;
    }

    private void sendLinkRequest(boolean isFree) {

        if (!checkAddressContent()) {
            return;
        }

        if (!NetworkUtils.isNetworkAvailable()) {
            showAlertDialog(UIUtils.getString(R.string.networdk_not_available));
            return;
        }

        showProgressDialog(UIUtils.getString(R.string.server_is_processing_please_wait));
        sendRequest(isFree);
    }

    private void sendRequest(final boolean isFree) {
        MsgToSubmit msg = new MsgToSubmit();
        msg.code = isFree ? "1" : "2";
        msg.url = getContentFromAddressView();
        msg.commitMsg = "";

        WebSocketLauncher.getInstance().sendMsgToServer(new Gson().toJson(msg));

        Message handlerMsg = new Message();
        handlerMsg.what = MSG_WHAT_REQUEST_RESULT;
        handlerMsg.obj = isFree;

        if (isFree) {
            MobclickAgent.onEvent(MainActivity.this, UmengEvent.UMENG_EVENT_FREE_SUBMIT);
            mHandler.sendMessageDelayed(handlerMsg, MSG_POLL_DURATION);
        }
    }

    private void showWatchUnderNonWifiAlert() {
        showAlertDialog(UIUtils.getString(R.string.watch_ad_under_non_wifi), UIUtils.getString(R.string.Confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAdActivity();
            }
        }, UIUtils.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void requestAdvertisement() {

        if (!checkAddressContent()) {
            return;
        }

        if (!NetworkUtils.isNetworkAvailable()) {
            showAlertDialog(UIUtils.getString(R.string.networdk_not_available));
            return;
        }

        if (!NetworkUtils.isWiFiConnected()) {
            showWatchUnderNonWifiAlert();
        } else {
            showAdActivity();
        }
    }

    private void showAdActivity() {
        if (!mRewardedVideoAd.isLoaded()) {
            MobclickAgent.onEvent(MainActivity.this, UmengEvent.UMENG_EVENT_SUBMIT_AD_REQUEST);
            showProgressDialog(UIUtils.getString(R.string.sending_ad_request));
            mRewardedVideoAd.loadAd(AdMediatorAccounts.ADMOB_AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, UIUtils.getString(R.string.inviting_title));
        intent.putExtra(Intent.EXTRA_TEXT, UIUtils.getString(R.string.inviting_words, UIUtils.getString(R.string.app_name), UIUtils.getString(R.string.download_address)));
        intent.putExtra(Intent.EXTRA_TITLE, UIUtils.getString(R.string.inviting_title));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ActivityUtils.startActivitySafely(mContext, Intent.createChooser(intent, UIUtils.getString(R.string.inviting_title)));
    }

    private void clearAddressContent() {
        if (mAddressView != null) {
            mAddressView.setText("");
        }
    }

    private void showAboutInfo() {
        showAlertDialog(UIUtils.showAlertDialog(this, R.drawable.lollipop, UIUtils.getString(R.string.about),
                UIUtils.getString(R.string.author_info, UIUtils.getString(R.string.app_name), SystemUtils.getVersionName())));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_submit_btn:

                if (TextUtils.isEmpty(getContentFromAddressView())) {
                    showAlertDialog(UIUtils.getString(R.string.address_cannot_be_empty));
                    return;
                }

                saveAddressHistory(getContentFromAddressView());

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        final boolean isBalls = HttpRequestHelper.simulateClick(getContentFromAddressView());
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isBalls) {
                                        sendLinkRequest(true);
                                    } else {
                                        showAlertDialog(UIUtils.getString(R.string.click_finished_tip));
                                    }

                                }
                            });
                    }
                };

                thread.start();

                break;
            case R.id.ad_submit_btn:
                requestAdvertisement();
                break;
            case R.id.clear_address_btn:
                clearAddressContent();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case RequestCode.REQUEST_CODE_WATCH_AD:
                sendLinkRequest(false);
                break;
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (mRewardedVideoAd.isLoaded()) {
            MobclickAgent.onEvent(MainActivity.this, UmengEvent.UMENG_EVENT_SUBMIT_AD_SUCCESS);
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {}

    @Override
    public void onRewardedVideoStarted() {
        dismissProgressDialog();
        dismissAlertDialog();
    }

    @Override
    public void onRewardedVideoAdClosed() {}

    @Override
    public void onRewarded(RewardItem rewardItem) {
        sendLinkRequest(false);
        saveAddressHistory(getContentFromAddressView());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {}

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        showAlertDialog(UIUtils.getString(R.string.no_ad_available));
        MobclickAgent.onEvent(MainActivity.this, UmengEvent.UMENG_EVENT_SUBMIT_AD_FAILED);
    }
}
