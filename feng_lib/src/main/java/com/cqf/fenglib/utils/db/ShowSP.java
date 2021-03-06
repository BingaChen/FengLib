package com.cqf.fenglib.utils.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.cqf.fenglib.utils.MyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public <T> T getObject(String key,Class<T> object){

        String content=preferences.getString(key,"");
        if (TextUtils.isEmpty(content)){
            return null;
        }
        getObject("",MyUtils.class);
        return new Gson().fromJson(content, object);
    }
    //Type type=new TypeToken<List<?>>(){}.getType();
    public ArrayList getArrayList(String key, final Type type){
        String content=preferences.getString(key,"");
        if (TextUtils.isEmpty(content)){
            return null;
        }
        return new Gson().fromJson(content,type);
    }

    public void putObject(String key,Object object){
        if (object==null){
            return;
        }
        String content=new Gson().toJson(object);
        Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(key, content);
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
