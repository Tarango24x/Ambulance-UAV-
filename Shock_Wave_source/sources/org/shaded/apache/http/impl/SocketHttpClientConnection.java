package org.shaded.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import org.shaded.apache.http.HttpInetConnection;
import org.shaded.apache.http.impl.p005io.SocketInputBuffer;
import org.shaded.apache.http.impl.p005io.SocketOutputBuffer;
import org.shaded.apache.http.p006io.SessionInputBuffer;
import org.shaded.apache.http.p006io.SessionOutputBuffer;
import org.shaded.apache.http.params.HttpConnectionParams;
import org.shaded.apache.http.params.HttpParams;

public class SocketHttpClientConnection extends AbstractHttpClientConnection implements HttpInetConnection {
    private volatile boolean open;
    private volatile Socket socket = null;

    /* access modifiers changed from: protected */
    public void assertNotOpen() {
        if (this.open) {
            throw new IllegalStateException("Connection is already open");
        }
    }

    /* access modifiers changed from: protected */
    public void assertOpen() {
        if (!this.open) {
            throw new IllegalStateException("Connection is not open");
        }
    }

    /* access modifiers changed from: protected */
    public SessionInputBuffer createSessionInputBuffer(Socket socket2, int buffersize, HttpParams params) throws IOException {
        return new SocketInputBuffer(socket2, buffersize, params);
    }

    /* access modifiers changed from: protected */
    public SessionOutputBuffer createSessionOutputBuffer(Socket socket2, int buffersize, HttpParams params) throws IOException {
        return new SocketOutputBuffer(socket2, buffersize, params);
    }

    /* access modifiers changed from: protected */
    public void bind(Socket socket2, HttpParams params) throws IOException {
        if (socket2 == null) {
            throw new IllegalArgumentException("Socket may not be null");
        } else if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.socket = socket2;
            int buffersize = HttpConnectionParams.getSocketBufferSize(params);
            init(createSessionInputBuffer(socket2, buffersize, params), createSessionOutputBuffer(socket2, buffersize, params), params);
            this.open = true;
        }
    }

    public boolean isOpen() {
        return this.open;
    }

    /* access modifiers changed from: protected */
    public Socket getSocket() {
        return this.socket;
    }

    public InetAddress getLocalAddress() {
        if (this.socket != null) {
            return this.socket.getLocalAddress();
        }
        return null;
    }

    public int getLocalPort() {
        if (this.socket != null) {
            return this.socket.getLocalPort();
        }
        return -1;
    }

    public InetAddress getRemoteAddress() {
        if (this.socket != null) {
            return this.socket.getInetAddress();
        }
        return null;
    }

    public int getRemotePort() {
        if (this.socket != null) {
            return this.socket.getPort();
        }
        return -1;
    }

    public void setSocketTimeout(int timeout) {
        assertOpen();
        if (this.socket != null) {
            try {
                this.socket.setSoTimeout(timeout);
            } catch (SocketException e) {
            }
        }
    }

    public int getSocketTimeout() {
        if (this.socket == null) {
            return -1;
        }
        try {
            return this.socket.getSoTimeout();
        } catch (SocketException e) {
            return -1;
        }
    }

    public void shutdown() throws IOException {
        this.open = false;
        Socket tmpsocket = this.socket;
        if (tmpsocket != null) {
            tmpsocket.close();
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001b A[ExcHandler: UnsupportedOperationException (e java.lang.UnsupportedOperationException), Splitter:B:3:0x000b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        /*
            r1 = this;
            boolean r0 = r1.open
            if (r0 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            r0 = 0
            r1.open = r0
            r1.doFlush()
            java.net.Socket r0 = r1.socket     // Catch:{ IOException -> 0x001f, UnsupportedOperationException -> 0x001b }
            r0.shutdownOutput()     // Catch:{ IOException -> 0x001f, UnsupportedOperationException -> 0x001b }
        L_0x0010:
            java.net.Socket r0 = r1.socket     // Catch:{ IOException -> 0x001d, UnsupportedOperationException -> 0x001b }
            r0.shutdownInput()     // Catch:{ IOException -> 0x001d, UnsupportedOperationException -> 0x001b }
        L_0x0015:
            java.net.Socket r0 = r1.socket
            r0.close()
            goto L_0x0004
        L_0x001b:
            r0 = move-exception
            goto L_0x0015
        L_0x001d:
            r0 = move-exception
            goto L_0x0015
        L_0x001f:
            r0 = move-exception
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.http.impl.SocketHttpClientConnection.close():void");
    }
}
