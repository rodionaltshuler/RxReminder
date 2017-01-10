package com.ottamotta.reminder.userActivity;

import com.google.android.gms.location.ActivityRecognitionResult;

import rx.Observable;
import rx.functions.Func1;


public class ActivityRecognizedTransformer implements Observable.Transformer<ActivityRecognitionResult, UserActivity> {

    @Override
    public Observable<UserActivity> call(Observable<ActivityRecognitionResult> activityRecognitionResultObservable) {
        return activityRecognitionResultObservable.flatMap(new Func1<ActivityRecognitionResult, Observable<UserActivity>>() {
            @Override
            public Observable<UserActivity> call(ActivityRecognitionResult activityRecognitionResult) {
                return Observable.just(UserActivity.fromDetectedActivity(activityRecognitionResult.getMostProbableActivity()));
            };
        });
    }
}
