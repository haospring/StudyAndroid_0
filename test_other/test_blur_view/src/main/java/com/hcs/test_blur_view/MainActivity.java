package com.hcs.test_blur_view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.hcs.test_blur_view.databinding.ActivityMainBinding;
import com.hcs.test_blur_view.utils.BitmapUtils;
import com.hcs.test_blur_view.utils.BlurUtil;
import com.hcs.test_blur_view.utils.ScreenShotUtil;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mBinding;
    private Map<String, MutableLiveData<Integer>> mIntValueMap = new ArrayMap<>();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        try {
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            SurfaceControl surfaceControl = new SurfaceControl.Builder()
                    .setBufferSize(displayMetrics.widthPixels, displayMetrics.heightPixels)
                    .build();
            Surface surface = new Surface(surfaceControl);
            surface.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBinding.rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdo_btn_open) {
                    Log.d(TAG, "onCheckedChanged open");
                    setIntProperty(123, 456, 1);
                } else if (checkedId == R.id.rdo_btn_close) {
                    Log.d(TAG, "onCheckedChanged close");
                    setIntProperty(123, 456, 0);
                }
            }
        });

        getIntProperty(123, 456).observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 1) {
                    Log.d(TAG, "onChanged integer = 1");
                    mBinding.rdoBtnOpen.setChecked(true);
                } else if (integer == 0) {
                    Log.d(TAG, "onChanged integer = 0");
                    mBinding.rdoBtnClose.setChecked(true);
                } else {
                    Log.d(TAG, "onChanged integer: " + integer);
                }
            }
        });
    }

    private MutableLiveData<Integer> getIntProperty(int propertyId, int areaId) {
        int val = getRandomNumber();
        String key = propertyId + "," + areaId;
        Log.d(TAG, "getIntProperty val = " + val);
        if (mIntValueMap.containsKey(key)) {
            if (mIntValueMap.get(key).getValue() != val) {
                mIntValueMap.get(key).postValue(val);
            }
        } else {
            mIntValueMap.put(key, new MutableLiveData<>(val));
        }
        return mIntValueMap.get(key);
    }

    private void setIntProperty(int propertyId, int areaId, int val) {
        Log.d(TAG, "setIntProperty val = " + val);
        String key = propertyId + "," + areaId;
        mIntValueMap.get(key).postValue(val);
    }

    private int getRandomNumber() {
        return new Random().nextInt(2);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setBlurBackground() {
        Bitmap bitmap = ScreenShotUtil.takeScreenShot(getApplicationContext());
        if (bitmap == null) {
            Log.d(TAG, "setBlurBackground bitmap == null");
            return;
        }
        Bitmap cutBitmap = ScreenShotUtil.convertHardWareBitmap(bitmap);
        Bitmap blurBitmap = BlurUtil.blur(getApplicationContext(), cutBitmap, BlurUtil.BLUR_RADIUS_MAX);
        BitmapUtils.recycleImageView(mBinding.imgBlur);
//        mBinding.imgBlur.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBinding.imgBlur.setImageBitmap(blurBitmap);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onClick(View v) {
        mBinding.imgBlur.setVisibility(View.VISIBLE);
        setBlurBackground();
    }
}