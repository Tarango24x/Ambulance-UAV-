package org.shaded.apache.http.impl.p005io;

import org.shaded.apache.http.p006io.HttpTransportMetrics;

/* renamed from: org.shaded.apache.http.impl.io.HttpTransportMetricsImpl */
public class HttpTransportMetricsImpl implements HttpTransportMetrics {
    private long bytesTransferred = 0;

    public long getBytesTransferred() {
        return this.bytesTransferred;
    }

    public void setBytesTransferred(long count) {
        this.bytesTransferred = count;
    }

    public void incrementBytesTransferred(long count) {
        this.bytesTransferred += count;
    }

    public void reset() {
        this.bytesTransferred = 0;
    }
}
