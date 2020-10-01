package com.shaded.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.TimeZone;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonFormat {
    public static final String DEFAULT_LOCALE = "##default";
    public static final String DEFAULT_TIMEZONE = "##default";

    String locale() default "##default";

    String pattern() default "";

    Shape shape() default Shape.ANY;

    String timezone() default "##default";

    public enum Shape {
        ANY,
        SCALAR,
        ARRAY,
        OBJECT,
        NUMBER,
        NUMBER_FLOAT,
        NUMBER_INT,
        STRING,
        BOOLEAN;

        public boolean isNumeric() {
            return this == NUMBER || this == NUMBER_INT || this == NUMBER_FLOAT;
        }

        public boolean isStructured() {
            return this == OBJECT || this == ARRAY;
        }
    }

    public static class Value {
        private final Locale locale;
        private final String pattern;
        private final Shape shape;
        private final TimeZone timezone;

        public Value() {
            this("", Shape.ANY, "", "");
        }

        public Value(JsonFormat jsonFormat) {
            this(jsonFormat.pattern(), jsonFormat.shape(), jsonFormat.locale(), jsonFormat.timezone());
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public Value(java.lang.String r4, com.shaded.fasterxml.jackson.annotation.JsonFormat.Shape r5, java.lang.String r6, java.lang.String r7) {
            /*
                r3 = this;
                r0 = 0
                if (r6 == 0) goto L_0x0011
                int r1 = r6.length()
                if (r1 == 0) goto L_0x0011
                java.lang.String r1 = "##default"
                boolean r1 = r1.equals(r6)
                if (r1 == 0) goto L_0x0026
            L_0x0011:
                r1 = r0
            L_0x0012:
                if (r7 == 0) goto L_0x0022
                int r2 = r7.length()
                if (r2 == 0) goto L_0x0022
                java.lang.String r2 = "##default"
                boolean r2 = r2.equals(r7)
                if (r2 == 0) goto L_0x002c
            L_0x0022:
                r3.<init>((java.lang.String) r4, (com.shaded.fasterxml.jackson.annotation.JsonFormat.Shape) r5, (java.util.Locale) r1, (java.util.TimeZone) r0)
                return
            L_0x0026:
                java.util.Locale r1 = new java.util.Locale
                r1.<init>(r6)
                goto L_0x0012
            L_0x002c:
                java.util.TimeZone r0 = java.util.TimeZone.getTimeZone(r7)
                goto L_0x0022
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.annotation.JsonFormat.Value.<init>(java.lang.String, com.shaded.fasterxml.jackson.annotation.JsonFormat$Shape, java.lang.String, java.lang.String):void");
        }

        public Value(String str, Shape shape2, Locale locale2, TimeZone timeZone) {
            this.pattern = str;
            this.shape = shape2;
            this.locale = locale2;
            this.timezone = timeZone;
        }

        public Value withPattern(String str) {
            return new Value(str, this.shape, this.locale, this.timezone);
        }

        public Value withShape(Shape shape2) {
            return new Value(this.pattern, shape2, this.locale, this.timezone);
        }

        public Value withLocale(Locale locale2) {
            return new Value(this.pattern, this.shape, locale2, this.timezone);
        }

        public Value withTimeZone(TimeZone timeZone) {
            return new Value(this.pattern, this.shape, this.locale, timeZone);
        }

        public String getPattern() {
            return this.pattern;
        }

        public Shape getShape() {
            return this.shape;
        }

        public Locale getLocale() {
            return this.locale;
        }

        public TimeZone getTimeZone() {
            return this.timezone;
        }
    }
}
