/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Presenters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.logging.Handler;

import com.camera.easying.filecamera.GeneralFragmentManager;
import com.camera.easying.filecamera.UsbFileListAdapter;
import com.github.mjdev.libaums.fs.UsbFile;

import Contracts.UsbFileContract;
import Contracts.UsbFileManagerContract;
import Models.UsbFileManager;
import Views.UsbFileFragment;

import android.app.Fragment;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class UsbFilePresenter implements UsbFileContract.Presenter,UsbFileContract.ViewListener, UsbFileManagerContract.DeviceStateCallback {
    public static final int MSG_SHOW_FRAGMENT = 1;
    private UsbFileListAdapter mAdapter;
    private UsbFileContract.View mView;
    private UsbFileManager mUsbFileManager;
    private Deque<UsbFile> dirs = new ArrayDeque<UsbFile>();

    public MsgHandler msgHandler = new MsgHandler();

    public UsbFilePresenter(UsbFileContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mView.setViewListener(this);
        mUsbFileManager = UsbFileManager.getInstance();
        Log.d("UsbFile", "construct UsbFilePresenter, Context = " + mView.getParentActivity());
        mUsbFileManager.init(mView.getParentActivity(), this);

    }
    @Override
    public void onListItemClick(int pos) {
        UsbFile entry = mAdapter.getItem(pos);
        try {
            if (entry.isDirectory()) {
                dirs.push(mAdapter.getCurrentDir());
                mAdapter = new UsbFileListAdapter(mView.getParentActivity(), entry);
                mView.setListAdapter(mAdapter);
                //listView.setAdapter(adapter = new UsbFileListAdapter(this, entry));

            } else {
//                CopyTaskParam param = new CopyTaskParam();
//                param.from = entry;
//                File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//                        + "/usbfileman/cache");
//                f.mkdirs();
//                int index = entry.getName().lastIndexOf(".");
//                String prefix = entry.getName().substring(0, index);
//                String ext = entry.getName().substring(index);
//                // prefix must be at least 3 characters
//                if(prefix.length() < 3) {
//                    prefix += "pad";
//                }
//                param.to = File.createTempFile(prefix, ext, f);
//                new CopyTask().execute(param);
            }
        } catch (IOException e) {
            Log.e("UsbFile", "error staring to copy!", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            UsbFile dir = dirs.pop();
            mView.setListAdapter(mAdapter = new UsbFileListAdapter(mView.getParentActivity(), dir));
        } catch (IOException e) {
            Log.e("UsbFile", "error initializing adapter!", e);
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onActivityCreated() {
        Log.d("UsbFile", "mUsbFileManager.register");


    }

    @Override
    public void onDeviceNotFound() {
        mView.setListAdapter(null);
        mView.showToast("Usb Storage Device Not Found");
    }

    @Override
    public void onDeviceFound() {
        mView.showToast("Usb Storage Device Found");
    }

    @Override
    public void onDeviceSetup(UsbFile root) {
        mView.showToast("Set up usb storage device");
        Log.d("UsbFile", "onDeviceSetup : root = " + root);
        try {
            mAdapter = new UsbFileListAdapter(mView.getParentActivity(), root);
            mView.setListAdapter(mAdapter);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showFragment() {
        GeneralFragmentManager.getInstance().showFragment((UsbFileFragment) mView);
    }

    @Override
    public void start() {

    }

    class MsgHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_FRAGMENT:
                    showFragment();
                    break;
                default:
                    break;
            }
        }
    }
}
