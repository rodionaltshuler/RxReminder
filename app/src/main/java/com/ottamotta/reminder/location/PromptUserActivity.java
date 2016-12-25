package com.ottamotta.reminder.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.ottamotta.reminder.location.exceptions.GoogleApiClientConnectionException;
import com.ottamotta.reminder.location.exceptions.LocationDisabledException;
import com.ottamotta.reminder.location.exceptions.MissingPermissionException;

//TODO don't use it as standalone activity - it affects UX
public class PromptUserActivity extends AppCompatActivity {

    public static final int ENABLE_LOCATION_REQUEST_CODE = 101;

    public static final int GET_LOCATION_PERMISSIONS_REQUEST_REQUEST_CODE = 102;

    public static final int LOCATION_SERVICES_CONNECTION_RESULT = 103;

    private static final String KEY_REQUEST_CODE = "key_request_code";
    private static final String KEY_CONNECTION_RESULT = "key_connection_result";

    String[] locationPermissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static void startResolution(Activity src, Throwable e) {
        if (e instanceof GoogleApiClientConnectionException) {
            handleLocationServiceConnectionError(src, ((GoogleApiClientConnectionException) e).connectionResult);
        }

        if (e instanceof SecurityException || e instanceof MissingPermissionException) {
            getLocationPermissions(src);
        }

        if (e instanceof LocationDisabledException) {
            notifyLocationDisabled(src);
        }
    }

    private static void notifyLocationDisabled(Context context) {
        Intent i = new Intent(context, PromptUserActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(KEY_REQUEST_CODE, ENABLE_LOCATION_REQUEST_CODE);
        context.startActivity(i);
    }

    private static void getLocationPermissions(Context context) {
        Intent i = new Intent(context, PromptUserActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(KEY_REQUEST_CODE, GET_LOCATION_PERMISSIONS_REQUEST_REQUEST_CODE);
        context.startActivity(i);
    }


    private static void handleLocationServiceConnectionError(Context context, ConnectionResult connectionResult) {
        Intent i = new Intent(context, PromptUserActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(KEY_REQUEST_CODE, LOCATION_SERVICES_CONNECTION_RESULT);
        i.putExtra(KEY_CONNECTION_RESULT, connectionResult);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int code = getIntent().getIntExtra(KEY_REQUEST_CODE, 0);
        switch (code) {
            case ENABLE_LOCATION_REQUEST_CODE:
                Toast.makeText(this, "Location disabled - please enable", Toast.LENGTH_SHORT).show();
                Intent resolutionIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(resolutionIntent, ENABLE_LOCATION_REQUEST_CODE);
                break;
            case GET_LOCATION_PERMISSIONS_REQUEST_REQUEST_CODE:
                ActivityCompat.requestPermissions(this, locationPermissions, GET_LOCATION_PERMISSIONS_REQUEST_REQUEST_CODE);
                break;
            case LOCATION_SERVICES_CONNECTION_RESULT:
                ConnectionResult connectionResult = getIntent().getParcelableExtra(KEY_CONNECTION_RESULT);
                if (connectionResult != null) {
                    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
                    int result = googleAPI.isGooglePlayServicesAvailable(this);
                    if (!connectionResult.isSuccess() && googleAPI.isUserResolvableError(result)) {
                        googleAPI.getErrorDialog(this, result, 9000).show();
                    } else {
                        Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GET_LOCATION_PERMISSIONS_REQUEST_REQUEST_CODE:
                for (int result : grantResults) {
                    if (0 == result) {
                        Toast.makeText(this, "Stopping location service", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                Toast.makeText(this, "Got location permissions!", Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
