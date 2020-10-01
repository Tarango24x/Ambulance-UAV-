package org.shaded.apache.http.impl.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;
import org.shaded.apache.commons.logging.Log;
import org.shaded.apache.commons.logging.LogFactory;
import org.shaded.apache.http.ConnectionReuseStrategy;
import org.shaded.apache.http.Header;
import org.shaded.apache.http.HttpEntity;
import org.shaded.apache.http.HttpEntityEnclosingRequest;
import org.shaded.apache.http.HttpException;
import org.shaded.apache.http.HttpHost;
import org.shaded.apache.http.HttpRequest;
import org.shaded.apache.http.HttpResponse;
import org.shaded.apache.http.ProtocolException;
import org.shaded.apache.http.annotation.NotThreadSafe;
import org.shaded.apache.http.auth.AuthScheme;
import org.shaded.apache.http.auth.AuthScope;
import org.shaded.apache.http.auth.AuthState;
import org.shaded.apache.http.auth.AuthenticationException;
import org.shaded.apache.http.auth.Credentials;
import org.shaded.apache.http.auth.MalformedChallengeException;
import org.shaded.apache.http.client.AuthenticationHandler;
import org.shaded.apache.http.client.CredentialsProvider;
import org.shaded.apache.http.client.HttpRequestRetryHandler;
import org.shaded.apache.http.client.RedirectException;
import org.shaded.apache.http.client.RedirectHandler;
import org.shaded.apache.http.client.RequestDirector;
import org.shaded.apache.http.client.UserTokenHandler;
import org.shaded.apache.http.client.params.ClientPNames;
import org.shaded.apache.http.client.params.HttpClientParams;
import org.shaded.apache.http.client.protocol.ClientContext;
import org.shaded.apache.http.client.utils.URIUtils;
import org.shaded.apache.http.conn.ClientConnectionManager;
import org.shaded.apache.http.conn.ConnectionKeepAliveStrategy;
import org.shaded.apache.http.conn.ManagedClientConnection;
import org.shaded.apache.http.conn.routing.BasicRouteDirector;
import org.shaded.apache.http.conn.routing.HttpRoute;
import org.shaded.apache.http.conn.routing.HttpRouteDirector;
import org.shaded.apache.http.conn.routing.HttpRoutePlanner;
import org.shaded.apache.http.entity.BufferedHttpEntity;
import org.shaded.apache.http.message.BasicHttpRequest;
import org.shaded.apache.http.params.HttpParams;
import org.shaded.apache.http.params.HttpProtocolParams;
import org.shaded.apache.http.protocol.ExecutionContext;
import org.shaded.apache.http.protocol.HttpContext;
import org.shaded.apache.http.protocol.HttpProcessor;
import org.shaded.apache.http.protocol.HttpRequestExecutor;

@NotThreadSafe
public class DefaultRequestDirector implements RequestDirector {
    protected final ClientConnectionManager connManager;
    protected final HttpProcessor httpProcessor;
    protected final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log;
    protected ManagedClientConnection managedConn;
    private int maxRedirects;
    protected final HttpParams params;
    protected final AuthenticationHandler proxyAuthHandler;
    protected final AuthState proxyAuthState;
    private int redirectCount;
    protected final RedirectHandler redirectHandler;
    protected final HttpRequestExecutor requestExec;
    protected final HttpRequestRetryHandler retryHandler;
    protected final ConnectionReuseStrategy reuseStrategy;
    protected final HttpRoutePlanner routePlanner;
    protected final AuthenticationHandler targetAuthHandler;
    protected final AuthState targetAuthState;
    protected final UserTokenHandler userTokenHandler;
    private HttpHost virtualHost;

    DefaultRequestDirector(Log log2, HttpRequestExecutor requestExec2, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor2, HttpRequestRetryHandler retryHandler2, RedirectHandler redirectHandler2, AuthenticationHandler targetAuthHandler2, AuthenticationHandler proxyAuthHandler2, UserTokenHandler userTokenHandler2, HttpParams params2) {
        if (log2 == null) {
            throw new IllegalArgumentException("Log may not be null.");
        } else if (requestExec2 == null) {
            throw new IllegalArgumentException("Request executor may not be null.");
        } else if (conman == null) {
            throw new IllegalArgumentException("Client connection manager may not be null.");
        } else if (reustrat == null) {
            throw new IllegalArgumentException("Connection reuse strategy may not be null.");
        } else if (kastrat == null) {
            throw new IllegalArgumentException("Connection keep alive strategy may not be null.");
        } else if (rouplan == null) {
            throw new IllegalArgumentException("Route planner may not be null.");
        } else if (httpProcessor2 == null) {
            throw new IllegalArgumentException("HTTP protocol processor may not be null.");
        } else if (retryHandler2 == null) {
            throw new IllegalArgumentException("HTTP request retry handler may not be null.");
        } else if (redirectHandler2 == null) {
            throw new IllegalArgumentException("Redirect handler may not be null.");
        } else if (targetAuthHandler2 == null) {
            throw new IllegalArgumentException("Target authentication handler may not be null.");
        } else if (proxyAuthHandler2 == null) {
            throw new IllegalArgumentException("Proxy authentication handler may not be null.");
        } else if (userTokenHandler2 == null) {
            throw new IllegalArgumentException("User token handler may not be null.");
        } else if (params2 == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.log = log2;
            this.requestExec = requestExec2;
            this.connManager = conman;
            this.reuseStrategy = reustrat;
            this.keepAliveStrategy = kastrat;
            this.routePlanner = rouplan;
            this.httpProcessor = httpProcessor2;
            this.retryHandler = retryHandler2;
            this.redirectHandler = redirectHandler2;
            this.targetAuthHandler = targetAuthHandler2;
            this.proxyAuthHandler = proxyAuthHandler2;
            this.userTokenHandler = userTokenHandler2;
            this.params = params2;
            this.managedConn = null;
            this.redirectCount = 0;
            this.maxRedirects = this.params.getIntParameter(ClientPNames.MAX_REDIRECTS, 100);
            this.targetAuthState = new AuthState();
            this.proxyAuthState = new AuthState();
        }
    }

    public DefaultRequestDirector(HttpRequestExecutor requestExec2, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor2, HttpRequestRetryHandler retryHandler2, RedirectHandler redirectHandler2, AuthenticationHandler targetAuthHandler2, AuthenticationHandler proxyAuthHandler2, UserTokenHandler userTokenHandler2, HttpParams params2) {
        this(LogFactory.getLog(DefaultRequestDirector.class), requestExec2, conman, reustrat, kastrat, rouplan, httpProcessor2, retryHandler2, redirectHandler2, targetAuthHandler2, proxyAuthHandler2, userTokenHandler2, params2);
    }

    private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
        if (request instanceof HttpEntityEnclosingRequest) {
            return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) request);
        }
        return new RequestWrapper(request);
    }

    /* access modifiers changed from: protected */
    public void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
        try {
            URI uri = request.getURI();
            if (route.getProxyHost() == null || route.isTunnelled()) {
                if (uri.isAbsolute()) {
                    request.setURI(URIUtils.rewriteURI(uri, (HttpHost) null));
                }
            } else if (!uri.isAbsolute()) {
                request.setURI(URIUtils.rewriteURI(uri, route.getTargetHost()));
            }
        } catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x01d3, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x01d4, code lost:
        abortConnection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x01d7, code lost:
        throw r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x01fc, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x01fd, code lost:
        abortConnection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0200, code lost:
        throw r10;
     */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x01d3 A[ExcHandler: HttpException (r10v2 'ex' org.shaded.apache.http.HttpException A[CUSTOM_DECLARE]), Splitter:B:2:0x0050] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x01fc A[ExcHandler: RuntimeException (r10v0 'ex' java.lang.RuntimeException A[CUSTOM_DECLARE]), Splitter:B:2:0x0050] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.shaded.apache.http.HttpResponse execute(org.shaded.apache.http.HttpHost r33, org.shaded.apache.http.HttpRequest r34, org.shaded.apache.http.protocol.HttpContext r35) throws org.shaded.apache.http.HttpException, java.io.IOException {
        /*
            r32 = this;
            r15 = r34
            r0 = r32
            org.shaded.apache.http.impl.client.RequestWrapper r17 = r0.wrapRequest(r15)
            r0 = r32
            org.shaded.apache.http.params.HttpParams r0 = r0.params
            r29 = r0
            r0 = r17
            r1 = r29
            r0.setParams(r1)
            r0 = r32
            r1 = r33
            r2 = r17
            r3 = r35
            org.shaded.apache.http.conn.routing.HttpRoute r16 = r0.determineRoute(r1, r2, r3)
            org.shaded.apache.http.params.HttpParams r29 = r15.getParams()
            java.lang.String r30 = "http.virtual-host"
            java.lang.Object r29 = r29.getParameter(r30)
            org.shaded.apache.http.HttpHost r29 = (org.shaded.apache.http.HttpHost) r29
            r0 = r29
            r1 = r32
            r1.virtualHost = r0
            org.shaded.apache.http.impl.client.RoutedRequest r23 = new org.shaded.apache.http.impl.client.RoutedRequest
            r0 = r23
            r1 = r17
            r2 = r16
            r0.<init>(r1, r2)
            r0 = r32
            org.shaded.apache.http.params.HttpParams r0 = r0.params
            r29 = r0
            long r26 = org.shaded.apache.http.conn.params.ConnManagerParams.getTimeout(r29)
            r11 = 0
            r22 = 0
            r5 = 0
            r19 = 0
        L_0x004e:
            if (r5 != 0) goto L_0x021f
            org.shaded.apache.http.impl.client.RequestWrapper r28 = r23.getRequest()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            org.shaded.apache.http.conn.routing.HttpRoute r24 = r23.getRoute()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r19 = 0
            java.lang.String r29 = "http.user-token"
            r0 = r35
            r1 = r29
            java.lang.Object r25 = r0.getAttribute(r1)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            if (r29 != 0) goto L_0x00df
            r0 = r32
            org.shaded.apache.http.conn.ClientConnectionManager r0 = r0.connManager     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r1 = r24
            r2 = r25
            org.shaded.apache.http.conn.ClientConnectionRequest r4 = r0.requestConnection(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            boolean r0 = r15 instanceof org.shaded.apache.http.client.methods.AbortableHttpRequest     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            if (r29 == 0) goto L_0x008c
            r0 = r15
            org.shaded.apache.http.client.methods.AbortableHttpRequest r0 = (org.shaded.apache.http.client.methods.AbortableHttpRequest) r0     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r0.setConnectionRequest(r4)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x008c:
            java.util.concurrent.TimeUnit r29 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException -> 0x01d8 }
            r0 = r26
            r2 = r29
            org.shaded.apache.http.conn.ManagedClientConnection r29 = r4.getConnection(r0, r2)     // Catch:{ InterruptedException -> 0x01d8 }
            r0 = r29
            r1 = r32
            r1.managedConn = r0     // Catch:{ InterruptedException -> 0x01d8 }
            r0 = r32
            org.shaded.apache.http.params.HttpParams r0 = r0.params     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = org.shaded.apache.http.params.HttpConnectionParams.isStaleCheckingEnabled(r29)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x00df
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isOpen()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x00df
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Stale connection check"
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isStale()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x00df
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Stale connection detected"
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r29.close()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x00df:
            boolean r0 = r15 instanceof org.shaded.apache.http.client.methods.AbortableHttpRequest     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            if (r29 == 0) goto L_0x00f3
            r0 = r15
            org.shaded.apache.http.client.methods.AbortableHttpRequest r0 = (org.shaded.apache.http.client.methods.AbortableHttpRequest) r0     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r29.setReleaseTrigger(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x00f3:
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isOpen()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 != 0) goto L_0x01e7
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r32
            org.shaded.apache.http.params.HttpParams r0 = r0.params     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r29
            r1 = r24
            r2 = r35
            r3 = r30
            r0.open(r1, r2, r3)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x0116:
            r0 = r32
            r1 = r24
            r2 = r35
            r0.establishRoute(r1, r2)     // Catch:{ TunnelRefusedException -> 0x0201 }
            r28.resetHeaders()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            r1 = r28
            r2 = r24
            r0.rewriteRequestURI(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.HttpHost r0 = r0.virtualHost     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r33 = r0
            if (r33 != 0) goto L_0x0137
            org.shaded.apache.http.HttpHost r33 = r24.getTargetHost()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x0137:
            org.shaded.apache.http.HttpHost r18 = r24.getProxyHost()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r29 = "http.target_host"
            r0 = r35
            r1 = r29
            r2 = r33
            r0.setAttribute(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r29 = "http.proxy_host"
            r0 = r35
            r1 = r29
            r2 = r18
            r0.setAttribute(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r29 = "http.connection"
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r35
            r1 = r29
            r2 = r30
            r0.setAttribute(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r29 = "http.auth.target-scope"
            r0 = r32
            org.shaded.apache.http.auth.AuthState r0 = r0.targetAuthState     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r35
            r1 = r29
            r2 = r30
            r0.setAttribute(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r29 = "http.auth.proxy-scope"
            r0 = r32
            org.shaded.apache.http.auth.AuthState r0 = r0.proxyAuthState     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r35
            r1 = r29
            r2 = r30
            r0.setAttribute(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.protocol.HttpRequestExecutor r0 = r0.requestExec     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r32
            org.shaded.apache.http.protocol.HttpProcessor r0 = r0.httpProcessor     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r29
            r1 = r28
            r2 = r30
            r3 = r35
            r0.preProcess(r1, r2, r3)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r21 = 1
            r20 = 0
        L_0x019f:
            if (r21 == 0) goto L_0x035b
            int r11 = r11 + 1
            r28.incrementExecCount()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            int r29 = r28.getExecCount()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = 1
            r0 = r29
            r1 = r30
            if (r0 <= r1) goto L_0x0248
            boolean r29 = r28.isRepeatable()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 != 0) goto L_0x0248
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Cannot retry non-repeatable request"
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r20 == 0) goto L_0x0240
            org.shaded.apache.http.client.NonRepeatableRequestException r29 = new org.shaded.apache.http.client.NonRepeatableRequestException     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r30 = "Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed."
            r0 = r29
            r1 = r30
            r2 = r20
            r0.<init>(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            throw r29     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x01d3:
            r10 = move-exception
            r32.abortConnection()
            throw r10
        L_0x01d8:
            r13 = move-exception
            java.io.InterruptedIOException r14 = new java.io.InterruptedIOException     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r14.<init>()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r14.initCause(r13)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            throw r14     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x01e2:
            r10 = move-exception
            r32.abortConnection()
            throw r10
        L_0x01e7:
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r32
            org.shaded.apache.http.params.HttpParams r0 = r0.params     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            int r30 = org.shaded.apache.http.params.HttpConnectionParams.getSoTimeout(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29.setSocketTimeout(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            goto L_0x0116
        L_0x01fc:
            r10 = move-exception
            r32.abortConnection()
            throw r10
        L_0x0201:
            r10 = move-exception
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isDebugEnabled()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x021b
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = r10.getMessage()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x021b:
            org.shaded.apache.http.HttpResponse r19 = r10.getResponse()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x021f:
            if (r19 == 0) goto L_0x0231
            org.shaded.apache.http.HttpEntity r29 = r19.getEntity()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x0231
            org.shaded.apache.http.HttpEntity r29 = r19.getEntity()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            boolean r29 = r29.isStreaming()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 != 0) goto L_0x0469
        L_0x0231:
            if (r22 == 0) goto L_0x023c
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r29.markReusable()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x023c:
            r32.releaseConnection()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x023f:
            return r19
        L_0x0240:
            org.shaded.apache.http.client.NonRepeatableRequestException r29 = new org.shaded.apache.http.client.NonRepeatableRequestException     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r30 = "Cannot retry request with a non-repeatable request entity."
            r29.<init>(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            throw r29     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x0248:
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isDebugEnabled()     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x0278
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.StringBuilder r30 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r30.<init>()     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            java.lang.String r31 = "Attempt "
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r11)     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            java.lang.String r31 = " to execute request"
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            java.lang.String r30 = r30.toString()     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r29.debug(r30)     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
        L_0x0278:
            r0 = r32
            org.shaded.apache.http.protocol.HttpRequestExecutor r0 = r0.requestExec     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r29
            r1 = r28
            r2 = r30
            r3 = r35
            org.shaded.apache.http.HttpResponse r19 = r0.execute(r1, r2, r3)     // Catch:{ IOException -> 0x0294, HttpException -> 0x01d3, RuntimeException -> 0x01fc }
            r21 = 0
            goto L_0x019f
        L_0x0294:
            r10 = move-exception
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Closing the connection."
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r29.close()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.client.HttpRequestRetryHandler r0 = r0.retryHandler     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r1 = r35
            boolean r29 = r0.retryRequest(r10, r11, r1)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x034b
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isInfoEnabled()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x02f7
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.StringBuilder r30 = new java.lang.StringBuilder     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30.<init>()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r31 = "I/O exception ("
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.Class r31 = r10.getClass()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r31 = r31.getName()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r31 = ") caught when processing request: "
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r31 = r10.getMessage()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r30 = r30.toString()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29.info(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x02f7:
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isDebugEnabled()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x0314
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = r10.getMessage()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r29
            r1 = r30
            r0.debug(r1, r10)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x0314:
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Retrying request"
            r29.info(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r20 = r10
            boolean r29 = r24.isTunnelled()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 != 0) goto L_0x034c
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Reopening the direct connection."
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r32
            org.shaded.apache.http.params.HttpParams r0 = r0.params     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r29
            r1 = r24
            r2 = r35
            r3 = r30
            r0.open(r1, r2, r3)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            goto L_0x019f
        L_0x034b:
            throw r10     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x034c:
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Proxied connection. Need to start over."
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r21 = 0
            goto L_0x019f
        L_0x035b:
            if (r19 == 0) goto L_0x004e
            r0 = r32
            org.shaded.apache.http.params.HttpParams r0 = r0.params     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r19
            r1 = r29
            r0.setParams(r1)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.protocol.HttpRequestExecutor r0 = r0.requestExec     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r32
            org.shaded.apache.http.protocol.HttpProcessor r0 = r0.httpProcessor     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30 = r0
            r0 = r29
            r1 = r19
            r2 = r30
            r3 = r35
            r0.postProcess(r1, r2, r3)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.ConnectionReuseStrategy r0 = r0.reuseStrategy     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r1 = r19
            r2 = r35
            boolean r22 = r0.keepAlive(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r22 == 0) goto L_0x03e8
            r0 = r32
            org.shaded.apache.http.conn.ConnectionKeepAliveStrategy r0 = r0.keepAliveStrategy     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r1 = r19
            r2 = r35
            long r6 = r0.getKeepAliveDuration(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.util.concurrent.TimeUnit r30 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r29
            r1 = r30
            r0.setIdleDuration(r6, r1)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            boolean r29 = r29.isDebugEnabled()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 == 0) goto L_0x03e8
            r30 = 0
            int r29 = (r6 > r30 ? 1 : (r6 == r30 ? 0 : -1))
            if (r29 < 0) goto L_0x042b
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.StringBuilder r30 = new java.lang.StringBuilder     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r30.<init>()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r31 = "Connection can be kept alive for "
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r6)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r31 = " ms"
            java.lang.StringBuilder r30 = r30.append(r31)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r30 = r30.toString()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x03e8:
            r0 = r32
            r1 = r23
            r2 = r19
            r3 = r35
            org.shaded.apache.http.impl.client.RoutedRequest r12 = r0.handleResponse(r1, r2, r3)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r12 != 0) goto L_0x0437
            r5 = 1
        L_0x03f7:
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            if (r29 == 0) goto L_0x004e
            if (r25 != 0) goto L_0x004e
            r0 = r32
            org.shaded.apache.http.client.UserTokenHandler r0 = r0.userTokenHandler     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r1 = r35
            java.lang.Object r25 = r0.getUserToken(r1)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            java.lang.String r29 = "http.user-token"
            r0 = r35
            r1 = r29
            r2 = r25
            r0.setAttribute(r1, r2)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r25 == 0) goto L_0x004e
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r1 = r25
            r0.setState(r1)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            goto L_0x004e
        L_0x042b:
            r0 = r32
            org.shaded.apache.commons.logging.Log r0 = r0.log     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            java.lang.String r30 = "Connection can be kept alive indefinitely"
            r29.debug(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            goto L_0x03e8
        L_0x0437:
            if (r22 == 0) goto L_0x045f
            org.shaded.apache.http.HttpEntity r8 = r19.getEntity()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r8 == 0) goto L_0x0442
            r8.consumeContent()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x0442:
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r29.markReusable()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x044b:
            org.shaded.apache.http.conn.routing.HttpRoute r29 = r12.getRoute()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            org.shaded.apache.http.conn.routing.HttpRoute r30 = r23.getRoute()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            boolean r29 = r29.equals(r30)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            if (r29 != 0) goto L_0x045c
            r32.releaseConnection()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
        L_0x045c:
            r23 = r12
            goto L_0x03f7
        L_0x045f:
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r29.close()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            goto L_0x044b
        L_0x0469:
            org.shaded.apache.http.HttpEntity r8 = r19.getEntity()     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            org.shaded.apache.http.conn.BasicManagedEntity r9 = new org.shaded.apache.http.conn.BasicManagedEntity     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r32
            org.shaded.apache.http.conn.ManagedClientConnection r0 = r0.managedConn     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r29 = r0
            r0 = r29
            r1 = r22
            r9.<init>(r8, r0, r1)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            r0 = r19
            r0.setEntity(r9)     // Catch:{ HttpException -> 0x01d3, IOException -> 0x01e2, RuntimeException -> 0x01fc }
            goto L_0x023f
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.http.impl.client.DefaultRequestDirector.execute(org.shaded.apache.http.HttpHost, org.shaded.apache.http.HttpRequest, org.shaded.apache.http.protocol.HttpContext):org.shaded.apache.http.HttpResponse");
    }

    /* access modifiers changed from: protected */
    public void releaseConnection() {
        try {
            this.managedConn.releaseConnection();
        } catch (IOException ignored) {
            this.log.debug("IOException releasing connection", ignored);
        }
        this.managedConn = null;
    }

    /* access modifiers changed from: protected */
    public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        if (target == null) {
            target = (HttpHost) request.getParams().getParameter(ClientPNames.DEFAULT_HOST);
        }
        if (target != null) {
            return this.routePlanner.determineRoute(target, request, context);
        }
        throw new IllegalStateException("Target host must not be null, or set in parameters.");
    }

    /* access modifiers changed from: protected */
    public void establishRoute(HttpRoute route, HttpContext context) throws HttpException, IOException {
        int step;
        HttpRouteDirector rowdy = new BasicRouteDirector();
        do {
            HttpRoute fact = this.managedConn.getRoute();
            step = rowdy.nextStep(route, fact);
            switch (step) {
                case -1:
                    throw new IllegalStateException("Unable to establish route.\nplanned = " + route + "\ncurrent = " + fact);
                case 0:
                    break;
                case 1:
                case 2:
                    this.managedConn.open(route, context, this.params);
                    continue;
                case 3:
                    boolean secure = createTunnelToTarget(route, context);
                    this.log.debug("Tunnel to target created.");
                    this.managedConn.tunnelTarget(secure, this.params);
                    continue;
                case 4:
                    int hop = fact.getHopCount() - 1;
                    boolean secure2 = createTunnelToProxy(route, hop, context);
                    this.log.debug("Tunnel to proxy created.");
                    this.managedConn.tunnelProxy(route.getHopTarget(hop), secure2, this.params);
                    continue;
                case 5:
                    this.managedConn.layerProtocol(context, this.params);
                    continue;
                default:
                    throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
            }
        } while (step > 0);
    }

    /* access modifiers changed from: protected */
    public boolean createTunnelToTarget(HttpRoute route, HttpContext context) throws HttpException, IOException {
        HttpHost proxy = route.getProxyHost();
        HttpHost target = route.getTargetHost();
        HttpResponse response = null;
        boolean done = false;
        while (true) {
            if (done) {
                break;
            }
            done = true;
            if (!this.managedConn.isOpen()) {
                this.managedConn.open(route, context, this.params);
            }
            HttpRequest connect = createConnectRequest(route, context);
            connect.setParams(this.params);
            context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
            context.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxy);
            context.setAttribute(ExecutionContext.HTTP_CONNECTION, this.managedConn);
            context.setAttribute(ClientContext.TARGET_AUTH_STATE, this.targetAuthState);
            context.setAttribute(ClientContext.PROXY_AUTH_STATE, this.proxyAuthState);
            context.setAttribute(ExecutionContext.HTTP_REQUEST, connect);
            this.requestExec.preProcess(connect, this.httpProcessor, context);
            response = this.requestExec.execute(connect, this.managedConn, context);
            response.setParams(this.params);
            this.requestExec.postProcess(response, this.httpProcessor, context);
            if (response.getStatusLine().getStatusCode() < 200) {
                throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
            }
            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
            if (credsProvider != null && HttpClientParams.isAuthenticating(this.params)) {
                if (this.proxyAuthHandler.isAuthenticationRequested(response, context)) {
                    this.log.debug("Proxy requested authentication");
                    try {
                        processChallenges(this.proxyAuthHandler.getChallenges(response, context), this.proxyAuthState, this.proxyAuthHandler, response, context);
                    } catch (AuthenticationException ex) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Authentication error: " + ex.getMessage());
                            break;
                        }
                    }
                    updateAuthState(this.proxyAuthState, proxy, credsProvider);
                    if (this.proxyAuthState.getCredentials() != null) {
                        done = false;
                        if (this.reuseStrategy.keepAlive(response, context)) {
                            this.log.debug("Connection kept alive");
                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                entity.consumeContent();
                            }
                        } else {
                            this.managedConn.close();
                        }
                    }
                } else {
                    this.proxyAuthState.setAuthScope((AuthScope) null);
                }
            }
        }
        if (response.getStatusLine().getStatusCode() > 299) {
            HttpEntity entity2 = response.getEntity();
            if (entity2 != null) {
                response.setEntity(new BufferedHttpEntity(entity2));
            }
            this.managedConn.close();
            throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
        }
        this.managedConn.markReusable();
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context) throws HttpException, IOException {
        throw new UnsupportedOperationException("Proxy chains are not supported.");
    }

    /* access modifiers changed from: protected */
    public HttpRequest createConnectRequest(HttpRoute route, HttpContext context) {
        HttpHost target = route.getTargetHost();
        String host = target.getHostName();
        int port = target.getPort();
        if (port < 0) {
            port = this.connManager.getSchemeRegistry().getScheme(target.getSchemeName()).getDefaultPort();
        }
        StringBuilder buffer = new StringBuilder(host.length() + 6);
        buffer.append(host);
        buffer.append(':');
        buffer.append(Integer.toString(port));
        return new BasicHttpRequest("CONNECT", buffer.toString(), HttpProtocolParams.getVersion(this.params));
    }

    /* access modifiers changed from: protected */
    public RoutedRequest handleResponse(RoutedRequest roureq, HttpResponse response, HttpContext context) throws HttpException, IOException {
        HttpRoute route = roureq.getRoute();
        RequestWrapper request = roureq.getRequest();
        HttpParams params2 = request.getParams();
        if (!HttpClientParams.isRedirecting(params2) || !this.redirectHandler.isRedirectRequested(response, context)) {
            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
            if (credsProvider != null && HttpClientParams.isAuthenticating(params2)) {
                if (this.targetAuthHandler.isAuthenticationRequested(response, context)) {
                    HttpHost target = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                    if (target == null) {
                        target = route.getTargetHost();
                    }
                    this.log.debug("Target requested authentication");
                    try {
                        processChallenges(this.targetAuthHandler.getChallenges(response, context), this.targetAuthState, this.targetAuthHandler, response, context);
                    } catch (AuthenticationException ex) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Authentication error: " + ex.getMessage());
                            return null;
                        }
                    }
                    updateAuthState(this.targetAuthState, target, credsProvider);
                    if (this.targetAuthState.getCredentials() == null) {
                        return null;
                    }
                    return roureq;
                }
                this.targetAuthState.setAuthScope((AuthScope) null);
                if (this.proxyAuthHandler.isAuthenticationRequested(response, context)) {
                    HttpHost proxy = route.getProxyHost();
                    this.log.debug("Proxy requested authentication");
                    try {
                        processChallenges(this.proxyAuthHandler.getChallenges(response, context), this.proxyAuthState, this.proxyAuthHandler, response, context);
                    } catch (AuthenticationException ex2) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Authentication error: " + ex2.getMessage());
                            return null;
                        }
                    }
                    updateAuthState(this.proxyAuthState, proxy, credsProvider);
                    if (this.proxyAuthState.getCredentials() == null) {
                        return null;
                    }
                    return roureq;
                }
                this.proxyAuthState.setAuthScope((AuthScope) null);
            }
            return null;
        } else if (this.redirectCount >= this.maxRedirects) {
            throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
        } else {
            this.redirectCount++;
            this.virtualHost = null;
            URI uri = this.redirectHandler.getLocationURI(response, context);
            HttpHost newTarget = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
            this.targetAuthState.setAuthScope((AuthScope) null);
            this.proxyAuthState.setAuthScope((AuthScope) null);
            if (!route.getTargetHost().equals(newTarget)) {
                this.targetAuthState.invalidate();
                AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
                if (authScheme != null && authScheme.isConnectionBased()) {
                    this.proxyAuthState.invalidate();
                }
            }
            HttpRedirect httpRedirect = new HttpRedirect(request.getMethod(), uri);
            httpRedirect.setHeaders(request.getOriginal().getAllHeaders());
            RequestWrapper requestWrapper = new RequestWrapper(httpRedirect);
            requestWrapper.setParams(params2);
            HttpRoute newRoute = determineRoute(newTarget, requestWrapper, context);
            RoutedRequest newRequest = new RoutedRequest(requestWrapper, newRoute);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Redirecting to '" + uri + "' via " + newRoute);
            }
            return newRequest;
        }
    }

    private void abortConnection() {
        ManagedClientConnection mcc = this.managedConn;
        if (mcc != null) {
            this.managedConn = null;
            try {
                mcc.abortConnection();
            } catch (IOException ex) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(ex.getMessage(), ex);
                }
            }
            try {
                mcc.releaseConnection();
            } catch (IOException ignored) {
                this.log.debug("Error releasing connection", ignored);
            }
        }
    }

    private void processChallenges(Map<String, Header> challenges, AuthState authState, AuthenticationHandler authHandler, HttpResponse response, HttpContext context) throws MalformedChallengeException, AuthenticationException {
        AuthScheme authScheme = authState.getAuthScheme();
        if (authScheme == null) {
            authScheme = authHandler.selectScheme(challenges, response, context);
            authState.setAuthScheme(authScheme);
        }
        String id = authScheme.getSchemeName();
        Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));
        if (challenge == null) {
            throw new AuthenticationException(id + " authorization challenge expected, but not found");
        }
        authScheme.processChallenge(challenge);
        this.log.debug("Authorization challenge processed");
    }

    private void updateAuthState(AuthState authState, HttpHost host, CredentialsProvider credsProvider) {
        if (authState.isValid()) {
            String hostname = host.getHostName();
            int port = host.getPort();
            if (port < 0) {
                port = this.connManager.getSchemeRegistry().getScheme(host).getDefaultPort();
            }
            AuthScheme authScheme = authState.getAuthScheme();
            AuthScope authScope = new AuthScope(hostname, port, authScheme.getRealm(), authScheme.getSchemeName());
            if (this.log.isDebugEnabled()) {
                this.log.debug("Authentication scope: " + authScope);
            }
            Credentials creds = authState.getCredentials();
            if (creds == null) {
                creds = credsProvider.getCredentials(authScope);
                if (this.log.isDebugEnabled()) {
                    if (creds != null) {
                        this.log.debug("Found credentials");
                    } else {
                        this.log.debug("Credentials not found");
                    }
                }
            } else if (authScheme.isComplete()) {
                this.log.debug("Authentication failed");
                creds = null;
            }
            authState.setAuthScope(authScope);
            authState.setCredentials(creds);
        }
    }
}
