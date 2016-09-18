/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Views;

import com.camera.easying.filecamera.GeneralFragmentManager;
import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbCameraDeviceListAdapter;

import Contracts.UsbSettingContract;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by yangjie11 on 2016/9/13.
 */

public class UsbSettingFragment extends BaseFragment implements UsbSettingContract.View, View.OnClickListener {
    private View mContentView;
    private Button mRrefreshButton;
    private UsbSettingContract.ViewListener mViewListener;
    private Spinner mSpinner;

    @Override
    public void setPresenter(Object presenter) {

    }

    public static UsbSettingFragment newInstance() {
        Bundle arguments = new Bundle();
        UsbSettingFragment fragment = new UsbSettingFragment();
        fragment.setArguments(arguments);
        fragment.setType(GeneralFragmentManager.TYPE_USB_SETTING);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_usb_setting, null);
        mRrefreshButton = (Button) mContentView.findViewById(R.id.bt_refresh);
        mSpinner = (Spinner) mContentView.findViewById(R.id.sp_device_path);
        mRrefreshButton.setOnClickListener(this);
        return mContentView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_refresh) {
            if (mViewListener != null) {
                mViewListener.onClick(R.id.bt_refresh);
            }
        }
    }

    @Override
    public Activity getParentActivity() {
        return getActivity();
    }

    @Override
    public void setViewListener(UsbSettingContract.ViewListener listener) {
        mViewListener = listener;
    }

    @Override
    public void setSpinnerAdapter(UsbCameraDeviceListAdapter adapter) {
        mSpinner.setAdapter(adapter);
    }
}
