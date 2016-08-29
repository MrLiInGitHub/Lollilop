package org.fastgame.bbt.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.autoupdate.IFlytekUpdate;
import com.iflytek.autoupdate.IFlytekUpdateListener;
import com.iflytek.autoupdate.UpdateConstants;
import com.iflytek.autoupdate.UpdateErrorCode;
import com.iflytek.autoupdate.UpdateInfo;
import com.iflytek.autoupdate.UpdateType;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.R;
import org.fastgame.bbt.connectivity.WebSocketLauncher;
import org.fastgame.bbt.constant.RequestCode;
import org.fastgame.bbt.entity.MsgFromServer;
import org.fastgame.bbt.entity.MsgToSubmit;
import org.fastgame.bbt.event.SocketEvent;
import org.fastgame.bbt.utility.ActivityUtils;
import org.fastgame.bbt.utility.NetworkUtils;
import org.fastgame.bbt.utility.UIUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * The main Activity is for user operation to interact.
 *
 * @Author MrLi
 * @Since 08/20/2016
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int MSG_POLL_DURATION = 2000;
    private static final int MSG_WHAT_REQUEST_RESULT = 1;

    private final Context mContext = BBT.getAppContext();

    private EditText mAddressView;
    private Button mFreeSubmitBtn;
    private Button mAdSubmitBtn;
    private TextView mTipView;
    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

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
                    showAlertDialog(UIUtils.getString(R.string.no_need_to_update));
                    return;
                }
                mUpdateManager.showUpdateInfo(MainActivity.this, updateInfo);
            } else {
                showAlertDialog(UIUtils.getString(R.string.requesting_update_error, i));
            }
        }
    };

    private void showAlertDialog(CharSequence message, CharSequence confirmBtnText, DialogInterface.OnClickListener confirmBtnListener,
                                 CharSequence cancelBtnText, DialogInterface.OnClickListener cancelBtnListener) {

        dismissProgressDialog();
        dismissAlertDialog();

        mAlertDialog = UIUtils.showAlertDialog(this, message, confirmBtnText, confirmBtnListener, cancelBtnText, cancelBtnListener);

        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    private void showAlertDialog(CharSequence message) {
        dismissProgressDialog();
        dismissAlertDialog();

        mAlertDialog = UIUtils.showAlertDialog(this, message);

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

    private void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
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
        requestFullScreen();

        initView();
        checkUpdate();

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

    private void initView() {
        mAddressView = (EditText) findViewById(R.id.address_text);
        mFreeSubmitBtn = (Button) findViewById(R.id.free_submit_btn);
        mAdSubmitBtn = (Button) findViewById(R.id.ad_submit_btn);
        mTipView = (TextView) findViewById(R.id.tip);

        mAdSubmitBtn.setOnClickListener(this);
        mFreeSubmitBtn.setOnClickListener(this);

        mTipView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/cuhuoyijianti.ttf"));
    }

    private void checkUpdate() {
        mUpdateManager = IFlytekUpdate.getInstance(mContext);
        mUpdateManager.setDebugMode(true);
        mUpdateManager.setParameter(UpdateConstants.EXTRA_WIFIONLY, "true");
        mUpdateManager.setParameter(UpdateConstants.EXTRA_NOTI_ICON, "true");
        mUpdateManager.setParameter(UpdateConstants.EXTRA_STYLE, UpdateConstants.UPDATE_UI_DIALOG);

        mUpdateManager.autoUpdate(mContext, mIFlytekUpdateListener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SocketEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getType()) {
            case SocketEvent.SOCKET_ON_CONNECTED:
//                showAlertDialog(UIUtils.getString(R.string.connect_to_server_successfully));
                WebSocketLauncher.getInstance().sendAllWaitingMessage(event.getWebSocket());
                break;
            case SocketEvent.SOCKET_ON_TEXT_MESSAGE:
                MsgFromServer msg = new Gson().fromJson(event.getText(), MsgFromServer.class);

                if (msg != null && !TextUtils.isEmpty(msg.line0) && msg.line0.equals("success")) {
                    showAlertDialog(processMsgFromServer(msg));
                    isRequestSuccessful = true;
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
        ActivityUtils.startAdColonyActivityForResult(this, RequestCode.REQUEST_CODE_WATCH_AD);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.free_submit_btn:
                sendLinkRequest(true);
                break;
            case R.id.ad_submit_btn:
                requestAdvertisement();
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

}
