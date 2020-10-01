package org.shaded.apache.http.impl.conn;

import java.io.IOException;
import org.shaded.apache.http.annotation.Immutable;
import org.shaded.apache.http.p006io.HttpTransportMetrics;
import org.shaded.apache.http.p006io.SessionInputBuffer;
import org.shaded.apache.http.util.CharArrayBuffer;

@Immutable
public class LoggingSessionInputBuffer implements SessionInputBuffer {

    /* renamed from: in */
    private final SessionInputBuffer f276in;
    private final Wire wire;

    public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire2) {
        this.f276in = in;
        this.wire = wire2;
    }

    public boolean isDataAvailable(int timeout) throws IOException {
        return this.f276in.isDataAvailable(timeout);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int l = this.f276in.read(b, off, len);
        if (this.wire.enabled() && l > 0) {
            this.wire.input(b, off, l);
        }
        return l;
    }

    public int read() throws IOException {
        int l = this.f276in.read();
        if (this.wire.enabled() && l != -1) {
            this.wire.input(l);
        }
        return l;
    }

    public int read(byte[] b) throws IOException {
        int l = this.f276in.read(b);
        if (this.wire.enabled() && l > 0) {
            this.wire.input(b, 0, l);
        }
        return l;
    }

    public String readLine() throws IOException {
        String s = this.f276in.readLine();
        if (this.wire.enabled() && s != null) {
            this.wire.input(s + "[EOL]");
        }
        return s;
    }

    public int readLine(CharArrayBuffer buffer) throws IOException {
        int l = this.f276in.readLine(buffer);
        if (this.wire.enabled() && l >= 0) {
            this.wire.input(new String(buffer.buffer(), buffer.length() - l, l) + "[EOL]");
        }
        return l;
    }

    public HttpTransportMetrics getMetrics() {
        return this.f276in.getMetrics();
    }
}
