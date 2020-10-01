package com.shaded.fasterxml.jackson.databind.ser;

import com.shaded.fasterxml.jackson.annotation.JsonInclude;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.BeanDescription;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.SerializationConfig;
import com.shaded.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaded.fasterxml.jackson.databind.introspect.Annotated;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.util.Annotations;

public class PropertyBuilder {
    protected final AnnotationIntrospector _annotationIntrospector = this._config.getAnnotationIntrospector();
    protected final BeanDescription _beanDesc;
    protected final SerializationConfig _config;
    protected Object _defaultBean;
    protected final JsonInclude.Include _outputProps;

    public PropertyBuilder(SerializationConfig serializationConfig, BeanDescription beanDescription) {
        this._config = serializationConfig;
        this._beanDesc = beanDescription;
        this._outputProps = beanDescription.findSerializationInclusion(serializationConfig.getSerializationInclusion());
    }

    public Annotations getClassAnnotations() {
        return this._beanDesc.getClassAnnotations();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter buildWriter(com.shaded.fasterxml.jackson.databind.introspect.BeanPropertyDefinition r13, com.shaded.fasterxml.jackson.databind.JavaType r14, com.shaded.fasterxml.jackson.databind.JsonSerializer<?> r15, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r16, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r17, com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember r18, boolean r19) {
        /*
            r12 = this;
            r0 = r18
            r1 = r19
            com.shaded.fasterxml.jackson.databind.JavaType r2 = r12.findSerializationType(r0, r1, r14)
            if (r17 == 0) goto L_0x00cd
            if (r2 != 0) goto L_0x000d
            r2 = r14
        L_0x000d:
            com.shaded.fasterxml.jackson.databind.JavaType r3 = r2.getContentType()
            if (r3 != 0) goto L_0x0050
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Problem trying to create BeanPropertyWriter for property '"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = r13.getName()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "' (of type "
            java.lang.StringBuilder r4 = r4.append(r5)
            com.shaded.fasterxml.jackson.databind.BeanDescription r5 = r12._beanDesc
            com.shaded.fasterxml.jackson.databind.JavaType r5 = r5.getType()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "); serialization type "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.StringBuilder r2 = r4.append(r2)
            java.lang.String r4 = " has no content"
            java.lang.StringBuilder r2 = r2.append(r4)
            java.lang.String r2 = r2.toString()
            r3.<init>(r2)
            throw r3
        L_0x0050:
            r0 = r17
            com.shaded.fasterxml.jackson.databind.JavaType r9 = r2.withContentTypeHandler(r0)
            r9.getContentType()
        L_0x0059:
            r11 = 0
            r2 = 0
            com.shaded.fasterxml.jackson.databind.AnnotationIntrospector r3 = r12._annotationIntrospector
            com.shaded.fasterxml.jackson.annotation.JsonInclude$Include r4 = r12._outputProps
            r0 = r18
            com.shaded.fasterxml.jackson.annotation.JsonInclude$Include r3 = r3.findSerializationInclusion(r0, r4)
            if (r3 == 0) goto L_0x0072
            int[] r4 = com.shaded.fasterxml.jackson.databind.ser.PropertyBuilder.C08501.$SwitchMap$com$fasterxml$jackson$annotation$JsonInclude$Include
            int r3 = r3.ordinal()
            r3 = r4[r3]
            switch(r3) {
                case 1: goto L_0x0094;
                case 2: goto L_0x00b3;
                case 3: goto L_0x00b8;
                case 4: goto L_0x00b9;
                default: goto L_0x0072;
            }
        L_0x0072:
            r10 = r2
        L_0x0073:
            com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter r2 = new com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter
            com.shaded.fasterxml.jackson.databind.BeanDescription r3 = r12._beanDesc
            com.shaded.fasterxml.jackson.databind.util.Annotations r5 = r3.getClassAnnotations()
            r3 = r13
            r4 = r18
            r6 = r14
            r7 = r15
            r8 = r16
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11)
            com.shaded.fasterxml.jackson.databind.AnnotationIntrospector r3 = r12._annotationIntrospector
            r0 = r18
            com.shaded.fasterxml.jackson.databind.util.NameTransformer r3 = r3.findUnwrappingNameTransformer(r0)
            if (r3 == 0) goto L_0x0093
            com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter r2 = r2.unwrappingWriter(r3)
        L_0x0093:
            return r2
        L_0x0094:
            java.lang.String r3 = r13.getName()
            r0 = r18
            java.lang.Object r11 = r12.getDefaultValue(r3, r0)
            if (r11 != 0) goto L_0x00a3
            r2 = 1
            r10 = r2
            goto L_0x0073
        L_0x00a3:
            java.lang.Class r3 = r11.getClass()
            boolean r3 = r3.isArray()
            if (r3 == 0) goto L_0x0072
            java.lang.Object r11 = com.shaded.fasterxml.jackson.databind.util.ArrayBuilders.getArrayComparator(r11)
            r10 = r2
            goto L_0x0073
        L_0x00b3:
            r2 = 1
            java.lang.Object r11 = com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter.MARKER_FOR_EMPTY
            r10 = r2
            goto L_0x0073
        L_0x00b8:
            r2 = 1
        L_0x00b9:
            boolean r3 = r14.isContainerType()
            if (r3 == 0) goto L_0x0072
            com.shaded.fasterxml.jackson.databind.SerializationConfig r3 = r12._config
            com.shaded.fasterxml.jackson.databind.SerializationFeature r4 = com.shaded.fasterxml.jackson.databind.SerializationFeature.WRITE_EMPTY_JSON_ARRAYS
            boolean r3 = r3.isEnabled(r4)
            if (r3 != 0) goto L_0x0072
            java.lang.Object r11 = com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter.MARKER_FOR_EMPTY
            r10 = r2
            goto L_0x0073
        L_0x00cd:
            r9 = r2
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.PropertyBuilder.buildWriter(com.shaded.fasterxml.jackson.databind.introspect.BeanPropertyDefinition, com.shaded.fasterxml.jackson.databind.JavaType, com.shaded.fasterxml.jackson.databind.JsonSerializer, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer, com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember, boolean):com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter");
    }

    /* access modifiers changed from: protected */
    public JavaType findSerializationType(Annotated annotated, boolean z, JavaType javaType) {
        JavaType javaType2;
        boolean z2;
        JsonSerialize.Typing findSerializationTyping;
        boolean z3 = true;
        Class<?> findSerializationType = this._annotationIntrospector.findSerializationType(annotated);
        if (findSerializationType != null) {
            Class<?> rawClass = javaType.getRawClass();
            if (findSerializationType.isAssignableFrom(rawClass)) {
                javaType2 = javaType.widenBy(findSerializationType);
            } else if (!rawClass.isAssignableFrom(findSerializationType)) {
                throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + annotated.getName() + "': class " + findSerializationType.getName() + " not a super-type of (declared) class " + rawClass.getName());
            } else {
                javaType2 = this._config.constructSpecializedType(javaType, findSerializationType);
            }
            z = true;
        } else {
            javaType2 = javaType;
        }
        JavaType modifySecondaryTypesByAnnotation = BeanSerializerFactory.modifySecondaryTypesByAnnotation(this._config, annotated, javaType2);
        if (modifySecondaryTypesByAnnotation != javaType2) {
            javaType2 = modifySecondaryTypesByAnnotation;
            z2 = true;
        } else {
            z2 = z;
        }
        if (z2 || (findSerializationTyping = this._annotationIntrospector.findSerializationTyping(annotated)) == null) {
            z3 = z2;
        } else if (findSerializationTyping != JsonSerialize.Typing.STATIC) {
            z3 = false;
        }
        if (z3) {
            return javaType2;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Object getDefaultBean() {
        if (this._defaultBean == null) {
            this._defaultBean = this._beanDesc.instantiateBean(this._config.canOverrideAccessModifiers());
            if (this._defaultBean == null) {
                throw new IllegalArgumentException("Class " + this._beanDesc.getClassInfo().getAnnotated().getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
            }
        }
        return this._defaultBean;
    }

    /* access modifiers changed from: protected */
    public Object getDefaultValue(String str, AnnotatedMember annotatedMember) {
        Object defaultBean = getDefaultBean();
        try {
            return annotatedMember.getValue(defaultBean);
        } catch (Exception e) {
            return _throwWrapped(e, str, defaultBean);
        }
    }

    /* access modifiers changed from: protected */
    public Object _throwWrapped(Exception exc, String str, Object obj) {
        Throwable th = exc;
        while (th.getCause() != null) {
            th = th.getCause();
        }
        if (th instanceof Error) {
            throw ((Error) th);
        } else if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        } else {
            throw new IllegalArgumentException("Failed to get property '" + str + "' of default " + obj.getClass().getName() + " instance");
        }
    }
}
