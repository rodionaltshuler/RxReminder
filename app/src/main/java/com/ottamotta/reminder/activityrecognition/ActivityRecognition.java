package com.ottamotta.reminder.activityrecognition;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.ottamotta.reminder.googleapiclient.GoogleApiClientObservable;

import rx.Observable;
import rx.functions.Func1;

public class ActivityRecognition {

    private Context context;

    public ActivityRecognition(Context context) {
        this.context = context;
    }

    public Observable<ActivityRecognitionResult> getObservable() {
        return new GoogleApiClientObservable(context)
                .toObservable()
                .flatMap(new Func1<GoogleApiClient, Observable<ActivityRecognitionResult>>() {
                    @Override
                    public Observable<ActivityRecognitionResult> call(GoogleApiClient googleApiClient) {
                        return new ActivityRecognitionObservable(context, googleApiClient);
                    }
                })
                .doOnNext(activityRecognitionResult ->
                        Log.d("ActvitiyRecognition", activityRecognitionResult.getMostProbableActivity().toString()))
                .doOnError(error -> Log.e("ActvitiyRecognition", error.getMessage()));
    }
}
