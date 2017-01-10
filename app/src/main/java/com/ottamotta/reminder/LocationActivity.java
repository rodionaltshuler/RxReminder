package com.ottamotta.reminder;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ottamotta.reminder.activityrecognition.ActivityRecognition;
import com.ottamotta.reminder.userActivity.UserActivity;
import com.ottamotta.reminder.location.LocationSubject;
import com.ottamotta.reminder.location.PromptUserActivity;
import com.ottamotta.reminder.store.TrackPoint;
import com.ottamotta.reminder.userActivity.ActivityRecognizedTransformer;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class LocationActivity extends AppCompatActivity {

    private TextView textView;

    private LocationSubject locationObservable;

    private Subscription subscription;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        textView = (TextView) findViewById(R.id.text);
        locationObservable = LocationSubject.getInstance(getApplicationContext());
        context = this.getApplicationContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscription = trackPointObservable(
                locationObservable.getLocationObservable(),
                new ActivityRecognition(context).getObservable().compose(new ActivityRecognizedTransformer())
        )
                .doOnNext(trackPoint -> Log.d("TrackPoint", trackPoint.toString()))
                .subscribe(observer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private Observable<TrackPoint> trackPointObservable(Observable<Location> locationObservable,
                                                        Observable<UserActivity> userActivityObservable) {
        return Observable.combineLatest(locationObservable, userActivityObservable, (location, userActivity) -> {
            TrackPoint trackPoint = new TrackPoint();
            trackPoint.setLocation(location.getLatitude(), location.getLongitude());
            trackPoint.setUserActivity(userActivity);
            return trackPoint;
        });
    }

    private Observer<TrackPoint> observer = new Observer<TrackPoint>() {
        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {
            PromptUserActivity.startResolution(LocationActivity.this, e);
            textView.setText(e.getMessage());
        }

        @Override
        public void onNext(TrackPoint trackPoint) {
            textView.setText(trackPoint.toString());
        }
    };

}

