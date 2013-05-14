package abs.android.bluetooth.bt;

import abs.android.bluetooth.LogUtils;
import abs.android.bluetooth.bt.BluetoothManager.Callback;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothReceiver{
	
	private Context mContext;
	private Callback mCallback;
	private BluetoothDeviceCache mBluetoothDeviceCache;
	public BluetoothReceiver(Context context, Callback callback, BluetoothDeviceCache cache){
		mContext = context;
		mCallback = callback;
		mBluetoothDeviceCache = cache;
	}
	
	public void register(){
		// Register the BroadcastReceiver
		if (mBroadcastReceiver != null){
			IntentFilter filter = new IntentFilter();
			
		    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		        
			filter.addAction(BluetoothDevice.ACTION_FOUND);

			filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
			filter.addAction(BluetoothDevice.ACTION_UUID);
			filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
			filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
			mContext.registerReceiver(mBroadcastReceiver, filter); // Don't forget to unregister during onDestroy
		}
	}
	
	public void unregister(){
		if (mBroadcastReceiver != null){
			mContext.unregisterReceiver(mBroadcastReceiver);
		}
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.d(this.getClass().getName(), "BluetoothReceiver action: " + intent.getAction());
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            if (mBluetoothDeviceCache != null){
	            	mBluetoothDeviceCache.onDeviceAdded(device);
	            }
	            
	            if (mCallback != null){
	            	mCallback.onDeviceAdded();
	            }
	        }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
	            if (mBluetoothDeviceCache != null){
	            	mBluetoothDeviceCache.onDeviceCleared();
	            }
	            
	            if (mCallback != null){
	            	mCallback.onDeviceRefreshed();
	            }
	        }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	        	
	        }
		}
	};
}