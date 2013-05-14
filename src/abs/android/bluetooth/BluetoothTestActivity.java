package abs.android.bluetooth;

import java.util.List;

import abs.android.bluetooth.adapter.AbsBaseAdapter;
import abs.android.bluetooth.adapter.DeviceListAdapter;
import abs.android.bluetooth.bt.BluetoothManager;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class BluetoothTestActivity extends Activity implements OnClickListener, OnItemClickListener{
	
	private Button mBtnOpenServer;
	private Button mBtnCloseServer;
	private Button mBtnRefresh;
	private ListView mDeviceListView;
	private AbsBaseAdapter<BluetoothDevice> mDeviceAdapter;
	private BluetoothManager mBluetoothManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initUI();
		initObject();
	}
		
	private void initUI(){
		mBtnOpenServer 	= (Button) findViewById(R.id.server_open);
		mBtnCloseServer = (Button) findViewById(R.id.server_close);
		mBtnRefresh 	= (Button) findViewById(R.id.server_refreshed);
		
		mDeviceListView = (ListView) findViewById(R.id.device_list);
		mDeviceListView.setOnItemClickListener(this);
		mDeviceAdapter 	= new DeviceListAdapter<BluetoothDevice>(this);
		mDeviceListView.setAdapter(mDeviceAdapter);
		
		mBtnOpenServer.setOnClickListener(this);
		mBtnCloseServer.setOnClickListener(this);
		mBtnRefresh.setOnClickListener(this);
	}
	
	private void initObject(){
		mBluetoothManager = new BluetoothManager(this, mCallback);
		if (mBluetoothManager.checkBlutoothAvailable()){
			updateDeviceList(mBluetoothManager.getBluetoothDeviceList());
		}else{
			mBluetoothManager.enableBluetooth(this);
		}
	}
	
	private void updateDeviceList(List<BluetoothDevice> list){
		if (mDeviceAdapter != null){
			mDeviceAdapter.updateData(list);
			mDeviceAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void finish() {
		if (mBluetoothManager != null){
			mBluetoothManager.dispose();
			mBluetoothManager = null;
		}
		super.finish();
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnOpenServer){
			if (mBluetoothManager != null){
				mBluetoothManager.startServer();
			}
		}
		
		if (v == mBtnCloseServer){
			if (mBluetoothManager != null){
				mBluetoothManager.closeServer();
			}
		}
		
		if (v == mBtnRefresh){
			if (mBluetoothManager != null){
				mBluetoothManager.refreshBluetoothDeviceList();
			}
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mBluetoothManager != null){
			mBluetoothManager.connect(mDeviceAdapter.getItem(position));
		}	
	}
	
	
	private BluetoothManager.Callback mCallback = new BluetoothManager.Callback(){
		@Override
		public void onDeviceAdded() {
			updateDeviceList(mBluetoothManager.getBluetoothDeviceList());
		}

		@Override
		public void onDeviceRemoved() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeviceRefreshed() {
			updateDeviceList(mBluetoothManager.getBluetoothDeviceList());	
		}
	};
}