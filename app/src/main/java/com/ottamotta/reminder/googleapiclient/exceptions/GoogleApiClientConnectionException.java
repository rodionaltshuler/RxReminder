package com.ottamotta.reminder.googleapiclient.exceptions;

import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;

public class GoogleApiClientConnectionException extends Throwable {

    @Nullable public final ConnectionResult connectionResult;

    public GoogleApiClientConnectionException(@Nullable ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
    }
}
