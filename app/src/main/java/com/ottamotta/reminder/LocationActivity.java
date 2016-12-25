package com.ottamotta.reminder;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ottamotta.reminder.location.LocationSubject;
import com.ottamotta.reminder.location.PromptUserActivity;

import rx.Observer;
import rx.Subscription;

public class LocationActivity extends AppCompatActivity {

    private TextView textView;

    private LocationSubject locationObservable;

    private Subscription locationSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        textView = (TextView) findViewById(R.id.text);
        locationObservable = LocationSubject.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationSubscription = locationObservable.subscribe(locationObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationSubscription != null && !locationSubscription.isUnsubscribed()) {
            locationSubscription.unsubscribe();
        }
    }

    private Observer<Location> locationObserver = new Observer<Location>() {
        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {
            PromptUserActivity.startResolution(LocationActivity.this, e);
            textView.setText(e.getMessage());
        }

        @Override
        public void onNext(Location location) {
            textView.setText(location.toString());
        }
    };

}
