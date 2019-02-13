package com.cqf.project_pool.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cqf.project_pool.R;

import java.util.List;

/**
 * Created by Binga on 8/30/2018.
 */

public class ContentsAdapter extends BaseAdapter<String,BaseViewHolder> {
    public ContentsAdapter(@Nullable List<String> data) {
        super(R.layout.item_simple_text_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tvItem,item);
    }
}
