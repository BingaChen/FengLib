package com.cqf.project_pool;

import android.content.pm.ActivityInfo;

import com.cqf.fenglib.base.BaseApplication;
import com.cqf.fenglib.utils.UiUitls;

/**
 * Created by Binga on 9/4/2018.
 */

public class MyApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void setBack() {
        UiUitls.setBackResId(R.drawable.back);
    }

    @Override
    public void setAppTheme() {
        UiUitls.setThemeResId(R.style.AppTheme1);
    }

    @Override
    public void setOrientation() {
        UiUitls.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
