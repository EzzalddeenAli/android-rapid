package com.lh.rapid.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import static android.R.attr.defaultValue;

/**
 * Created by WE-WIN-027 on 2016/9/1.
 *
 * @des 管理SharedPreferences
 */
public class SPUtil {

    private final String LOTER_DATA = "loter_data";

    private final String IS_LOGIN = "is_login";
    private final String TOKNE = "token";
    private final String USER = "user";
    private final String REGISTRATIONID = "RegistrationID";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String FLGSETPAY = "flgsetpay";
    private final String FIRST_LOGIN = "first_login";
    private final String FLG_MALL = "flg_mall";
    private final String CIRCLE_ID = "circle_id";
    private final String DICTIONARY_DATA = "Dictionary_data";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Inject
    public SPUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(LOTER_DATA, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void save(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    private void save(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    private void save(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void save(String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.commit();
    }

    private double getDouble(String key) {
        return Double.longBitsToDouble(sharedPreferences.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private void save(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    private String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    private int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    private long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    private boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    //================================================================

    public void setIS_LOGIN(int is_login) {
        save(IS_LOGIN, is_login);
    }

    public int getIS_LOGIN() {
        return getInt(IS_LOGIN);
    }

    public void setTOKNE(String token) {
        save(TOKNE, token);
    }

    public String getTOKNE() {
        return getString(TOKNE);
    }

    public void setCIRCLE_ID(int circle_id) {
        save(CIRCLE_ID, circle_id);
    }

    public int getCIRCLE_ID() {
        return getInt(CIRCLE_ID);
    }

    public void setDICTIONARY_DATA(String dictionary_data) {
        save(DICTIONARY_DATA, dictionary_data);
    }

    public String getDICTIONARY_DATA() {
        return getString(DICTIONARY_DATA);
    }

    public void setUSER(String user) {
        save(USER, user);
    }

    public String getUSER() {
        return getString(USER);
    }

    public void setREGISTRATIONID(String registrationID) {
        save(REGISTRATIONID, registrationID);
    }

    public String getREGISTRATIONID() {
        return getString(REGISTRATIONID);
    }

    public void setLATITUDE(double latitude) {
        save(LATITUDE, latitude);
    }

    public double getLATITUDE() {
        return getDouble(LATITUDE);
    }

    public void setLONGITUDE(double longitude) {
        save(LONGITUDE, longitude);
    }

    public double getLONGITUDE() {
        return getDouble(LONGITUDE);
    }

    public void setFLGSETPAY(int flgsetpay) {
        save(FLGSETPAY, flgsetpay);
    }

    public int getFLGSETPAY() {
        return getInt(FLGSETPAY);
    }

    public void setFIRST_LOGIN(int first_login) {
        save(FIRST_LOGIN, first_login);
    }

    public int getFIRST_LOGIN() {
        return getInt(FIRST_LOGIN);
    }

    public void setFLG_MALL(int flg_mall) {
        save(FLG_MALL, flg_mall);
    }

    public int getFLG_MALL() {
        return getInt(FLG_MALL);
    }
}
