package com.shaded.fasterxml.jackson.core.p004io;

import com.google.appinventor.components.runtime.util.FullScreenVideoUtil;
import com.shaded.fasterxml.jackson.core.util.BufferRecycler;
import com.shaded.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.shaded.fasterxml.jackson.core.util.TextBuffer;
import java.lang.ref.SoftReference;

/* renamed from: com.shaded.fasterxml.jackson.core.io.JsonStringEncoder */
public final class JsonStringEncoder {
    private static final byte[] HEX_BYTES = CharTypes.copyHexBytes();
    private static final char[] HEX_CHARS = CharTypes.copyHexChars();
    private static final int INT_0 = 48;
    private static final int INT_BACKSLASH = 92;
    private static final int INT_U = 117;
    private static final int SURR1_FIRST = 55296;
    private static final int SURR1_LAST = 56319;
    private static final int SURR2_FIRST = 56320;
    private static final int SURR2_LAST = 57343;
    protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder = new ThreadLocal<>();
    protected ByteArrayBuilder _byteBuilder;
    protected final char[] _quoteBuffer = new char[6];
    protected TextBuffer _textBuffer;

    public JsonStringEncoder() {
        this._quoteBuffer[0] = '\\';
        this._quoteBuffer[2] = '0';
        this._quoteBuffer[3] = '0';
    }

    public static JsonStringEncoder getInstance() {
        SoftReference softReference = _threadEncoder.get();
        JsonStringEncoder jsonStringEncoder = softReference == null ? null : (JsonStringEncoder) softReference.get();
        if (jsonStringEncoder != null) {
            return jsonStringEncoder;
        }
        JsonStringEncoder jsonStringEncoder2 = new JsonStringEncoder();
        _threadEncoder.set(new SoftReference(jsonStringEncoder2));
        return jsonStringEncoder2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0030, code lost:
        if (r9 >= 0) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0032, code lost:
        r2 = _appendNumericEscape(r2, r11._quoteBuffer);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003b, code lost:
        if ((r1 + r2) <= r3.length) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003d, code lost:
        r9 = r3.length - r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003f, code lost:
        if (r9 <= 0) goto L_0x0046;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0041, code lost:
        java.lang.System.arraycopy(r11._quoteBuffer, 0, r3, r1, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0046, code lost:
        r3 = r0.finishCurrentSegment();
        r1 = r2 - r9;
        java.lang.System.arraycopy(r11._quoteBuffer, r9, r3, 0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0051, code lost:
        r2 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006b, code lost:
        r2 = _appendNamedEscape(r9, r11._quoteBuffer);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0072, code lost:
        java.lang.System.arraycopy(r11._quoteBuffer, 0, r3, r1, r2);
        r1 = r1 + r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0028, code lost:
        r4 = r2 + 1;
        r2 = r12.charAt(r2);
        r9 = r6[r2];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public char[] quoteAsString(java.lang.String r12) {
        /*
            r11 = this;
            r5 = 0
            com.shaded.fasterxml.jackson.core.util.TextBuffer r0 = r11._textBuffer
            if (r0 != 0) goto L_0x000d
            com.shaded.fasterxml.jackson.core.util.TextBuffer r0 = new com.shaded.fasterxml.jackson.core.util.TextBuffer
            r1 = 0
            r0.<init>(r1)
            r11._textBuffer = r0
        L_0x000d:
            char[] r3 = r0.emptyAndGetCurrentSegment()
            int[] r6 = com.shaded.fasterxml.jackson.core.p004io.CharTypes.get7BitOutputEscapes()
            int r7 = r6.length
            int r8 = r12.length()
            r1 = r5
            r2 = r5
        L_0x001c:
            if (r2 >= r8) goto L_0x0063
        L_0x001e:
            char r9 = r12.charAt(r2)
            if (r9 >= r7) goto L_0x0053
            r4 = r6[r9]
            if (r4 == 0) goto L_0x0053
            int r4 = r2 + 1
            char r2 = r12.charAt(r2)
            r9 = r6[r2]
            if (r9 >= 0) goto L_0x006b
            char[] r9 = r11._quoteBuffer
            int r2 = r11._appendNumericEscape(r2, r9)
        L_0x0038:
            int r9 = r1 + r2
            int r10 = r3.length
            if (r9 <= r10) goto L_0x0072
            int r9 = r3.length
            int r9 = r9 - r1
            if (r9 <= 0) goto L_0x0046
            char[] r10 = r11._quoteBuffer
            java.lang.System.arraycopy(r10, r5, r3, r1, r9)
        L_0x0046:
            char[] r3 = r0.finishCurrentSegment()
            int r1 = r2 - r9
            char[] r2 = r11._quoteBuffer
            java.lang.System.arraycopy(r2, r9, r3, r5, r1)
        L_0x0051:
            r2 = r4
            goto L_0x001c
        L_0x0053:
            int r4 = r3.length
            if (r1 < r4) goto L_0x0079
            char[] r3 = r0.finishCurrentSegment()
            r4 = r5
        L_0x005b:
            int r1 = r4 + 1
            r3[r4] = r9
            int r2 = r2 + 1
            if (r2 < r8) goto L_0x001e
        L_0x0063:
            r0.setCurrentLength(r1)
            char[] r0 = r0.contentsAsArray()
            return r0
        L_0x006b:
            char[] r2 = r11._quoteBuffer
            int r2 = r11._appendNamedEscape(r9, r2)
            goto L_0x0038
        L_0x0072:
            char[] r9 = r11._quoteBuffer
            java.lang.System.arraycopy(r9, r5, r3, r1, r2)
            int r1 = r1 + r2
            goto L_0x0051
        L_0x0079:
            r4 = r1
            goto L_0x005b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.core.p004io.JsonStringEncoder.quoteAsString(java.lang.String):char[]");
    }

    public byte[] quoteAsUTF8(String str) {
        int i;
        int i2;
        byte[] bArr;
        char c;
        int i3;
        int i4;
        int i5;
        ByteArrayBuilder byteArrayBuilder = this._byteBuilder;
        if (byteArrayBuilder == null) {
            byteArrayBuilder = new ByteArrayBuilder((BufferRecycler) null);
            this._byteBuilder = byteArrayBuilder;
        }
        int length = str.length();
        byte[] resetAndGetFirstSegment = byteArrayBuilder.resetAndGetFirstSegment();
        int i6 = 0;
        int i7 = 0;
        loop0:
        while (i7 < length) {
            int[] iArr = CharTypes.get7BitOutputEscapes();
            while (true) {
                char charAt = str.charAt(i7);
                if (charAt <= 127 && iArr[charAt] == 0) {
                    if (i6 >= resetAndGetFirstSegment.length) {
                        resetAndGetFirstSegment = byteArrayBuilder.finishCurrentSegment();
                        i5 = 0;
                    } else {
                        i5 = i6;
                    }
                    i6 = i5 + 1;
                    resetAndGetFirstSegment[i5] = (byte) charAt;
                    i7++;
                    if (i7 >= length) {
                        break loop0;
                    }
                }
            }
            if (i6 >= resetAndGetFirstSegment.length) {
                resetAndGetFirstSegment = byteArrayBuilder.finishCurrentSegment();
                i6 = 0;
            }
            int i8 = i7 + 1;
            char charAt2 = str.charAt(i7);
            if (charAt2 <= 127) {
                i6 = _appendByteEscape(charAt2, iArr[charAt2], byteArrayBuilder, i6);
                resetAndGetFirstSegment = byteArrayBuilder.getCurrentSegment();
                i7 = i8;
            } else {
                if (charAt2 <= 2047) {
                    i2 = i6 + 1;
                    resetAndGetFirstSegment[i6] = (byte) ((charAt2 >> 6) | FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE);
                    bArr = resetAndGetFirstSegment;
                    c = (charAt2 & '?') | 128;
                } else if (charAt2 < SURR1_FIRST || charAt2 > SURR2_LAST) {
                    int i9 = i6 + 1;
                    resetAndGetFirstSegment[i6] = (byte) ((charAt2 >> 12) | 224);
                    if (i9 >= resetAndGetFirstSegment.length) {
                        resetAndGetFirstSegment = byteArrayBuilder.finishCurrentSegment();
                        i = 0;
                    } else {
                        i = i9;
                    }
                    i2 = i + 1;
                    resetAndGetFirstSegment[i] = (byte) (((charAt2 >> 6) & 63) | 128);
                    bArr = resetAndGetFirstSegment;
                    c = (charAt2 & '?') | 128;
                } else {
                    if (charAt2 > SURR1_LAST) {
                        _illegalSurrogate(charAt2);
                    }
                    if (i8 >= length) {
                        _illegalSurrogate(charAt2);
                    }
                    int i10 = i8 + 1;
                    int _convertSurrogate = _convertSurrogate(charAt2, str.charAt(i8));
                    if (_convertSurrogate > 1114111) {
                        _illegalSurrogate(_convertSurrogate);
                    }
                    int i11 = i6 + 1;
                    resetAndGetFirstSegment[i6] = (byte) ((_convertSurrogate >> 18) | 240);
                    if (i11 >= resetAndGetFirstSegment.length) {
                        resetAndGetFirstSegment = byteArrayBuilder.finishCurrentSegment();
                        i3 = 0;
                    } else {
                        i3 = i11;
                    }
                    int i12 = i3 + 1;
                    resetAndGetFirstSegment[i3] = (byte) (((_convertSurrogate >> 12) & 63) | 128);
                    if (i12 >= resetAndGetFirstSegment.length) {
                        resetAndGetFirstSegment = byteArrayBuilder.finishCurrentSegment();
                        i4 = 0;
                    } else {
                        i4 = i12;
                    }
                    i2 = i4 + 1;
                    resetAndGetFirstSegment[i4] = (byte) (((_convertSurrogate >> 6) & 63) | 128);
                    i8 = i10;
                    byte[] bArr2 = resetAndGetFirstSegment;
                    c = (_convertSurrogate & '?') | 128;
                    bArr = bArr2;
                }
                if (i2 >= bArr.length) {
                    bArr = byteArrayBuilder.finishCurrentSegment();
                    i2 = 0;
                }
                int i13 = i2 + 1;
                bArr[i2] = (byte) c;
                resetAndGetFirstSegment = bArr;
                i7 = i8;
                i6 = i13;
            }
        }
        return this._byteBuilder.completeAndCoalesce(i6);
    }

    public byte[] encodeAsUTF8(String str) {
        int i;
        int i2;
        int i3;
        char c;
        int i4;
        ByteArrayBuilder byteArrayBuilder = this._byteBuilder;
        if (byteArrayBuilder == null) {
            byteArrayBuilder = new ByteArrayBuilder((BufferRecycler) null);
            this._byteBuilder = byteArrayBuilder;
        }
        int length = str.length();
        byte[] resetAndGetFirstSegment = byteArrayBuilder.resetAndGetFirstSegment();
        int length2 = resetAndGetFirstSegment.length;
        int i5 = 0;
        int i6 = 0;
        loop0:
        while (true) {
            if (i6 >= length) {
                i = i5;
                break;
            }
            int i7 = i6 + 1;
            char charAt = str.charAt(i6);
            int i8 = length2;
            byte[] bArr = resetAndGetFirstSegment;
            int i9 = i5;
            int i10 = i8;
            while (charAt <= 127) {
                if (i9 >= i10) {
                    bArr = byteArrayBuilder.finishCurrentSegment();
                    i10 = bArr.length;
                    i9 = 0;
                }
                int i11 = i9 + 1;
                bArr[i9] = (byte) charAt;
                if (i7 >= length) {
                    i = i11;
                    break loop0;
                }
                charAt = str.charAt(i7);
                i7++;
                i9 = i11;
            }
            if (i9 >= i10) {
                bArr = byteArrayBuilder.finishCurrentSegment();
                i10 = bArr.length;
                i2 = 0;
            } else {
                i2 = i9;
            }
            if (charAt < 2048) {
                i3 = i2 + 1;
                bArr[i2] = (byte) ((charAt >> 6) | FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE);
                c = charAt;
                i6 = i7;
            } else if (charAt < SURR1_FIRST || charAt > SURR2_LAST) {
                int i12 = i2 + 1;
                bArr[i2] = (byte) ((charAt >> 12) | 224);
                if (i12 >= i10) {
                    bArr = byteArrayBuilder.finishCurrentSegment();
                    i10 = bArr.length;
                    i12 = 0;
                }
                bArr[i12] = (byte) (((charAt >> 6) & 63) | 128);
                i3 = i12 + 1;
                c = charAt;
                i6 = i7;
            } else {
                if (charAt > SURR1_LAST) {
                    _illegalSurrogate(charAt);
                }
                if (i7 >= length) {
                    _illegalSurrogate(charAt);
                }
                int i13 = i7 + 1;
                int _convertSurrogate = _convertSurrogate(charAt, str.charAt(i7));
                if (_convertSurrogate > 1114111) {
                    _illegalSurrogate(_convertSurrogate);
                }
                int i14 = i2 + 1;
                bArr[i2] = (byte) ((_convertSurrogate >> 18) | 240);
                if (i14 >= i10) {
                    bArr = byteArrayBuilder.finishCurrentSegment();
                    i10 = bArr.length;
                    i14 = 0;
                }
                int i15 = i14 + 1;
                bArr[i14] = (byte) (((_convertSurrogate >> 12) & 63) | 128);
                if (i15 >= i10) {
                    bArr = byteArrayBuilder.finishCurrentSegment();
                    i10 = bArr.length;
                    i4 = 0;
                } else {
                    i4 = i15;
                }
                bArr[i4] = (byte) (((_convertSurrogate >> 6) & 63) | 128);
                i3 = i4 + 1;
                c = _convertSurrogate;
                i6 = i13;
            }
            if (i3 >= i10) {
                bArr = byteArrayBuilder.finishCurrentSegment();
                i10 = bArr.length;
                i3 = 0;
            }
            int i16 = i3 + 1;
            bArr[i3] = (byte) ((c & '?') | 128);
            resetAndGetFirstSegment = bArr;
            length2 = i10;
            i5 = i16;
        }
        return this._byteBuilder.completeAndCoalesce(i);
    }

    private int _appendNumericEscape(int i, char[] cArr) {
        cArr[1] = 'u';
        cArr[4] = HEX_CHARS[i >> 4];
        cArr[5] = HEX_CHARS[i & 15];
        return 6;
    }

    private int _appendNamedEscape(int i, char[] cArr) {
        cArr[1] = (char) i;
        return 2;
    }

    private int _appendByteEscape(int i, int i2, ByteArrayBuilder byteArrayBuilder, int i3) {
        byteArrayBuilder.setCurrentSegmentLength(i3);
        byteArrayBuilder.append(92);
        if (i2 < 0) {
            byteArrayBuilder.append(117);
            if (i > 255) {
                int i4 = i >> 8;
                byteArrayBuilder.append(HEX_BYTES[i4 >> 4]);
                byteArrayBuilder.append(HEX_BYTES[i4 & 15]);
                i &= 255;
            } else {
                byteArrayBuilder.append(48);
                byteArrayBuilder.append(48);
            }
            byteArrayBuilder.append(HEX_BYTES[i >> 4]);
            byteArrayBuilder.append(HEX_BYTES[i & 15]);
        } else {
            byteArrayBuilder.append((byte) i2);
        }
        return byteArrayBuilder.getCurrentSegmentLength();
    }

    protected static int _convertSurrogate(int i, int i2) {
        if (i2 >= SURR2_FIRST && i2 <= SURR2_LAST) {
            return 65536 + ((i - SURR1_FIRST) << 10) + (i2 - SURR2_FIRST);
        }
        throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(i) + ", second 0x" + Integer.toHexString(i2) + "; illegal combination");
    }

    protected static void _illegalSurrogate(int i) {
        throw new IllegalArgumentException(UTF8Writer.illegalSurrogateDesc(i));
    }
}
