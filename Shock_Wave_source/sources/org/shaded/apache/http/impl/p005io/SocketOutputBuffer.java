package org.shaded.apache.http.impl.p005io;

import java.io.IOException;
import java.net.Socket;
import org.shaded.apache.http.params.HttpParams;

/* renamed from: org.shaded.apache.http.impl.io.SocketOutputBuffer */
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
    public SocketOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null");
        }
        buffersize = buffersize < 0 ? socket.getSendBufferSize() : buffersize;
        init(socket.getOutputStream(), buffersize < 1024 ? 1024 : buffersize, params);
    }
}
