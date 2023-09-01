package com.hcs.testviewanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hcs.testviewanimation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initClickListener();
    }

    private void initClickListener() {
        mBinding.btnAnimationSet.setOnClickListener(this);
        mBinding.btnAnimationTranslate.setOnClickListener(this);
        mBinding.btnAnimationScale.setOnClickListener(this);
        mBinding.btnAnimationAlpha.setOnClickListener(this);
        mBinding.btnAnimationRotate.setOnClickListener(this);
        mBinding.btnAnotherActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Animation animation;
        if (mBinding.btnAnimationSet == v) {
            animation = AnimationUtils.loadAnimation(this, R.anim.animation_set);
        } else if (mBinding.btnAnimationTranslate == v) {
            animation = AnimationUtils.loadAnimation(this, R.anim.animation_translate);
        } else if (mBinding.btnAnimationScale == v) {
            animation = AnimationUtils.loadAnimation(this, R.anim.animation_scale);
        } else if (mBinding.btnAnimationAlpha == v) {
            animation = AnimationUtils.loadAnimation(this, R.anim.animation_alpha);
        } else if (mBinding.btnAnimationRotate == v) {
            animation = AnimationUtils.loadAnimation(this, R.anim.animation_rotate);
        } else if (mBinding.btnAnotherActivity == v) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.animation_enter_2_next, R.anim.animation_exit_2_next);
            return;
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.animation_set);
        }
        animation.setFillAfter(true);
        animation.setDuration(1000);
        v.startAnimation(animation);
    }
}