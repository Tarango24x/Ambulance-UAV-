package org.shaded.apache.http.impl.conn.tsccm;

import com.shaded.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.util.concurrent.TimeUnit;
import org.shaded.apache.commons.logging.Log;
import org.shaded.apache.commons.logging.LogFactory;
import org.shaded.apache.http.conn.ClientConnectionManager;
import org.shaded.apache.http.conn.ClientConnectionOperator;
import org.shaded.apache.http.conn.ClientConnectionRequest;
import org.shaded.apache.http.conn.ConnectionPoolTimeoutException;
import org.shaded.apache.http.conn.ManagedClientConnection;
import org.shaded.apache.http.conn.routing.HttpRoute;
import org.shaded.apache.http.conn.scheme.SchemeRegistry;
import org.shaded.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.shaded.apache.http.params.HttpParams;

public class ThreadSafeClientConnManager implements ClientConnectionManager {
    protected final ClientConnectionOperator connOperator;
    protected final AbstractConnPool connectionPool;
    /* access modifiers changed from: private */
    public final Log log = LogFactory.getLog((Class) getClass());
    protected final SchemeRegistry schemeRegistry;

    public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else if (schreg == null) {
            throw new IllegalArgumentException("Scheme registry may not be null");
        } else {
            this.schemeRegistry = schreg;
            this.connOperator = createConnectionOperator(schreg);
            this.connectionPool = createConnectionPool(params);
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    /* access modifiers changed from: protected */
    public AbstractConnPool createConnectionPool(HttpParams params) {
        return new ConnPoolByRoute(this.connOperator, params);
    }

    /* access modifiers changed from: protected */
    public ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    public ClientConnectionRequest requestConnection(final HttpRoute route, Object state) {
        final PoolEntryRequest poolRequest = this.connectionPool.requestPoolEntry(route, state);
        return new ClientConnectionRequest() {
            public void abortRequest() {
                poolRequest.abortRequest();
            }

            public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
                if (route == null) {
                    throw new IllegalArgumentException("Route may not be null.");
                }
                if (ThreadSafeClientConnManager.this.log.isDebugEnabled()) {
                    ThreadSafeClientConnManager.this.log.debug("ThreadSafeClientConnManager.getConnection: " + route + ", timeout = " + timeout);
                }
                return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, poolRequest.getPoolEntry(timeout, tunit));
            }
        };
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:39:0x0079=Splitter:B:39:0x0079, B:21:0x003d=Splitter:B:21:0x003d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void releaseConnection(org.shaded.apache.http.conn.ManagedClientConnection r11, long r12, java.util.concurrent.TimeUnit r14) {
        /*
            r10 = this;
            boolean r1 = r11 instanceof org.shaded.apache.http.impl.conn.tsccm.BasicPooledConnAdapter
            if (r1 != 0) goto L_0x000c
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "Connection class mismatch, connection not obtained from this manager."
            r1.<init>(r4)
            throw r1
        L_0x000c:
            r0 = r11
            org.shaded.apache.http.impl.conn.tsccm.BasicPooledConnAdapter r0 = (org.shaded.apache.http.impl.conn.tsccm.BasicPooledConnAdapter) r0
            org.shaded.apache.http.impl.conn.AbstractPoolEntry r1 = r0.getPoolEntry()
            if (r1 == 0) goto L_0x0023
            org.shaded.apache.http.conn.ClientConnectionManager r1 = r0.getManager()
            if (r1 == r10) goto L_0x0023
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "Connection not obtained from this manager."
            r1.<init>(r4)
            throw r1
        L_0x0023:
            monitor-enter(r0)
            org.shaded.apache.http.impl.conn.AbstractPoolEntry r2 = r0.getPoolEntry()     // Catch:{ all -> 0x005e }
            org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry r2 = (org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry) r2     // Catch:{ all -> 0x005e }
            if (r2 != 0) goto L_0x002e
            monitor-exit(r0)     // Catch:{ all -> 0x005e }
        L_0x002d:
            return
        L_0x002e:
            boolean r1 = r0.isOpen()     // Catch:{ IOException -> 0x0069 }
            if (r1 == 0) goto L_0x003d
            boolean r1 = r0.isMarkedReusable()     // Catch:{ IOException -> 0x0069 }
            if (r1 != 0) goto L_0x003d
            r0.shutdown()     // Catch:{ IOException -> 0x0069 }
        L_0x003d:
            boolean r3 = r0.isMarkedReusable()     // Catch:{ all -> 0x005e }
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ all -> 0x005e }
            if (r1 == 0) goto L_0x0052
            if (r3 == 0) goto L_0x0061
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            java.lang.String r4 = "Released connection is reusable."
            r1.debug(r4)     // Catch:{ all -> 0x005e }
        L_0x0052:
            r0.detach()     // Catch:{ all -> 0x005e }
            org.shaded.apache.http.impl.conn.tsccm.AbstractConnPool r1 = r10.connectionPool     // Catch:{ all -> 0x005e }
            r4 = r12
            r6 = r14
            r1.freeEntry(r2, r3, r4, r6)     // Catch:{ all -> 0x005e }
        L_0x005c:
            monitor-exit(r0)     // Catch:{ all -> 0x005e }
            goto L_0x002d
        L_0x005e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x005e }
            throw r1
        L_0x0061:
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            java.lang.String r4 = "Released connection is not reusable."
            r1.debug(r4)     // Catch:{ all -> 0x005e }
            goto L_0x0052
        L_0x0069:
            r7 = move-exception
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x00a1 }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ all -> 0x00a1 }
            if (r1 == 0) goto L_0x0079
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x00a1 }
            java.lang.String r4 = "Exception shutting down released connection."
            r1.debug(r4, r7)     // Catch:{ all -> 0x00a1 }
        L_0x0079:
            boolean r3 = r0.isMarkedReusable()     // Catch:{ all -> 0x005e }
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ all -> 0x005e }
            if (r1 == 0) goto L_0x008e
            if (r3 == 0) goto L_0x0099
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            java.lang.String r4 = "Released connection is reusable."
            r1.debug(r4)     // Catch:{ all -> 0x005e }
        L_0x008e:
            r0.detach()     // Catch:{ all -> 0x005e }
            org.shaded.apache.http.impl.conn.tsccm.AbstractConnPool r1 = r10.connectionPool     // Catch:{ all -> 0x005e }
            r4 = r12
            r6 = r14
            r1.freeEntry(r2, r3, r4, r6)     // Catch:{ all -> 0x005e }
            goto L_0x005c
        L_0x0099:
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            java.lang.String r4 = "Released connection is not reusable."
            r1.debug(r4)     // Catch:{ all -> 0x005e }
            goto L_0x008e
        L_0x00a1:
            r1 = move-exception
            r8 = r1
            boolean r3 = r0.isMarkedReusable()     // Catch:{ all -> 0x005e }
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ all -> 0x005e }
            if (r1 == 0) goto L_0x00b8
            if (r3 == 0) goto L_0x00c3
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            java.lang.String r4 = "Released connection is reusable."
            r1.debug(r4)     // Catch:{ all -> 0x005e }
        L_0x00b8:
            r0.detach()     // Catch:{ all -> 0x005e }
            org.shaded.apache.http.impl.conn.tsccm.AbstractConnPool r1 = r10.connectionPool     // Catch:{ all -> 0x005e }
            r4 = r12
            r6 = r14
            r1.freeEntry(r2, r3, r4, r6)     // Catch:{ all -> 0x005e }
            throw r8     // Catch:{ all -> 0x005e }
        L_0x00c3:
            org.shaded.apache.commons.logging.Log r1 = r10.log     // Catch:{ all -> 0x005e }
            java.lang.String r4 = "Released connection is not reusable."
            r1.debug(r4)     // Catch:{ all -> 0x005e }
            goto L_0x00b8
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager.releaseConnection(org.shaded.apache.http.conn.ManagedClientConnection, long, java.util.concurrent.TimeUnit):void");
    }

    public void shutdown() {
        this.log.debug("Shutting down");
        this.connectionPool.shutdown();
    }

    public int getConnectionsInPool(HttpRoute route) {
        return ((ConnPoolByRoute) this.connectionPool).getConnectionsInPool(route);
    }

    public int getConnectionsInPool() {
        this.connectionPool.poolLock.lock();
        int count = this.connectionPool.numConnections;
        this.connectionPool.poolLock.unlock();
        return count;
    }

    public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle for " + idleTimeout + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + tunit);
        }
        this.connectionPool.closeIdleConnections(idleTimeout, tunit);
        this.connectionPool.deleteClosedConnections();
    }

    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.connectionPool.closeExpiredConnections();
        this.connectionPool.deleteClosedConnections();
    }
}
