package com.maps.psabharw.maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
		TextView tvAddress;
		TextView tvFacilities;

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
			holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
			holder.tvFacilities = (TextView) convertView.findViewById(R.id.tvFacilities);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();



		final ViewHolder finalHolder = holder;

		final AQuery aq = listAq.recycle(convertView);

		holder.txtPlaceName.setText(Html.fromHtml("<font color='black'><b>" + rowItem.get("place_name").toString() + "</b></font>"));
		try {
			if (null != rowItem.get("privateplaceattributes")){
				JSONObject privateplaceattributes = new JSONObject(rowItem.get("privateplaceattributes").toString());
				JSONObject owner = privateplaceattributes.getJSONObject("owner");

				String name = owner.getJSONObject("name").toString();
				String email = owner.getJSONObject("email").toString();
				String username = owner.getJSONObject("username").toString();
				String userid = owner.getJSONObject("id").toString();

				System.out.println("Sytems owner print");
				System.out.println(owner);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			JSONArray facilities = new JSONArray(rowItem.get("facilities").toString());
			StringBuilder out = new StringBuilder();

			for(int i=0;i<facilities.length();i++)
			{
				out.append(facilities.getString(i)); // item at index i
				if( (i+1) < facilities.length() ){
					out.append(", ");
				}
			}
			holder.tvFacilities.setText(out);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		holder.tvAddress.setText(Html.fromHtml(rowItem.get("street").toString() + "  " + rowItem.get("locality").toString()));

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
				.image(imageUrl, true, true, 0, R.mipmap.img, null, 0,
						AQuery.RATIO_PRESERVE);

		finalHolder.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, DetailActivity.class);
                context.startActivity(i);
			}
		});

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
