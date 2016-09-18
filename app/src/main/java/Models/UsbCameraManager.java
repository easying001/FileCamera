/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Models;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;

import Contracts.UsbCameraContract;
import Contracts.UsbCameraManagerContract;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class UsbCameraManager implements UsbCameraManagerContract.UsbCameraManagement, CameraDialog.CameraDialogParent{
    private static final int CORE_POOL_SIZE = 1;		// initial/minimum threads
    private static final int MAX_POOL_SIZE = 4;			// maximum threads
    private static final int KEEP_ALIVE_TIME = 10;		// time periods while keep the idle thread

    private static UsbCameraManager mInstance = null;
    private SurfaceView mPreviewSurfaceView;
    private Surface mPreviewSurface;
    public USBMonitor mUSBMonitor;
    private UVCCamera mUVCCamera;
    private Context mContext;
    private boolean isActive, isPreview;
    private final Object mSync = new Object();

    protected static final ThreadPoolExecutor EXECUTER
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public UsbCameraManager() {

    }

    public void init(Context context) {
        mContext = context;
        mUSBMonitor = new USBMonitor(mContext, mDeviceConnectListener);
        mUSBMonitor.register();
    }

    public static UsbCameraManager getInstance() {
        if (null == mInstance) {
            synchronized(UsbCameraManager.class) {
                if (null == mInstance) {
                    mInstance = new UsbCameraManager();
                }
            }
        }
        return mInstance;
    }

    public UVCCamera getUVCCamera() {
        return mUVCCamera;
    }

    public void destoryUVCCamera() {
        synchronized (mSync) {
            mUVCCamera.destroy();
            mUVCCamera = null;
            isActive = isPreview = false;
        }
    }

    private final USBMonitor.OnDeviceConnectListener mDeviceConnectListener = new USBMonitor.OnDeviceConnectListener() {
        @Override
        public void onAttach(UsbDevice device) {

        }

        @Override
        public void onDettach(UsbDevice device) {

        }

        @Override
        public void onConnect(UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock, boolean createNew) {
            Log.d("UsbCamera", "onConnect");
            synchronized(mSync) {
                if (mUVCCamera != null) {
                    mUVCCamera.destroy();

                }
            }

            EXECUTER.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized(mSync) {
                        mUVCCamera = new UVCCamera();
                        mUVCCamera.open(ctrlBlock);
                        Log.d("UsbCamera", "SupportedSize: " + mUVCCamera.getSupportedSize());
                        try {
                            mUVCCamera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera
                                    .DEFAULT_PREVIEW_HEIGHT, UVCCamera.FRAME_FORMAT_MJPEG);
                        } catch (final IllegalArgumentException e) {
                            try {
                                mUVCCamera.setPreviewSize(UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera
                                        .DEFAULT_PREVIEW_HEIGHT,UVCCamera.DEFAULT_PREVIEW_MODE);
                            } catch (final IllegalArgumentException e1) {
                                mUVCCamera.destroy();
                                mUVCCamera = null;
                            }
                        }

                        if ((mUVCCamera != null) && (mPreviewSurface != null)) {
                            isActive = true;
                            mUVCCamera.setPreviewDisplay(mPreviewSurface);
                            mUVCCamera.startPreview();
                            isPreview = true;
                        }
                    }
                }
            });
        }

        @Override
        public void onDisconnect(UsbDevice device, USBMonitor.UsbControlBlock ctrlBlock) {

        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    public void setPreviewSurfaceView(SurfaceView view) {
        mPreviewSurfaceView = view;
        mPreviewSurfaceView.getHolder().addCallback(mSurfaceCallback);
    }

    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d("UsbCamera", "surfaceCreated");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if ((width == 0) || (height == 0)) {
                return;
            }

            Log.d("UsbCamera", "surfaceChanged");
            mPreviewSurface = holder.getSurface();
            synchronized(mSync) {
                if (isActive && !isPreview) {
                    mUVCCamera.setPreviewDisplay(mPreviewSurface);
                    mUVCCamera.startPreview();
                    isPreview = true;
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d("UsbCamera", "surfaceDestroyed");
            synchronized(mSync) {
                if (mUVCCamera != null) {
                    mUVCCamera.stopPreview();
                }
                isPreview = false;
            }
            mPreviewSurface = null;
        }
    };

    @Override
    public USBMonitor getUSBMonitor() {
        return mUSBMonitor;
    }
}
