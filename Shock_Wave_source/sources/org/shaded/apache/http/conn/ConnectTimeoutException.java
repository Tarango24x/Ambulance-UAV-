package org.shaded.apache.http.conn;

import java.io.InterruptedIOException;
import org.shaded.apache.http.annotation.Immutable;

@Immutable
public class ConnectTimeoutException extends InterruptedIOException {
    private static final long serialVersionUID = -4816682903149535989L;

    public ConnectTimeoutException() {
    }

    public ConnectTimeoutException(String message) {
        super(message);
    }
}
