/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Contracts;

import com.camera.easying.filecamera.UsbFileListAdapter;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.UsbFile;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by yangjie11 on 2016/9/7.
 */

public interface UsbFileContract {

    interface View extends BaseView {
        Activity getParentActivity();
        void showToast(String content);
        void showFragment();
        void setListAdapter(UsbFileListAdapter adapter);
        void setViewListener(ViewListener listener);
    }

    interface ViewListener {
        void onListItemClick(int pos);
        void onBackPressed();
        void onResume();
        void onActivityCreated();

    }

    interface Presenter extends BasePresenter {
        void showFragment();

    }

    interface DeviceManagement {
        void register();
        void unregister();
        UsbMassStorageDevice discoverDevice();
        UsbFile setupDevice(UsbMassStorageDevice device);
        void grantPermission(UsbMassStorageDevice device);

    }
}
