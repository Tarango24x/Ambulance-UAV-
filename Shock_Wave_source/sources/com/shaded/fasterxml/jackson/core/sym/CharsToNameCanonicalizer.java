package com.shaded.fasterxml.jackson.core.sym;

import java.util.Arrays;

public final class CharsToNameCanonicalizer {
    protected static final int DEFAULT_TABLE_SIZE = 64;
    public static final int HASH_MULT = 33;
    static final int MAX_COLL_CHAIN_FOR_REUSE = 63;
    static final int MAX_COLL_CHAIN_LENGTH = 255;
    static final int MAX_ENTRIES_FOR_REUSE = 12000;
    protected static final int MAX_TABLE_SIZE = 65536;
    static final CharsToNameCanonicalizer sBootstrapSymbolTable = new CharsToNameCanonicalizer();
    protected Bucket[] _buckets;
    protected final boolean _canonicalize;
    protected boolean _dirty;
    private final int _hashSeed;
    protected int _indexMask;
    protected final boolean _intern;
    protected int _longestCollisionList;
    protected CharsToNameCanonicalizer _parent;
    protected int _size;
    protected int _sizeThreshold;
    protected String[] _symbols;

    public static CharsToNameCanonicalizer createRoot() {
        long currentTimeMillis = System.currentTimeMillis();
        return createRoot((((int) (currentTimeMillis >>> 32)) + ((int) currentTimeMillis)) | 1);
    }

    protected static CharsToNameCanonicalizer createRoot(int i) {
        return sBootstrapSymbolTable.makeOrphan(i);
    }

    private CharsToNameCanonicalizer() {
        this._canonicalize = true;
        this._intern = true;
        this._dirty = true;
        this._hashSeed = 0;
        this._longestCollisionList = 0;
        initTables(64);
    }

    private void initTables(int i) {
        this._symbols = new String[i];
        this._buckets = new Bucket[(i >> 1)];
        this._indexMask = i - 1;
        this._size = 0;
        this._longestCollisionList = 0;
        this._sizeThreshold = _thresholdSize(i);
    }

    private static int _thresholdSize(int i) {
        return i - (i >> 2);
    }

    private CharsToNameCanonicalizer(CharsToNameCanonicalizer charsToNameCanonicalizer, boolean z, boolean z2, String[] strArr, Bucket[] bucketArr, int i, int i2, int i3) {
        this._parent = charsToNameCanonicalizer;
        this._canonicalize = z;
        this._intern = z2;
        this._symbols = strArr;
        this._buckets = bucketArr;
        this._size = i;
        this._hashSeed = i2;
        int length = strArr.length;
        this._sizeThreshold = _thresholdSize(length);
        this._indexMask = length - 1;
        this._longestCollisionList = i3;
        this._dirty = false;
    }

    public CharsToNameCanonicalizer makeChild(boolean z, boolean z2) {
        String[] strArr;
        Bucket[] bucketArr;
        int i;
        int i2;
        int i3;
        synchronized (this) {
            strArr = this._symbols;
            bucketArr = this._buckets;
            i = this._size;
            i2 = this._hashSeed;
            i3 = this._longestCollisionList;
        }
        return new CharsToNameCanonicalizer(this, z, z2, strArr, bucketArr, i, i2, i3);
    }

    private CharsToNameCanonicalizer makeOrphan(int i) {
        return new CharsToNameCanonicalizer((CharsToNameCanonicalizer) null, true, true, this._symbols, this._buckets, this._size, i, this._longestCollisionList);
    }

    private void mergeChild(CharsToNameCanonicalizer charsToNameCanonicalizer) {
        if (charsToNameCanonicalizer.size() > MAX_ENTRIES_FOR_REUSE || charsToNameCanonicalizer._longestCollisionList > 63) {
            synchronized (this) {
                initTables(64);
                this._dirty = false;
            }
        } else if (charsToNameCanonicalizer.size() > size()) {
            synchronized (this) {
                this._symbols = charsToNameCanonicalizer._symbols;
                this._buckets = charsToNameCanonicalizer._buckets;
                this._size = charsToNameCanonicalizer._size;
                this._sizeThreshold = charsToNameCanonicalizer._sizeThreshold;
                this._indexMask = charsToNameCanonicalizer._indexMask;
                this._longestCollisionList = charsToNameCanonicalizer._longestCollisionList;
                this._dirty = false;
            }
        }
    }

    public void release() {
        if (maybeDirty() && this._parent != null) {
            this._parent.mergeChild(this);
            this._dirty = false;
        }
    }

    public int size() {
        return this._size;
    }

    public int bucketCount() {
        return this._symbols.length;
    }

    public boolean maybeDirty() {
        return this._dirty;
    }

    public int hashSeed() {
        return this._hashSeed;
    }

    public int collisionCount() {
        int i = 0;
        for (Bucket bucket : this._buckets) {
            if (bucket != null) {
                i += bucket.length();
            }
        }
        return i;
    }

    public int maxCollisionLength() {
        return this._longestCollisionList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0023 A[LOOP:0: B:11:0x0023->B:16:0x0033, LOOP_START, PHI: r0 
      PHI: (r0v18 int) = (r0v17 int), (r0v19 int) binds: [B:10:0x0022, B:16:0x0033] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String findSymbol(char[] r8, int r9, int r10, int r11) {
        /*
            r7 = this;
            r6 = 255(0xff, float:3.57E-43)
            r5 = 1
            if (r10 >= r5) goto L_0x0008
            java.lang.String r1 = ""
        L_0x0007:
            return r1
        L_0x0008:
            boolean r0 = r7._canonicalize
            if (r0 != 0) goto L_0x0012
            java.lang.String r1 = new java.lang.String
            r1.<init>(r8, r9, r10)
            goto L_0x0007
        L_0x0012:
            int r1 = r7._hashToIndex(r11)
            java.lang.String[] r0 = r7._symbols
            r2 = r0[r1]
            if (r2 == 0) goto L_0x0046
            int r0 = r2.length()
            if (r0 != r10) goto L_0x0036
            r0 = 0
        L_0x0023:
            char r3 = r2.charAt(r0)
            int r4 = r9 + r0
            char r4 = r8[r4]
            if (r3 == r4) goto L_0x0031
        L_0x002d:
            if (r0 != r10) goto L_0x0036
            r1 = r2
            goto L_0x0007
        L_0x0031:
            int r0 = r0 + 1
            if (r0 < r10) goto L_0x0023
            goto L_0x002d
        L_0x0036:
            com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer$Bucket[] r0 = r7._buckets
            int r2 = r1 >> 1
            r0 = r0[r2]
            if (r0 == 0) goto L_0x0046
            java.lang.String r0 = r0.find(r8, r9, r10)
            if (r0 == 0) goto L_0x0046
            r1 = r0
            goto L_0x0007
        L_0x0046:
            boolean r0 = r7._dirty
            if (r0 != 0) goto L_0x0070
            r7.copyArrays()
            r7._dirty = r5
            r0 = r1
        L_0x0050:
            java.lang.String r1 = new java.lang.String
            r1.<init>(r8, r9, r10)
            boolean r2 = r7._intern
            if (r2 == 0) goto L_0x005f
            com.shaded.fasterxml.jackson.core.util.InternCache r2 = com.shaded.fasterxml.jackson.core.util.InternCache.instance
            java.lang.String r1 = r2.intern(r1)
        L_0x005f:
            int r2 = r7._size
            int r2 = r2 + 1
            r7._size = r2
            java.lang.String[] r2 = r7._symbols
            r2 = r2[r0]
            if (r2 != 0) goto L_0x0082
            java.lang.String[] r2 = r7._symbols
            r2[r0] = r1
            goto L_0x0007
        L_0x0070:
            int r0 = r7._size
            int r2 = r7._sizeThreshold
            if (r0 < r2) goto L_0x00a6
            r7.rehash()
            int r0 = r7.calcHash(r8, r9, r10)
            int r0 = r7._hashToIndex(r0)
            goto L_0x0050
        L_0x0082:
            int r0 = r0 >> 1
            com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer$Bucket r2 = new com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer$Bucket
            com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer$Bucket[] r3 = r7._buckets
            r3 = r3[r0]
            r2.<init>(r1, r3)
            com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer$Bucket[] r3 = r7._buckets
            r3[r0] = r2
            int r0 = r2.length()
            int r2 = r7._longestCollisionList
            int r0 = java.lang.Math.max(r0, r2)
            r7._longestCollisionList = r0
            int r0 = r7._longestCollisionList
            if (r0 <= r6) goto L_0x0007
            r7.reportTooManyCollisions(r6)
            goto L_0x0007
        L_0x00a6:
            r0 = r1
            goto L_0x0050
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer.findSymbol(char[], int, int, int):java.lang.String");
    }

    public int _hashToIndex(int i) {
        return ((i >>> 15) + i) & this._indexMask;
    }

    public int calcHash(char[] cArr, int i, int i2) {
        int i3 = this._hashSeed;
        int i4 = 0;
        while (i4 < i2) {
            i4++;
            i3 = cArr[i4] + (i3 * 33);
        }
        if (i3 == 0) {
            return 1;
        }
        return i3;
    }

    public int calcHash(String str) {
        int length = str.length();
        int i = this._hashSeed;
        int i2 = 0;
        while (i2 < length) {
            i2++;
            i = str.charAt(i2) + (i * 33);
        }
        if (i == 0) {
            return 1;
        }
        return i;
    }

    private void copyArrays() {
        String[] strArr = this._symbols;
        int length = strArr.length;
        this._symbols = new String[length];
        System.arraycopy(strArr, 0, this._symbols, 0, length);
        Bucket[] bucketArr = this._buckets;
        int length2 = bucketArr.length;
        this._buckets = new Bucket[length2];
        System.arraycopy(bucketArr, 0, this._buckets, 0, length2);
    }

    private void rehash() {
        int length = this._symbols.length;
        int i = length + length;
        if (i > 65536) {
            this._size = 0;
            Arrays.fill(this._symbols, (Object) null);
            Arrays.fill(this._buckets, (Object) null);
            this._dirty = true;
            return;
        }
        String[] strArr = this._symbols;
        Bucket[] bucketArr = this._buckets;
        this._symbols = new String[i];
        this._buckets = new Bucket[(i >> 1)];
        this._indexMask = i - 1;
        this._sizeThreshold = _thresholdSize(i);
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            String str = strArr[i4];
            if (str != null) {
                i3++;
                int _hashToIndex = _hashToIndex(calcHash(str));
                if (this._symbols[_hashToIndex] == null) {
                    this._symbols[_hashToIndex] = str;
                } else {
                    int i5 = _hashToIndex >> 1;
                    Bucket bucket = new Bucket(str, this._buckets[i5]);
                    this._buckets[i5] = bucket;
                    i2 = Math.max(i2, bucket.length());
                }
            }
        }
        int i6 = length >> 1;
        int i7 = 0;
        int i8 = i3;
        int i9 = i2;
        while (i7 < i6) {
            int i10 = i9;
            for (Bucket bucket2 = bucketArr[i7]; bucket2 != null; bucket2 = bucket2.getNext()) {
                i8++;
                String symbol = bucket2.getSymbol();
                int _hashToIndex2 = _hashToIndex(calcHash(symbol));
                if (this._symbols[_hashToIndex2] == null) {
                    this._symbols[_hashToIndex2] = symbol;
                } else {
                    int i11 = _hashToIndex2 >> 1;
                    Bucket bucket3 = new Bucket(symbol, this._buckets[i11]);
                    this._buckets[i11] = bucket3;
                    i10 = Math.max(i10, bucket3.length());
                }
            }
            i7++;
            i9 = i10;
        }
        this._longestCollisionList = i9;
        if (i8 != this._size) {
            throw new Error("Internal error on SymbolTable.rehash(): had " + this._size + " entries; now have " + i8 + ".");
        }
    }

    /* access modifiers changed from: protected */
    public void reportTooManyCollisions(int i) {
        throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._size + ") now exceeds maximum, " + i + " -- suspect a DoS attack based on hash collisions");
    }

    static final class Bucket {
        private final int _length;
        private final Bucket _next;
        private final String _symbol;

        public Bucket(String str, Bucket bucket) {
            this._symbol = str;
            this._next = bucket;
            this._length = bucket == null ? 1 : bucket._length + 1;
        }

        public String getSymbol() {
            return this._symbol;
        }

        public Bucket getNext() {
            return this._next;
        }

        public int length() {
            return this._length;
        }

        /* JADX WARNING: Removed duplicated region for block: B:4:0x000b A[LOOP:1: B:4:0x000b->B:8:0x001a, LOOP_START, PHI: r2 
          PHI: (r2v2 int) = (r2v1 int), (r2v3 int) binds: [B:3:0x000a, B:8:0x001a] A[DONT_GENERATE, DONT_INLINE]] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String find(char[] r6, int r7, int r8) {
            /*
                r5 = this;
                java.lang.String r1 = r5._symbol
                com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer$Bucket r0 = r5._next
            L_0x0004:
                int r2 = r1.length()
                if (r2 != r8) goto L_0x001d
                r2 = 0
            L_0x000b:
                char r3 = r1.charAt(r2)
                int r4 = r7 + r2
                char r4 = r6[r4]
                if (r3 == r4) goto L_0x0018
            L_0x0015:
                if (r2 != r8) goto L_0x001d
            L_0x0017:
                return r1
            L_0x0018:
                int r2 = r2 + 1
                if (r2 < r8) goto L_0x000b
                goto L_0x0015
            L_0x001d:
                if (r0 != 0) goto L_0x0021
                r1 = 0
                goto L_0x0017
            L_0x0021:
                java.lang.String r1 = r0.getSymbol()
                com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer$Bucket r0 = r0.getNext()
                goto L_0x0004
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.sym.CharsToNameCanonicalizer.Bucket.find(char[], int, int):java.lang.String");
        }
    }
}
