package com.hcs.testviewbasic.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hcs.testviewbasic.R;

public class TestScrollView extends TextView {
    private static final String TAG = TestScrollView.class.getSimpleName();
    private final Animation mAnimation;
    private final ObjectAnimator mObjectAnimator;
    private int mLastAction;

    public TestScrollView(Context context) {
        this(context, null);
    }

    public TestScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TestScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.test_translate);

        mObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", 0, 100);
        mObjectAnimator.setDuration(1000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && mLastAction == MotionEvent.ACTION_DOWN) {
            // startAnimation(mAnimation);
            mObjectAnimator.start();
        }
        mLastAction = event.getAction();

        return super.onTouchEvent(event);
    }
}
