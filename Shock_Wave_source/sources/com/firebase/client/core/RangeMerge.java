package com.firebase.client.core;

import com.firebase.client.snapshot.Node;

public class RangeMerge {
    static final /* synthetic */ boolean $assertionsDisabled = (!RangeMerge.class.desiredAssertionStatus());
    private final Path optExclusiveStart;
    private final Path optInclusiveEnd;
    private final Node snap;

    public RangeMerge(Path optExclusiveStart2, Path optInclusiveEnd2, Node snap2) {
        this.optExclusiveStart = optExclusiveStart2;
        this.optInclusiveEnd = optInclusiveEnd2;
        this.snap = snap2;
    }

    public Node applyTo(Node node) {
        return updateRangeInNode(Path.getEmptyPath(), node, this.snap);
    }

    /* access modifiers changed from: package-private */
    public Path getStart() {
        return this.optExclusiveStart;
    }

    /* access modifiers changed from: package-private */
    public Path getEnd() {
        return this.optInclusiveEnd;
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0057 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0061 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x008f A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a0 A[LOOP:0: B:47:0x009a->B:49:0x00a0, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00b8 A[LOOP:1: B:51:0x00b2->B:53:0x00b8, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00fb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.firebase.client.snapshot.Node updateRangeInNode(com.firebase.client.core.Path r17, com.firebase.client.snapshot.Node r18, com.firebase.client.snapshot.Node r19) {
        /*
            r16 = this;
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optExclusiveStart
            if (r13 != 0) goto L_0x003b
            r10 = 1
        L_0x0007:
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optInclusiveEnd
            if (r13 != 0) goto L_0x0046
            r4 = -1
        L_0x000e:
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optExclusiveStart
            if (r13 == 0) goto L_0x0051
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optExclusiveStart
            r0 = r17
            boolean r13 = r0.contains(r13)
            if (r13 == 0) goto L_0x0051
            r11 = 1
        L_0x0021:
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optInclusiveEnd
            if (r13 == 0) goto L_0x0053
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optInclusiveEnd
            r0 = r17
            boolean r13 = r0.contains(r13)
            if (r13 == 0) goto L_0x0053
            r5 = 1
        L_0x0034:
            if (r10 <= 0) goto L_0x0055
            if (r4 >= 0) goto L_0x0055
            if (r5 != 0) goto L_0x0055
        L_0x003a:
            return r19
        L_0x003b:
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optExclusiveStart
            r0 = r17
            int r10 = r0.compareTo((com.firebase.client.core.Path) r13)
            goto L_0x0007
        L_0x0046:
            r0 = r16
            com.firebase.client.core.Path r13 = r0.optInclusiveEnd
            r0 = r17
            int r4 = r0.compareTo((com.firebase.client.core.Path) r13)
            goto L_0x000e
        L_0x0051:
            r11 = 0
            goto L_0x0021
        L_0x0053:
            r5 = 0
            goto L_0x0034
        L_0x0055:
            if (r10 <= 0) goto L_0x005f
            if (r5 == 0) goto L_0x005f
            boolean r13 = r19.isLeafNode()
            if (r13 != 0) goto L_0x003a
        L_0x005f:
            if (r10 <= 0) goto L_0x008d
            if (r4 != 0) goto L_0x008d
            boolean r13 = $assertionsDisabled
            if (r13 != 0) goto L_0x006f
            if (r5 != 0) goto L_0x006f
            java.lang.AssertionError r13 = new java.lang.AssertionError
            r13.<init>()
            throw r13
        L_0x006f:
            boolean r13 = $assertionsDisabled
            if (r13 != 0) goto L_0x007f
            boolean r13 = r19.isLeafNode()
            if (r13 == 0) goto L_0x007f
            java.lang.AssertionError r13 = new java.lang.AssertionError
            r13.<init>()
            throw r13
        L_0x007f:
            boolean r13 = r18.isLeafNode()
            if (r13 == 0) goto L_0x008a
            com.firebase.client.snapshot.EmptyNode r19 = com.firebase.client.snapshot.EmptyNode.Empty()
            goto L_0x003a
        L_0x008a:
            r19 = r18
            goto L_0x003a
        L_0x008d:
            if (r11 != 0) goto L_0x0091
            if (r5 == 0) goto L_0x012a
        L_0x0091:
            java.util.HashSet r1 = new java.util.HashSet
            r1.<init>()
            java.util.Iterator r6 = r18.iterator()
        L_0x009a:
            boolean r13 = r6.hasNext()
            if (r13 == 0) goto L_0x00ae
            java.lang.Object r2 = r6.next()
            com.firebase.client.snapshot.NamedNode r2 = (com.firebase.client.snapshot.NamedNode) r2
            com.firebase.client.snapshot.ChildKey r13 = r2.getName()
            r1.add(r13)
            goto L_0x009a
        L_0x00ae:
            java.util.Iterator r6 = r19.iterator()
        L_0x00b2:
            boolean r13 = r6.hasNext()
            if (r13 == 0) goto L_0x00c6
            java.lang.Object r2 = r6.next()
            com.firebase.client.snapshot.NamedNode r2 = (com.firebase.client.snapshot.NamedNode) r2
            com.firebase.client.snapshot.ChildKey r13 = r2.getName()
            r1.add(r13)
            goto L_0x00b2
        L_0x00c6:
            java.util.ArrayList r7 = new java.util.ArrayList
            int r13 = r1.size()
            int r13 = r13 + 1
            r7.<init>(r13)
            r7.addAll(r1)
            com.firebase.client.snapshot.Node r13 = r19.getPriority()
            boolean r13 = r13.isEmpty()
            if (r13 == 0) goto L_0x00e8
            com.firebase.client.snapshot.Node r13 = r18.getPriority()
            boolean r13 = r13.isEmpty()
            if (r13 != 0) goto L_0x00ef
        L_0x00e8:
            com.firebase.client.snapshot.ChildKey r13 = com.firebase.client.snapshot.ChildKey.getPriorityKey()
            r7.add(r13)
        L_0x00ef:
            r9 = r18
            java.util.Iterator r6 = r7.iterator()
        L_0x00f5:
            boolean r13 = r6.hasNext()
            if (r13 == 0) goto L_0x0126
            java.lang.Object r8 = r6.next()
            com.firebase.client.snapshot.ChildKey r8 = (com.firebase.client.snapshot.ChildKey) r8
            r0 = r18
            com.firebase.client.snapshot.Node r3 = r0.getImmediateChild(r8)
            r0 = r17
            com.firebase.client.core.Path r13 = r0.child((com.firebase.client.snapshot.ChildKey) r8)
            r0 = r18
            com.firebase.client.snapshot.Node r14 = r0.getImmediateChild(r8)
            r0 = r19
            com.firebase.client.snapshot.Node r15 = r0.getImmediateChild(r8)
            r0 = r16
            com.firebase.client.snapshot.Node r12 = r0.updateRangeInNode(r13, r14, r15)
            if (r12 == r3) goto L_0x00f5
            com.firebase.client.snapshot.Node r9 = r9.updateImmediateChild(r8, r12)
            goto L_0x00f5
        L_0x0126:
            r19 = r9
            goto L_0x003a
        L_0x012a:
            boolean r13 = $assertionsDisabled
            if (r13 != 0) goto L_0x0138
            if (r4 > 0) goto L_0x0138
            if (r10 <= 0) goto L_0x0138
            java.lang.AssertionError r13 = new java.lang.AssertionError
            r13.<init>()
            throw r13
        L_0x0138:
            r19 = r18
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.firebase.client.core.RangeMerge.updateRangeInNode(com.firebase.client.core.Path, com.firebase.client.snapshot.Node, com.firebase.client.snapshot.Node):com.firebase.client.snapshot.Node");
    }

    public String toString() {
        return "RangeMerge{optExclusiveStart=" + this.optExclusiveStart + ", optInclusiveEnd=" + this.optInclusiveEnd + ", snap=" + this.snap + '}';
    }
}
