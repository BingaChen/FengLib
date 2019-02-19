package com.cqf.fenglib.base;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqf.fenglib.R;
import com.cqf.fenglib.utils.MyUtils;


/**
 * Created by wpc on 2018/1/6.
 */

public abstract class BaseFragment extends Fragment implements BaseView {


    protected String TAG = getClass().getSimpleName();
    protected String title;
    protected Activity mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        Log.v(TAG, "onActivityCreated()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(TAG, "onAttach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()");
    }

    public void onBack() {
        getActivity().finish();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void showToast(String msg) {
        MyUtils.showToast(mContext,msg);
    }

    @Override
    public void showLongToast(String msg) {
        MyUtils.showLongToast(mContext,msg);
    }

    @Override
    public void showDebug(String msg) {
        MyUtils.showMyLog(msg);
    }

    Dialog gifdialog;

    @Override
    public void showProgressGIF(int resId) {

    }

    @Override
    public void dismissProgressGIF() {
        if (gifdialog != null && gifdialog.isShowing()) {
            gifdialog.dismiss();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        MyUtils.showMyLog("isHidden:" + hidden + "");
    }
}
