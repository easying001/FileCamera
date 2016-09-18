/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Contracts;

import Views.BaseFragment;
import android.app.Fragment;
import android.app.FragmentManager;

/**
 * Created by yangjie11 on 2016/9/9.
 */

public interface IFragmentManager {

    interface ActivityListener {
        FragmentManager getActivityFragmentManager();
        void getContext();
    }

    interface FragmentListener {
        void back();
        int getCurrentFragmentType();
        Fragment getCurrentFragment();
        void showFragment();
        void showFragment(BaseFragment fragment);
        void replace();
    }

}
