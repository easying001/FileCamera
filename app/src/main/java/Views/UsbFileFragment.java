/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Views;

import com.camera.easying.filecamera.GeneralFragmentManager;
import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbFileListAdapter;

import Contracts.UsbFileContract;
import Models.UsbFileManager;
import Presenters.UsbFilePresenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by yangjie11 on 2016/9/10.
 */

public class UsbFileFragment extends BaseFragment implements UsbFileContract.View, AdapterView.OnItemClickListener {

    private UsbFileContract.ViewListener mListener;
    private UsbFileManager mUsbFileManager;
    private UsbFilePresenter mPresenter;
    private View mContentView;
    private ListView lv_usb_files;

    public static UsbFileFragment newInstance() {

        UsbFileFragment fragment = new UsbFileFragment();
        fragment.setType(GeneralFragmentManager.TYPE_USB_FILE);

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_file_management, null);
        lv_usb_files = (ListView) mContentView.findViewById(R.id.lv_folders);
        lv_usb_files.setOnItemClickListener(this);

        return mContentView;
    }

    @Override
    public Activity getParentActivity() {
        return getActivity();
    }

    @Override
    public void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFragment() {

    }

    @Override
    public void setListAdapter(UsbFileListAdapter adapter) {
        lv_usb_files.setAdapter(adapter);
    }

    @Override
    public void setViewListener(UsbFileContract.ViewListener listener) {
        mListener = listener;
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onListItemClick(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("UsbFile","onActivityCreated");
        if (mListener != null) {
            mListener.onActivityCreated();
        }
        mPresenter = new UsbFilePresenter(this);
    }
}
