/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Presenters;

import java.io.IOException;

import com.camera.easying.filecamera.GeneralFragmentManager;
import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbFileListAdapter;
import com.github.mjdev.libaums.fs.UsbFile;

import Contracts.MainActivityContract;
import Models.UsbCameraManager;

import Models.UsbFileManager;
import Views.BaseDialog;

import Views.UsbCameraDeviceDialog;
import Views.UsbCameraFragment;
import Views.UsbFileDeviceDialog;
import Views.UsbFileFragment;
import Views.UsbSettingFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class MainActivityPresenter implements MainActivityContract.ViewListener,
        MainActivityContract.Presenter, UsbFileDeviceDialog.FragmentInterface, UsbCameraDeviceDialog.FragmentInterface {
    private MainActivityContract.View mView;

    private GeneralFragmentManager mFragmentManager;
    //private UsbCameraManager mUsbCameraManager;
    UsbFileListAdapter mAdapter;

    private UsbFileFragment mUsbFileFragment;
    private UsbCameraFragment mUsbCameraFragment;
    private UsbSettingFragment mUsbSettingFragment;

    public MainActivityPresenter(MainActivityContract.View view) {
        Log.d("UsbFile", "construct MainActivityPresenter");
        mView = view;
        mFragmentManager = GeneralFragmentManager.getInstance();
        mFragmentManager.init(mView.getParentActivity());
        mUsbFileFragment = (UsbFileFragment) mFragmentManager.findFragment(R.id.id_fragment_usb_file);
        mUsbCameraFragment = (UsbCameraFragment) mFragmentManager.findFragment(R.id.id_fragment_usb_camera);
        mUsbSettingFragment = (UsbSettingFragment) mFragmentManager.findFragment(R.id.id_fragment_usb_setting);

        mFragmentManager.hideFragment(mUsbFileFragment);
        mFragmentManager.hideFragment(mUsbSettingFragment);
        mFragmentManager.showFragment(mUsbCameraFragment);
        mView.setViewListener(this);

    }

    @Override
    public void onButtonClick(int key) {
        switch(key) {
            case R.id.ib_open_file:
                // In case Mass Storage device is not found, we should popup the alert window to user
                // User could do a refresh on this windows
                if (UsbFileManager.getInstance().getUsbMassStorageDevice() == null) {
                    UsbFileDeviceDialog mUsbFileDialog = UsbFileDeviceDialog.newInstance();
                    mFragmentManager.showAlertDialog(mUsbFileDialog);
                } else {
                    mFragmentManager.showFragment(mUsbFileFragment);
                }
                break;
            case R.id.ib_start_preview:
                Log.d("UsbFile", "show Fragment usbcamera " + mUsbCameraFragment);
                mFragmentManager.showFragment(mUsbCameraFragment);
                if(UsbCameraManager.getInstance().getUVCCamera() == null) {
                    UsbCameraDeviceDialog mUsbCameraDialog = UsbCameraDeviceDialog.newInstance();
                    mFragmentManager.showAlertDialog(mUsbCameraDialog);
                } else {
                    mFragmentManager.showFragment(mUsbCameraFragment);
                }
                break;
            case R.id.ib_setting:
                mFragmentManager.showFragment(mUsbSettingFragment);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackButtonPressed() {
        if(mFragmentManager.getCurrentFragmentType() == GeneralFragmentManager.TYPE_USB_FILE) {
            //mUsbFilePresenter.onBackPressed();
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void setAdapter() {

    }

    @Override
    public void start() {

    }


    @Override
    public void showUsbFileFragment() {
        mFragmentManager.showFragment(mUsbFileFragment);
    }

    @Override
    public void showUsbCameraFragment() {
        mFragmentManager.showFragment(mUsbCameraFragment);
    }
}
