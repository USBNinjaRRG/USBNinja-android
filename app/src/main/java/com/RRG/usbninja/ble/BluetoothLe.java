package com.RRG.usbninja.ble;

import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class BluetoothLe
{
    private final static String TAG = BluetoothLe.class.getSimpleName();
    
    private Context mContext;

    protected BluetoothManager mBluetoothManager;
    protected BluetoothAdapter mBluetoothAdapter;
    protected String mBluetoothDeviceAddress;
    protected BluetoothGatt mBluetoothGatt;
    
    private BluetoothAdapter.LeScanCallback mScanCallback;

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CLIENT_CHARACTERISTIC_CONFIG = 
    		UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    
	public BluetoothLe(Context context)
	{
		mContext = context;
	}
	
	public BluetoothGatt getGatt()
	{
		return mBluetoothGatt;
	}
	
    /**
     * Check Local Device Support BLE
     *
     * @param no
     *
     * @return Support=true，else=false
     * 
     */
	public boolean isBleSupported()
	{
		if(!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
			return false;
		
		return true;
	}
	
    /**
     * Check Local Bluetooth is Opened
     *
     * @param no
     *
     * @return Open=true，else=false
     * 
     */
	public boolean isOpened()
	{
		if(!mBluetoothAdapter.isEnabled())
			return false;
		
		return true;
	}
	
    /**
     * Set Search Result Callback Method
     *
     * @param scanCallback Method
     *
     * @return Success=true，else=false
     * 
     */
	public boolean setScanCallBack(BluetoothAdapter.LeScanCallback scanCallback)
	{
		if(scanCallback == null)
			return false;
		
		mScanCallback = scanCallback;
		return true;
	}
	
    /**
     * Start Scan
     *
     * @param no
     *
     * @return Success=true，else=false
     * 
     */
	public boolean startLeScan()
	{
		if(mScanCallback == null)
			return false;

		return mBluetoothAdapter.startLeScan(mScanCallback);		
	}
	
    /**
     * Stop Scan
     *
     * @param no
     *
     * @return Success=true，else=false
     * 
     */
	public boolean stopLeScan()
	{
		if(mScanCallback == null)
			return false;
		
		mBluetoothAdapter.stopLeScan(mScanCallback);	
		
		return true;
	}
	
    /**
     * Connect Local Device
     *
     * @param no
     *
     * @return Success=true，else=false
     * 
     */
	public boolean connectLocalDevice()
	{
        if (mBluetoothManager == null) 
        {
            mBluetoothManager = (BluetoothManager)mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) 
            {
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) 
        {
            return false;
        }

        return true;
	}
	
    /**
     * Close Local Device
     *
     * @param no
     *
     * @return Success=true，else=false
     *
     */
    public void disconnectLocalDevice() 
    {
        if (mBluetoothGatt == null) 
        {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
    
    /**
     * Connect Remote Device by MAC
     *
     * @param address Remote Device`s MAC Address
     * @param gattCallback CallBack Method
     *
     * @return Success=true，else=false
     * 
     */
    public boolean connectDevice(final String address, BluetoothGattCallback gattCallback)
    {
        if (mBluetoothAdapter == null || address == null) 
        {
            return false;
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null)
        {
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(mContext, false, gattCallback);
        if(mBluetoothGatt == null)
        	return false;

        mBluetoothDeviceAddress = address;
        return true;
    }
    
    /**
     * Close Remote Device
     *
     * @param no
     *
     * @return no
     * 
     */
    public void disconnectDevice() 
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) 
        {
            return ;
        }
        mBluetoothGatt.disconnect();
        Log.d(TAG, "Bluetooth disconnect");
    }
    

    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) 
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) 
        {
            return false;
        }
        return mBluetoothGatt.readCharacteristic(characteristic);
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) 
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) 
        {
            return false;
        }
        //characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        return mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic,boolean enabled) 
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) 
        {
            return false;
        }
        if(!mBluetoothGatt.setCharacteristicNotification(characteristic, enabled))
        	return false;

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_CLIENT_CHARACTERISTIC_CONFIG);
        if(descriptor == null)
        	return false;
        
        byte[] data;
        if(enabled)
        	data = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        else 
        	data = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        
        if(!descriptor.setValue(data))
        	return false;
        return mBluetoothGatt.writeDescriptor(descriptor);
    }

    /**
     * Get Service List
     *	
     * @param No
     *
     * @return Service List
     * 
     */
    public List<BluetoothGattService> getSupportedGattServices() 
    {
        if (mBluetoothGatt == null) 
        	return null;

        return mBluetoothGatt.getServices();
    }
}
