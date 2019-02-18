package com.cqf.fenglib.utils.db;

import android.content.Context;
import android.os.Build;

/**
 * Created by Binga on 2/18/2019.
 */

public class ThemeUtils {
    public void SetCustomTheme(Context context) {
        //区分android版本,根据不同版本设置主题
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 19) {

        } else if (sdkVersion >= 19 && sdkVersion < 21) {//19分界线

        } else if (sdkVersion >= 21 && sdkVersion < 23) {//21分界线

        } else if (sdkVersion >= 23) {//23分界线

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(Color.BLACK);
//            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
//            //getWindow().setNavigationBarColor(Color.BLUE);
        }
//        context.setTheme();
    }
}
