/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Views;

import com.camera.easying.filecamera.GeneralFragmentManager;
import com.camera.easying.filecamera.R;
import Contracts.UsbCameraContract;
import Presenters.UsbCameraPresenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by yangjie11 on 2016/9/6.
 */

public class UsbCameraFragment extends BaseFragment implements UsbCameraContract.View, View.OnClickListener {
    private static final String TAG = UsbCameraFragment.class.getSimpleName();
    private View mContentView;
    private SurfaceView mUVCCameraView;
    private ImageButton mStartCameraButton;
    private UsbCameraContract.Presenter mPresenter;
    private UsbCameraContract.ViewListener mViewListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "getActivity = " + getActivity());
    }

    public static UsbCameraFragment newInstance() {
        Bundle arguments = new Bundle();
        UsbCameraFragment fragment = new UsbCameraFragment();
        fragment.setArguments(arguments);
        fragment.setType(GeneralFragmentManager.TYPE_USB_CAMERA);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_camera_preview, null);
        mUVCCameraView = (SurfaceView)mContentView.findViewById(R.id.sv_camera_preview);
        mStartCameraButton = (ImageButton) mContentView.findViewById(R.id.ib_start_record);
        mStartCameraButton.setOnClickListener(this);
        if (mViewListener != null) {
            mViewListener.onViewCreated();
        }
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewListener != null) {
            mViewListener.onResume();
        }
    }

    @Override
    public Activity getParentActivity() {
        return getActivity();
    }

    @Override
    public SurfaceView getSurfaceView() {
        return mUVCCameraView;
    }

    @Override
    public void showCameraPreview() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("UsbFile", "mViewListener: " + mViewListener);
        if (mViewListener != null) {
            mViewListener.onActivityCreated();
        }
        mPresenter = new UsbCameraPresenter(this);
    }

    @Override
    public void showToast(String content) {
        Log.d(TAG, content + "getActivity() = " + getActivity());

        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setViewListener(UsbCameraContract.ViewListener listener) {
        mViewListener = listener;
    }

    @Override
    public void setPresenter(Object presenter) {
        mPresenter = (UsbCameraContract.Presenter) presenter;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.ib_start_record:
                if (mViewListener != null) {
                    mViewListener.onClick(R.id.ib_start_record);
                }
                break;
            default:
                break;
        }
    }
}
