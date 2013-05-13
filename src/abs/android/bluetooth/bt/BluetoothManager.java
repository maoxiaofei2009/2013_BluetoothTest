package abs.android.bluetooth.bt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import abs.android.bluetooth.LogUtils;
import abs.android.bluetooth.impl.IBluetoothClient;
import abs.android.bluetooth.impl.IBluetoothServer;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;




public class BluetoothManager implements IBluetoothServer, IBluetoothClient{
	private static final int REQUEST_ENABLE_BT = 0;
	/** Called when the activity is first created. */
	
	private BluetoothAdapter mBluetoothAdapter;
	private BroadcastReceiver mReceiver;
	private List<BluetoothDevice> mBluetoothList;
	private Context mContext;
	
	private IBluetoothServer mBluetoothServer;
	private IBluetoothClient mBluetoothClient;
	public BluetoothManager(Context context){
		mContext = context;
		
		//If the device does not support Bluetooth, return null;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		LogUtils.d(this.getClass().getName(), "initBluetooth : " + mBluetoothAdapter);
		
		init();
	}
	
	private void init() {
		registerReceiver();
	}
	
	public void dispose(){
		unregisterReceiver();
	}
	
	public boolean checkBlutoothAvailable(){
		if(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()){
			return true;
		}else{
			return false;
		}
	}
	
	public void enableBluetooth(Activity activity){
		Intent enableBtIntent =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	}
	
	public List<BluetoothDevice> getBluetoothList(){
		List<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if(pairedDevices.size()>0){
		    // Loop through paired devices
		    for(BluetoothDevice device : pairedDevices){
		        // Add the name and address to an array adapter to show in a ListView
		    	LogUtils.d(this.getClass().getName(), "Name = " + device.getName());
		    	list.add(device);
		    }
		}
		return list;
	}
	
	private void registerReceiver(){
		// Register the BroadcastReceiver
		if (mReceiver == null){
			mReceiver = new BluetoothReceiver();
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
			filter.addAction(BluetoothDevice.ACTION_UUID);
			filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
			filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			filter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
			mContext.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		}
	}
	
	private void unregisterReceiver(){
		if (mReceiver != null){
			mContext.unregisterReceiver(mReceiver);
		}
	}
	
	private class BluetoothReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.d(this.getClass().getName(), "BluetoothReceiver action: " + intent.getAction());
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            // Add the name and address to an array adapter to show in a ListView
	            if (mBluetoothList == null){
	            	mBluetoothList = new ArrayList<BluetoothDevice>();
	            }
	            mBluetoothList.add(device);
	        }
		
		}
	}

	
	
	@Override
	public void startServer() {
		if (mBluetoothServer == null){
			mBluetoothServer = new BluetoothServer(mBluetoothAdapter);
			mBluetoothServer.startServer();
		}
	}

	@Override
	public void closeServer() {
		if (mBluetoothServer != null){
			mBluetoothServer.closeServer();
			mBluetoothServer = null;
		}
		
	}

	@Override
	public void connect(BluetoothDevice device) {
		if (mBluetoothClient == null){
			mBluetoothClient = new BluetoothClient(mBluetoothAdapter);
			mBluetoothClient.connect(device);
		}
	}

	@Override
	public void disConnect() {
		if (mBluetoothClient != null){
			mBluetoothClient.disConnect();
			mBluetoothClient = null;
		}
	}

	@Override
	public boolean hasConnected() {
		if (mBluetoothClient != null){
			return mBluetoothClient.hasConnected();
		}
		return false;
	}
}