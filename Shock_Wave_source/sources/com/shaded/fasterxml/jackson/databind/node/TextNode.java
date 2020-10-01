package com.shaded.fasterxml.jackson.databind.node;

import com.shaded.fasterxml.jackson.core.Base64Variant;
import com.shaded.fasterxml.jackson.core.Base64Variants;
import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.core.JsonLocation;
import com.shaded.fasterxml.jackson.core.JsonParseException;
import com.shaded.fasterxml.jackson.core.JsonProcessingException;
import com.shaded.fasterxml.jackson.core.JsonToken;
import com.shaded.fasterxml.jackson.core.p004io.CharTypes;
import com.shaded.fasterxml.jackson.core.p004io.NumberInput;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public final class TextNode extends ValueNode {
    static final TextNode EMPTY_STRING_NODE = new TextNode("");
    static final int INT_SPACE = 32;
    final String _value;

    public TextNode(String str) {
        this._value = str;
    }

    public static TextNode valueOf(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return EMPTY_STRING_NODE;
        }
        return new TextNode(str);
    }

    public JsonNodeType getNodeType() {
        return JsonNodeType.STRING;
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_STRING;
    }

    public String textValue() {
        return this._value;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002a, code lost:
        _reportInvalidBase64(r12, r0, 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
        if (r1 < r5) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
        _reportBase64EOF();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0032, code lost:
        r0 = r1 + 1;
        r1 = r4.charAt(r1);
        r7 = r12.decodeBase64Char(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003c, code lost:
        if (r7 >= 0) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003e, code lost:
        _reportInvalidBase64(r12, r1, 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0042, code lost:
        r1 = (r6 << 6) | r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0045, code lost:
        if (r0 < r5) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
        if (r12.usesPadding() != false) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004d, code lost:
        r3.append(r1 >> 4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0053, code lost:
        _reportBase64EOF();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
        r6 = r0 + 1;
        r0 = r4.charAt(r0);
        r7 = r12.decodeBase64Char(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        if (r7 >= 0) goto L_0x00a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0062, code lost:
        if (r7 == -2) goto L_0x0068;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0064, code lost:
        _reportInvalidBase64(r12, r0, 2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0068, code lost:
        if (r6 < r5) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006a, code lost:
        _reportBase64EOF();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006d, code lost:
        r0 = r6 + 1;
        r6 = r4.charAt(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0077, code lost:
        if (r12.usesPaddingChar(r6) != false) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        _reportInvalidBase64(r12, r6, 3, "expected padding character '" + r12.getPaddingChar() + "'");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0099, code lost:
        r3.append(r1 >> 4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00a0, code lost:
        r1 = (r1 << 6) | r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a4, code lost:
        if (r6 < r5) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00aa, code lost:
        if (r12.usesPadding() != false) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00ac, code lost:
        r3.appendTwoBytes(r1 >> 2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00b3, code lost:
        _reportBase64EOF();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b6, code lost:
        r0 = r6 + 1;
        r6 = r4.charAt(r6);
        r7 = r12.decodeBase64Char(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c0, code lost:
        if (r7 >= 0) goto L_0x00ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c2, code lost:
        if (r7 == -2) goto L_0x00c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c4, code lost:
        _reportInvalidBase64(r12, r6, 3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00c7, code lost:
        r3.appendTwoBytes(r1 >> 2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ce, code lost:
        r3.appendThreeBytes((r1 << 6) | r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0024, code lost:
        r6 = r12.decodeBase64Char(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0028, code lost:
        if (r6 >= 0) goto L_0x002d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getBinaryValue(com.shaded.fasterxml.jackson.core.Base64Variant r12) throws java.io.IOException {
        /*
            r11 = this;
            r10 = 3
            r2 = 0
            r9 = -2
            com.shaded.fasterxml.jackson.core.util.ByteArrayBuilder r3 = new com.shaded.fasterxml.jackson.core.util.ByteArrayBuilder
            r0 = 100
            r3.<init>((int) r0)
            java.lang.String r4 = r11._value
            int r5 = r4.length()
            r0 = r2
        L_0x0011:
            if (r0 >= r5) goto L_0x001b
        L_0x0013:
            int r1 = r0 + 1
            char r0 = r4.charAt(r0)
            if (r1 < r5) goto L_0x0020
        L_0x001b:
            byte[] r0 = r3.toByteArray()
            return r0
        L_0x0020:
            r6 = 32
            if (r0 <= r6) goto L_0x00d6
            int r6 = r12.decodeBase64Char((char) r0)
            if (r6 >= 0) goto L_0x002d
            r11._reportInvalidBase64(r12, r0, r2)
        L_0x002d:
            if (r1 < r5) goto L_0x0032
            r11._reportBase64EOF()
        L_0x0032:
            int r0 = r1 + 1
            char r1 = r4.charAt(r1)
            int r7 = r12.decodeBase64Char((char) r1)
            if (r7 >= 0) goto L_0x0042
            r8 = 1
            r11._reportInvalidBase64(r12, r1, r8)
        L_0x0042:
            int r1 = r6 << 6
            r1 = r1 | r7
            if (r0 < r5) goto L_0x0056
            boolean r6 = r12.usesPadding()
            if (r6 != 0) goto L_0x0053
            int r0 = r1 >> 4
            r3.append(r0)
            goto L_0x001b
        L_0x0053:
            r11._reportBase64EOF()
        L_0x0056:
            int r6 = r0 + 1
            char r0 = r4.charAt(r0)
            int r7 = r12.decodeBase64Char((char) r0)
            if (r7 >= 0) goto L_0x00a0
            if (r7 == r9) goto L_0x0068
            r7 = 2
            r11._reportInvalidBase64(r12, r0, r7)
        L_0x0068:
            if (r6 < r5) goto L_0x006d
            r11._reportBase64EOF()
        L_0x006d:
            int r0 = r6 + 1
            char r6 = r4.charAt(r6)
            boolean r7 = r12.usesPaddingChar((char) r6)
            if (r7 != 0) goto L_0x0099
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "expected padding character '"
            java.lang.StringBuilder r7 = r7.append(r8)
            char r8 = r12.getPaddingChar()
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r8 = "'"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            r11._reportInvalidBase64(r12, r6, r10, r7)
        L_0x0099:
            int r1 = r1 >> 4
            r3.append(r1)
            goto L_0x0011
        L_0x00a0:
            int r0 = r1 << 6
            r1 = r0 | r7
            if (r6 < r5) goto L_0x00b6
            boolean r0 = r12.usesPadding()
            if (r0 != 0) goto L_0x00b3
            int r0 = r1 >> 2
            r3.appendTwoBytes(r0)
            goto L_0x001b
        L_0x00b3:
            r11._reportBase64EOF()
        L_0x00b6:
            int r0 = r6 + 1
            char r6 = r4.charAt(r6)
            int r7 = r12.decodeBase64Char((char) r6)
            if (r7 >= 0) goto L_0x00ce
            if (r7 == r9) goto L_0x00c7
            r11._reportInvalidBase64(r12, r6, r10)
        L_0x00c7:
            int r1 = r1 >> 2
            r3.appendTwoBytes(r1)
            goto L_0x0011
        L_0x00ce:
            int r1 = r1 << 6
            r1 = r1 | r7
            r3.appendThreeBytes(r1)
            goto L_0x0011
        L_0x00d6:
            r0 = r1
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.node.TextNode.getBinaryValue(com.shaded.fasterxml.jackson.core.Base64Variant):byte[]");
    }

    public byte[] binaryValue() throws IOException {
        return getBinaryValue(Base64Variants.getDefaultVariant());
    }

    public String asText() {
        return this._value;
    }

    public boolean asBoolean(boolean z) {
        if (this._value == null || !"true".equals(this._value.trim())) {
            return z;
        }
        return true;
    }

    public int asInt(int i) {
        return NumberInput.parseAsInt(this._value, i);
    }

    public long asLong(long j) {
        return NumberInput.parseAsLong(this._value, j);
    }

    public double asDouble(double d) {
        return NumberInput.parseAsDouble(this._value, d);
    }

    public final void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (this._value == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(this._value);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return ((TextNode) obj)._value.equals(this._value);
    }

    public int hashCode() {
        return this._value.hashCode();
    }

    public String toString() {
        int length = this._value.length();
        StringBuilder sb = new StringBuilder((length >> 4) + length + 2);
        appendQuoted(sb, this._value);
        return sb.toString();
    }

    protected static void appendQuoted(StringBuilder sb, String str) {
        sb.append('\"');
        CharTypes.appendQuoted(sb, str);
        sb.append('\"');
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidBase64(Base64Variant base64Variant, char c, int i) throws JsonParseException {
        _reportInvalidBase64(base64Variant, c, i, (String) null);
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidBase64(Base64Variant base64Variant, char c, int i, String str) throws JsonParseException {
        String str2;
        if (c <= ' ') {
            str2 = "Illegal white space character (code 0x" + Integer.toHexString(c) + ") as character #" + (i + 1) + " of 4-char base64 unit: can only used between units";
        } else if (base64Variant.usesPaddingChar(c)) {
            str2 = "Unexpected padding character ('" + base64Variant.getPaddingChar() + "') as character #" + (i + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        } else if (!Character.isDefined(c) || Character.isISOControl(c)) {
            str2 = "Illegal character (code 0x" + Integer.toHexString(c) + ") in base64 content";
        } else {
            str2 = "Illegal character '" + c + "' (code 0x" + Integer.toHexString(c) + ") in base64 content";
        }
        if (str != null) {
            str2 = str2 + ": " + str;
        }
        throw new JsonParseException(str2, JsonLocation.f270NA);
    }

    /* access modifiers changed from: protected */
    public void _reportBase64EOF() throws JsonParseException {
        throw new JsonParseException("Unexpected end-of-String when base64 content", JsonLocation.f270NA);
    }
}
