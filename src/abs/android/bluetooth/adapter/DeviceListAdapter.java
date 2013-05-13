package abs.android.bluetooth.adapter;

import abs.android.bluetooth.R;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeviceListAdapter<T> extends AbsBaseAdapter<T>{
	private class ViewHolder{
		public TextView name;
	}
	
	public DeviceListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null){
			convertView = getLayoutInflater().inflate(R.layout.device_list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.device_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		BluetoothDevice device = (BluetoothDevice) getItem(position);
		if (device != null){
			holder.name.setText(device.getName());
		}
		
		return convertView;
	}
	
}