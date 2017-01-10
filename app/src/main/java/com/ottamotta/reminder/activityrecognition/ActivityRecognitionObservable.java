package com.ottamotta.reminder.activityrecognition;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;

import rx.Observable;
import rx.Observer;

class ActivityRecognitionObservable extends Observable<ActivityRecognitionResult> {

    private static final String TAG = ActivityRecognitionObservable.class.getSimpleName();

    private static final String ACTION_ACTIVITY_DETECTED = "com.ottamotta.smartreminder.ACTION_ACTIVITY_UPDATE_DETECTED";

    private static final int INTERVAL = 3 * 1000;

    public ActivityRecognitionObservable(Context context, GoogleApiClient apiClient) {
        super(subscriber -> {
            ActivityUpdatesBroadcastReceiver receiver = new ActivityUpdatesBroadcastReceiver(subscriber);
            context.registerReceiver(receiver, new IntentFilter(ACTION_ACTIVITY_DETECTED));
            PendingIntent receiverIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_ACTIVITY_DETECTED), PendingIntent.FLAG_UPDATE_CURRENT);
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient, INTERVAL, receiverIntent);
        });
    }

    private static class ActivityUpdatesBroadcastReceiver extends BroadcastReceiver {
        private final Observer<? super ActivityRecognitionResult> observer;

        public ActivityUpdatesBroadcastReceiver(Observer<? super ActivityRecognitionResult> observer) {
            this.observer = observer;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ActivityRecognitionResult.hasResult(intent)) {
                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                observer.onNext(result);
            }
        }
    }
}
