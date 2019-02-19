package com.cqf.fenglib.utils;

import com.cqf.fenglib.Config;

/**
 * Created by Binga on 2/18/2019.
 */

public class UiUitls {
    public static void setBackResId(int resId){
        Config.BACK_RESID=resId;
    }
    public static void setThemeResId(int resId){
        Config.THEME_RESID=resId;
    }
    public static void setOrientation(int orientation){
        Config.ORIENTATION =orientation;//0横屏，1竖屏
    }
}
