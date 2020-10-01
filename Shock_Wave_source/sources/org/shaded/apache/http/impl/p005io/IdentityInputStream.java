package org.shaded.apache.http.impl.p005io;

import java.io.IOException;
import java.io.InputStream;
import org.shaded.apache.http.p006io.SessionInputBuffer;

/* renamed from: org.shaded.apache.http.impl.io.IdentityInputStream */
public class IdentityInputStream extends InputStream {
    private boolean closed = false;

    /* renamed from: in */
    private final SessionInputBuffer f279in;

    public IdentityInputStream(SessionInputBuffer in) {
        if (in == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        }
        this.f279in = in;
    }

    public int available() throws IOException {
        if (this.closed || !this.f279in.isDataAvailable(10)) {
            return 0;
        }
        return 1;
    }

    public void close() throws IOException {
        this.closed = true;
    }

    public int read() throws IOException {
        if (this.closed) {
            return -1;
        }
        return this.f279in.read();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            return -1;
        }
        return this.f279in.read(b, off, len);
    }
}
