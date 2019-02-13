package com.cqf.project_pool.ui;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqf.fenglib.base.BaseToolbarActivity;
import com.cqf.fenglib.utils.MyUtils;
import com.cqf.project_pool.R;
import com.cqf.project_pool.adapter.ContentsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopupWindowActivity extends BaseToolbarActivity {

    @BindView(R.id.RvPopupWindow)
    RecyclerView RvPopupWindow;
    ContentsAdapter adapter;
    List<String> datas=new ArrayList<>();

    PopupWindow popupWindow;
    View popupView;
    View parentView;
    TextView tvCancel,tvSure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_popup_window);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setTitle("PopupWindow");
        adapter=new ContentsAdapter(datas);
        adapter.bindRecyclerView(RvPopupWindow);
        popupView= LayoutInflater.from(context).inflate(R.layout.custom_popup_window_layout,null);
        parentView= LayoutInflater.from(context).inflate(R.layout.activity_popup_window,null);
        initPopupWindow(popupView);
    }

    private void initPopupWindow(View popupView) {
        popupWindow=new PopupWindow(context);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        tvCancel=popupView.findViewById(R.id.tvCancel);
        tvSure=popupView.findViewById(R.id.tvSure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.showToast(context,"do something!");
                popupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void initData() {
        datas.add("Top");
        datas.add("LeftBottom");
        datas.add("CenterRight");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 0:
                        popupWindow.showAtLocation(parentView, Gravity.TOP,0,0);
                        break;
                    case 1:
                        popupWindow.showAtLocation(parentView, Gravity.LEFT|Gravity.BOTTOM,0,0);
                        break;
                    case 2:
                        popupWindow.showAtLocation(parentView, Gravity.CENTER|Gravity.RIGHT,0,0);
                        break;
                }
            }
        });
    }
}
