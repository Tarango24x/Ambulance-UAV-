package com.firebase.client.core;

import com.firebase.client.annotations.NotNull;
import com.firebase.client.core.view.QuerySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ZombieEventManager implements EventRegistrationZombieListener {
    static final /* synthetic */ boolean $assertionsDisabled = (!ZombieEventManager.class.desiredAssertionStatus());
    private static ZombieEventManager defaultInstance = new ZombieEventManager();
    final HashMap<EventRegistration, List<EventRegistration>> globalEventRegistrations = new HashMap<>();

    private ZombieEventManager() {
    }

    @NotNull
    public static ZombieEventManager getInstance() {
        return defaultInstance;
    }

    public void recordEventRegistration(EventRegistration registration) {
        synchronized (this.globalEventRegistrations) {
            List<EventRegistration> registrationList = this.globalEventRegistrations.get(registration);
            if (registrationList == null) {
                registrationList = new ArrayList<>();
                this.globalEventRegistrations.put(registration, registrationList);
            }
            registrationList.add(registration);
            if (!registration.getQuerySpec().isDefault()) {
                EventRegistration defaultRegistration = registration.clone(QuerySpec.defaultQueryAtPath(registration.getQuerySpec().getPath()));
                List<EventRegistration> registrationList2 = this.globalEventRegistrations.get(defaultRegistration);
                if (registrationList2 == null) {
                    registrationList2 = new ArrayList<>();
                    this.globalEventRegistrations.put(defaultRegistration, registrationList2);
                }
                registrationList2.add(registration);
            }
            registration.setIsUserInitiated(true);
            registration.setOnZombied(this);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x004c, code lost:
        r0 = r7.clone(com.firebase.client.core.view.QuerySpec.defaultQueryAtPath(r7.getQuerySpec().getPath()));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void unRecordEventRegistration(com.firebase.client.core.EventRegistration r7) {
        /*
            r6 = this;
            java.util.HashMap<com.firebase.client.core.EventRegistration, java.util.List<com.firebase.client.core.EventRegistration>> r5 = r6.globalEventRegistrations
            monitor-enter(r5)
            r1 = 0
            java.util.HashMap<com.firebase.client.core.EventRegistration, java.util.List<com.firebase.client.core.EventRegistration>> r4 = r6.globalEventRegistrations     // Catch:{ all -> 0x003c }
            java.lang.Object r3 = r4.get(r7)     // Catch:{ all -> 0x003c }
            java.util.List r3 = (java.util.List) r3     // Catch:{ all -> 0x003c }
            if (r3 == 0) goto L_0x002a
            r2 = 0
        L_0x000f:
            int r4 = r3.size()     // Catch:{ all -> 0x003c }
            if (r2 >= r4) goto L_0x001f
            java.lang.Object r4 = r3.get(r2)     // Catch:{ all -> 0x003c }
            if (r4 != r7) goto L_0x003f
            r1 = 1
            r3.remove(r2)     // Catch:{ all -> 0x003c }
        L_0x001f:
            boolean r4 = r3.isEmpty()     // Catch:{ all -> 0x003c }
            if (r4 == 0) goto L_0x002a
            java.util.HashMap<com.firebase.client.core.EventRegistration, java.util.List<com.firebase.client.core.EventRegistration>> r4 = r6.globalEventRegistrations     // Catch:{ all -> 0x003c }
            r4.remove(r7)     // Catch:{ all -> 0x003c }
        L_0x002a:
            boolean r4 = $assertionsDisabled     // Catch:{ all -> 0x003c }
            if (r4 != 0) goto L_0x0042
            if (r1 != 0) goto L_0x0042
            boolean r4 = r7.isUserInitiated()     // Catch:{ all -> 0x003c }
            if (r4 == 0) goto L_0x0042
            java.lang.AssertionError r4 = new java.lang.AssertionError     // Catch:{ all -> 0x003c }
            r4.<init>()     // Catch:{ all -> 0x003c }
            throw r4     // Catch:{ all -> 0x003c }
        L_0x003c:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003c }
            throw r4
        L_0x003f:
            int r2 = r2 + 1
            goto L_0x000f
        L_0x0042:
            com.firebase.client.core.view.QuerySpec r4 = r7.getQuerySpec()     // Catch:{ all -> 0x003c }
            boolean r4 = r4.isDefault()     // Catch:{ all -> 0x003c }
            if (r4 != 0) goto L_0x0081
            com.firebase.client.core.view.QuerySpec r4 = r7.getQuerySpec()     // Catch:{ all -> 0x003c }
            com.firebase.client.core.Path r4 = r4.getPath()     // Catch:{ all -> 0x003c }
            com.firebase.client.core.view.QuerySpec r4 = com.firebase.client.core.view.QuerySpec.defaultQueryAtPath(r4)     // Catch:{ all -> 0x003c }
            com.firebase.client.core.EventRegistration r0 = r7.clone(r4)     // Catch:{ all -> 0x003c }
            java.util.HashMap<com.firebase.client.core.EventRegistration, java.util.List<com.firebase.client.core.EventRegistration>> r4 = r6.globalEventRegistrations     // Catch:{ all -> 0x003c }
            java.lang.Object r3 = r4.get(r0)     // Catch:{ all -> 0x003c }
            java.util.List r3 = (java.util.List) r3     // Catch:{ all -> 0x003c }
            if (r3 == 0) goto L_0x0081
            r2 = 0
        L_0x0067:
            int r4 = r3.size()     // Catch:{ all -> 0x003c }
            if (r2 >= r4) goto L_0x0076
            java.lang.Object r4 = r3.get(r2)     // Catch:{ all -> 0x003c }
            if (r4 != r7) goto L_0x0083
            r3.remove(r2)     // Catch:{ all -> 0x003c }
        L_0x0076:
            boolean r4 = r3.isEmpty()     // Catch:{ all -> 0x003c }
            if (r4 == 0) goto L_0x0081
            java.util.HashMap<com.firebase.client.core.EventRegistration, java.util.List<com.firebase.client.core.EventRegistration>> r4 = r6.globalEventRegistrations     // Catch:{ all -> 0x003c }
            r4.remove(r0)     // Catch:{ all -> 0x003c }
        L_0x0081:
            monitor-exit(r5)     // Catch:{ all -> 0x003c }
            return
        L_0x0083:
            int r2 = r2 + 1
            goto L_0x0067
        */
        throw new UnsupportedOperationException("Method not decompiled: com.firebase.client.core.ZombieEventManager.unRecordEventRegistration(com.firebase.client.core.EventRegistration):void");
    }

    public void zombifyForRemove(EventRegistration registration) {
        synchronized (this.globalEventRegistrations) {
            List<EventRegistration> registrationList = this.globalEventRegistrations.get(registration);
            if (registrationList != null && !registrationList.isEmpty()) {
                if (registration.getQuerySpec().isDefault()) {
                    HashSet<QuerySpec> zombiedQueries = new HashSet<>();
                    for (int i = registrationList.size() - 1; i >= 0; i--) {
                        EventRegistration currentRegistration = registrationList.get(i);
                        if (!zombiedQueries.contains(currentRegistration.getQuerySpec())) {
                            zombiedQueries.add(currentRegistration.getQuerySpec());
                            currentRegistration.zombify();
                        }
                    }
                } else {
                    registrationList.get(0).zombify();
                }
            }
        }
    }

    public void onZombied(EventRegistration zombiedInstance) {
        unRecordEventRegistration(zombiedInstance);
    }
}
