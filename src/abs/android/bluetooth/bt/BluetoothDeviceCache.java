package abs.android.bluetooth.bt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;





public class BluetoothDeviceCache{
	private List<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	
	public BluetoothDeviceCache(){
		
	}
	
	public synchronized void onDeviceAdded(BluetoothDevice device){
		if (mDeviceList != null){
			onDeviceRemoved(device);
			mDeviceList.add(device);
		}
	}
	
	public synchronized void onDeviceRemoved(BluetoothDevice device){
		if (mDeviceList != null ){
			int index = findDeviceIndex(device);
			if (index != -1){
				mDeviceList.remove(index);
			}
		}
	}
	
    public synchronized int findDeviceIndex(BluetoothDevice device) {
        for (int i = mDeviceList.size() - 1; i >= 0; i--) {
        	BluetoothDevice cachedDevice = mDeviceList.get(i);
            if (cachedDevice.equals(device)) {
                return i;
            }
        }
        return -1;
    }
	
	public synchronized List<BluetoothDevice> getBluetoothList(){
		List<BluetoothDevice> device = new ArrayList<BluetoothDevice>();
		Iterator<BluetoothDevice> iterator = mDeviceList.iterator();
		while (iterator.hasNext()){
			device.add(iterator.next());
		}
		return device;
	}
	
	public synchronized void onDeviceCleared(){
		if (mDeviceList != null){
			for (int i=0; i<mDeviceList.size(); i++){
				BluetoothDevice device = mDeviceList.get(i);
				if (device.getBondState() == BluetoothDevice.BOND_NONE){
					mDeviceList.remove(i);
					i--;
				}
			}
		}
	}
}