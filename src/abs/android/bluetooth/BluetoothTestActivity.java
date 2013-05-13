package abs.android.bluetooth;

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
		mBtnOpenServer = (Button) findViewById(R.id.server_open);
		mBtnCloseServer = (Button) findViewById(R.id.server_close);
		
		mDeviceListView = (ListView) findViewById(R.id.device_list);
		mDeviceListView.setOnItemClickListener(this);
		mDeviceAdapter = new DeviceListAdapter<BluetoothDevice>(this);
		mDeviceListView.setAdapter(mDeviceAdapter);
		
		mBtnOpenServer.setOnClickListener(this);
		mBtnCloseServer.setOnClickListener(this);
	}
	
	private void initObject(){
		mBluetoothManager = new BluetoothManager(this);
		if (mBluetoothManager.checkBlutoothAvailable()){
			mBluetoothManager.getBluetoothList();
			
			mDeviceAdapter.updateData(mBluetoothManager.getBluetoothList());
		}else{
			mBluetoothManager.enableBluetooth(this);
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
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mBluetoothManager != null){
			mBluetoothManager.connect(mDeviceAdapter.getItem(position));
		}	
	}
}