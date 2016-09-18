package Views;

import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.camera.easying.filecamera.MainActivity;
import com.camera.easying.filecamera.R;
import com.camera.easying.filecamera.UsbCameraDeviceListAdapter;
import com.camera.easying.filecamera.UsbStorageDeviceListAdapter;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.DeviceFilter;

import java.util.List;

import Models.UsbCameraManager;
import Models.UsbFileManager;

/**
 * Created by think on 2016/9/17.
 */

public class UsbCameraDeviceDialog extends BaseDialog {
    private View mContentView;
    private ListView mDeviceListView;
    private UsbDevice mDeviceListAdapter;
    private TextView mTvNoDevice;
    UsbCameraDeviceDialog.FragmentInterface mListerner;

    public interface FragmentInterface {
        void showUsbCameraFragment();
    }

    public static UsbCameraDeviceDialog newInstance() {

        UsbCameraDeviceDialog dialog = new UsbCameraDeviceDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title" , "Select USB Camera Device: ");
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public View InitView() {
        mContentView = LayoutInflater.from(getActivity()).inflate(R.layout.usb_camera_dialog, null);
        mTvNoDevice = (TextView) mContentView.findViewById(R.id.tv_no_device);
        mDeviceListView = (ListView) mContentView.findViewById(R.id.lv_usb_camera_devices);
        mTvNoDevice.setVisibility(View.INVISIBLE);

        return mContentView;
    }

    @Override
    public void onClickListener(int key) {
        // confirm button
        if (key == DialogInterface.BUTTON_POSITIVE) {
            if (UsbCameraManager.getInstance().getUVCCamera() != null) {
                mListerner = MainActivity.mPresenter;
                mListerner.showUsbCameraFragment();
            }
        } else if (key == DialogInterface.BUTTON_NEGATIVE) {

        } else if (key == DialogInterface.BUTTON_NEUTRAL) {

        }
    }

    public void updateDevices() {
//		mUSBMonitor.dumpDevices();
        //final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(getActivity(), com.serenegiant.uvccamera.R.xml.device_filter);
        mDeviceListAdapter = new UsbCameraDeviceListAdapter(getActivity(), (List<UsbDevice>) UsbCameraManager.getInstance().getUSBMonitor().getDeviceList());
        mDeviceListView.setAdapter(mDeviceListAdapter);
    }

}
