package com.firebase.client.core.utilities;

import com.firebase.client.collection.ImmutableSortedMap;
import com.firebase.client.collection.StandardComparator;
import com.firebase.client.core.Path;
import com.firebase.client.snapshot.ChildKey;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImmutableTree<T> implements Iterable<Map.Entry<Path, T>> {
    private static final ImmutableTree EMPTY = new ImmutableTree((Object) null, EMPTY_CHILDREN);
    private static final ImmutableSortedMap EMPTY_CHILDREN = ImmutableSortedMap.Builder.emptyMap(StandardComparator.getComparator(ChildKey.class));
    private final ImmutableSortedMap<ChildKey, ImmutableTree<T>> children;
    private final T value;

    public interface TreeVisitor<T, R> {
        R onNodeValue(Path path, T t, R r);
    }

    public static <V> ImmutableTree<V> emptyInstance() {
        return EMPTY;
    }

    public ImmutableTree(T value2, ImmutableSortedMap<ChildKey, ImmutableTree<T>> children2) {
        this.value = value2;
        this.children = children2;
    }

    public ImmutableTree(T value2) {
        this(value2, EMPTY_CHILDREN);
    }

    public T getValue() {
        return this.value;
    }

    public ImmutableSortedMap<ChildKey, ImmutableTree<T>> getChildren() {
        return this.children;
    }

    public boolean isEmpty() {
        return this.value == null && this.children.isEmpty();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0018, code lost:
        r1 = r7.getFront();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.firebase.client.core.Path findRootMostMatchingPath(com.firebase.client.core.Path r7, com.firebase.client.core.utilities.Predicate<? super T> r8) {
        /*
            r6 = this;
            r3 = 0
            T r4 = r6.value
            if (r4 == 0) goto L_0x0012
            T r4 = r6.value
            boolean r4 = r8.evaluate(r4)
            if (r4 == 0) goto L_0x0012
            com.firebase.client.core.Path r3 = com.firebase.client.core.Path.getEmptyPath()
        L_0x0011:
            return r3
        L_0x0012:
            boolean r4 = r7.isEmpty()
            if (r4 != 0) goto L_0x0011
            com.firebase.client.snapshot.ChildKey r1 = r7.getFront()
            com.firebase.client.collection.ImmutableSortedMap<com.firebase.client.snapshot.ChildKey, com.firebase.client.core.utilities.ImmutableTree<T>> r4 = r6.children
            java.lang.Object r0 = r4.get(r1)
            com.firebase.client.core.utilities.ImmutableTree r0 = (com.firebase.client.core.utilities.ImmutableTree) r0
            if (r0 == 0) goto L_0x0011
            com.firebase.client.core.Path r4 = r7.popFront()
            com.firebase.client.core.Path r2 = r0.findRootMostMatchingPath(r4, r8)
            if (r2 == 0) goto L_0x0011
            com.firebase.client.core.Path r3 = new com.firebase.client.core.Path
            r4 = 1
            com.firebase.client.snapshot.ChildKey[] r4 = new com.firebase.client.snapshot.ChildKey[r4]
            r5 = 0
            r4[r5] = r1
            r3.<init>((com.firebase.client.snapshot.ChildKey[]) r4)
            com.firebase.client.core.Path r3 = r3.child((com.firebase.client.core.Path) r2)
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: com.firebase.client.core.utilities.ImmutableTree.findRootMostMatchingPath(com.firebase.client.core.Path, com.firebase.client.core.utilities.Predicate):com.firebase.client.core.Path");
    }

    public Path findRootMostPathWithValue(Path relativePath) {
        return findRootMostMatchingPath(relativePath, Predicate.TRUE);
    }

    public T rootMostValue(Path relativePath) {
        return rootMostValueMatching(relativePath, Predicate.TRUE);
    }

    public T rootMostValueMatching(Path relativePath, Predicate<? super T> predicate) {
        if (this.value != null && predicate.evaluate(this.value)) {
            return this.value;
        }
        ImmutableTree immutableTree = this;
        Iterator i$ = relativePath.iterator();
        while (i$.hasNext() && (immutableTree = immutableTree.children.get(i$.next())) != null) {
            if (immutableTree.value != null && predicate.evaluate(immutableTree.value)) {
                return immutableTree.value;
            }
        }
        return null;
    }

    public T leafMostValue(Path relativePath) {
        return leafMostValueMatching(relativePath, Predicate.TRUE);
    }

    public T leafMostValueMatching(Path path, Predicate<? super T> predicate) {
        T currentValue = (this.value == null || !predicate.evaluate(this.value)) ? null : this.value;
        ImmutableTree immutableTree = this;
        Iterator i$ = path.iterator();
        while (i$.hasNext() && (immutableTree = immutableTree.children.get(i$.next())) != null) {
            if (immutableTree.value != null && predicate.evaluate(immutableTree.value)) {
                currentValue = immutableTree.value;
            }
        }
        return currentValue;
    }

    public boolean containsMatchingValue(Predicate<? super T> predicate) {
        if (this.value != null && predicate.evaluate(this.value)) {
            return true;
        }
        Iterator i$ = this.children.iterator();
        while (i$.hasNext()) {
            if (i$.next().getValue().containsMatchingValue(predicate)) {
                return true;
            }
        }
        return false;
    }

    public ImmutableTree<T> getChild(ChildKey child) {
        ImmutableTree<T> childTree = this.children.get(child);
        return childTree != null ? childTree : emptyInstance();
    }

    public ImmutableTree<T> subtree(Path relativePath) {
        if (relativePath.isEmpty()) {
            return this;
        }
        ImmutableTree<T> childTree = this.children.get(relativePath.getFront());
        if (childTree != null) {
            return childTree.subtree(relativePath.popFront());
        }
        return emptyInstance();
    }

    public ImmutableTree<T> set(Path relativePath, T value2) {
        if (relativePath.isEmpty()) {
            return new ImmutableTree<>(value2, this.children);
        }
        ChildKey front = relativePath.getFront();
        ImmutableTree immutableTree = this.children.get(front);
        if (immutableTree == null) {
            immutableTree = emptyInstance();
        }
        return new ImmutableTree<>(this.value, this.children.insert(front, immutableTree.set(relativePath.popFront(), value2)));
    }

    public ImmutableTree<T> remove(Path relativePath) {
        ImmutableSortedMap<ChildKey, ImmutableTree<T>> newChildren;
        if (!relativePath.isEmpty()) {
            ChildKey front = relativePath.getFront();
            ImmutableTree immutableTree = this.children.get(front);
            if (immutableTree == null) {
                return this;
            }
            ImmutableTree remove = immutableTree.remove(relativePath.popFront());
            if (remove.isEmpty()) {
                newChildren = this.children.remove(front);
            } else {
                newChildren = this.children.insert(front, remove);
            }
            if (this.value != null || !newChildren.isEmpty()) {
                return new ImmutableTree<>(this.value, newChildren);
            }
            return emptyInstance();
        } else if (this.children.isEmpty()) {
            return emptyInstance();
        } else {
            return new ImmutableTree<>((Object) null, this.children);
        }
    }

    public T get(Path relativePath) {
        if (relativePath.isEmpty()) {
            return this.value;
        }
        ImmutableTree<T> child = this.children.get(relativePath.getFront());
        if (child != null) {
            return child.get(relativePath.popFront());
        }
        return null;
    }

    public ImmutableTree<T> setTree(Path relativePath, ImmutableTree<T> newTree) {
        ImmutableSortedMap<ChildKey, ImmutableTree<T>> newChildren;
        if (relativePath.isEmpty()) {
            return newTree;
        }
        ChildKey front = relativePath.getFront();
        ImmutableTree<T> child = this.children.get(front);
        if (child == null) {
            child = emptyInstance();
        }
        ImmutableTree<T> newChild = child.setTree(relativePath.popFront(), newTree);
        if (newChild.isEmpty()) {
            newChildren = this.children.remove(front);
        } else {
            newChildren = this.children.insert(front, newChild);
        }
        return new ImmutableTree<>(this.value, newChildren);
    }

    public void foreach(TreeVisitor<T, Void> visitor) {
        fold(Path.getEmptyPath(), visitor, (Object) null);
    }

    public <R> R fold(R accum, TreeVisitor<? super T, R> visitor) {
        return fold(Path.getEmptyPath(), visitor, accum);
    }

    private <R> R fold(Path relativePath, TreeVisitor<? super T, R> visitor, R accum) {
        Iterator i$ = this.children.iterator();
        while (i$.hasNext()) {
            Map.Entry<ChildKey, ImmutableTree<T>> subtree = i$.next();
            accum = subtree.getValue().fold(relativePath.child(subtree.getKey()), visitor, accum);
        }
        if (this.value != null) {
            return visitor.onNodeValue(relativePath, this.value, accum);
        }
        return accum;
    }

    public Collection<T> values() {
        final ArrayList<T> list = new ArrayList<>();
        foreach(new TreeVisitor<T, Void>() {
            public Void onNodeValue(Path relativePath, T value, Void accum) {
                list.add(value);
                return null;
            }
        });
        return list;
    }

    public Iterator<Map.Entry<Path, T>> iterator() {
        final List<Map.Entry<Path, T>> list = new ArrayList<>();
        foreach(new TreeVisitor<T, Void>() {
            public Void onNodeValue(Path relativePath, T value, Void accum) {
                list.add(new AbstractMap.SimpleImmutableEntry(relativePath, value));
                return null;
            }
        });
        return list.iterator();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ImmutableTree { value=");
        builder.append(getValue());
        builder.append(", children={");
        Iterator i$ = this.children.iterator();
        while (i$.hasNext()) {
            Map.Entry<ChildKey, ImmutableTree<T>> child = i$.next();
            builder.append(child.getKey().asString());
            builder.append("=");
            builder.append(child.getValue());
        }
        builder.append("} }");
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImmutableTree that = (ImmutableTree) o;
        if (this.children == null ? that.children != null : !this.children.equals(that.children)) {
            return false;
        }
        if (this.value != null) {
            if (this.value.equals(that.value)) {
                return true;
            }
        } else if (that.value == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.value != null) {
            result = this.value.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.children != null) {
            i = this.children.hashCode();
        }
        return i2 + i;
    }
}
