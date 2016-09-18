package com.camera.easying.filecamera;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mjdev.libaums.UsbMassStorageDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2016/9/17.
 */

public class UsbStorageDeviceListAdapter extends MyListAdapter<UsbMassStorageDevice> {

    public UsbStorageDeviceListAdapter(Context context, List<UsbMassStorageDevice> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.device_list_item, viewGroup, false);
        }

        final UsbMassStorageDevice device = (UsbMassStorageDevice) getItem(i);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_device_name);
        tv_name.setText(String.format("UVC Camera:(%x:%x:%s)", device.getUsbDevice().getVendorId(), device.getUsbDevice().getProductId(), device.getUsbDevice()
                .getDeviceName()));

        return view;
    }
}
