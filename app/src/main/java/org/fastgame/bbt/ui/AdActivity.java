package org.fastgame.bbt.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import org.fastgame.bbt.R;
import org.fastgame.bbt.utility.UIUtils;

/**
 * Base activity for those who shows advertisement.
 *
 * @Author MrLi
 * @Since 08/25/2016
 */
public class AdActivity extends BaseActivity {

    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;

    private DialogInterface.OnClickListener mCancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            onCanceledResult();
        }
    };

    /**
     * Show the {@link AlertDialog} while dismiss the {@link ProgressDialog}.
     *
     * @param text  the text on the {@link AlertDialog}.
     * @param confirmListener  the Listener of the confirm button.
     */
    protected void showAlertDialog(CharSequence text, DialogInterface.OnClickListener confirmListener) {
        if (mAlertDialog == null) {
            mAlertDialog = UIUtils.showAlertDialog(this, text, confirmListener);
        }

        dismissProgressDialog();

        mAlertDialog.setMessage(text);
        mAlertDialog.show();
    }

    /**
     * Show the {@link AlertDialog} while dismiss the {@link ProgressDialog}.
     *
     * @param text  the text on the {@link AlertDialog}.
     */
    protected void showAlertDialog(CharSequence text) {
        showAlertDialog(text, mCancelListener);
    }

    /**
     * Dismiss the {@link AlertDialog}
     */
    protected void dismissAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    /**
     * Show the {@link ProgressDialog} while dismiss the {@link AlertDialog}.
     *
     * @param text  tht text on the {@link ProgressDialog}
     * @param confirmListener  the listener of the confrim button.
     */
    protected void showProgressDialog(CharSequence text, DialogInterface.OnClickListener confirmListener) {
        if (mProgressDialog == null) {
            mProgressDialog = UIUtils.showProgressDialog(this, text, confirmListener);
        }

        dismissAlertDialog();

        mProgressDialog.setMessage(text);
        mProgressDialog.show();
    }

    /**
     * Show the {@link ProgressDialog} while dismiss the {@link AlertDialog}.
     *
     * @param text  the text on the {@link ProgressDialog}.
     */
    protected void showProgressDialog(CharSequence text) {
        showProgressDialog(text, mCancelListener);
    }

    /**
     * Dismiss the {@link ProgressDialog}.
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void onAdsPlayStart() {
        /** dismiss the Dialogs */
        dismissAlertDialog();
        dismissProgressDialog();
    }

    protected void onAdsPlayFinished() {
        onSuccessfulResult();
    }

    protected void onNoAvailableAds() {
        showAlertDialog(UIUtils.getString(R.string.no_ad_available));
    }

    /** Indicates user finished watching ad. */
    protected void onSuccessfulResult() {
        setResult(RESULT_OK);
        finish();
    }

    /** Indicates user does not finish the ad watching process. */
    protected void onCanceledResult() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestFullScreen();

        showProgressDialog(UIUtils.getString(R.string.sending_ad_request));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dismissAlertDialog();
        dismissProgressDialog();
    }
}
