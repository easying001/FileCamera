/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.camera.easying.filecamera;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.NoSuchElementException;

import Contracts.MainActivityContract;
import Presenters.MainActivityPresenter;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class MainActivity extends Activity implements View.OnClickListener, MainActivityContract.View {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageButton mStartPreviewButton;
    private ImageButton mOpenFileSystemButton;
    private ImageButton mSettingButton;

    private MainActivityContract.ViewListener mViewListener;

    public static MainActivityPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        mStartPreviewButton = (ImageButton) findViewById(R.id.ib_start_preview);
        mOpenFileSystemButton = (ImageButton) findViewById(R.id.ib_open_file);
        mSettingButton = (ImageButton) findViewById(R.id.ib_setting);



        mStartPreviewButton.setOnClickListener(this);
        mOpenFileSystemButton.setOnClickListener(this);
        mSettingButton.setOnClickListener(this);

        mPresenter = new MainActivityPresenter(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewListener != null) {
            mViewListener.onResume();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ib_open_file:
                if (mViewListener != null) {
                    mViewListener.onButtonClick(R.id.ib_open_file);
                }
                //gFragmentMananger.showFragment(fileStorageFragment);
                break;
            case R.id.ib_start_preview:
                Log.d("UsbFile", "onClick in Actvity");
                if (mViewListener != null) {
                    mViewListener.onButtonClick(R.id.ib_start_preview);
                }
                //gFragmentMananger.showFragment(cameraPreviewFragment);
                //mCameraPresenter.openCamera();
                break;
            case R.id.ib_setting:
                if (mViewListener != null) {
                    mViewListener.onButtonClick(R.id.ib_setting);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Activity getParentActivity() {
        return this;
    }

    @Override
    public Context getParentContext() {
        return this;
    }

    @Override
    public void showToast(String content) {

    }

    @Override
    public void setViewListener(MainActivityContract.ViewListener listener) {
        mViewListener = listener;
    }

    @Override
    public void showFragment(int type) {

    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onBackPressed() {
        try {
            mViewListener.onBackButtonPressed();
        } catch (NoSuchElementException e) {
            super.onBackPressed();
        }
    }
}
