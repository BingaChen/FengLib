package com.cqf.fenglib.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cqf.fenglib.R;


/**
 * Created by Binga on 9/20/2018.
 */

public class MyAlert {

    static MyAlert alert;
    static AlertDialog dialog;
    static View view;
    static TextView tvMessage, tvCancel, tvSure;
    static Activity activity;

    public MyAlert() {
    }

    public MyAlert Builder(Activity activity){
        this.activity=activity;
        if (alert == null) {
            synchronized (MyAlert.class) {
                if (alert == null) {
                    alert = new MyAlert();
                }
            }
        }
        view = LayoutInflater.from(activity).inflate(R.layout.alert_normal_layout, null);
        dialog = new AlertDialog.Builder(activity, R.style.MyCustomDialogTheme)
                .setView(view)
                .create();
        tvMessage = view.findViewById(R.id.tvMessage);
        tvCancel = view.findViewById(R.id.tvCancel);
        tvSure = view.findViewById(R.id.tvSure);
        return alert;
    }
    public MyAlert setNegativeButton(String tag, View.OnClickListener listener){
        alert.tvCancel.setText(tag);
        alert.tvCancel.setOnClickListener(listener);
        return alert;
    }
    public MyAlert setPositiveButton(String tag, View.OnClickListener listener){
        alert.tvSure.setText(tag);
        alert.tvSure.setOnClickListener(listener);
        return alert;
    }
    public MyAlert setNegativeButton(String tag, String color, View.OnClickListener listener){
        alert.tvCancel.setText(tag);
        alert.tvCancel.setTextColor(Color.parseColor(color));
        alert.tvCancel.setOnClickListener(listener);
        return alert;
    }
    public MyAlert setPositiveButton(String tag, String color, View.OnClickListener listener){
        alert.tvSure.setText(tag);
        alert.tvSure.setTextColor(Color.parseColor(color));
        alert.tvSure.setOnClickListener(listener);
        return alert;
    }
    public MyAlert setMessage(String msg){
        alert.tvMessage.setText(msg);
        return alert;
    }
    public void show(){
        alert.dialog.show();
    }
    public void dismiss(){
        alert.dialog.dismiss();
    }
}
