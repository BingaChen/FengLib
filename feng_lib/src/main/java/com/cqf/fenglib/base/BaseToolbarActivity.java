package com.cqf.fenglib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqf.fenglib.Config;
import com.cqf.fenglib.R;

/**
 * Created by Binga on 1/19/2019.
 */

public class BaseToolbarActivity extends BaseActivity{

    protected View titleBarView;
    protected TextView tvTitle, tvRight;
    protected ImageView ivLeft,ivRight, ivRight2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setToolbar() {
        titleBarView =findViewById(R.id.titleToolbar);
        titleBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tvTitle=titleBarView.findViewById(R.id.tvTitle);
        tvRight=titleBarView.findViewById(R.id.tvRight);
        ivLeft=titleBarView.findViewById(R.id.ivLeft);
        ivRight=titleBarView.findViewById(R.id.ivRight);
        ivRight2=titleBarView.findViewById(R.id.ivRight2);
        initBar();
    }

    private void initBar() {
        if (Config.BACK_RES_ID !=0){
            ivLeft.setImageResource(Config.BACK_RES_ID);
        }
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

}
