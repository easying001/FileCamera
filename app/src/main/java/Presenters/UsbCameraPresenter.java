/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Presenters;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;

import Contracts.UsbCameraContract;
import Models.UsbCameraManager;


import android.util.Log;
import android.view.Surface;


/**
 * Created by yangjie11 on 2016/9/7.
 */

public class UsbCameraPresenter implements UsbCameraContract.Presenter, UsbCameraContract.ViewListener, CameraDialog.CameraDialogParent {
    private static final String TAG = UsbCameraPresenter.class.getSimpleName();
    private static final int CORE_POOL_SIZE = 1;		// initial/minimum threads
    private static final int MAX_POOL_SIZE = 4;			// maximum threads
    private static final int KEEP_ALIVE_TIME = 10;		// time periods while keep the idle thread
    private static final boolean DEBUG = true;

    private final UsbCameraContract.View mView;
    private UsbCameraManager mUsbCameraManager;

    private USBMonitor.UsbControlBlock mControlBlock;
    // for accessing USB and USB camera
    private USBMonitor mUSBMonitor;
    private UVCCamera mUVCCamera;
    private Surface mPreviewSurface;
    private boolean isActive, isPreview;
    private final Object mSync = new Object();
    protected static final ThreadPoolExecutor EXECUTER
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public UsbCameraPresenter(UsbCameraContract.View view) {
        Log.d("UsbFile", "construct UsbCameraPresenter");
        mView = view;
        mView.setPresenter(this);
        mView.setViewListener(this);
        mUsbCameraManager = UsbCameraManager.getInstance();
        mUsbCameraManager.init(mView.getParentActivity());
        mUsbCameraManager.setPreviewSurfaceView(mView.getSurfaceView());
    }
    @Override
    public void openCamera() {
    }

    @Override
    public void closeCamera() {
        if (DEBUG) {
            Log.d(TAG, "close camera");
        }

        synchronized(mSync) {
            if (mUVCCamera != null) {
                mUVCCamera.destroy();
                mUVCCamera = null;
            }
            isActive = isPreview = false;
        }

        if (mUSBMonitor !=null) {
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }

    }



    @Override
    public void start() {

    }

    @Override
    public void onResume() {


//        if (mUVCCamera == null) {
//            // XXX calling CameraDialog.showDialog is necessary at only first time(only when app has no permission).
//            CameraDialog.showDialog(mView.getParentActivity());
//        } else {
//            synchronized (mSync) {
//                mUVCCamera.destroy();
//                mUVCCamera = null;
//                isActive = isPreview = false;
//            }
//        }
    }

    @Override
    public void onActivityCreated() {
        Log.d("UsbFile", "Usb Camera onActivityCreated");

    }

    @Override
    public void onViewCreated() {
        Log.d("UsbCamera", "Usb Camera onViewCreated");

    }

    @Override
    public void onClick(int key) {

    }

    @Override
    public USBMonitor getUSBMonitor() {
        return mUsbCameraManager.getUSBMonitor();
    }
}
