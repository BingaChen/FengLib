package com.cqf.fenglib.utils;

import android.content.Context;
import android.content.res.Resources;

import com.cqf.fenglib.Config;

/**
 * Created by Binga on 2/18/2019.
 */

public class UiUtils {
    public static void setBackResId(int resId){
        Config.BACK_RESID=resId;
    }

    public static void setThemeResId(int resId){
        Config.THEME_RESID=resId;
    }
    public static void setOrientation(int orientation){
        Config.ORIENTATION =orientation;//0横屏，1竖屏
    }


    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
