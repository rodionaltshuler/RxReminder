package com.ottamotta.reminder.userActivity;

import android.support.annotation.Nullable;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.annotations.SerializedName;

public enum UserActivity {

    @SerializedName("walking")
    WALKING("walking"),
    @SerializedName("cycling")
    CYCLING("cycling"),
    @SerializedName("driving")
    DRIVING("driving");

    private String name;

    UserActivity(String name) {
        this.name = name;
    }

    public static UserActivity fromDetectedActivity(DetectedActivity detectedActivity) {
        switch (detectedActivity.getType()) {
            case 0:
                return DRIVING;
            case 1:
                return CYCLING;
            default:
                return UserActivity.WALKING;
        }
    }

    public static @Nullable UserActivity byName(String name) {
        for (UserActivity activity : values()) {
            if (activity.getName().equals(name)) {
                return activity;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

}
