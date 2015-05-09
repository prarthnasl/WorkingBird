package com.maps.psabharw.maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;


public class ItemAdaptor extends BaseAdapter {

	Context context;
	private String staticImagePath = "http://static.wishberg.com/images/wb_w/";

	private String TAG = "wishAdapter";

	public ArrayList<HashMap<String, Object>> mData;
	private AQuery listAq;

	public ItemAdaptor(Context context, ArrayList<HashMap<String, Object>> arrayList) {

		// TODO Auto-generated constructor stub
		this.mData = arrayList;
		this.context = context;
		this.listAq = new AQuery(context);
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView imageView;
		TextView txtPlaceName;
		ImageButton btn_like;
		ImageButton btn_addwish;
		ImageButton btn_markdone;
		TextView tvGesture;
		TextView ivDescription;
		
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		int tabNumber = 0;
		
		final HashMap<String, Object> rowItem = (HashMap<String, Object>) getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		System.out.println(rowItem);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.place_row, null);
			holder = new ViewHolder();

			holder.txtPlaceName = (TextView) convertView.findViewById(R.id.tvPlaceName);
			holder.imageView = (ImageView) convertView.findViewById(R.id.ivPlaceImage);
			holder.ivDescription = (TextView) convertView.findViewById(R.id.tvDescription);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();



		final ViewHolder finalHolder = holder;
		holder.txtPlaceName.setText(rowItem.get("place_name").toString());
//		holder.ivDescription.setText(rowItem.get("description").toString());

		final AQuery aq = listAq.recycle(convertView);

		finalHolder.txtPlaceName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context,DetailActivity.class);
				context.startActivity(i);
			}
		});

		String imageUrl = " ";
		aq.id(R.id.ivPlaceImage)
				.progress(R.id.img_progressbar)
				.image(imageUrl, true, true, 0, 0, null, 0,
						AQuery.RATIO_PRESERVE);
		return convertView;
	}
	
		
	@Override
	public int getCount() {

		try {
			return mData.size();
		} catch (Exception e) {
			// Log.i("m4k","mData is empty");
			// System.err.println(mData);
		}
		return 0;

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		// System.out.println(mData);
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
