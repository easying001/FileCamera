/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package Contracts;

import Views.BaseFragment;

/**
 * Created by yangjie11 on 2016/9/12.
 */

public interface IFragmentFactory {

    public BaseFragment createFragment(int type);

}
