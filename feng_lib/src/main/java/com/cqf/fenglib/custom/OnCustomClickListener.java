package com.cqf.fenglib.custom;

import android.view.View;

/**
 * Created by Binga on 2/3/2019.
 */

public abstract class OnCustomClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 300L;

    public OnCustomClickListener() {

    }

    public OnCustomClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick();
            mLastClickTime = nowTime;
        } else {
            // 快速点击事件
            onFastClick();
        }
    }

    protected abstract void onSingleClick();

    protected abstract void onFastClick();
}
