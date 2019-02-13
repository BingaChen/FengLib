package com.cqf.project_pool.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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

public class AlertDialogActivity extends BaseToolbarActivity {

    @BindView(R.id.RvDialog)
    RecyclerView RvDialog;
    ContentsAdapter adapter;
    List<String> datas=new ArrayList<>();
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alert_dialog);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setTitle("AlertDialog");
        adapter=new ContentsAdapter(datas);
        adapter.bindRecyclerView(RvDialog);
    }

    @Override
    public void initData() {
        datas.add("NormalDialog");
        datas.add("CustomDialog");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 0:
                        initNormalDialog();
                        break;
                    case 1:
                        initCustomDialog(R.layout.custom_alert_dialog_layout);
                        break;
                }
            }
        });
    }

    private void initCustomDialog(int resId) {
        View customView=LayoutInflater.from(context).inflate(resId,null);
        dialog=new AlertDialog.Builder(context)
                .setView(customView)
                .create();
        TextView tvCancel=customView.findViewById(R.id.tvCancel);
        TextView tvSure=customView.findViewById(R.id.tvSure);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.showToast(context,"do something!");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initNormalDialog() {
        dialog=new AlertDialog.Builder(context)
                .setTitle("This is a title!")
                .setMessage("This is a message!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyUtils.showToast(context,"Click Ok!Then do something.");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
