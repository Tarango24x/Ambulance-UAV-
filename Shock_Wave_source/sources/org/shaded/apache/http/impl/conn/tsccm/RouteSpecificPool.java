package org.shaded.apache.http.impl.conn.tsccm;

import java.util.LinkedList;
import java.util.Queue;
import org.shaded.apache.commons.logging.Log;
import org.shaded.apache.commons.logging.LogFactory;
import org.shaded.apache.http.annotation.NotThreadSafe;
import org.shaded.apache.http.conn.routing.HttpRoute;

@NotThreadSafe
public class RouteSpecificPool {
    protected final LinkedList<BasicPoolEntry> freeEntries;
    private final Log log = LogFactory.getLog((Class) getClass());
    protected final int maxEntries;
    protected int numEntries;
    protected final HttpRoute route;
    protected final Queue<WaitingThread> waitingThreads;

    public RouteSpecificPool(HttpRoute route2, int maxEntries2) {
        this.route = route2;
        this.maxEntries = maxEntries2;
        this.freeEntries = new LinkedList<>();
        this.waitingThreads = new LinkedList();
        this.numEntries = 0;
    }

    public final HttpRoute getRoute() {
        return this.route;
    }

    public final int getMaxEntries() {
        return this.maxEntries;
    }

    public boolean isUnused() {
        return this.numEntries < 1 && this.waitingThreads.isEmpty();
    }

    public int getCapacity() {
        return this.maxEntries - this.numEntries;
    }

    public final int getEntryCount() {
        return this.numEntries;
    }

    /* JADX WARNING: Removed duplicated region for block: B:5:0x001a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry allocEntry(java.lang.Object r7) {
        /*
            r6 = this;
            java.util.LinkedList<org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry> r4 = r6.freeEntries
            boolean r4 = r4.isEmpty()
            if (r4 != 0) goto L_0x0034
            java.util.LinkedList<org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry> r4 = r6.freeEntries
            java.util.LinkedList<org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry> r5 = r6.freeEntries
            int r5 = r5.size()
            java.util.ListIterator r3 = r4.listIterator(r5)
        L_0x0014:
            boolean r4 = r3.hasPrevious()
            if (r4 == 0) goto L_0x0034
            java.lang.Object r1 = r3.previous()
            org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry r1 = (org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry) r1
            java.lang.Object r4 = r1.getState()
            if (r4 == 0) goto L_0x0030
            java.lang.Object r4 = r1.getState()
            boolean r4 = org.shaded.apache.http.util.LangUtils.equals((java.lang.Object) r7, (java.lang.Object) r4)
            if (r4 == 0) goto L_0x0014
        L_0x0030:
            r3.remove()
        L_0x0033:
            return r1
        L_0x0034:
            int r4 = r6.getCapacity()
            if (r4 != 0) goto L_0x005e
            java.util.LinkedList<org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry> r4 = r6.freeEntries
            boolean r4 = r4.isEmpty()
            if (r4 != 0) goto L_0x005e
            java.util.LinkedList<org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry> r4 = r6.freeEntries
            java.lang.Object r1 = r4.remove()
            org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry r1 = (org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry) r1
            r1.shutdownEntry()
            org.shaded.apache.http.conn.OperatedClientConnection r0 = r1.getConnection()
            r0.close()     // Catch:{ IOException -> 0x0055 }
            goto L_0x0033
        L_0x0055:
            r2 = move-exception
            org.shaded.apache.commons.logging.Log r4 = r6.log
            java.lang.String r5 = "I/O error closing connection"
            r4.debug(r5, r2)
            goto L_0x0033
        L_0x005e:
            r1 = 0
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled: org.shaded.apache.http.impl.conn.tsccm.RouteSpecificPool.allocEntry(java.lang.Object):org.shaded.apache.http.impl.conn.tsccm.BasicPoolEntry");
    }

    public void freeEntry(BasicPoolEntry entry) {
        if (this.numEntries < 1) {
            throw new IllegalStateException("No entry created for this pool. " + this.route);
        } else if (this.numEntries <= this.freeEntries.size()) {
            throw new IllegalStateException("No entry allocated from this pool. " + this.route);
        } else {
            this.freeEntries.add(entry);
        }
    }

    public void createdEntry(BasicPoolEntry entry) {
        if (!this.route.equals(entry.getPlannedRoute())) {
            throw new IllegalArgumentException("Entry not planned for this pool.\npool: " + this.route + "\nplan: " + entry.getPlannedRoute());
        }
        this.numEntries++;
    }

    public boolean deleteEntry(BasicPoolEntry entry) {
        boolean found = this.freeEntries.remove(entry);
        if (found) {
            this.numEntries--;
        }
        return found;
    }

    public void dropEntry() {
        if (this.numEntries < 1) {
            throw new IllegalStateException("There is no entry that could be dropped.");
        }
        this.numEntries--;
    }

    public void queueThread(WaitingThread wt) {
        if (wt == null) {
            throw new IllegalArgumentException("Waiting thread must not be null.");
        }
        this.waitingThreads.add(wt);
    }

    public boolean hasThread() {
        return !this.waitingThreads.isEmpty();
    }

    public WaitingThread nextThread() {
        return this.waitingThreads.peek();
    }

    public void removeThread(WaitingThread wt) {
        if (wt != null) {
            this.waitingThreads.remove(wt);
        }
    }
}
