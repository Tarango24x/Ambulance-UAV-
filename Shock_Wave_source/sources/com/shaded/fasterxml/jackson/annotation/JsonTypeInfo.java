package com.shaded.fasterxml.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonTypeInfo {

    /* renamed from: com.shaded.fasterxml.jackson.annotation.JsonTypeInfo$As */
    public enum C0806As {
        PROPERTY,
        WRAPPER_OBJECT,
        WRAPPER_ARRAY,
        EXTERNAL_PROPERTY
    }

    public static abstract class None {
    }

    Class<?> defaultImpl() default None.class;

    C0806As include() default C0806As.PROPERTY;

    String property() default "";

    C0807Id use();

    boolean visible() default false;

    /* renamed from: com.shaded.fasterxml.jackson.annotation.JsonTypeInfo$Id */
    public enum C0807Id {
        NONE((String) null),
        CLASS("@class"),
        MINIMAL_CLASS("@c"),
        NAME("@type"),
        CUSTOM((String) null);
        
        private final String _defaultPropertyName;

        private C0807Id(String str) {
            this._defaultPropertyName = str;
        }

        public String getDefaultPropertyName() {
            return this._defaultPropertyName;
        }
    }
}
