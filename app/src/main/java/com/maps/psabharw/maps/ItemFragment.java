package com.maps.psabharw.maps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.maps.psabharw.maps.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends ListFragment implements AbsListView.OnScrollListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private Context context = null;
    private final String TAG = "item";
    private final String cachedName = "places";
    JSONArray jsonArray = null;
    private ProgressBar progressBar2;
    private TextView finished;
    private Boolean isDownloading = false;
    private Boolean stopDownPagination = false;
    private Integer pageStart = 1;
    private Integer pageEnd = 15;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ItemAdaptor itemAdapter = null;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sendRequest(getUrl());
        // TODO: Change Adapter to display your content
        ArrayList<HashMap<String, Object>> arrayList = null;
        itemAdapter = new ItemAdaptor(getActivity(), arrayList);
        setListAdapter(itemAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    public void sendRequest(String url) {
        new SendRequest().execute(url);
    }




    private class SendRequest extends AsyncTask<String, Integer, ArrayList<HashMap<String, Object>> > {

        // http://www.mysamplecode.com/2012/11/android-progress-bar-example.html

        protected ArrayList<HashMap<String, Object>>  doInBackground(String... requestURL) {

            HttpURLConnection httpUrlConnection = null;

            try {
                URL url = new URL(requestURL[0]);
                URLConnection connection = url.openConnection();
                connection.setUseCaches(true);

                Object response = connection.getContent();

                if (response instanceof Bitmap) {
                }

                InputStream in = new BufferedInputStream(
                        connection.getInputStream());

                String jsonString = Common.readStream(in);
                System.out.println(jsonString);
                Common.cacheResponse(context,cachedName,jsonString);

                jsonArray = Common.getArrayFromJsonString(jsonString, 0);
                ArrayList<HashMap<String, Object>> placeList = Common.parseJson(jsonArray);

                return placeList;

            } catch (MalformedURLException exception) {
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                Log.e(TAG, "IOException");
            } finally {
                if (null != httpUrlConnection)
                    httpUrlConnection.disconnect();
            }
            return null;

        }


        protected void onProgressUpdate(Integer... progress) {
            finished.setVisibility(View.VISIBLE);
            finished.setText(String.valueOf(progress[0]) + "%");
            progressBar2.setProgress(progress[0]);
        }

        protected void onCancelled() {
            Toast toast = Toast.makeText(context, "Error connecting to Server",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            isDownloading = true;
//            Common.setRefreshActionButtonState(true,optionsMenu);
        }

        protected void onPostExecute(ArrayList<HashMap<String, Object>> itemList) {

            if(itemList == null){
                stopDownPagination = true;
            }

            if (pageEnd > Constants.limit && stopDownPagination == false) {
                itemAdapter.mData.addAll(itemList);
                itemAdapter.notifyDataSetChanged();
            }else if(stopDownPagination == false){
                Log.i(TAG,"Inside for first time ");
                itemAdapter = new ItemAdaptor(getActivity(),itemList);
                setListAdapter(itemAdapter);
            }

//            Common.setRefreshActionButtonState(false,optionsMenu);
            isDownloading = false;
        }

    }

    private ArrayList<HashMap<String, Object>> getPlaceCache() {

        String jsonString = Common.getCachedResponse(context, cachedName);

        try {
            jsonArray = Common.getArrayFromJsonString(jsonString, 0);
            ArrayList<HashMap<String, Object>> places = Common
                    .parseJson(jsonArray);
            return places;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;

    }

    public static JSONArray getObjectFromJsonString(String jsonString, int index) {

        try {
            JSONObject obj = new JSONObject(jsonString);
            JSONObject m_j_rObject = obj.getJSONObject("results");

            return m_j_rObject.getJSONArray("feeds");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getUrl() {
        String url = "http://django-startupguwahati.rhcloud.com/places"; //?search=" + userid + "&responsetype=json&viewtype=other";

        Log.i(TAG,url);
        return url;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && isDownloading == false) {

            pageStart = pageEnd + 1;
            pageEnd = pageStart + (Constants.limit -1);
            String url = getUrl();
            sendRequest(url);
            isDownloading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}


}
