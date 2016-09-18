/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Presenters;

import java.util.List;

import com.camera.easying.filecamera.UsbCameraDeviceListAdapter;
import com.serenegiant.usb.DeviceFilter;

import Contracts.UsbSettingContract;
import Models.UsbCameraManager;

/**
 * Created by yangjie11 on 2016/9/13.
 */

public class UsbSettingPresenter implements UsbSettingContract.Presenter, UsbSettingContract.ViewListener {

    private UsbCameraDeviceListAdapter mDeviceListAdapter;
    private UsbSettingContract.View mView;
    public UsbSettingPresenter(UsbSettingContract.View view) {
        mView = view;
        mView.setViewListener(this);

    }
    @Override
    public void start() {

    }

    @Override
    public void onClick(int key) {

    }

    public void updateDevices() {
//        final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(mView.getParentActivity(), com.serenegiant
//                .uvccamera.R
//                .xml.device_filter);
//        mDeviceListAdapter = new UsbCameraDeviceListAdapter(mView.getParentActivity(), UsbCameraManager.getInstance()
//                .mUSBMonitor
//                .getDeviceList(filter
//                .get(0)));
//        mView.setSpinnerAdapter(mDeviceListAdapter);
    }
}
