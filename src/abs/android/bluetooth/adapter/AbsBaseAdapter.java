package abs.android.bluetooth.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.AbsListView.OnScrollListener;

public abstract class AbsBaseAdapter<T> extends BaseAdapter implements OnScrollListener{
	private List<T> mDataList = null;
	private Context mContext;
	private LayoutInflater mInflater;
	public AbsBaseAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void updateData(List<T> data){
		mDataList = data;
	}

	protected LayoutInflater getLayoutInflater(){
		return mInflater;
	}
	
	protected Context getContext(){
		return mContext;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if (mDataList != null){
			count = mDataList.size();
		}
		return count;
	}

	@Override
	public T getItem(int position) {
		T item = null;
		if (position < getCount()){
			item = mDataList.get(position);
		}
		return item;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}