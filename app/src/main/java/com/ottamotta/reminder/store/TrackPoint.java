package com.ottamotta.reminder.store;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ottamotta.reminder.activityrecognition.UserActivity;

public class TrackPoint {

    @Expose
    private long time;

    @Expose
    @SerializedName("time_mins")
    private int timeMins;

    @Expose
    private double lat;

    @Expose
    private double lon;

    @Expose
    private String comment;

    @Expose
    @SerializedName("activity")
    private UserActivity userActivity;

    @Expose
    @SerializedName("store_name")
    private String detectedStoreName;

    public TrackPoint() {

    }

    public TrackPoint(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        time = location.getTime();
    }

    public long getTimeMillis() {
        return timeMins == 0 ? time : timeMins * 60 * 1000;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getComment() {
        return comment;
    }

    public UserActivity getUserActivity() {
        return userActivity;
    }

    @Override
    public String toString() {
        return "TrackPoint{" +
                "time=" + time +
                ", timeMins=" + timeMins +
                ", lat=" + lat +
                ", lon=" + lon +
                ", comment='" + comment + '\'' +
                ", userActivity=" + userActivity +
                '}';
    }

    public void setUserActivity(UserActivity userActivity) {
        this.userActivity = userActivity;
    }

    public void setLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setDetectedStoreName(String detectedStoreName) {
        this.detectedStoreName = detectedStoreName;
    }
}
