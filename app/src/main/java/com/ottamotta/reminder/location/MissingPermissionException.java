package com.ottamotta.reminder.location;

import java.util.ArrayList;
import java.util.List;

public class MissingPermissionException extends Throwable {

    public final List<String> missingPermissions = new ArrayList<>();

    public MissingPermissionException(List<String> missingPermissions) {
        this.missingPermissions.addAll(missingPermissions);
    }
}
