package com.cqf.fenglib.base;

/**
 * 类描述:
 * 创建人: Administrator
 * 创建时间: 2018/3/16 0016 上午 9:03
 * 修改人:
 * 修改时间:
 * 修改备注:
 */

public interface BaseView {

    void init();

    void initView();

    void initData();

    void initListener();

    void showToast(String msg);

    void showLongToast(String msg);

    void showDebug(String msg);

    void showLoading();

    void dismissLoading();

}
