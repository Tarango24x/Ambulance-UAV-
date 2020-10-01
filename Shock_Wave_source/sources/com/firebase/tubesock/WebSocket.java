package com.firebase.tubesock;

import java.io.IOException;
import java.lang.Thread;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.shaded.apache.http.conn.ssl.StrictHostnameVerifier;
import org.shaded.apache.http.protocol.HTTP;

public class WebSocket {
    static final byte OPCODE_BINARY = 2;
    static final byte OPCODE_CLOSE = 8;
    static final byte OPCODE_NONE = 0;
    static final byte OPCODE_PING = 9;
    static final byte OPCODE_PONG = 10;
    static final byte OPCODE_TEXT = 1;
    private static final String THREAD_BASE_NAME = "TubeSock";
    private static final Charset UTF8 = Charset.forName(HTTP.UTF_8);
    private static final AtomicInteger clientCount = new AtomicInteger(0);
    private static ThreadInitializer intializer = new ThreadInitializer() {
        public void setName(Thread t, String name) {
            t.setName(name);
        }
    };
    private static ThreadFactory threadFactory = Executors.defaultThreadFactory();
    private final int clientId;
    private WebSocketEventHandler eventHandler;
    private final WebSocketHandshake handshake;
    private final Thread innerThread;
    private final WebSocketReceiver receiver;
    private volatile Socket socket;
    private volatile State state;
    private final URI url;
    private final WebSocketWriter writer;

    private enum State {
        NONE,
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }

    static ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    static ThreadInitializer getIntializer() {
        return intializer;
    }

    public static void setThreadFactory(ThreadFactory threadFactory2, ThreadInitializer intializer2) {
        threadFactory = threadFactory2;
        intializer = intializer2;
    }

    public WebSocket(URI url2) {
        this(url2, (String) null);
    }

    public WebSocket(URI url2, String protocol) {
        this(url2, protocol, (Map<String, String>) null);
    }

    public WebSocket(URI url2, String protocol, Map<String, String> extraHeaders) {
        this.state = State.NONE;
        this.socket = null;
        this.eventHandler = null;
        this.clientId = clientCount.incrementAndGet();
        this.innerThread = getThreadFactory().newThread(new Runnable() {
            public void run() {
                WebSocket.this.runReader();
            }
        });
        this.url = url2;
        this.handshake = new WebSocketHandshake(url2, protocol, extraHeaders);
        this.receiver = new WebSocketReceiver(this);
        this.writer = new WebSocketWriter(this, THREAD_BASE_NAME, this.clientId);
    }

    public void setEventHandler(WebSocketEventHandler eventHandler2) {
        this.eventHandler = eventHandler2;
    }

    /* access modifiers changed from: package-private */
    public WebSocketEventHandler getEventHandler() {
        return this.eventHandler;
    }

    public synchronized void connect() {
        if (this.state != State.NONE) {
            this.eventHandler.onError(new WebSocketException("connect() already called"));
            close();
        } else {
            getIntializer().setName(getInnerThread(), "TubeSockReader-" + this.clientId);
            this.state = State.CONNECTING;
            getInnerThread().start();
        }
    }

    public synchronized void send(String data) {
        send((byte) 1, data.getBytes(UTF8));
    }

    public synchronized void send(byte[] data) {
        send((byte) 2, data);
    }

    /* access modifiers changed from: package-private */
    public synchronized void pong(byte[] data) {
        send((byte) 10, data);
    }

    private synchronized void send(byte opcode, byte[] data) {
        if (this.state != State.CONNECTED) {
            this.eventHandler.onError(new WebSocketException("error while sending data: not connected"));
        } else {
            try {
                this.writer.send(opcode, true, data);
            } catch (IOException e) {
                this.eventHandler.onError(new WebSocketException("Failed to send frame", e));
                close();
            }
        }
        return;
    }

    /* access modifiers changed from: package-private */
    public void handleReceiverError(WebSocketException e) {
        this.eventHandler.onError(e);
        if (this.state == State.CONNECTED) {
            close();
        }
        closeSocket();
    }

    public synchronized void close() {
        switch (this.state) {
            case NONE:
                this.state = State.DISCONNECTED;
                break;
            case CONNECTING:
                closeSocket();
                break;
            case CONNECTED:
                sendCloseHandshake();
                break;
        }
    }

    /* access modifiers changed from: package-private */
    public void onCloseOpReceived() {
        closeSocket();
    }

    private synchronized void closeSocket() {
        if (this.state != State.DISCONNECTED) {
            this.receiver.stopit();
            this.writer.stopIt();
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            this.state = State.DISCONNECTED;
            this.eventHandler.onClose();
        }
    }

    private void sendCloseHandshake() {
        try {
            this.state = State.DISCONNECTING;
            this.writer.stopIt();
            this.writer.send((byte) 8, true, new byte[0]);
        } catch (IOException e) {
            this.eventHandler.onError(new WebSocketException("Failed to send close frame", e));
        }
    }

    private Socket createSocket() {
        String scheme = this.url.getScheme();
        String host = this.url.getHost();
        int port = this.url.getPort();
        if (scheme != null && scheme.equals("ws")) {
            if (port == -1) {
                port = 80;
            }
            try {
                return new Socket(host, port);
            } catch (UnknownHostException uhe) {
                throw new WebSocketException("unknown host: " + host, uhe);
            } catch (IOException ioe) {
                throw new WebSocketException("error while creating socket to " + this.url, ioe);
            }
        } else if (scheme == null || !scheme.equals("wss")) {
            throw new WebSocketException("unsupported protocol: " + scheme);
        } else {
            if (port == -1) {
                port = 443;
            }
            try {
                Socket socket2 = SSLSocketFactory.getDefault().createSocket(host, port);
                verifyHost((SSLSocket) socket2, host);
                return socket2;
            } catch (UnknownHostException uhe2) {
                throw new WebSocketException("unknown host: " + host, uhe2);
            } catch (IOException ioe2) {
                throw new WebSocketException("error while creating secure socket to " + this.url, ioe2);
            }
        }
    }

    private void verifyHost(SSLSocket socket2, String host) throws SSLException {
        new StrictHostnameVerifier().verify(host, (X509Certificate) socket2.getSession().getPeerCertificates()[0]);
    }

    public void blockClose() throws InterruptedException {
        if (this.writer.getInnerThread().getState() != Thread.State.NEW) {
            this.writer.getInnerThread().join();
        }
        getInnerThread().join();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r9 = new java.io.DataInputStream(r16.getInputStream());
        r14 = r16.getOutputStream();
        r14.write(r22.handshake.getHandshake());
        r5 = false;
        r3 = new byte[1000];
        r15 = 0;
        r6 = new java.util.ArrayList<>();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0077, code lost:
        if (r5 != false) goto L_0x012c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0079, code lost:
        r2 = r9.read();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0081, code lost:
        if (r2 != -1) goto L_0x00ba;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x008a, code lost:
        throw new com.firebase.tubesock.WebSocketException("Connection closed before handshake was complete");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
        r3[r15] = (byte) r2;
        r15 = r15 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00cb, code lost:
        if (r3[r15 - 1] != 10) goto L_0x0102;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00d7, code lost:
        if (r3[r15 - 2] != 13) goto L_0x0102;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00d9, code lost:
        r13 = new java.lang.String(r3, UTF8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00ec, code lost:
        if (r13.trim().equals("") == false) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00ee, code lost:
        r5 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00ef, code lost:
        r3 = new byte[1000];
        r15 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00f3, code lost:
        r6.add(r13.trim());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0106, code lost:
        if (r15 != 1000) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x012b, code lost:
        throw new com.firebase.tubesock.WebSocketException("Unexpected long line in handshake: " + new java.lang.String(r3, UTF8));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x012c, code lost:
        r22.handshake.verifyServerStatusLine(r6.get(0));
        r6.remove(0);
        r7 = new java.util.HashMap<>();
        r8 = r6.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0157, code lost:
        if (r8.hasNext() == false) goto L_0x017b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0159, code lost:
        r11 = r8.next().split(": ", 2);
        r7.put(r11[0], r11[1]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x017b, code lost:
        r22.handshake.verifyServerHandshakeHeaders(r7);
        r22.writer.setOutput(r14);
        r22.receiver.setInput(r9);
        r22.state = com.firebase.tubesock.WebSocket.State.CONNECTED;
        r22.writer.getInnerThread().start();
        r22.eventHandler.onOpen();
        r22.receiver.run();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x01c3, code lost:
        close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void runReader() {
        /*
            r22 = this;
            java.net.Socket r16 = r22.createSocket()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            monitor-enter(r22)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r16
            r1 = r22
            r1.socket = r0     // Catch:{ all -> 0x0038 }
            r0 = r22
            com.firebase.tubesock.WebSocket$State r0 = r0.state     // Catch:{ all -> 0x0038 }
            r18 = r0
            com.firebase.tubesock.WebSocket$State r19 = com.firebase.tubesock.WebSocket.State.DISCONNECTED     // Catch:{ all -> 0x0038 }
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x004d
            r0 = r22
            java.net.Socket r0 = r0.socket     // Catch:{ IOException -> 0x002f }
            r18 = r0
            r18.close()     // Catch:{ IOException -> 0x002f }
            r18 = 0
            r0 = r18
            r1 = r22
            r1.socket = r0     // Catch:{ all -> 0x0038 }
            monitor-exit(r22)     // Catch:{ all -> 0x0038 }
            r22.close()
        L_0x002e:
            return
        L_0x002f:
            r4 = move-exception
            java.lang.RuntimeException r18 = new java.lang.RuntimeException     // Catch:{ all -> 0x0038 }
            r0 = r18
            r0.<init>(r4)     // Catch:{ all -> 0x0038 }
            throw r18     // Catch:{ all -> 0x0038 }
        L_0x0038:
            r18 = move-exception
            monitor-exit(r22)     // Catch:{ all -> 0x0038 }
            throw r18     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
        L_0x003b:
            r17 = move-exception
            r0 = r22
            com.firebase.tubesock.WebSocketEventHandler r0 = r0.eventHandler     // Catch:{ all -> 0x00fd }
            r18 = r0
            r0 = r18
            r1 = r17
            r0.onError(r1)     // Catch:{ all -> 0x00fd }
            r22.close()
            goto L_0x002e
        L_0x004d:
            monitor-exit(r22)     // Catch:{ all -> 0x0038 }
            java.io.DataInputStream r9 = new java.io.DataInputStream     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.io.InputStream r18 = r16.getInputStream()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r18
            r9.<init>(r0)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.io.OutputStream r14 = r16.getOutputStream()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r22
            com.firebase.tubesock.WebSocketHandshake r0 = r0.handshake     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = r0
            byte[] r18 = r18.getHandshake()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r18
            r14.write(r0)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r5 = 0
            r12 = 1000(0x3e8, float:1.401E-42)
            byte[] r3 = new byte[r12]     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r15 = 0
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r6.<init>()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
        L_0x0077:
            if (r5 != 0) goto L_0x012c
            int r2 = r9.read()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = -1
            r0 = r18
            if (r2 != r0) goto L_0x00ba
            com.firebase.tubesock.WebSocketException r18 = new com.firebase.tubesock.WebSocketException     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r19 = "Connection closed before handshake was complete"
            r18.<init>(r19)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            throw r18     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
        L_0x008b:
            r10 = move-exception
            r0 = r22
            com.firebase.tubesock.WebSocketEventHandler r0 = r0.eventHandler     // Catch:{ all -> 0x00fd }
            r18 = r0
            com.firebase.tubesock.WebSocketException r19 = new com.firebase.tubesock.WebSocketException     // Catch:{ all -> 0x00fd }
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ all -> 0x00fd }
            r20.<init>()     // Catch:{ all -> 0x00fd }
            java.lang.String r21 = "error while connecting: "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ all -> 0x00fd }
            java.lang.String r21 = r10.getMessage()     // Catch:{ all -> 0x00fd }
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ all -> 0x00fd }
            java.lang.String r20 = r20.toString()     // Catch:{ all -> 0x00fd }
            r0 = r19
            r1 = r20
            r0.<init>(r1, r10)     // Catch:{ all -> 0x00fd }
            r18.onError(r19)     // Catch:{ all -> 0x00fd }
            r22.close()
            goto L_0x002e
        L_0x00ba:
            byte r0 = (byte) r2
            r18 = r0
            r3[r15] = r18     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            int r15 = r15 + 1
            int r18 = r15 + -1
            byte r18 = r3[r18]     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r19 = 10
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0102
            int r18 = r15 + -2
            byte r18 = r3[r18]     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r19 = 13
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0102
            java.lang.String r13 = new java.lang.String     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.nio.charset.Charset r18 = UTF8     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r18
            r13.<init>(r3, r0)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r18 = r13.trim()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r19 = ""
            boolean r18 = r18.equals(r19)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            if (r18 == 0) goto L_0x00f3
            r5 = 1
        L_0x00ef:
            byte[] r3 = new byte[r12]     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r15 = 0
            goto L_0x0077
        L_0x00f3:
            java.lang.String r18 = r13.trim()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r18
            r6.add(r0)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            goto L_0x00ef
        L_0x00fd:
            r18 = move-exception
            r22.close()
            throw r18
        L_0x0102:
            r18 = 1000(0x3e8, float:1.401E-42)
            r0 = r18
            if (r15 != r0) goto L_0x0077
            java.lang.String r13 = new java.lang.String     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.nio.charset.Charset r18 = UTF8     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r18
            r13.<init>(r3, r0)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            com.firebase.tubesock.WebSocketException r18 = new com.firebase.tubesock.WebSocketException     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r19.<init>()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r20 = "Unexpected long line in handshake: "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r19
            java.lang.StringBuilder r19 = r0.append(r13)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r19 = r19.toString()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18.<init>(r19)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            throw r18     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
        L_0x012c:
            r0 = r22
            com.firebase.tubesock.WebSocketHandshake r0 = r0.handshake     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r19 = r0
            r18 = 0
            r0 = r18
            java.lang.Object r18 = r6.get(r0)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r18 = (java.lang.String) r18     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r19
            r1 = r18
            r0.verifyServerStatusLine(r1)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = 0
            r0 = r18
            r6.remove(r0)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.util.HashMap r7 = new java.util.HashMap     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r7.<init>()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.util.Iterator r8 = r6.iterator()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
        L_0x0153:
            boolean r18 = r8.hasNext()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            if (r18 == 0) goto L_0x017b
            java.lang.Object r13 = r8.next()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            java.lang.String r18 = ": "
            r19 = 2
            r0 = r18
            r1 = r19
            java.lang.String[] r11 = r13.split(r0, r1)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = 0
            r18 = r11[r18]     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r19 = 1
            r19 = r11[r19]     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r18
            r1 = r19
            r7.put(r0, r1)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            goto L_0x0153
        L_0x017b:
            r0 = r22
            com.firebase.tubesock.WebSocketHandshake r0 = r0.handshake     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = r0
            r0 = r18
            r0.verifyServerHandshakeHeaders(r7)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r22
            com.firebase.tubesock.WebSocketWriter r0 = r0.writer     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = r0
            r0 = r18
            r0.setOutput(r14)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r22
            com.firebase.tubesock.WebSocketReceiver r0 = r0.receiver     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = r0
            r0 = r18
            r0.setInput(r9)     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            com.firebase.tubesock.WebSocket$State r18 = com.firebase.tubesock.WebSocket.State.CONNECTED     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r18
            r1 = r22
            r1.state = r0     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r22
            com.firebase.tubesock.WebSocketWriter r0 = r0.writer     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = r0
            java.lang.Thread r18 = r18.getInnerThread()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18.start()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r22
            com.firebase.tubesock.WebSocketEventHandler r0 = r0.eventHandler     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = r0
            r18.onOpen()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r0 = r22
            com.firebase.tubesock.WebSocketReceiver r0 = r0.receiver     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r18 = r0
            r18.run()     // Catch:{ WebSocketException -> 0x003b, IOException -> 0x008b }
            r22.close()
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.firebase.tubesock.WebSocket.runReader():void");
    }

    /* access modifiers changed from: package-private */
    public Thread getInnerThread() {
        return this.innerThread;
    }
}
