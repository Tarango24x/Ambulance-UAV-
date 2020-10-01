package com.shaded.fasterxml.jackson.databind.introspect;

import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.DeserializationConfig;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.SerializationConfig;
import com.shaded.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.shaded.fasterxml.jackson.databind.cfg.MapperConfig;
import com.shaded.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.shaded.fasterxml.jackson.databind.type.SimpleType;
import java.io.Serializable;

public class BasicClassIntrospector extends ClassIntrospector implements Serializable {
    protected static final BasicBeanDescription BOOLEAN_DESC = BasicBeanDescription.forOtherUse((MapperConfig<?>) null, SimpleType.constructUnsafe(Boolean.TYPE), AnnotatedClass.constructWithoutSuperTypes(Boolean.TYPE, (AnnotationIntrospector) null, (ClassIntrospector.MixInResolver) null));
    protected static final BasicBeanDescription INT_DESC = BasicBeanDescription.forOtherUse((MapperConfig<?>) null, SimpleType.constructUnsafe(Integer.TYPE), AnnotatedClass.constructWithoutSuperTypes(Integer.TYPE, (AnnotationIntrospector) null, (ClassIntrospector.MixInResolver) null));
    protected static final BasicBeanDescription LONG_DESC = BasicBeanDescription.forOtherUse((MapperConfig<?>) null, SimpleType.constructUnsafe(Long.TYPE), AnnotatedClass.constructWithoutSuperTypes(Long.TYPE, (AnnotationIntrospector) null, (ClassIntrospector.MixInResolver) null));
    protected static final BasicBeanDescription STRING_DESC = BasicBeanDescription.forOtherUse((MapperConfig<?>) null, SimpleType.constructUnsafe(String.class), AnnotatedClass.constructWithoutSuperTypes(String.class, (AnnotationIntrospector) null, (ClassIntrospector.MixInResolver) null));
    public static final BasicClassIntrospector instance = new BasicClassIntrospector();
    private static final long serialVersionUID = 1;

    public BasicBeanDescription forSerialization(SerializationConfig serializationConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver) {
        BasicBeanDescription _findCachedDesc = _findCachedDesc(javaType);
        if (_findCachedDesc == null) {
            return BasicBeanDescription.forSerialization(collectProperties(serializationConfig, javaType, mixInResolver, true, "set"));
        }
        return _findCachedDesc;
    }

    public BasicBeanDescription forDeserialization(DeserializationConfig deserializationConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver) {
        BasicBeanDescription _findCachedDesc = _findCachedDesc(javaType);
        if (_findCachedDesc == null) {
            return BasicBeanDescription.forDeserialization(collectProperties(deserializationConfig, javaType, mixInResolver, false, "set"));
        }
        return _findCachedDesc;
    }

    public BasicBeanDescription forDeserializationWithBuilder(DeserializationConfig deserializationConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver) {
        return BasicBeanDescription.forDeserialization(collectPropertiesWithBuilder(deserializationConfig, javaType, mixInResolver, false));
    }

    public BasicBeanDescription forCreation(DeserializationConfig deserializationConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver) {
        BasicBeanDescription _findCachedDesc = _findCachedDesc(javaType);
        if (_findCachedDesc == null) {
            return BasicBeanDescription.forDeserialization(collectProperties(deserializationConfig, javaType, mixInResolver, false, "set"));
        }
        return _findCachedDesc;
    }

    public BasicBeanDescription forClassAnnotations(MapperConfig<?> mapperConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver) {
        return BasicBeanDescription.forOtherUse(mapperConfig, javaType, AnnotatedClass.construct(javaType.getRawClass(), mapperConfig.isAnnotationProcessingEnabled() ? mapperConfig.getAnnotationIntrospector() : null, mixInResolver));
    }

    public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> mapperConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver) {
        boolean isAnnotationProcessingEnabled = mapperConfig.isAnnotationProcessingEnabled();
        AnnotationIntrospector annotationIntrospector = mapperConfig.getAnnotationIntrospector();
        Class<?> rawClass = javaType.getRawClass();
        if (!isAnnotationProcessingEnabled) {
            annotationIntrospector = null;
        }
        return BasicBeanDescription.forOtherUse(mapperConfig, javaType, AnnotatedClass.constructWithoutSuperTypes(rawClass, annotationIntrospector, mixInResolver));
    }

    /* access modifiers changed from: protected */
    public POJOPropertiesCollector collectProperties(MapperConfig<?> mapperConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver, boolean z, String str) {
        return constructPropertyCollector(mapperConfig, AnnotatedClass.construct(javaType.getRawClass(), mapperConfig.isAnnotationProcessingEnabled() ? mapperConfig.getAnnotationIntrospector() : null, mixInResolver), javaType, z, str).collect();
    }

    /* access modifiers changed from: protected */
    public POJOPropertiesCollector collectPropertiesWithBuilder(MapperConfig<?> mapperConfig, JavaType javaType, ClassIntrospector.MixInResolver mixInResolver, boolean z) {
        AnnotationIntrospector annotationIntrospector;
        JsonPOJOBuilder.Value value = null;
        if (mapperConfig.isAnnotationProcessingEnabled()) {
            annotationIntrospector = mapperConfig.getAnnotationIntrospector();
        } else {
            annotationIntrospector = null;
        }
        AnnotatedClass construct = AnnotatedClass.construct(javaType.getRawClass(), annotationIntrospector, mixInResolver);
        if (annotationIntrospector != null) {
            value = annotationIntrospector.findPOJOBuilderConfig(construct);
        }
        return constructPropertyCollector(mapperConfig, construct, javaType, z, value == null ? "with" : value.withPrefix).collect();
    }

    /* access modifiers changed from: protected */
    public POJOPropertiesCollector constructPropertyCollector(MapperConfig<?> mapperConfig, AnnotatedClass annotatedClass, JavaType javaType, boolean z, String str) {
        return new POJOPropertiesCollector(mapperConfig, z, javaType, annotatedClass, str);
    }

    /* access modifiers changed from: protected */
    public BasicBeanDescription _findCachedDesc(JavaType javaType) {
        Class<?> rawClass = javaType.getRawClass();
        if (rawClass == String.class) {
            return STRING_DESC;
        }
        if (rawClass == Boolean.TYPE) {
            return BOOLEAN_DESC;
        }
        if (rawClass == Integer.TYPE) {
            return INT_DESC;
        }
        if (rawClass == Long.TYPE) {
            return LONG_DESC;
        }
        return null;
    }
}
