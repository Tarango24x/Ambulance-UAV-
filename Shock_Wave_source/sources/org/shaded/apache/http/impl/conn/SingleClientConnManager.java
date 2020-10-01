package org.shaded.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.shaded.apache.commons.logging.Log;
import org.shaded.apache.commons.logging.LogFactory;
import org.shaded.apache.http.annotation.GuardedBy;
import org.shaded.apache.http.annotation.ThreadSafe;
import org.shaded.apache.http.conn.ClientConnectionManager;
import org.shaded.apache.http.conn.ClientConnectionOperator;
import org.shaded.apache.http.conn.ClientConnectionRequest;
import org.shaded.apache.http.conn.ManagedClientConnection;
import org.shaded.apache.http.conn.routing.HttpRoute;
import org.shaded.apache.http.conn.routing.RouteTracker;
import org.shaded.apache.http.conn.scheme.SchemeRegistry;
import org.shaded.apache.http.params.HttpParams;

@ThreadSafe
public class SingleClientConnManager implements ClientConnectionManager {
    public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    protected final boolean alwaysShutDown;
    protected final ClientConnectionOperator connOperator;
    @GuardedBy("this")
    protected long connectionExpiresTime;
    protected volatile boolean isShutDown;
    @GuardedBy("this")
    protected long lastReleaseTime;
    private final Log log = LogFactory.getLog((Class) getClass());
    @GuardedBy("this")
    protected ConnAdapter managedConn;
    protected final SchemeRegistry schemeRegistry;
    @GuardedBy("this")
    protected PoolEntry uniquePoolEntry;

    public SingleClientConnManager(HttpParams params, SchemeRegistry schreg) {
        if (schreg == null) {
            throw new IllegalArgumentException("Scheme registry must not be null.");
        }
        this.schemeRegistry = schreg;
        this.connOperator = createConnectionOperator(schreg);
        this.uniquePoolEntry = new PoolEntry();
        this.managedConn = null;
        this.lastReleaseTime = -1;
        this.alwaysShutDown = false;
        this.isShutDown = false;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    /* access modifiers changed from: protected */
    public ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }

    /* access modifiers changed from: protected */
    public final void assertStillUp() throws IllegalStateException {
        if (this.isShutDown) {
            throw new IllegalStateException("Manager is shut down.");
        }
    }

    public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
        return new ClientConnectionRequest() {
            public void abortRequest() {
            }

            public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
                return SingleClientConnManager.this.getConnection(route, state);
            }
        };
    }

    public synchronized ManagedClientConnection getConnection(HttpRoute route, Object state) {
        if (route == null) {
            throw new IllegalArgumentException("Route may not be null.");
        }
        assertStillUp();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Get connection for route " + route);
        }
        if (this.managedConn != null) {
            throw new IllegalStateException(MISUSE_MESSAGE);
        }
        boolean recreate = false;
        boolean shutdown = false;
        closeExpiredConnections();
        if (this.uniquePoolEntry.connection.isOpen()) {
            RouteTracker tracker = this.uniquePoolEntry.tracker;
            if (tracker == null || !tracker.toRoute().equals(route)) {
                shutdown = true;
            } else {
                shutdown = false;
            }
        } else {
            recreate = true;
        }
        if (shutdown) {
            recreate = true;
            try {
                this.uniquePoolEntry.shutdown();
            } catch (IOException iox) {
                this.log.debug("Problem shutting down connection.", iox);
            }
        }
        if (recreate) {
            this.uniquePoolEntry = new PoolEntry();
        }
        this.managedConn = new ConnAdapter(this.uniquePoolEntry, route);
        return this.managedConn;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:35:0x0071=Splitter:B:35:0x0071, B:45:0x00a3=Splitter:B:45:0x00a3} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void releaseConnection(org.shaded.apache.http.conn.ManagedClientConnection r11, long r12, java.util.concurrent.TimeUnit r14) {
        /*
            r10 = this;
            r8 = 0
            monitor-enter(r10)
            r10.assertStillUp()     // Catch:{ all -> 0x0012 }
            boolean r5 = r11 instanceof org.shaded.apache.http.impl.conn.SingleClientConnManager.ConnAdapter     // Catch:{ all -> 0x0012 }
            if (r5 != 0) goto L_0x0015
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0012 }
            java.lang.String r6 = "Connection class mismatch, connection not obtained from this manager."
            r5.<init>(r6)     // Catch:{ all -> 0x0012 }
            throw r5     // Catch:{ all -> 0x0012 }
        L_0x0012:
            r5 = move-exception
            monitor-exit(r10)
            throw r5
        L_0x0015:
            org.shaded.apache.commons.logging.Log r5 = r10.log     // Catch:{ all -> 0x0012 }
            boolean r5 = r5.isDebugEnabled()     // Catch:{ all -> 0x0012 }
            if (r5 == 0) goto L_0x0035
            org.shaded.apache.commons.logging.Log r5 = r10.log     // Catch:{ all -> 0x0012 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0012 }
            r6.<init>()     // Catch:{ all -> 0x0012 }
            java.lang.String r7 = "Releasing connection "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0012 }
            java.lang.StringBuilder r6 = r6.append(r11)     // Catch:{ all -> 0x0012 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0012 }
            r5.debug(r6)     // Catch:{ all -> 0x0012 }
        L_0x0035:
            r0 = r11
            org.shaded.apache.http.impl.conn.SingleClientConnManager$ConnAdapter r0 = (org.shaded.apache.http.impl.conn.SingleClientConnManager.ConnAdapter) r0     // Catch:{ all -> 0x0012 }
            r4 = r0
            org.shaded.apache.http.impl.conn.AbstractPoolEntry r5 = r4.poolEntry     // Catch:{ all -> 0x0012 }
            if (r5 != 0) goto L_0x003f
        L_0x003d:
            monitor-exit(r10)
            return
        L_0x003f:
            org.shaded.apache.http.conn.ClientConnectionManager r3 = r4.getManager()     // Catch:{ all -> 0x0012 }
            if (r3 == 0) goto L_0x004f
            if (r3 == r10) goto L_0x004f
            java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException     // Catch:{ all -> 0x0012 }
            java.lang.String r6 = "Connection not obtained from this manager."
            r5.<init>(r6)     // Catch:{ all -> 0x0012 }
            throw r5     // Catch:{ all -> 0x0012 }
        L_0x004f:
            boolean r5 = r4.isOpen()     // Catch:{ IOException -> 0x0093 }
            if (r5 == 0) goto L_0x0071
            boolean r5 = r10.alwaysShutDown     // Catch:{ IOException -> 0x0093 }
            if (r5 != 0) goto L_0x005f
            boolean r5 = r4.isMarkedReusable()     // Catch:{ IOException -> 0x0093 }
            if (r5 != 0) goto L_0x0071
        L_0x005f:
            org.shaded.apache.commons.logging.Log r5 = r10.log     // Catch:{ IOException -> 0x0093 }
            boolean r5 = r5.isDebugEnabled()     // Catch:{ IOException -> 0x0093 }
            if (r5 == 0) goto L_0x006e
            org.shaded.apache.commons.logging.Log r5 = r10.log     // Catch:{ IOException -> 0x0093 }
            java.lang.String r6 = "Released connection open but not reusable."
            r5.debug(r6)     // Catch:{ IOException -> 0x0093 }
        L_0x006e:
            r4.shutdown()     // Catch:{ IOException -> 0x0093 }
        L_0x0071:
            r4.detach()     // Catch:{ all -> 0x0012 }
            r5 = 0
            r10.managedConn = r5     // Catch:{ all -> 0x0012 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0012 }
            r10.lastReleaseTime = r6     // Catch:{ all -> 0x0012 }
            int r5 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r5 <= 0) goto L_0x008b
            long r6 = r14.toMillis(r12)     // Catch:{ all -> 0x0012 }
            long r8 = r10.lastReleaseTime     // Catch:{ all -> 0x0012 }
            long r6 = r6 + r8
            r10.connectionExpiresTime = r6     // Catch:{ all -> 0x0012 }
            goto L_0x003d
        L_0x008b:
            r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r10.connectionExpiresTime = r6     // Catch:{ all -> 0x0012 }
            goto L_0x003d
        L_0x0093:
            r2 = move-exception
            org.shaded.apache.commons.logging.Log r5 = r10.log     // Catch:{ all -> 0x00c6 }
            boolean r5 = r5.isDebugEnabled()     // Catch:{ all -> 0x00c6 }
            if (r5 == 0) goto L_0x00a3
            org.shaded.apache.commons.logging.Log r5 = r10.log     // Catch:{ all -> 0x00c6 }
            java.lang.String r6 = "Exception shutting down released connection."
            r5.debug(r6, r2)     // Catch:{ all -> 0x00c6 }
        L_0x00a3:
            r4.detach()     // Catch:{ all -> 0x0012 }
            r5 = 0
            r10.managedConn = r5     // Catch:{ all -> 0x0012 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0012 }
            r10.lastReleaseTime = r6     // Catch:{ all -> 0x0012 }
            int r5 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r5 <= 0) goto L_0x00bd
            long r6 = r14.toMillis(r12)     // Catch:{ all -> 0x0012 }
            long r8 = r10.lastReleaseTime     // Catch:{ all -> 0x0012 }
            long r6 = r6 + r8
            r10.connectionExpiresTime = r6     // Catch:{ all -> 0x0012 }
            goto L_0x003d
        L_0x00bd:
            r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r10.connectionExpiresTime = r6     // Catch:{ all -> 0x0012 }
            goto L_0x003d
        L_0x00c6:
            r5 = move-exception
            r4.detach()     // Catch:{ all -> 0x0012 }
            r6 = 0
            r10.managedConn = r6     // Catch:{ all -> 0x0012 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0012 }
            r10.lastReleaseTime = r6     // Catch:{ all -> 0x0012 }
            int r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1))
            if (r6 <= 0) goto L_0x00e1
            long r6 = r14.toMillis(r12)     // Catch:{ all -> 0x0012 }
            long r8 = r10.lastReleaseTime     // Catch:{ all -> 0x0012 }
            long r6 = r6 + r8
            r10.connectionExpiresTime = r6     // Catch:{ all -> 0x0012 }
        L_0x00e0:
            throw r5     // Catch:{ all -> 0x0012 }
        L_0x00e1:
            r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r10.connectionExpiresTime = r6     // Catch:{ all -> 0x0012 }
            goto L_0x00e0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.http.impl.conn.SingleClientConnManager.releaseConnection(org.shaded.apache.http.conn.ManagedClientConnection, long, java.util.concurrent.TimeUnit):void");
    }

    public synchronized void closeExpiredConnections() {
        if (System.currentTimeMillis() >= this.connectionExpiresTime) {
            closeIdleConnections(0, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void closeIdleConnections(long idletime, TimeUnit tunit) {
        assertStillUp();
        if (tunit == null) {
            throw new IllegalArgumentException("Time unit must not be null.");
        } else if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
            if (this.lastReleaseTime <= System.currentTimeMillis() - tunit.toMillis(idletime)) {
                try {
                    this.uniquePoolEntry.close();
                } catch (IOException iox) {
                    this.log.debug("Problem closing idle connection.", iox);
                }
            }
        }
        return;
    }

    public synchronized void shutdown() {
        this.isShutDown = true;
        if (this.managedConn != null) {
            this.managedConn.detach();
        }
        try {
            if (this.uniquePoolEntry != null) {
                this.uniquePoolEntry.shutdown();
            }
            this.uniquePoolEntry = null;
        } catch (IOException iox) {
            this.log.debug("Problem while shutting down manager.", iox);
            this.uniquePoolEntry = null;
        } catch (Throwable th) {
            this.uniquePoolEntry = null;
            throw th;
        }
        return;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public synchronized void revokeConnection() {
        if (this.managedConn != null) {
            this.managedConn.detach();
            try {
                this.uniquePoolEntry.shutdown();
            } catch (IOException iox) {
                this.log.debug("Problem while shutting down connection.", iox);
            }
        }
        return;
    }

    protected class PoolEntry extends AbstractPoolEntry {
        protected PoolEntry() {
            super(SingleClientConnManager.this.connOperator, (HttpRoute) null);
        }

        /* access modifiers changed from: protected */
        public void close() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.close();
            }
        }

        /* access modifiers changed from: protected */
        public void shutdown() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.shutdown();
            }
        }
    }

    protected class ConnAdapter extends AbstractPooledConnAdapter {
        protected ConnAdapter(PoolEntry entry, HttpRoute route) {
            super(SingleClientConnManager.this, entry);
            markReusable();
            entry.route = route;
        }
    }
}
