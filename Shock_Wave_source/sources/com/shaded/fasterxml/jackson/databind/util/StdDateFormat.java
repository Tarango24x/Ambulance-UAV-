package com.shaded.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormat extends DateFormat {
    protected static final String[] ALL_FORMATS = {DATE_FORMAT_STR_ISO8601, DATE_FORMAT_STR_ISO8601_Z, "EEE, dd MMM yyyy HH:mm:ss zzz", DATE_FORMAT_STR_PLAIN};
    protected static final DateFormat DATE_FORMAT_ISO8601 = new SimpleDateFormat(DATE_FORMAT_STR_ISO8601);
    protected static final DateFormat DATE_FORMAT_ISO8601_Z = new SimpleDateFormat(DATE_FORMAT_STR_ISO8601_Z);
    protected static final DateFormat DATE_FORMAT_PLAIN = new SimpleDateFormat(DATE_FORMAT_STR_PLAIN);
    protected static final DateFormat DATE_FORMAT_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    protected static final String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    protected static final String DATE_FORMAT_STR_ISO8601_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    protected static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";
    protected static final String DATE_FORMAT_STR_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT");
    public static final StdDateFormat instance = new StdDateFormat();
    protected transient DateFormat _formatISO8601;
    protected transient DateFormat _formatISO8601_z;
    protected transient DateFormat _formatPlain;
    protected transient DateFormat _formatRFC1123;
    protected transient TimeZone _timezone;

    static {
        DATE_FORMAT_RFC1123.setTimeZone(DEFAULT_TIMEZONE);
        DATE_FORMAT_ISO8601.setTimeZone(DEFAULT_TIMEZONE);
        DATE_FORMAT_ISO8601_Z.setTimeZone(DEFAULT_TIMEZONE);
        DATE_FORMAT_PLAIN.setTimeZone(DEFAULT_TIMEZONE);
    }

    public StdDateFormat() {
    }

    public StdDateFormat(TimeZone timeZone) {
        this._timezone = timeZone;
    }

    public static TimeZone getDefaultTimeZone() {
        return DEFAULT_TIMEZONE;
    }

    public StdDateFormat withTimeZone(TimeZone timeZone) {
        if (timeZone == null) {
            timeZone = DEFAULT_TIMEZONE;
        }
        return new StdDateFormat(timeZone);
    }

    public StdDateFormat clone() {
        return new StdDateFormat();
    }

    public static DateFormat getBlueprintISO8601Format() {
        return DATE_FORMAT_ISO8601;
    }

    public static DateFormat getISO8601Format(TimeZone timeZone) {
        return _cloneFormat(DATE_FORMAT_ISO8601, timeZone);
    }

    public static DateFormat getBlueprintRFC1123Format() {
        return DATE_FORMAT_RFC1123;
    }

    public static DateFormat getRFC1123Format(TimeZone timeZone) {
        return _cloneFormat(DATE_FORMAT_RFC1123, timeZone);
    }

    public void setTimeZone(TimeZone timeZone) {
        if (timeZone != this._timezone) {
            this._formatRFC1123 = null;
            this._formatISO8601 = null;
            this._formatISO8601_z = null;
            this._formatPlain = null;
            this._timezone = timeZone;
        }
    }

    public Date parse(String str) throws ParseException {
        String trim = str.trim();
        ParsePosition parsePosition = new ParsePosition(0);
        Date parse = parse(trim, parsePosition);
        if (parse != null) {
            return parse;
        }
        StringBuilder sb = new StringBuilder();
        for (String str2 : ALL_FORMATS) {
            if (sb.length() > 0) {
                sb.append("\", \"");
            } else {
                sb.append('\"');
            }
            sb.append(str2);
        }
        sb.append('\"');
        throw new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)", new Object[]{trim, sb.toString()}), parsePosition.getErrorIndex());
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0032  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Date parse(java.lang.String r5, java.text.ParsePosition r6) {
        /*
            r4 = this;
            boolean r0 = r4.looksLikeISO8601(r5)
            if (r0 == 0) goto L_0x000b
            java.util.Date r0 = r4.parseAsISO8601(r5, r6)
        L_0x000a:
            return r0
        L_0x000b:
            int r0 = r5.length()
        L_0x000f:
            int r0 = r0 + -1
            if (r0 < 0) goto L_0x001f
            char r1 = r5.charAt(r0)
            r2 = 48
            if (r1 < r2) goto L_0x001f
            r2 = 57
            if (r1 <= r2) goto L_0x000f
        L_0x001f:
            if (r0 >= 0) goto L_0x0032
            r0 = 0
            boolean r0 = com.shaded.fasterxml.jackson.core.p004io.NumberInput.inLongRange(r5, r0)
            if (r0 == 0) goto L_0x0032
            java.util.Date r0 = new java.util.Date
            long r2 = java.lang.Long.parseLong(r5)
            r0.<init>(r2)
            goto L_0x000a
        L_0x0032:
            java.util.Date r0 = r4.parseAsRFC1123(r5, r6)
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.util.StdDateFormat.parse(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if (this._formatISO8601 == null) {
            this._formatISO8601 = _cloneFormat(DATE_FORMAT_ISO8601);
        }
        return this._formatISO8601.format(date, stringBuffer, fieldPosition);
    }

    /* access modifiers changed from: protected */
    public boolean looksLikeISO8601(String str) {
        if (str.length() < 5 || !Character.isDigit(str.charAt(0)) || !Character.isDigit(str.charAt(3)) || str.charAt(4) != '-') {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public Date parseAsISO8601(String str, ParsePosition parsePosition) {
        DateFormat dateFormat;
        int length = str.length();
        char charAt = str.charAt(length - 1);
        if (length <= 10 && Character.isDigit(charAt)) {
            dateFormat = this._formatPlain;
            if (dateFormat == null) {
                dateFormat = _cloneFormat(DATE_FORMAT_PLAIN);
                this._formatPlain = dateFormat;
            }
        } else if (charAt == 'Z') {
            dateFormat = this._formatISO8601_z;
            if (dateFormat == null) {
                dateFormat = _cloneFormat(DATE_FORMAT_ISO8601_Z);
                this._formatISO8601_z = dateFormat;
            }
            if (str.charAt(length - 4) == ':') {
                StringBuilder sb = new StringBuilder(str);
                sb.insert(length - 1, ".000");
                str = sb.toString();
            }
        } else if (hasTimeZone(str)) {
            char charAt2 = str.charAt(length - 3);
            if (charAt2 == ':') {
                StringBuilder sb2 = new StringBuilder(str);
                sb2.delete(length - 3, length - 2);
                str = sb2.toString();
            } else if (charAt2 == '+' || charAt2 == '-') {
                str = str + "00";
            }
            int length2 = str.length();
            if (Character.isDigit(str.charAt(length2 - 9))) {
                StringBuilder sb3 = new StringBuilder(str);
                sb3.insert(length2 - 5, ".000");
                str = sb3.toString();
            }
            dateFormat = this._formatISO8601;
            if (this._formatISO8601 == null) {
                dateFormat = _cloneFormat(DATE_FORMAT_ISO8601);
                this._formatISO8601 = dateFormat;
            }
        } else {
            StringBuilder sb4 = new StringBuilder(str);
            if ((length - str.lastIndexOf(84)) - 1 <= 8) {
                sb4.append(".000");
            }
            sb4.append('Z');
            str = sb4.toString();
            dateFormat = this._formatISO8601_z;
            if (dateFormat == null) {
                dateFormat = _cloneFormat(DATE_FORMAT_ISO8601_Z);
                this._formatISO8601_z = dateFormat;
            }
        }
        return dateFormat.parse(str, parsePosition);
    }

    /* access modifiers changed from: protected */
    public Date parseAsRFC1123(String str, ParsePosition parsePosition) {
        if (this._formatRFC1123 == null) {
            this._formatRFC1123 = _cloneFormat(DATE_FORMAT_RFC1123);
        }
        return this._formatRFC1123.parse(str, parsePosition);
    }

    private static final boolean hasTimeZone(String str) {
        char charAt;
        char charAt2;
        char charAt3;
        int length = str.length();
        if (length < 6 || ((charAt = str.charAt(length - 6)) != '+' && charAt != '-' && (charAt2 = str.charAt(length - 5)) != '+' && charAt2 != '-' && (charAt3 = str.charAt(length - 3)) != '+' && charAt3 != '-')) {
            return false;
        }
        return true;
    }

    private final DateFormat _cloneFormat(DateFormat dateFormat) {
        return _cloneFormat(dateFormat, this._timezone);
    }

    private static final DateFormat _cloneFormat(DateFormat dateFormat, TimeZone timeZone) {
        DateFormat dateFormat2 = (DateFormat) dateFormat.clone();
        if (timeZone != null) {
            dateFormat2.setTimeZone(timeZone);
        }
        return dateFormat2;
    }
}
