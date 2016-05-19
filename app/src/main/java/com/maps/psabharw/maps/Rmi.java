package com.maps.psabharw.maps;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

public class Rmi {
	
	
	String data = null;
	private static final String TAG = "RMI";
	private JSONObject returnJsonObj = null;
	private String returnJsonString = null;
	
	private String requestURL = null;
		
	public JSONObject get(String URL){
		
		data = sendRequest(URL);		
		
		new HttpGetTask().execute();
		
		try {
			if(null!=data){
				returnJsonObj = new JSONObject(data);
			}else{
				Log.i(TAG,"Response data is null");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnJsonObj;	
	}
	
	public JSONObject post(String URL,Map<String, String> params){
		
		data = sendRequest(URL);		
		
		try {
			returnJsonObj = new JSONObject(data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnJsonObj;	
	}
	

	private class HttpGetTask extends AsyncTask<String, Void, String> {

		
		
		
		
		@Override
		protected String doInBackground(String... params) {
			String data = "";
			HttpURLConnection httpUrlConnection = null;

			try {
				httpUrlConnection = (HttpURLConnection) new URL(requestURL)
						.openConnection();

				InputStream in = new BufferedInputStream(
						httpUrlConnection.getInputStream());

				data = readStream(in);

			} catch (MalformedURLException exception) {
				Log.e(TAG, "MalformedURLException");
			} catch (IOException exception) {
				Log.e(TAG, "IOException");
			} finally {
				if (null != httpUrlConnection)
					httpUrlConnection.disconnect();
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			returnJsonString = result;
		}
		
	}
	
	private String readStream(InputStream in) {
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
	
	
	
	private String sendRequest(final String URL) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.
		ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		new Thread(new Runnable() {
			   public void run() {

				   String data = "";

                   // TODO Auto-generated method stub
					HttpURLConnection httpUrlConnection = null;

					try {
						httpUrlConnection = (HttpURLConnection) new URL(URL)
								.openConnection();
						InputStream in = new BufferedInputStream(
								httpUrlConnection.getInputStream());
						data = readStream(in);
						Log.i(TAG,"Respose from "+URL);
						System.out.println(data);

					} catch (MalformedURLException exception) {
						Log.e(TAG, "MalformedURLException");
					} catch (IOException exception) {
						Log.e(TAG, "IOException");
					} finally {
						if (null != httpUrlConnection)
							httpUrlConnection.disconnect();
					}
			   }                        
		}).start();
		
		return data;
	}
}
