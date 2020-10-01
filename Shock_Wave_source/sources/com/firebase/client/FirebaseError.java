package com.firebase.client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FirebaseError {
    public static final int AUTHENTICATION_PROVIDER_DISABLED = -12;
    public static final int DATA_STALE = -1;
    public static final int DENIED_BY_USER = -19;
    public static final int DISCONNECTED = -4;
    public static final int EMAIL_TAKEN = -18;
    public static final int EXPIRED_TOKEN = -6;
    public static final int INVALID_AUTH_ARGUMENTS = -21;
    public static final int INVALID_CONFIGURATION = -13;
    public static final int INVALID_CREDENTIALS = -20;
    public static final int INVALID_EMAIL = -15;
    public static final int INVALID_PASSWORD = -16;
    public static final int INVALID_PROVIDER = -14;
    public static final int INVALID_TOKEN = -7;
    public static final int LIMITS_EXCEEDED = -23;
    public static final int MAX_RETRIES = -8;
    public static final int NETWORK_ERROR = -24;
    public static final int OPERATION_FAILED = -2;
    public static final int OVERRIDDEN_BY_SET = -9;
    public static final int PERMISSION_DENIED = -3;
    public static final int PREEMPTED = -5;
    public static final int PROVIDER_ERROR = -22;
    public static final int UNAVAILABLE = -10;
    public static final int UNKNOWN_ERROR = -999;
    public static final int USER_CODE_EXCEPTION = -11;
    public static final int USER_DOES_NOT_EXIST = -17;
    public static final int WRITE_CANCELED = -25;
    private static final Map<String, Integer> errorCodes = new HashMap();
    private static final Map<Integer, String> errorReasons = new HashMap();
    private final int code;
    private final String details;
    private final String message;

    static {
        errorReasons.put(-1, "The transaction needs to be run again with current data");
        errorReasons.put(-2, "The server indicated that this operation failed");
        errorReasons.put(-3, "This client does not have permission to perform this operation");
        errorReasons.put(-4, "The operation had to be aborted due to a network disconnect");
        errorReasons.put(-5, "The active or pending auth credentials were superseded by another call to auth");
        errorReasons.put(-6, "The supplied auth token has expired");
        errorReasons.put(-7, "The supplied auth token was invalid");
        errorReasons.put(-8, "The transaction had too many retries");
        errorReasons.put(-9, "The transaction was overridden by a subsequent set");
        errorReasons.put(-10, "The service is unavailable");
        errorReasons.put(-11, "User code called from the Firebase runloop threw an exception:\n");
        errorReasons.put(-12, "The specified authentication type is not enabled for this Firebase.");
        errorReasons.put(-13, "The specified authentication type is not properly configured for this Firebase.");
        errorReasons.put(-14, "Invalid provider specified, please check application code.");
        errorReasons.put(-15, "The specified email address is incorrect.");
        errorReasons.put(-16, "The specified password is incorrect.");
        errorReasons.put(-17, "The specified user does not exist.");
        errorReasons.put(-18, "The specified email address is already in use.");
        errorReasons.put(-19, "User denied authentication request.");
        errorReasons.put(-20, "Invalid authentication credentials provided.");
        errorReasons.put(-21, "Invalid authentication arguments provided.");
        errorReasons.put(-22, "A third-party provider error occurred. See data for details.");
        errorReasons.put(-23, "Limits exceeded.");
        errorReasons.put(-24, "The operation could not be performed due to a network error");
        errorReasons.put(-25, "The write was canceled by the user.");
        errorReasons.put(Integer.valueOf(UNKNOWN_ERROR), "An unknown error occurred");
        errorCodes.put("datastale", -1);
        errorCodes.put("failure", -2);
        errorCodes.put("permission_denied", -3);
        errorCodes.put("disconnected", -4);
        errorCodes.put("preempted", -5);
        errorCodes.put("expired_token", -6);
        errorCodes.put("invalid_token", -7);
        errorCodes.put("maxretries", -8);
        errorCodes.put("overriddenbyset", -9);
        errorCodes.put("unavailable", -10);
        errorCodes.put("authentication_disabled", -12);
        errorCodes.put("invalid_configuration", -13);
        errorCodes.put("invalid_provider", -14);
        errorCodes.put("invalid_email", -15);
        errorCodes.put("invalid_password", -16);
        errorCodes.put("invalid_user", -17);
        errorCodes.put("email_taken", -18);
        errorCodes.put("user_denied", -19);
        errorCodes.put("invalid_credentials", -20);
        errorCodes.put("invalid_arguments", -21);
        errorCodes.put("provider_error", -22);
        errorCodes.put("limits_exceeded", -23);
        errorCodes.put("network_error", -24);
        errorCodes.put("write_canceled", -25);
    }

    public static FirebaseError fromStatus(String status) {
        return fromStatus(status, (String) null);
    }

    public static FirebaseError fromStatus(String status, String reason) {
        return fromStatus(status, reason, (String) null);
    }

    public static FirebaseError fromCode(int code2) {
        if (errorReasons.containsKey(Integer.valueOf(code2))) {
            return new FirebaseError(code2, errorReasons.get(Integer.valueOf(code2)), (String) null);
        }
        throw new IllegalArgumentException("Invalid Firebase error code: " + code2);
    }

    public static FirebaseError fromStatus(String status, String reason, String details2) {
        String message2;
        Integer code2 = errorCodes.get(status.toLowerCase());
        if (code2 == null) {
            code2 = Integer.valueOf(UNKNOWN_ERROR);
        }
        if (reason == null) {
            message2 = errorReasons.get(code2);
        } else {
            message2 = reason;
        }
        return new FirebaseError(code2.intValue(), message2, details2);
    }

    public static FirebaseError fromException(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return new FirebaseError(-11, errorReasons.get(-11) + stringWriter.toString());
    }

    public FirebaseError(int code2, String message2) {
        this(code2, message2, (String) null);
    }

    public FirebaseError(int code2, String message2, String details2) {
        this.code = code2;
        this.message = message2;
        this.details = details2 == null ? "" : details2;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }

    public String toString() {
        return "FirebaseError: " + this.message;
    }

    public FirebaseException toException() {
        return new FirebaseException("Firebase error: " + this.message);
    }
}
