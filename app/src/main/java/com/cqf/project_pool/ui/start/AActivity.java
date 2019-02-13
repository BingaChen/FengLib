package com.cqf.project_pool.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.cqf.fenglib.base.BaseActivity;
import com.cqf.fenglib.customView.CountdownTextView;
import com.cqf.project_pool.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AActivity extends BaseActivity {

    @BindView(R.id.tvCountdown)
    CountdownTextView tvCountdown;
    @BindView(R.id.etType)
    EditText etType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        ButterKnife.bind(this);
        startActivity(new Intent(context, BActivity.class));
        startActivity(new Intent(context, CActivity.class));

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick(R.id.tvCountdown)
    public void onViewClicked() {
        tvCountdown.countDown(5, new CountdownTextView.CountdownCallback() {
            @Override
            public void complete() {
                tvCountdown.setText("click");
                if (!TextUtils.isEmpty(etType.getText().toString())){
                    startActivity(new Intent(context, BActivity.class));
                }else {
                    startActivity(new Intent(context, CActivity.class));
                }
            }
        });
    }
}
