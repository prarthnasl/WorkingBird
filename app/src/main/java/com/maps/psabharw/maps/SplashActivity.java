package com.maps.psabharw.maps;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.annotation.TargetApi;
import android.app.Activity;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class SplashActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String STORAGEFILE = "StorageFile";
    private GoogleApiClient mGoogleApiClient;
    String mLatitude;
    String mLongitude;

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        this.buildGoogleApiClient();
        settings = getSharedPreferences(STORAGEFILE, 0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Thread timer = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{


                    SharedPreferences settings = getSharedPreferences(STORAGEFILE, 0);

                    //Just For Testing Purpose...
                    //SharedPreferences.Editor editor = settings.edit();
                    //editor.clear();
                    //editor.commit();

                    mLatitude = settings.getString("mLatitude", "null");
                    mLongitude = settings.getString("mLongitude", "null");

                    Intent intent = new Intent(SplashActivity.this,DashboardActivity.class);
                    intent.putExtra("mLatitude", mLatitude);
                    intent.putExtra("mLongitude", mLongitude);
                    startActivity(intent);

                }
            }
        };
        timer.start();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("mLatitude", String.valueOf(mLastLocation.getLatitude()));
            editor.putString("mLongitude", String.valueOf(mLastLocation.getLongitude()));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
