package com.cqf.project_pool;

import android.content.pm.ActivityInfo;

import com.cqf.fenglib.base.BaseApplication;
import com.cqf.fenglib.utils.UiUtils;

/**
 * Created by Binga on 9/4/2018.
 */

public class MyApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        setBack();
        setAppTheme();
        setOrientation();
    }

    @Override
    public void setBack() {
        UiUtils.setBackResId(R.drawable.back);
    }

    @Override
    public void setAppTheme() {
        UiUtils.setThemeResId(R.style.AppTheme1);
    }

    @Override
    public void setOrientation() {
        UiUtils.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
