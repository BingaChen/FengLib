package com.cqf.project_pool;

import com.cqf.fenglib.base.BaseApplication;
import com.cqf.fenglib.utils.UiUitls;

/**
 * Created by Binga on 9/4/2018.
 */

public class MyApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        UiUitls.setBackResId(R.drawable.back);
        UiUitls.setThemeResId(R.style.AppTheme1);
    }
}
