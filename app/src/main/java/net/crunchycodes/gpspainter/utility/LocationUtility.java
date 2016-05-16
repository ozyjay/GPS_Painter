package net.crunchycodes.gpspainter.utility;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationUtility {
    private LocationUpdateListener listener;
    private LocationUtilityImpl implementation;
    private GoogleApiClient client;
    private LocationRequest request;
    private Activity activity;
    private int accuracy;
    private boolean isAccurate;

    public LocationUtility(Activity activity) {
        this.activity = activity;
        accuracy = 10;
        implementation = new LocationUtilityImpl();
        client = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(implementation)
                .addOnConnectionFailedListener(implementation)
                .addApi(LocationServices.API)
                .build();

        request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)
                .setFastestInterval(1000);
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public boolean isAccurate() {
        return isAccurate;
    }

    public void setLocationUpdate(LocationUpdateListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (client.isConnected()) stop();
        client.connect();
    }

    public void stop() {
        if (client.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, implementation);
            client.disconnect();
        }
    }


    // OOP implementation
    private class LocationUtilityImpl implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener {

        @Override
        public void onConnected(Bundle bundle) {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
                Log.i("LocationUtility", "connected and receiving location updates");
            } else {
                Log.w("LocationUtility", "permission denied: fine location data");
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.w("LocationUtility", "connection suspended");
            LocationServices.FusedLocationApi.removeLocationUpdates(client, implementation);
        }

        @Override
        public void onLocationChanged(Location location) {
            if (listener == null) return;
            if (location.getAccuracy() <= accuracy) {
                isAccurate = true;
                listener.update(location.getLatitude(), location.getLongitude());
            } else {
                isAccurate = false;
            }
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            int errorCode = connectionResult.getErrorCode();
            if (errorCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, errorCode, 1);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        client.connect(); // try again
                    }
                });
                dialog.show();
            } else {
                Log.i("LocationUtility", "Location services connection failed with code " + errorCode);
            }
        }
    }
}
