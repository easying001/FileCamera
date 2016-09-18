package Views;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.camera.easying.filecamera.MainActivity;
import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbStorageDeviceListAdapter;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.serenegiant.usb.DeviceFilter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Models.UsbFileManager;
import Presenters.UsbFilePresenter;

/**
 * Created by think on 2016/9/17.
 */

public class UsbFileDeviceDialog extends BaseDialog {
    private View mContentView;
    private ListView mDeviceListView;
    private UsbStorageDeviceListAdapter mDeviceListAdapter;
    private TextView mTvNoDevice;
    FragmentInterface mListerner;
    public interface FragmentInterface {
        void showUsbFileFragment();
    }
    public UsbFileDeviceDialog() {

    }

    public static UsbFileDeviceDialog newInstance() {

        UsbFileDeviceDialog dialog = new UsbFileDeviceDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title" , "Select USB Storage Device: ");
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public View InitView() {

        mContentView = LayoutInflater.from(getActivity()).inflate(R.layout.usb_camera_dialog, null);
        mTvNoDevice = (TextView) mContentView.findViewById(R.id.tv_no_device);
        mDeviceListView = (ListView) mContentView.findViewById(R.id.lv_usb_camera_devices);
        mTvNoDevice.setVisibility(View.INVISIBLE);
        updateDevices();
        return mContentView;
    }

    @Override
    public void onClickListener(int key) {
        // confirm button
        if (key == DialogInterface.BUTTON_POSITIVE) {
            UsbMassStorageDevice usbDevice = UsbFileManager.getInstance().getUsbMassStorageDevice();
            if ((usbDevice != null) && UsbFileManager.getInstance().getmDeviceState() == UsbFileManager.STATE_SETUPED) {
                mListerner = MainActivity.mPresenter;
                mListerner.showUsbFileFragment();
            }
        } else if (key == DialogInterface.BUTTON_NEGATIVE) {

        } else if (key == DialogInterface.BUTTON_NEUTRAL) {
            updateDevices();
        }

    }

    public void updateDevices() {
        //final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(getActivity(), com.serenegiant.uvccamera.R.xml.device_filter);
        List<UsbMassStorageDevice> devList = UsbFileManager.getInstance().getMassStorageDevices();
        if (devList.size() > 0) {
            mDeviceListAdapter = new UsbStorageDeviceListAdapter(getActivity(),devList);
            mDeviceListView.setAdapter(mDeviceListAdapter);
        } else {
            mTvNoDevice.setVisibility(View.VISIBLE);
        }
    }
}
