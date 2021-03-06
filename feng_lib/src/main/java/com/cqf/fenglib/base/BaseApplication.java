package com.cqf.fenglib.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;

import com.cqf.fenglib.utils.LocalManageUtil;

import java.util.ArrayList;

/**
 * Created by Binga on 9/4/2018.
 */

public abstract class BaseApplication extends Application{

    public ArrayList<BaseActivity> activities;
    public static BaseApplication application;
    @Override
    protected void attachBaseContext(Context base) {
        //保存系统选择语言
        LocalManageUtil.saveSystemCurrentLanguage(base);
        super.attachBaseContext(LocalManageUtil.setLocal(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //保存系统选择语言
        LocalManageUtil.onConfigurationChanged(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        LocalManageUtil.setApplicationLanguage(this);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    public static BaseApplication getInstance(){
        return application;
    }

    public void addActivity(BaseActivity activity) {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        activities.add(activity);
    }

    public void removeActivity(BaseActivity activity) {
        activities.remove(activity);
    }

    public void exit() {
        for (BaseActivity item : activities) {
            item.finish();
        }
    }

    public abstract void setBack();
    public abstract void setAppTheme();
    public abstract void setOrientation();
    public abstract void showLoading();
    public abstract void dismissLoading();
}
