package com.shaded.fasterxml.jackson.core.p004io;

/* renamed from: com.shaded.fasterxml.jackson.core.io.NumberInput */
public final class NumberInput {
    static final long L_BILLION = 1000000000;
    static final String MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);
    static final String MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
    public static final String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";

    public static int parseInt(char[] cArr, int i, int i2) {
        int i3 = cArr[i] - '0';
        int i4 = i2 + i;
        int i5 = i + 1;
        if (i5 >= i4) {
            return i3;
        }
        int i6 = (i3 * 10) + (cArr[i5] - '0');
        int i7 = i5 + 1;
        if (i7 >= i4) {
            return i6;
        }
        int i8 = (i6 * 10) + (cArr[i7] - '0');
        int i9 = i7 + 1;
        if (i9 >= i4) {
            return i8;
        }
        int i10 = (i8 * 10) + (cArr[i9] - '0');
        int i11 = i9 + 1;
        if (i11 >= i4) {
            return i10;
        }
        int i12 = (i10 * 10) + (cArr[i11] - '0');
        int i13 = i11 + 1;
        if (i13 >= i4) {
            return i12;
        }
        int i14 = (i12 * 10) + (cArr[i13] - '0');
        int i15 = i13 + 1;
        if (i15 >= i4) {
            return i14;
        }
        int i16 = (i14 * 10) + (cArr[i15] - '0');
        int i17 = i15 + 1;
        if (i17 >= i4) {
            return i16;
        }
        int i18 = (i16 * 10) + (cArr[i17] - '0');
        int i19 = i17 + 1;
        if (i19 < i4) {
            return (i18 * 10) + (cArr[i19] - '0');
        }
        return i18;
    }

    public static int parseInt(String str) {
        int i = 1;
        char charAt = str.charAt(0);
        int length = str.length();
        boolean z = charAt == '-';
        if (z) {
            if (length == 1 || length > 10) {
                return Integer.parseInt(str);
            }
            charAt = str.charAt(1);
            i = 2;
        } else if (length > 9) {
            return Integer.parseInt(str);
        }
        if (charAt > '9' || charAt < '0') {
            return Integer.parseInt(str);
        }
        int i2 = charAt - '0';
        if (i < length) {
            int i3 = i + 1;
            char charAt2 = str.charAt(i);
            if (charAt2 > '9' || charAt2 < '0') {
                return Integer.parseInt(str);
            }
            i2 = (i2 * 10) + (charAt2 - '0');
            if (i3 < length) {
                int i4 = i3 + 1;
                char charAt3 = str.charAt(i3);
                if (charAt3 > '9' || charAt3 < '0') {
                    return Integer.parseInt(str);
                }
                i2 = (i2 * 10) + (charAt3 - '0');
                if (i4 < length) {
                    while (true) {
                        int i5 = i4 + 1;
                        char charAt4 = str.charAt(i4);
                        if (charAt4 <= '9' && charAt4 >= '0') {
                            i2 = (i2 * 10) + (charAt4 - '0');
                            if (i5 >= length) {
                                break;
                            }
                            i4 = i5;
                        }
                    }
                    return Integer.parseInt(str);
                }
            }
        }
        return z ? -i2 : i2;
    }

    public static long parseLong(char[] cArr, int i, int i2) {
        int i3 = i2 - 9;
        return ((long) parseInt(cArr, i3 + i, 9)) + (((long) parseInt(cArr, i, i3)) * L_BILLION);
    }

    public static long parseLong(String str) {
        if (str.length() <= 9) {
            return (long) parseInt(str);
        }
        return Long.parseLong(str);
    }

    public static boolean inLongRange(char[] cArr, int i, int i2, boolean z) {
        String str = z ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int length = str.length();
        if (i2 < length) {
            return true;
        }
        if (i2 > length) {
            return false;
        }
        for (int i3 = 0; i3 < length; i3++) {
            int charAt = cArr[i + i3] - str.charAt(i3);
            if (charAt != 0) {
                return charAt < 0;
            }
        }
        return true;
    }

    public static boolean inLongRange(String str, boolean z) {
        String str2 = z ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int length = str2.length();
        int length2 = str.length();
        if (length2 < length) {
            return true;
        }
        if (length2 > length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            int charAt = str.charAt(i) - str2.charAt(i);
            if (charAt != 0) {
                return charAt < 0;
            }
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int parseAsInt(java.lang.String r6, int r7) {
        /*
            r1 = 1
            r0 = 0
            if (r6 != 0) goto L_0x0005
        L_0x0004:
            return r7
        L_0x0005:
            java.lang.String r3 = r6.trim()
            int r2 = r3.length()
            if (r2 == 0) goto L_0x0004
            if (r0 >= r2) goto L_0x0049
            char r4 = r3.charAt(r0)
            r5 = 43
            if (r4 != r5) goto L_0x0035
            java.lang.String r2 = r3.substring(r1)
            int r1 = r2.length()
        L_0x0021:
            if (r0 >= r1) goto L_0x0040
            char r3 = r2.charAt(r0)
            r4 = 57
            if (r3 > r4) goto L_0x002f
            r4 = 48
            if (r3 >= r4) goto L_0x003d
        L_0x002f:
            double r0 = parseDouble(r2)     // Catch:{ NumberFormatException -> 0x0045 }
            int r7 = (int) r0
            goto L_0x0004
        L_0x0035:
            r5 = 45
            if (r4 != r5) goto L_0x0049
            r0 = r1
            r1 = r2
            r2 = r3
            goto L_0x0021
        L_0x003d:
            int r0 = r0 + 1
            goto L_0x0021
        L_0x0040:
            int r7 = java.lang.Integer.parseInt(r2)     // Catch:{ NumberFormatException -> 0x0047 }
            goto L_0x0004
        L_0x0045:
            r0 = move-exception
            goto L_0x0004
        L_0x0047:
            r0 = move-exception
            goto L_0x0004
        L_0x0049:
            r1 = r2
            r2 = r3
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.p004io.NumberInput.parseAsInt(java.lang.String, int):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long parseAsLong(java.lang.String r7, long r8) {
        /*
            r1 = 1
            r0 = 0
            if (r7 != 0) goto L_0x0005
        L_0x0004:
            return r8
        L_0x0005:
            java.lang.String r3 = r7.trim()
            int r2 = r3.length()
            if (r2 == 0) goto L_0x0004
            if (r0 >= r2) goto L_0x0049
            char r4 = r3.charAt(r0)
            r5 = 43
            if (r4 != r5) goto L_0x0035
            java.lang.String r2 = r3.substring(r1)
            int r1 = r2.length()
        L_0x0021:
            if (r0 >= r1) goto L_0x0040
            char r3 = r2.charAt(r0)
            r4 = 57
            if (r3 > r4) goto L_0x002f
            r4 = 48
            if (r3 >= r4) goto L_0x003d
        L_0x002f:
            double r0 = parseDouble(r2)     // Catch:{ NumberFormatException -> 0x0045 }
            long r8 = (long) r0
            goto L_0x0004
        L_0x0035:
            r5 = 45
            if (r4 != r5) goto L_0x0049
            r0 = r1
            r1 = r2
            r2 = r3
            goto L_0x0021
        L_0x003d:
            int r0 = r0 + 1
            goto L_0x0021
        L_0x0040:
            long r8 = java.lang.Long.parseLong(r2)     // Catch:{ NumberFormatException -> 0x0047 }
            goto L_0x0004
        L_0x0045:
            r0 = move-exception
            goto L_0x0004
        L_0x0047:
            r0 = move-exception
            goto L_0x0004
        L_0x0049:
            r1 = r2
            r2 = r3
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.p004io.NumberInput.parseAsLong(java.lang.String, long):long");
    }

    public static double parseAsDouble(String str, double d) {
        if (str == null) {
            return d;
        }
        String trim = str.trim();
        if (trim.length() == 0) {
            return d;
        }
        try {
            return parseDouble(trim);
        } catch (NumberFormatException e) {
            return d;
        }
    }

    public static double parseDouble(String str) throws NumberFormatException {
        if (NASTY_SMALL_DOUBLE.equals(str)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(str);
    }
}
