/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.camera.easying.filecamera;

import java.util.ArrayList;

import Contracts.IFragmentFactory;
import Contracts.IFragmentManager;
import Models.UsbCameraManager;
import Views.BaseDialog;
import Views.BaseFragment;
import Views.UsbCameraFragment;
import Views.UsbFileFragment;
import Views.UsbSettingFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;

/**
 * Created by yangjie11 on 2016/9/9.
 */

public class GeneralFragmentManager implements IFragmentFactory, IFragmentManager.FragmentListener {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_LAUNCH = 1;
    public static final int TYPE_USB_FILE = 2;
    public static final int TYPE_USB_CAMERA = 3;
    public static final int TYPE_USB_SETTING = 4;

    private int mType = 0;
    private FragmentManager mFragmentManager;
    private BaseFragment mCurrentFragment;
    private static GeneralFragmentManager mInstance = null;
    private IFragmentFactory mFragmentFactory = null;
    private ArrayList<BaseFragment> mFragmentStack;
    private Context mContext;

    public GeneralFragmentManager() {
        Log.d("UsbFile", "construct GeneralFragmentManager");

        mFragmentStack = new ArrayList<BaseFragment>();
        setFragmentFactory(this);

    }

    public static GeneralFragmentManager getInstance() {
        if (null == mInstance) {
            synchronized(GeneralFragmentManager.class) {
                if (null == mInstance) {
                    mInstance = new GeneralFragmentManager();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mFragmentManager = ((Activity) mContext).getFragmentManager();
    }

    private void setFragmentFactory(IFragmentFactory factory) {
        mFragmentFactory = factory;
    }

    public BaseFragment createFragment(int type) {
        BaseFragment fragment = null;
        switch(type) {
            case TYPE_LAUNCH:

                break;
            case TYPE_USB_CAMERA:
                fragment = new UsbCameraFragment();
                break;
            case TYPE_USB_FILE:
                fragment = new UsbFileFragment();
                break;
            case TYPE_USB_SETTING:
                fragment = new UsbSettingFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

//    public GeneralFragmentManager getInstance() {
//        if (null == mInstance) {
//            synchronized(GeneralFragmentManager.this) {
//                if (null == mInstance) {
//                    mInstance = new GeneralFragmentManager();
//                }
//            }
//        }
//
//        return mInstance;
//    }

    @Override
    public void back() {
        if ((mCurrentFragment != null) && (mCurrentFragment.getType() == TYPE_USB_FILE)) {

        }
    }

    @Override
    public int getCurrentFragmentType() {
        if (mCurrentFragment != null) {
            return mCurrentFragment.getType();
        } else {
            return TYPE_NONE;
        }

    }

    @Override
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void showFragment() {

    }

    public void hideFragment(BaseFragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.hide(fragment);
            ft.commitAllowingStateLoss();
        }
    }

    public void showFragment(int type) {
        BaseFragment fragment = null;
        if (mFragmentFactory != null) {
            fragment = mFragmentFactory.createFragment(type);
        }

        if (fragment != null) {

        }


    }

    @Override
    public void showFragment(BaseFragment fragment) {
        Log.d("Usb", "fragment = " + fragment);
        replaceFragment(fragment);
        mCurrentFragment = fragment;
    }

    public void showAlertDialog(BaseDialog dialog) {
        dialog.show(mFragmentManager, "");
        //FragmentTransaction ft = mFragmentManager.beginTransaction();
    }

    @Override
    public void replace() {

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        if (mCurrentFragment != null) {
            Log.d("UsbFile", "Hide current fragment: " + mCurrentFragment);
            ft.hide(mCurrentFragment);
//            if (mCurrentFragment.getType() == TYPE_LAUNCH) {
//                ft.remove(mCurrentFragment);
//            } else {
//                ft.hide(mCurrentFragment);
//            }
        }

        if (fragment.isAdded()) {
            Log.d("UsbFile", "Show current fragment: " + mCurrentFragment);
            ft.show(fragment);
        } else {
            Log.d("UsbFile", "Add current fragment: " + mCurrentFragment);
            ft.add(R.id.fragment_container, fragment);
        }

        ft.commitAllowingStateLoss();
    }

    /* Traversing whole stack, find if specified fragment existed or not. if existed, remove it from
        stack, if not, create a new one and return
      */
    private BaseFragment getFragment(int type) {
        int index = 0;
        BaseFragment retFragment = null;
        for (BaseFragment fragment : mFragmentStack) {
            if (fragment.getType() == type) {
                retFragment = mFragmentStack.remove(index);
                break;
            }
            index++;
        }

        if (retFragment == null && mFragmentFactory != null) {
            retFragment = mFragmentFactory.createFragment(type);
        }

        return retFragment;

    }

    protected void push(BaseFragment fragment) {
        if (fragment == null) {
            return;
        }
        mFragmentStack.add(fragment);
    }

    protected BaseFragment pop() {
        int size = mFragmentStack.size();
        if (size <= 0) {
            return null;
        }

        BaseFragment fragment = mFragmentStack.remove(size - 1);
        return fragment;
    }

    public Fragment findFragment(int id) {
        if (mFragmentManager != null) {
            return mFragmentManager.findFragmentById(id);
        } else {
            return null;
        }
    }

}
