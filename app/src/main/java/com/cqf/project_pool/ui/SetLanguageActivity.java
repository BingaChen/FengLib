package com.cqf.project_pool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqf.fenglib.base.BaseToolbarActivity;
import com.cqf.fenglib.utils.LocalManageUtil;
import com.cqf.project_pool.R;
import com.cqf.project_pool.adapter.ContentsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetLanguageActivity extends BaseToolbarActivity {

    @BindView(R.id.RvLanguage)
    RecyclerView RvLanguage;
    ContentsAdapter adapter;
    List<String> datas=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_language);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setTitle("SetLanguage");
        adapter=new ContentsAdapter(datas);
        adapter.bindRecyclerView(RvLanguage);
    }

    @Override
    public void initData() {
        datas.add("自动");
        datas.add("中文");
        datas.add("English");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 0:
                        selectLanguage(0);
                        break;
                    case 1:
                        selectLanguage(1);
                        break;
                    case 2:
                        selectLanguage(2);
                        break;
                }
            }
        });
    }

    private void selectLanguage(int select) {
        LocalManageUtil.saveSelectLanguage(this, select);
        MainActivity.reStart(this);
    }
}
