package abs.android.bluetooth.bt;

import java.util.UUID;

import abs.android.bluetooth.LogUtils;
import android.os.Build;



public class BluetoothConfig{
	private static final String TAG = "BluetoothConfig";
	public static String getName(){
		return Build.MODEL + " Server";
	}
	
	public static UUID getUUID(){
		UUID uuid = UUID.fromString("a53e91a2-15e6-455f-a530-96aba122bd56");
		LogUtils.d(TAG, "uuid = " + uuid.toString());
		return uuid;
	}
}