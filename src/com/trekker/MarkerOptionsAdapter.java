package com.trekker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MarkerOptionsAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflater;
	private String[] values;
	
	public MarkerOptionsAdapter(Context context, String[] values) {
		this.values = values;
		this.context = context;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return values.length;
	}

	@Override
	public Object getItem(int position) {
		return values[position];
	}

	@Override
	public long getItemId(int position) {
		return values.length;
	}

	public static class ViewHolder {
		ImageView imgMarkerType;
		TextView markerType;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView==null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.markers_list_item, null);
			
			holder.markerType = (TextView) convertView.findViewById(R.id.marker_type);
			holder.imgMarkerType = (ImageView) convertView.findViewById(R.id.img_marker_type);
			
			if(values[position].equals("food")) {
			} else if(values[position].equals("landscape")) {
				holder.markerType.setText("landscape");
				holder.imgMarkerType.setImageResource(R.drawable.landscape);
			}
			
			convertView.setTag(holder);
		} else {
			holder=(ViewHolder)convertView.getTag();
		}
		return convertView;
	}

}
