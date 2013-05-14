package abs.android.bluetooth.bt;

import java.util.List;
import java.util.Set;

import abs.android.bluetooth.LogUtils;
import abs.android.bluetooth.impl.IBluetoothClient;
import abs.android.bluetooth.impl.IBluetoothServer;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;




public class BluetoothManager implements IBluetoothServer, IBluetoothClient{
	
	public interface Callback{
		public void onDeviceAdded();
		public void onDeviceRemoved();
		public void onDeviceRefreshed();
	}
	
	
	private static final int REQUEST_ENABLE_BT = 0;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDeviceCache mBluetoothDeviceCache;
	private BluetoothReceiver mBuletoothReceiver;
	private final Context mContext;
	private final Callback mCallback;
	
	private IBluetoothServer mBluetoothServer;
	private IBluetoothClient mBluetoothClient;
	

	public BluetoothManager(Context context, Callback callback){
		mContext = context;	
		mCallback = callback;
		init();
	}
	
	private void init() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();	
		mBluetoothAdapter.startDiscovery();
		
		mBluetoothDeviceCache = initBluetoothDeviceCache(mBluetoothAdapter);
		
		mBuletoothReceiver = new BluetoothReceiver(mContext, mCallback, mBluetoothDeviceCache);
		mBuletoothReceiver.register();
	}
	
	private BluetoothDeviceCache initBluetoothDeviceCache(BluetoothAdapter bluetoothAdapter){
		BluetoothDeviceCache cache = new BluetoothDeviceCache();
		
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if(pairedDevices != null && pairedDevices.size()>0){
		    for(BluetoothDevice device : pairedDevices){
		    	LogUtils.d(this.getClass().getName(), "Name = " + device.getName());
		    	cache.onDeviceAdded(device);
		    }
		}	
		return cache;
	}
	
	public void dispose(){
		if (mBuletoothReceiver != null){
			mBuletoothReceiver.unregister();
			mBuletoothReceiver = null;
		}
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
	
	
	public List<BluetoothDevice> getBluetoothDeviceList(){
		if (mBluetoothDeviceCache != null){
			return mBluetoothDeviceCache.getBluetoothList();
		}
		return null;
	}
	
	public void refreshBluetoothDeviceList(){
		if (mBluetoothAdapter != null){
			mBluetoothAdapter.startDiscovery();
		}
	}
	
	//==============================================================================
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

	
	//===============================================================================
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
	
	//===============================================================================
}