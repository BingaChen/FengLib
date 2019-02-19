package com.cqf.fenglib.customView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cqf.fenglib.utils.UiUtils;

/**
 * Created by wpc on 2018/7/12.
 */

public class StatusBarPlaceHolderView extends View {
    public StatusBarPlaceHolderView(Context context) {
        super(context);
    }

    public StatusBarPlaceHolderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarPlaceHolderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(Build.VERSION.SDK_INT>=19){
            setMeasuredDimension(widthMeasureSpec,  UiUtils.getStatusBarHeight(getContext()));
        }else {
            setMeasuredDimension(widthMeasureSpec,  0);
        }
    }
}
