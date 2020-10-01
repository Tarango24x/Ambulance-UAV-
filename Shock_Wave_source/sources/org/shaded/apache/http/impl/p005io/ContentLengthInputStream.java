package org.shaded.apache.http.impl.p005io;

import java.io.IOException;
import java.io.InputStream;
import org.shaded.apache.http.p006io.SessionInputBuffer;

/* renamed from: org.shaded.apache.http.impl.io.ContentLengthInputStream */
public class ContentLengthInputStream extends InputStream {
    private static final int BUFFER_SIZE = 2048;
    private boolean closed = false;
    private long contentLength;

    /* renamed from: in */
    private SessionInputBuffer f278in = null;
    private long pos = 0;

    public ContentLengthInputStream(SessionInputBuffer in, long contentLength2) {
        if (in == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        } else if (contentLength2 < 0) {
            throw new IllegalArgumentException("Content length may not be negative");
        } else {
            this.f278in = in;
            this.contentLength = contentLength2;
        }
    }

    public void close() throws IOException {
        if (!this.closed) {
            try {
                do {
                } while (read(new byte[2048]) >= 0);
            } finally {
                this.closed = true;
            }
        }
    }

    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.pos >= this.contentLength) {
            return -1;
        } else {
            this.pos++;
            return this.f278in.read();
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.pos >= this.contentLength) {
            return -1;
        } else {
            if (this.pos + ((long) len) > this.contentLength) {
                len = (int) (this.contentLength - this.pos);
            }
            int count = this.f278in.read(b, off, len);
            this.pos += (long) count;
            return count;
        }
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public long skip(long n) throws IOException {
        int l;
        if (n <= 0) {
            return 0;
        }
        byte[] buffer = new byte[2048];
        long remaining = Math.min(n, this.contentLength - this.pos);
        long count = 0;
        while (remaining > 0 && (l = read(buffer, 0, (int) Math.min(2048, remaining))) != -1) {
            count += (long) l;
            remaining -= (long) l;
        }
        return count;
    }
}
