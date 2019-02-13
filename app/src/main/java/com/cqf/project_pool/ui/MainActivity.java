package com.cqf.project_pool.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cqf.fenglib.aop.DoubleClick;
import com.cqf.fenglib.base.BaseActivity;
import com.cqf.project_pool.R;
import com.cqf.project_pool.adapter.ContentsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    ContentsAdapter contentsAdapter;
    List<String> mainList = new ArrayList<>();

    @BindView(R.id.RvMain)
    RecyclerView RvMain;
    @BindView(R.id.tvDoubleClick)
    TextView tvDoubleClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        MyUtils.initLocaleLanguage(MainActivity.this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        tvDoubleClick.setOnClickListener(new View.OnClickListener() {
            @DoubleClick
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AlertDialogActivity.class));
            }
        });
    }

    @Override
    public void initView() {
        contentsAdapter = new ContentsAdapter(mainList);
        contentsAdapter.bindRecyclerView(RvMain);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void reStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void initData() {
        mainList.add(getString(R.string.AlertDialog));
        mainList.add(getString(R.string.PopupWindow));
        mainList.add(getString(R.string.SetLanguage));

        contentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        contentsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Class<?> intentClass = null;
                switch (position) {
                    case 0:
                        intentClass = AlertDialogActivity.class;
                        break;
                    case 1:
                        intentClass = PopupWindowActivity.class;
                        break;
                    case 2:
                        intentClass = SetLanguageActivity.class;
                        break;
                }
                if (intentClass != null) {
                    startActivity(new Intent(context, intentClass));
                }
            }
        });
    }

}
