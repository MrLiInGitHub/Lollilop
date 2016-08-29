package org.fastgame.bbt.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.androidquery.util.Progress;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.R;

/**
 * @Author MrLi
 * @Since 08/22/2016
 */
public class UIUtils {

    public static void showToast(CharSequence textToShow, boolean isLong) {
        Toast.makeText(BBT.getAppContext(), textToShow, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public static void showToast(CharSequence textToShow) {
        showToast(textToShow, false);
    }

    public static void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    public static void showLongToast(CharSequence textToShow) {
        showToast(textToShow, true);
    }

    public static void showLongToast(@StringRes int resId) {
        showLongToast(getString(resId));
    }

    public static String getString(@StringRes int resId) {
        return BBT.getAppContext().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... args) {
        return BBT.getAppContext().getString(resId, args);
    }

    public static AlertDialog showAlertDialog(Context context, CharSequence message, DialogInterface.OnClickListener confirmListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setIcon(R.drawable.lollipop);
        alertDialog.setTitle(UIUtils.getString(R.string.Warm_Tip));
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, UIUtils.getString(R.string.Confirm), confirmListener);

        return alertDialog;
    }

    public static AlertDialog showAlertDialog(Context context, CharSequence message) {
        return showAlertDialog(context, message, null);
    }

    public static AlertDialog showAlertDialog(Context context, CharSequence message,
                                              CharSequence confirmBtnText, DialogInterface.OnClickListener confirmBtnListener,
                                              CharSequence cancelBtnText, DialogInterface.OnClickListener cancelBtnListener) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setIcon(R.drawable.lollipop);
        alertDialog.setTitle(UIUtils.getString(R.string.Warm_Tip));
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, confirmBtnText, confirmBtnListener);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancelBtnText, cancelBtnListener);

        return alertDialog;
    }

    public static ProgressDialog showProgressDialog(Context context, CharSequence message, DialogInterface.OnClickListener confirmListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setIcon(R.drawable.lollipop);
        progressDialog.setTitle(UIUtils.getString(R.string.Warm_Tip));
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, UIUtils.getString(R.string.Confirm), confirmListener);

        return progressDialog;
    }

    public static ProgressDialog showProgressDialog(Context context, CharSequence message) {
        return showProgressDialog(context, message, null);
    }

}
