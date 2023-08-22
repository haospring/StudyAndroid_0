package com.hcs.testviewbasic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hcs.testviewbasic.R;
import com.hcs.testviewbasic.databinding.ActivitySecondBinding;
import com.hcs.testviewbasic.databinding.LayoutContentBinding;
import com.hcs.testviewbasic.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding mBinding;
    private LayoutContentBinding mContentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView() {
        for (int i = 0; i < 2; i++) {
            // 每次循环都需要创建一个layout布局对象
            mContentBinding = LayoutContentBinding.inflate(getLayoutInflater(), mBinding.hsvContainer, false);
            mContentBinding.getRoot().getLayoutParams().width = CommonUtils.getScreenWidth(this);
            mContentBinding.getRoot().getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mContentBinding.tvTitle.setText("Title " + (i + 1));
            initListView();
            mBinding.hsvContainer.addView(mContentBinding.getRoot());
        }
    }

    private void initListView() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("item " + (i + 1));
        }
        @SuppressLint("ResourceType") ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.layout_content_item, R.id.name, list);
        mContentBinding.lvContent.setAdapter(adapter);
    }
}