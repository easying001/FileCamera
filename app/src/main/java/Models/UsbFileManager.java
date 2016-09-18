/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.camera.easying.filecamera.UsbFileListAdapter;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;

import Contracts.UsbFileContract;
import Contracts.UsbFileManagerContract;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class UsbFileManager implements UsbFileManagerContract.DeviceManagement {
    private static final String ACTION_USB_PERMISSION = "com.github.mjdev.libaums.USB_PERMISSION";
    private static final String TAG = "UsbFile";//UsbFileManager.class.getSimpleName();
    public static final int STATE_IDLE = 0;
    public static final int STATE_DISCOVERED = 1;
    public static final int STATE_GRANTED = 2;
    public static final int STATE_SETUPED = 3;

    private Context mContext;
    private UsbMassStorageDevice usbMassStorageDevice;
    private UsbFileManagerContract.DeviceStateCallback mDeviceStateCallback;
    private static UsbFileManager mInstance;
    private int mDeviceState = STATE_IDLE;

    public UsbFileManager() {
        Log.d("UsbFile", "construct UsbFileManager");
    }

    public static UsbFileManager getInstance() {
        if (null == mInstance) {
            synchronized(UsbFileManager.class) {
                if (null == mInstance) {
                    mInstance = new UsbFileManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context, UsbFileManagerContract.DeviceStateCallback callback) {
        mContext = context;
        setCallback(callback);
        register();
        Log.d("UsbFile", "UsbFileManager.init()");
    }

    public UsbMassStorageDevice getUsbMassStorageDevice() {
        return usbMassStorageDevice;
    }

    public int getmDeviceState() {
        return mDeviceState;
    }
    @Override
    public void setCallback(UsbFileManagerContract.DeviceStateCallback callback) {
        mDeviceStateCallback = callback;
    }

    @Override
    public void register() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        //Log.d(TAG, "mView.getParentActivity = " + mContext.getParentActivity());
        mContext.registerReceiver(usbReceiver, filter);
    }

    @Override
    public void unregister() {

    }

    public List<UsbMassStorageDevice> getMassStorageDevices() {

        if (usbMassStorageDevice == null) {
            discoverDevice();
        }
        //UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(mContext);
        Log.d(TAG, "devices = " + devices);

        return Arrays.asList(devices);

    }


    @Override
    public void discoverDevice() {
        UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        Log.d(TAG, "usb manager = " + usbManager);
        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(mContext);

        if (devices.length == 0) {
            Log.d(TAG, "no device found!");
            if (mDeviceStateCallback != null) {
                mDeviceStateCallback.onDeviceNotFound();
            }
            //listView.setAdapter(null);
            return;
        }

        // we only use the first device
        usbMassStorageDevice = devices[0];
        mDeviceState = STATE_DISCOVERED;
        Activity activity = (Activity)mContext;
        UsbDevice usbDevice = (UsbDevice) activity.getIntent().getParcelableExtra(UsbManager
                .EXTRA_DEVICE);

        if (usbDevice != null && usbManager.hasPermission(usbDevice)) {
            Log.d(TAG, "received usb device via intent");
            // requesting permission is not needed in this case
            setupDevice();
        } else {
            // first request permission from user to communicate with the
            // underlying
            // UsbDevice
            PendingIntent permissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(
                    ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(usbMassStorageDevice.getUsbDevice(), permissionIntent);
        }

    }

    @Override
    public void setupDevice() {
        try {
            usbMassStorageDevice.init();

            // we always use the first partition of the device
            FileSystem fs = usbMassStorageDevice.getPartitions().get(0).getFileSystem();
            Log.d(TAG, "Capacity: " + fs.getCapacity());
            Log.d(TAG, "Occupied Space: " + fs.getOccupiedSpace());
            Log.d(TAG, "Free Space: " + fs.getFreeSpace());
            UsbFile root = fs.getRootDirectory();
            //mView.showToast("Capacity = " + fs.getCapacity() + "Free Space:" + fs.getFreeSpace());
            //            ActionBar actionBar = getSupportActionBar();
            //            actionBar.setTitle(fs.getVolumeLabel());
            if (mDeviceStateCallback != null) {
                mDeviceStateCallback.onDeviceSetup(root);
            }
            mDeviceState = STATE_SETUPED;
            //adapter = new UsbFileListAdapter(mView.getParentActivity(), root);
            //mView.setListAdapter(adapter);
        } catch (IOException e) {
            Log.e(TAG, "error setting up device", e);
        }
    }

    /*
        Broadcast receiver to monitor the connection state for USB storage device
        STATE : Permission Granted, Device Attached, Device Detached
     */

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {

                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    mDeviceState = STATE_GRANTED;
                    if (device != null) {
                        Log.d(TAG, "device = " + device + ",permission granted");
                        setupDevice();
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Log.d(TAG, "USB device attached");

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    discoverDevice();
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                Log.d(TAG, "USB device detached");

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    if (usbMassStorageDevice != null) {
                        usbMassStorageDevice.close();
                        usbMassStorageDevice = null;
                        mDeviceState = STATE_IDLE;
                    }
                    // check if there are other devices or set action bar title
                    // to no device if not
                    discoverDevice();
                }
            }

        }
    };
}
