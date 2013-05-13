package abs.android.bluetooth.bt;

import java.io.IOException;
import java.io.InputStream;

import abs.android.bluetooth.LogUtils;
import abs.android.bluetooth.impl.IBluetoothClient;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;



public class BluetoothClient implements IBluetoothClient{
	private BluetoothClientThread mClientThread;
	private BluetoothSocket mClientSocket;
	private BluetoothAdapter mBluetoothAdapter;
	
	public BluetoothClient(BluetoothAdapter adapter){
		mBluetoothAdapter = adapter;
	}
	
	@Override
	public void connect(BluetoothDevice device){
		if (mClientThread == null){
			mClientThread = new BluetoothClientThread(device);
			mClientThread.start();
		}
	}
	
	@Override
	public void disConnect(){
		if (mClientThread != null){
			mClientThread.cancel(true);
			mClientThread = null;
		}
	}
	
	@Override
	public boolean hasConnected(){
		if (mClientThread != null){
			if (mClientSocket != null && mClientSocket.isConnected()){
				return true;
			}
		}	
		return false;
	}
	
	private class BluetoothClientThread extends Thread {
		private BluetoothSocket mSocket;
	    private final BluetoothDevice mDevice;

	    public BluetoothClientThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mDevice = device;

	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(BluetoothConfig.getUUID());
	        } catch (IOException e) { }
	        mSocket = tmp;
	    }

	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();

	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	        	mSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	            	mSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	        // Do work to manage the connection (in a separate thread)
	        manageConnectedSocket(mSocket);
	    }

	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel(boolean quitThread) {
	        try {
	        	if (mClientSocket != null){
	        		mClientSocket.close();
	        		mClientSocket = null;
	        	}
	        } catch (IOException e) { }
	    }
	}
	
	
	private void manageConnectedSocket(BluetoothSocket socket){
		mClientSocket = socket;
		try {
			LogUtils.d(this.getClass().getName(), "manageConnectedSocket enter");
			int length = 0;
			byte[] buffer = new byte[1024];		  
			while((length = mClientSocket.getInputStream().read(buffer)) != -1){
				LogUtils.d(this.getClass().getName(), "manageConnectedSocket read:  length = " + length);
				LogUtils.d(this.getClass().getName(), "manageConnectedSocket read:  " + new String(buffer).substring(0, length));
			}
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.e(this.getClass().getName(), "manageConnectedSocket IOException \n " + e.getMessage());
		}
		
		LogUtils.d(this.getClass().getName(), "manageConnectedSocket quit");
	}
}