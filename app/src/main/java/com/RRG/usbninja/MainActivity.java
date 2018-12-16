package com.RRG.usbninja;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.RRG.usbninja.app.MainApplication;
import com.RRG.usbninja.ble.ConvertData;
import com.RRG.usbninja.ble.HolloBluetooth;
import com.RRG.usbninja.config.ConfigInfo;
import com.wise.wisekit.activity.BaseActivity;
import com.wise.wisekit.utils.SPUtils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private HolloBluetooth mble;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 100;
    private Handler mHandler=new Handler();
    private boolean mScanning=true;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 5000;
    ListView mListView;
    ProgressBar mProgressBar;

    private LeDeviceListAdapter mLeDeviceListAdapter;

    private Button mScanBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitParam();
        LayoutInflater.from(this).inflate(R.layout.activity_main,topContentView);
        initView();

        mListView = findViewById(R.id.list);

        checkPermissions();

        //Get Bluetooth Context
        mble = HolloBluetooth.getInstance(getApplicationContext());
        //Check is support BLE
        if(!mble.isBleSupported() || !mble.connectLocalDevice())
        {
            Toast.makeText(this, "BLE is not supported on the device",Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }

        mScanBt = (Button)findViewById(R.id.scanBt);
        mProgressBar = findViewById(R.id.progressBar1);
        findViewById(R.id.scan_layout).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mScanning)
                {
                    scanLeDevice(false);
                    mScanBt.setText("Start scanning");
                    mProgressBar.setVisibility(View.GONE);
                }
                else
                {
                    mLeDeviceListAdapter.clear();
                    mLeDeviceListAdapter.notifyDataSetChanged();
                    scanLeDevice(true);
                    mScanBt.setText("Stop scanning");
                    mProgressBar.setVisibility(View.VISIBLE);
                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                if (device == null) return;

                final Intent intent = new Intent(MainActivity.this, SerialPortActivity.class);
                intent.putExtra(SerialPortActivity.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(SerialPortActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                if (mScanning)
                {
                    mble.stopLeScan();
                    mScanning = false;
                }

                startActivity(intent);

            }
        });
    }

    private void InitParam()
    {
        if(!SPUtils.getBool(this,"first_run")) {
            String BUTTON_KEY = "button_key";
            ButtonModel btnModel= new ButtonModel(BUTTON_KEY);
            btnModel.setTouchDown("423d4c0d0a");
            btnModel.setTouchUp("423d480d0a");
            btnModel.setName("ButtonB");
            SPUtils.put(MainApplication.getAppContext(), "num2", btnModel.toJsonObjectStr());
            btnModel.setTouchDown("413d4c0d0a");
            btnModel.setTouchUp("413d480d0a");
            btnModel.setName("ButtonA");
            SPUtils.put(MainApplication.getAppContext(), "num1", btnModel.toJsonObjectStr());
            SPUtils.putBool(this,"first_run",true);
        }
    }
    private void checkPermissions() {

        //if sdk version > 23 Ask Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Set Permission Description to Show to User.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "After Android 6.0 You need Open Location Permission to Search Device!", Toast.LENGTH_SHORT).show();
                }
                //Request Permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            } else {

            }
        }
    }

    private void initView() {

        topLeftBtn.setVisibility(View.GONE);
        topRightBtn.setVisibility(View.VISIBLE);
        setTitle("Main Page");
        setRightText("Password");

        topRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, SetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Check is BLE Opened
        if(!mble.isOpened())
        {
            Intent openIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(openIntent, REQUEST_ENABLE_BT);
        }

        //Set Bluetooth Scan CALLBACK func.
        mble.setScanCallBack(mLeScanCallback);
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        mListView.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);		//Start Scan
        mScanBt.setText("Stop scanning");
        mProgressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        scanLeDevice(false);		//Stop Scan
        mLeDeviceListAdapter.clear();	//Clear list
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //result of opening bluetooth
        if(resultCode == REQUEST_ENABLE_BT || resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(this,"Bluetooth Open Cancelled.",Toast.LENGTH_LONG).show();
            finish();
            return ;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    Runnable cancelScan = new Runnable()
    {
        @Override
        public void run()
        {
            mble.stopLeScan();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mble.startLeScan();
            mHandler.postDelayed(cancelScan,SCAN_PERIOD);
            invalidateOptionsMenu();
        }
    };

    //enable = true shows that scan was running.
    private void scanLeDevice(final boolean enable)
    {
        if (enable)
        {
            // SCAN_PERIOD times later, stop Scan.
            mHandler.postDelayed(cancelScan,SCAN_PERIOD);

            mScanning = true;
            mble.startLeScan();	//Start BLE Scan.
        }
        else
        {
            //Cancel Stop Scan`s Thread.
            mHandler.removeCallbacks(cancelScan);
            mScanning = false;
            mble.stopLeScan();	//Stop Scan.
        }
        invalidateOptionsMenu();
    }

    // Scan Result
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback()
            {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            String deviceName = "H01";//device.getName();

                            if(deviceName == null || deviceName.length() <= 0)
                                deviceName = "unknow device";
                            Log.d(TAG, deviceName);
                            Log.d(TAG, device.getAddress());
                            //Log.d(TAG,"BroadCast:"+ConvertData.bytesToHexString(scanRecord, false));
                            //02010603035869
                            if(scanRecord.length < 7 || scanRecord[0] != 0x02 || scanRecord[1] != 0x01 ||
                                    scanRecord[2] != 0x06 || scanRecord[3] != 0x03 || scanRecord[4] != 0x03 ||
                                    scanRecord[5] != 0x58 || scanRecord[6] != 0x69) {


                                if (scanRecord.length < 9 || scanRecord[0] != 0x02 || scanRecord[1] != 0x01 ||
                                        scanRecord[2] != 0x06 || scanRecord[3] != 0x05 || scanRecord[4] != 0x03 ||
                                        scanRecord[5] != 0x58 || scanRecord[6] != 0x69 || scanRecord[7] != (byte)0xE7 ||
                                        scanRecord[8] != (byte)0xFE) {

                                    if (scanRecord.length < 2 || scanRecord[0] != 0x1A || scanRecord[1] != (byte)0xFF) {

                                        if (scanRecord.length < 7 || scanRecord[0] != 0x02 || scanRecord[1] != 0x01 ||
                                                scanRecord[2] != 0x06 || scanRecord[3] != 0x03 || scanRecord[4] != 0x03 ||
                                                scanRecord[5] != (byte)0xE7 || scanRecord[6] != (byte)0xFE) {

                                            return;
                                        }
                                    }

                                }
                            }


                            byte[] temp =scanRecord;
                            Log.d(TAG, ConvertData.bytesToHexString(temp, false));
                            mLeDeviceListAdapter.addDevice(device,Integer.valueOf(rssi), ConvertData.bytesToHexString(temp, false));
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter
    {
        private ArrayList<BluetoothDevice> mLeDevices;
        private ArrayList<Integer> mLeRssi;
        private ArrayList<String> mLeRecord;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter(Context context)
        {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mLeRssi = new ArrayList<Integer>();
            mLeRecord = new ArrayList<String>();
            mInflator = LayoutInflater.from(context);//MainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, Integer rssi, String record)
        {
            if(!mLeDevices.contains(device))
            {
                mLeDevices.add(device);
                mLeRssi.add(rssi);
                mLeRecord.add(record);
            }
            else {
                int pos = mLeDevices.indexOf(device);
                mLeRecord.remove(pos);
                mLeRecord.add(pos, record);
                mLeRssi.remove(pos);
                mLeRssi.add(pos, rssi);
            }
        }

        public BluetoothDevice getDevice(int position)
        {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
            mLeRecord.clear();
            mLeRssi.clear();
        }

        @Override
        public int getCount()
        {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i)
        {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null)
            {
//                view = mInflator.inflate(R.layout.activity_main, null);
                view = mInflator.inflate(R.layout.list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.deviceRecord = (TextView)view.findViewById(R.id.device_record);
                viewHolder.deviceTime = (TextView)view.findViewById(R.id.device_time);
                view.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            Integer rssi = mLeRssi.get(i);
            String record = mLeRecord.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);

            viewHolder.deviceAddress.setText("address:"+device.getAddress() + "     RSSI:"+rssi+"dB");
            viewHolder.deviceRecord.setText("broadcast:"+record);
//            String timeStr = "time: " + time.year + "-" + time.month + "-" + time.monthDay + " " +
//                    time.hour + ":" + time.minute + ":" + time.second;
//            viewHolder.deviceTime.setText(timeStr);

            return view;
        }
    }

    static class ViewHolder
    {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRecord;
        TextView deviceTime;
    }
}
