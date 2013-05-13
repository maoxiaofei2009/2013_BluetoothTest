package abs.android.bluetooth.bt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import abs.android.bluetooth.LogUtils;
import abs.android.bluetooth.impl.IBluetoothServer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;


public class BluetoothServer implements IBluetoothServer{
	private BluetoothServerThread mServerThread;
	private BluetoothAdapter mBluetoothAdapter;
	
	public BluetoothServer(BluetoothAdapter adapter){
		mBluetoothAdapter = adapter;
	}
	
	@Override
	public void startServer(){
		if (mServerThread == null){
			mServerThread = new BluetoothServerThread();
			mServerThread.start();
		}
	}
	
	@Override
	public void closeServer(){
		if (mServerThread != null){
			mServerThread.cancel(true);
			mServerThread = null;
		}
	}
	
	
    private BluetoothServerSocket createServerSocket(){
    	BluetoothServerSocket serverSocket = null;
        try {
        	serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
            		BluetoothConfig.getName(), BluetoothConfig.getUUID());
        } catch (IOException e) { 
        	
        }      
        return serverSocket;
    }
    
	private class BluetoothServerThread extends Thread {
	    private BluetoothServerSocket mServerSocket;
	    private boolean mQuitThread = false;

	    public BluetoothServerThread() {
	    }

	    public void run() {
	        BluetoothSocket socket = null;
	        // Keep listening until exception occurs or a socket is returned
	        while (true) {        
	            try {
	            	//BluetoothServerSocket only can create one BluetoothSocket Object 
	            	//by execute accept() method;
	            	mServerSocket = createServerSocket();
	            	if (mServerSocket != null){
	            		socket = mServerSocket.accept();
	            	}
	            } catch (IOException e) {
	            	cancel(false|mQuitThread);
	            	if (mQuitThread){
	            		break;
	            	}else{
	            		continue;
	            	}
	            }
	            
	            // If a connection was accepted
	            if (socket != null) {
	                // Do work to manage the connection (in a separate thread)
	                manageConnectedSocket(socket);
	            	
	            	//BluetoothServerSocket only can create one BluetoothSocket Object 
	            	//by execute accept() method;
	            	//so after use, we close the BluetoothServerSocket, and this does't 
	            	//close BluetoothSocket.
	            	cancel(false|mQuitThread);
	            	if (mQuitThread){
	            		break;
	            	}
	            }
	        }
	    }

	    /** Will cancel the listening socket, and cause the thread to finish */
	    public void cancel(boolean quitThread) {
	    	mQuitThread = quitThread;
	        try {
	        	if (mServerSocket != null){
	        		mServerSocket.close();
	        		mServerSocket = null;
	        	}
	        } catch (IOException e) { 
	        	
	        }
	    }
	}
	
	private void manageConnectedSocket(BluetoothSocket socket){
		try {
			LogUtils.e(this.getClass().getName(), "manageConnectedSocket write");
			socket.getOutputStream().write("hello".getBytes());
			socket.getOutputStream().write("world".getBytes());
		} catch (IOException e) {
			LogUtils.e(this.getClass().getName(), "manageConnectedSocket IOException message = " + e.getMessage());
			e.printStackTrace();
		}
		LogUtils.d(this.getClass().getName(), "manageConnectedSocket " + socket);
	}
}