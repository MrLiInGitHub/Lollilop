package org.fastgame.bbt.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import org.fastgame.bbt.BBT;
import org.fastgame.bbt.R;

import java.util.Map;

/**
 * 地址输入框中成功提交的历史地址记录
 *
 * Created by dawni on 2016/9/5.
 */
public class AddressHistoryPreference {
    private SharedPreferences mSharedPreferences;

    public AddressHistoryPreference() {
        mSharedPreferences = BBT.getAppContext().getSharedPreferences("addressHistory.ini", Context.MODE_PRIVATE);
    }

    public int getFrequency(String address) {
        if (TextUtils.isEmpty(address) || mSharedPreferences == null) {
            return 0;
        }

        return mSharedPreferences.contains(address) ? mSharedPreferences.getInt(address, 0) : 0;
    }

    public boolean addAddressHistory(String address) {
        if (TextUtils.isEmpty(address) || mSharedPreferences == null) {
            return false;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(address, getFrequency(address) + 1);
        return editor.commit();
    }

    public ArrayAdapter getAllAddressHistory() {

        Map<String, ?> addressHistories = mSharedPreferences.getAll();
        addressHistories.keySet();

        return new ArrayAdapter(BBT.getAppContext(), R.layout.item_address_history, addressHistories.keySet().toArray());
    }

}
