package com.cqf.fenglib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.cqf.fenglib.R;


/**
 * Created by wpc on 2018/6/19.
 */

public class BaseFrameMainActivity extends BaseActivity {

    Class<BaseFragment>[] clases;
    BaseFragment[] fragments;

    int position = -1;
    int frameLayoutId;
    FragmentManager mFragmentManager;
    static boolean update;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
    }

    protected void regist(int id,Class... fragmentClass) {
        frameLayoutId=id;
        if (clases == null) {
            clases = fragmentClass;
        }
    }
    public static void referesh(){
        update=true;
    }

    protected void select(int i) {

        /*if (MyUtils.isNetworkConnected(mContext)){
            referesh();
        }*/
        if (position != i) {
            if (position >= 0) {
                mFragmentManager.beginTransaction().hide(fragments[position]).commit();
            }
            position = i;
            if (fragments == null) {
                fragments = new BaseFragment[clases.length];
            }
            if (fragments[i] == null||update) {
                update=false;
                try {
                    fragments[i] = clases[i].newInstance();
                    mFragmentManager.beginTransaction().add(frameLayoutId, fragments[i], clases[i].getSimpleName()).commit();

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            mFragmentManager.beginTransaction().show(fragments[i]).commit();
        }
    }

}
