package com.cqf.fenglib.utils.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Binga on 6/20/2018.
 */

public class ShowSP {
    private static final String SYS_CONF = "Configuration";

    /**
     * 用户配置
     */
    public static ShowSP instance;

    private SharedPreferences preferences;
    private ShowSP(Context context) {
        preferences = context.getSharedPreferences(SYS_CONF,
                Context.MODE_PRIVATE);
    }

    public static ShowSP getInstance(Context context) {
        if (instance == null) {
            instance = new ShowSP(context);
        }
        return instance;
    }

    public boolean containKey(String key) {
        return preferences.contains(key);
    }

    public void clear() {
        Editor preferencesEditor = preferences.edit();
        preferencesEditor.clear();
        preferencesEditor.commit();
    }

    public int getInt(String key, int defaultInt){
        return preferences.getInt(key, defaultInt);
    }

    public void putInt(String key, int intValue){
        Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(key, intValue);
        preferencesEditor.commit();
    }

    public long getLong(String key, long defaultLong){
        return preferences.getLong(key, defaultLong);
    }

    public void putLong(String key, long longValue){
        Editor preferencesEditor = preferences.edit();
        preferencesEditor.putLong(key, longValue);
        preferencesEditor.commit();
    }

    public String getString(String key, String defaultStr){
        return preferences.getString(key,defaultStr);
    }

    public void putString(String key, String strValue){
        Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(key, strValue);
        preferencesEditor.commit();
    }

    public boolean getBoolean(String key, boolean defaultStr){
        return preferences.getBoolean(key,defaultStr);
    }

    public void putBoolean(String key, boolean strValue){
        Editor preferencesEditor = preferences.edit();
        preferencesEditor.putBoolean(key, strValue);
        preferencesEditor.commit();
    }

    public float getFloat(String key, float defaultValue){
        return preferences.getFloat(key,defaultValue);
    }

    public void putFloat(String key,float value){
        Editor preferencesEditor = preferences.edit();
        preferencesEditor.putFloat(key, value);
        preferencesEditor.commit();
    }

}
