package com.shaded.fasterxml.jackson.databind.annotation;

import com.shaded.fasterxml.jackson.annotation.JacksonAnnotation;
import com.shaded.fasterxml.jackson.databind.JsonDeserializer;
import com.shaded.fasterxml.jackson.databind.KeyDeserializer;
import com.shaded.fasterxml.jackson.databind.util.Converter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonDeserialize {
    /* renamed from: as */
    Class<?> mo14862as() default NoClass.class;

    Class<?> builder() default NoClass.class;

    Class<?> contentAs() default NoClass.class;

    Class<? extends Converter<?, ?>> contentConverter() default Converter.None.class;

    Class<? extends JsonDeserializer<?>> contentUsing() default JsonDeserializer.None.class;

    Class<? extends Converter<?, ?>> converter() default Converter.None.class;

    Class<?> keyAs() default NoClass.class;

    Class<? extends KeyDeserializer> keyUsing() default KeyDeserializer.None.class;

    Class<? extends JsonDeserializer<?>> using() default JsonDeserializer.None.class;
}
