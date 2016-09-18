/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yangjie11 on 2016/9/6.
 */

public class BaseFragment extends Fragment {
    private static final String TAG = "UsbFile";
    private int fragmentType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Base Fragment: onCreateView, getActivity() = " + getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Base Fragment: onAttach, getActivity() = " + getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Base Fragment: onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Base Fragment: onResume, getActivity() = "  + getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Base Fragment: onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Base Fragment: onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Base Fragment: onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "Base Fragment: onDetach, getActivity() = " + getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Base Fragment: onActivityCreated, getActivity() = "+ getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "Base Fragment: onViewCreated, getActivity() = "  + getActivity());
    }

    public void setType(int type) {
        fragmentType = type;
    }

    public int getType() {
        return fragmentType;
    }
}
