package com.maps.psabharw.maps;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.util.TimeUtils.*;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Common {

	private static String TAG = "Common";
	private static final String STORAGEFILE = "StorageFile";
	private static HashMap<String, Object> m_li = null;
	
	public static String getCachedResponse(Context context, String filename) {

		String str = new String();
		try {
			FileInputStream fis = context.openFileInput(filename);
			int size = fis.available();
			byte[] buffer = new byte[size];

			fis.read(buffer);
			fis.close();
			str = new String(buffer);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG, "Returning from cached");
		return str;
	}

	public static void cacheResponse(Context context, String filename,
			String fileString) {

		try {
			if (null != fileString && null != context) {
				FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
				fos.write(fileString.getBytes());
				fos.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	public static ArrayList<HashMap<String, Object>> parseJson(JSONArray jsonArray) {

		try {

			ArrayList<HashMap<String, Object>> formList = new ArrayList<HashMap<String, Object>>();
			
			if (null == jsonArray) {
				return null;
			}

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject placeDetails = jsonArray.getJSONObject(i);
				getHashofPlaceDetails(placeDetails);
				formList.add(m_li);

			}
			return formList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuffer data = new StringBuffer("");
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				data.append(line);
			}
		} catch (IOException e) {
			Log.e(TAG, "IOException");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data.toString();
	}


	public static JSONArray getArrayFromJsonString(String jsonString, int index) {

		try {
			JSONObject obj = new JSONObject(jsonString);
//			JSONObject m_j_rObject = obj.getJSONObject("results");

			return obj.getJSONArray("results");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static JSONObject getObjectFromJsonString(String jsonString, int index) {

		try {
			JSONObject obj = new JSONObject(jsonString);
			JSONObject m_j_rObject = obj.getJSONObject("results");

			return m_j_rObject;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static boolean isReturnSuccess(JSONObject object) {

		try {

			String statuscode = object.getJSONObject("wishberg").getString(
					"statuscode");

			if (statuscode.contentEquals("200")) {
				Log.i(TAG, "Inside if ");
				return true;
			} else {
				Log.i(TAG, "Inside else ");
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		Log.i(TAG, "outside try");
		return false;
	}

	public static void logout(Context context) {
		SharedPreferences settings = context.getSharedPreferences(STORAGEFILE,
				0);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}

	public static Spanned makeHashtagColor(String wishname) {
		// String you want to perform on
		String toChange = wishname;

		// Matches all characters, numbers, underscores and dashes followed by
		// '#'
		// Does not match '#' followed by space or any other non word characters
		toChange = toChange.replaceAll("(#[A-Za-z0-9_-]+)",
				"<font color='#0000ff'>" + "$0" + "</font>");

		// Encloses the matched characters with html font tags

		// Html#fromHtml(String) returns a Spanned object

		return Html.fromHtml(toChange);
	}
	
	private static void getHashofPlaceDetails(JSONObject placeDetail) {
		
		try {
			m_li = new HashMap<String, Object>();
			m_li.put("place_name", placeDetail.getString("place_name"));
			m_li.put("description", "Some random description");
//			m_li.put("description", placeDetail.getString("description"));
			m_li.put("placeimages_set", placeDetail.getJSONArray("placeimages_set"));
			m_li.put("privateplaceattributes", placeDetail.getJSONObject("privateplaceattributes"));
			m_li.put("facilities", placeDetail.getJSONArray("facilities"));

			m_li.put("latitude", placeDetail.getString("latitude"));
			m_li.put("longitude", placeDetail.getString("longitude"));
			m_li.put("street", placeDetail.getString("street"));
			m_li.put("is_covered", placeDetail.getString("is_covered"));
			m_li.put("is_private", placeDetail.getString("is_private"));
			m_li.put("locality", placeDetail.getString("locality"));



			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static String getTimeAgo(Date date) {
		long days = (new Date().getTime() - date.getTime()) / 86400000;

	    if(days == 0) return "Today";
	    else if(days == 1) return "Yesterday";
	    else return days + " days ago";
    }

	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
