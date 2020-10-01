package com.firebase.client.snapshot;

import com.firebase.client.collection.ImmutableSortedMap;
import com.firebase.client.collection.LLRBNode;
import com.firebase.client.core.Path;
import com.firebase.client.snapshot.Node;
import com.firebase.client.utilities.Utilities;
import com.shaded.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChildrenNode implements Node {
    static final /* synthetic */ boolean $assertionsDisabled = (!ChildrenNode.class.desiredAssertionStatus());
    public static Comparator<ChildKey> NAME_ONLY_COMPARATOR = new Comparator<ChildKey>() {
        public int compare(ChildKey o1, ChildKey o2) {
            return o1.compareTo(o2);
        }
    };
    private final ImmutableSortedMap<ChildKey, Node> children;
    private String lazyHash;
    private final Node priority;

    private static class NamedNodeIterator implements Iterator<NamedNode> {
        private final Iterator<Map.Entry<ChildKey, Node>> iterator;

        public NamedNodeIterator(Iterator<Map.Entry<ChildKey, Node>> iterator2) {
            this.iterator = iterator2;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public NamedNode next() {
            Map.Entry<ChildKey, Node> entry = this.iterator.next();
            return new NamedNode(entry.getKey(), entry.getValue());
        }

        public void remove() {
            this.iterator.remove();
        }
    }

    public static abstract class ChildVisitor extends LLRBNode.NodeVisitor<ChildKey, Node> {
        public abstract void visitChild(ChildKey childKey, Node node);

        public void visitEntry(ChildKey key, Node value) {
            visitChild(key, value);
        }
    }

    protected ChildrenNode() {
        this.lazyHash = null;
        this.children = ImmutableSortedMap.Builder.emptyMap(NAME_ONLY_COMPARATOR);
        this.priority = PriorityUtilities.NullPriority();
    }

    protected ChildrenNode(ImmutableSortedMap<ChildKey, Node> children2, Node priority2) {
        this.lazyHash = null;
        if (!children2.isEmpty() || priority2.isEmpty()) {
            this.priority = priority2;
            this.children = children2;
            return;
        }
        throw new IllegalArgumentException("Can't create empty ChildrenNode with priority!");
    }

    public boolean hasChild(ChildKey name) {
        return !getImmediateChild(name).isEmpty();
    }

    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    public int getChildCount() {
        return this.children.size();
    }

    public Object getValue() {
        return getValue(false);
    }

    public Object getValue(boolean useExportFormat) {
        if (isEmpty()) {
            return null;
        }
        int numKeys = 0;
        int maxKey = 0;
        boolean allIntegerKeys = true;
        Map<String, Object> result = new HashMap<>();
        Iterator i$ = this.children.iterator();
        while (i$.hasNext()) {
            Map.Entry<ChildKey, Node> entry = i$.next();
            String key = entry.getKey().asString();
            result.put(key, entry.getValue().getValue(useExportFormat));
            numKeys++;
            if (allIntegerKeys) {
                if (key.length() <= 1 || key.charAt(0) != '0') {
                    Integer keyAsInt = Utilities.tryParseInt(key);
                    if (keyAsInt == null || keyAsInt.intValue() < 0) {
                        allIntegerKeys = false;
                    } else if (keyAsInt.intValue() > maxKey) {
                        maxKey = keyAsInt.intValue();
                    }
                } else {
                    allIntegerKeys = false;
                }
            }
        }
        if (useExportFormat || !allIntegerKeys || maxKey >= numKeys * 2) {
            if (useExportFormat && !this.priority.isEmpty()) {
                result.put(".priority", this.priority.getValue());
            }
            return result;
        }
        List<Object> arrayResult = new ArrayList<>(maxKey + 1);
        for (int i = 0; i <= maxKey; i++) {
            arrayResult.add(result.get("" + i));
        }
        return arrayResult;
    }

    public ChildKey getPredecessorChildKey(ChildKey childKey) {
        return this.children.getPredecessorKey(childKey);
    }

    public ChildKey getSuccessorChildKey(ChildKey childKey) {
        return this.children.getSuccessorKey(childKey);
    }

    public String getHashRepresentation(Node.HashVersion version) {
        if (version != Node.HashVersion.V1) {
            throw new IllegalArgumentException("Hashes on children nodes only supported for V1");
        }
        StringBuilder toHash = new StringBuilder();
        if (!this.priority.isEmpty()) {
            toHash.append("priority:");
            toHash.append(this.priority.getHashRepresentation(Node.HashVersion.V1));
            toHash.append(":");
        }
        List<NamedNode> nodes = new ArrayList<>();
        boolean sawPriority = false;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            NamedNode node = i$.next();
            nodes.add(node);
            sawPriority = sawPriority || !node.getNode().getPriority().isEmpty();
        }
        if (sawPriority) {
            Collections.sort(nodes, PriorityIndex.getInstance());
        }
        for (NamedNode node2 : nodes) {
            String hashString = node2.getNode().getHash();
            if (!hashString.equals("")) {
                toHash.append(":");
                toHash.append(node2.getName().asString());
                toHash.append(":");
                toHash.append(hashString);
            }
        }
        return toHash.toString();
    }

    public String getHash() {
        if (this.lazyHash == null) {
            String hashString = getHashRepresentation(Node.HashVersion.V1);
            this.lazyHash = hashString.isEmpty() ? "" : Utilities.sha1HexDigest(hashString);
        }
        return this.lazyHash;
    }

    public boolean isLeafNode() {
        return false;
    }

    public Node getPriority() {
        return this.priority;
    }

    public Node updatePriority(Node priority2) {
        if (this.children.isEmpty()) {
            return EmptyNode.Empty();
        }
        return new ChildrenNode(this.children, priority2);
    }

    public Node getImmediateChild(ChildKey name) {
        if (name.isPriorityChildName() && !this.priority.isEmpty()) {
            return this.priority;
        }
        if (this.children.containsKey(name)) {
            return this.children.get(name);
        }
        return EmptyNode.Empty();
    }

    /* Debug info: failed to restart local var, previous not found, register: 3 */
    public Node getChild(Path path) {
        ChildKey front = path.getFront();
        return front == null ? this : getImmediateChild(front).getChild(path.popFront());
    }

    public void forEachChild(ChildVisitor visitor) {
        forEachChild(visitor, false);
    }

    public void forEachChild(final ChildVisitor visitor, boolean includePriority) {
        if (!includePriority || getPriority().isEmpty()) {
            this.children.inOrderTraversal(visitor);
        } else {
            this.children.inOrderTraversal(new LLRBNode.NodeVisitor<ChildKey, Node>() {
                boolean passedPriorityKey = false;

                public void visitEntry(ChildKey key, Node value) {
                    if (!this.passedPriorityKey && key.compareTo(ChildKey.getPriorityKey()) > 0) {
                        this.passedPriorityKey = true;
                        visitor.visitChild(ChildKey.getPriorityKey(), ChildrenNode.this.getPriority());
                    }
                    visitor.visitChild(key, value);
                }
            });
        }
    }

    public ChildKey getFirstChildKey() {
        return this.children.getMinKey();
    }

    public ChildKey getLastChildKey() {
        return this.children.getMaxKey();
    }

    public Node updateChild(Path path, Node newChildNode) {
        ChildKey front = path.getFront();
        if (front == null) {
            return newChildNode;
        }
        if (!front.isPriorityChildName()) {
            return updateImmediateChild(front, getImmediateChild(front).updateChild(path.popFront(), newChildNode));
        }
        if ($assertionsDisabled || PriorityUtilities.isValidPriority(newChildNode)) {
            return updatePriority(newChildNode);
        }
        throw new AssertionError();
    }

    public Iterator<NamedNode> iterator() {
        return new NamedNodeIterator(this.children.iterator());
    }

    public Iterator<NamedNode> reverseIterator() {
        return new NamedNodeIterator(this.children.reverseIterator());
    }

    public Node updateImmediateChild(ChildKey key, Node newChildNode) {
        if (key.isPriorityChildName()) {
            return updatePriority(newChildNode);
        }
        ImmutableSortedMap<ChildKey, Node> newChildren = this.children;
        if (newChildren.containsKey(key)) {
            newChildren = newChildren.remove(key);
        }
        if (!newChildNode.isEmpty()) {
            newChildren = newChildren.insert(key, newChildNode);
        }
        if (newChildren.isEmpty()) {
            return EmptyNode.Empty();
        }
        return new ChildrenNode(newChildren, this.priority);
    }

    public int compareTo(Node o) {
        if (isEmpty()) {
            if (o.isEmpty()) {
                return 0;
            }
            return -1;
        } else if (o.isLeafNode()) {
            return 1;
        } else {
            if (o.isEmpty()) {
                return 1;
            }
            if (o == Node.MAX_NODE) {
                return -1;
            }
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x004b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r10) {
        /*
            r9 = this;
            r7 = 1
            r6 = 0
            if (r10 != 0) goto L_0x0006
            r5 = r6
        L_0x0005:
            return r5
        L_0x0006:
            if (r10 != r9) goto L_0x000a
            r5 = r7
            goto L_0x0005
        L_0x000a:
            boolean r5 = r10 instanceof com.firebase.client.snapshot.ChildrenNode
            if (r5 != 0) goto L_0x0010
            r5 = r6
            goto L_0x0005
        L_0x0010:
            r0 = r10
            com.firebase.client.snapshot.ChildrenNode r0 = (com.firebase.client.snapshot.ChildrenNode) r0
            com.firebase.client.snapshot.Node r5 = r9.getPriority()
            com.firebase.client.snapshot.Node r8 = r0.getPriority()
            boolean r5 = r5.equals(r8)
            if (r5 != 0) goto L_0x0023
            r5 = r6
            goto L_0x0005
        L_0x0023:
            com.firebase.client.collection.ImmutableSortedMap<com.firebase.client.snapshot.ChildKey, com.firebase.client.snapshot.Node> r5 = r9.children
            int r5 = r5.size()
            com.firebase.client.collection.ImmutableSortedMap<com.firebase.client.snapshot.ChildKey, com.firebase.client.snapshot.Node> r8 = r0.children
            int r8 = r8.size()
            if (r5 == r8) goto L_0x0033
            r5 = r6
            goto L_0x0005
        L_0x0033:
            com.firebase.client.collection.ImmutableSortedMap<com.firebase.client.snapshot.ChildKey, com.firebase.client.snapshot.Node> r5 = r9.children
            java.util.Iterator r3 = r5.iterator()
            com.firebase.client.collection.ImmutableSortedMap<com.firebase.client.snapshot.ChildKey, com.firebase.client.snapshot.Node> r5 = r0.children
            java.util.Iterator r1 = r5.iterator()
        L_0x003f:
            boolean r5 = r3.hasNext()
            if (r5 == 0) goto L_0x0079
            boolean r5 = r1.hasNext()
            if (r5 == 0) goto L_0x0079
            java.lang.Object r4 = r3.next()
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4
            java.lang.Object r2 = r1.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r5 = r4.getKey()
            com.firebase.client.snapshot.ChildKey r5 = (com.firebase.client.snapshot.ChildKey) r5
            java.lang.Object r8 = r2.getKey()
            boolean r5 = r5.equals(r8)
            if (r5 == 0) goto L_0x0077
            java.lang.Object r5 = r4.getValue()
            com.firebase.client.snapshot.Node r5 = (com.firebase.client.snapshot.Node) r5
            java.lang.Object r8 = r2.getValue()
            boolean r5 = r5.equals(r8)
            if (r5 != 0) goto L_0x003f
        L_0x0077:
            r5 = r6
            goto L_0x0005
        L_0x0079:
            boolean r5 = r3.hasNext()
            if (r5 != 0) goto L_0x0085
            boolean r5 = r1.hasNext()
            if (r5 == 0) goto L_0x008d
        L_0x0085:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "Something went wrong internally."
            r5.<init>(r6)
            throw r5
        L_0x008d:
            r5 = r7
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: com.firebase.client.snapshot.ChildrenNode.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int hashCode = 0;
        Iterator i$ = iterator();
        while (i$.hasNext()) {
            NamedNode entry = i$.next();
            hashCode = (((hashCode * 31) + entry.getName().hashCode()) * 17) + entry.getNode().hashCode();
        }
        return hashCode;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString(builder, 0);
        return builder.toString();
    }

    private static void addIndentation(StringBuilder builder, int indentation) {
        for (int i = 0; i < indentation; i++) {
            builder.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
    }

    private void toString(StringBuilder builder, int indentation) {
        if (!this.children.isEmpty() || !this.priority.isEmpty()) {
            builder.append("{\n");
            Iterator i$ = this.children.iterator();
            while (i$.hasNext()) {
                Map.Entry<ChildKey, Node> childEntry = i$.next();
                addIndentation(builder, indentation + 2);
                builder.append(childEntry.getKey().asString());
                builder.append("=");
                if (childEntry.getValue() instanceof ChildrenNode) {
                    ((ChildrenNode) childEntry.getValue()).toString(builder, indentation + 2);
                } else {
                    builder.append(childEntry.getValue().toString());
                }
                builder.append("\n");
            }
            if (!this.priority.isEmpty()) {
                addIndentation(builder, indentation + 2);
                builder.append(".priority=");
                builder.append(this.priority.toString());
                builder.append("\n");
            }
            addIndentation(builder, indentation);
            builder.append("}");
            return;
        }
        builder.append("{ }");
    }
}
