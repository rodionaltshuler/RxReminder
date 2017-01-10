package com.ottamotta.reminder.googleapiclient;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.ottamotta.reminder.googleapiclient.exceptions.GoogleApiClientConnectionException;

import rx.Single;

public class GoogleApiClientObservable extends Single<GoogleApiClient> {

    public GoogleApiClientObservable(Context context) {
        super(singleSubscriber -> {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addApi(ActivityRecognition.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();

            googleApiClient.registerConnectionFailedListener(connectionResult -> {
                singleSubscriber.onError(new GoogleApiClientConnectionException(connectionResult));
            });


            googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    singleSubscriber.onSuccess(googleApiClient);
                }

                @Override
                public void onConnectionSuspended(int i) {
                }
            });

            googleApiClient.connect();
        });
    }


}
