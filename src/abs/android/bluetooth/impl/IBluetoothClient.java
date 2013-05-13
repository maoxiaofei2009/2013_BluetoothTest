package abs.android.bluetooth.impl;

import android.bluetooth.BluetoothDevice;



public interface IBluetoothClient{
	public void connect(BluetoothDevice device);
	public void disConnect();
	public boolean hasConnected();
}