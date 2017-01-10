package com.ottamotta.reminder.activityrecognition;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.ottamotta.reminder.googleapiclient.GoogleApiClientObservable;

import rx.Single;
import rx.functions.Func1;

public class ActivityRecognition {

    private Context context;

    public ActivityRecognition(Context context) {
        this.context = context;
    }

    public Single<ActivityRecognitionResult> getSingle() {
        return new GoogleApiClientObservable(context)
                .flatMap(new Func1<GoogleApiClient, Single<ActivityRecognitionResult>>() {
                    @Override
                    public Single<ActivityRecognitionResult> call(GoogleApiClient googleApiClient) {
                        return new ActivityRecognitionSingle(context, googleApiClient);
                    }
                });
    }
}
