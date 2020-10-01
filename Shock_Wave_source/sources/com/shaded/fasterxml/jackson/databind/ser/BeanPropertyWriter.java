package com.shaded.fasterxml.jackson.databind.ser;

import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.core.p004io.SerializedString;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonNode;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.PropertyName;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.shaded.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.shaded.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.shaded.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.shaded.fasterxml.jackson.databind.node.ObjectNode;
import com.shaded.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.shaded.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.shaded.fasterxml.jackson.databind.util.Annotations;
import com.shaded.fasterxml.jackson.databind.util.NameTransformer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

public class BeanPropertyWriter implements BeanProperty {
    public static final Object MARKER_FOR_EMPTY = new Object();
    protected final Method _accessorMethod;
    protected final JavaType _cfgSerializationType;
    protected final Annotations _contextAnnotations;
    protected final JavaType _declaredType;
    protected PropertySerializerMap _dynamicSerializers;
    protected final Field _field;
    protected final Class<?>[] _includeInViews;
    protected HashMap<Object, Object> _internalSettings;
    protected final boolean _isRequired;
    protected final AnnotatedMember _member;
    protected final SerializedString _name;
    protected JavaType _nonTrivialBaseType;
    protected JsonSerializer<Object> _nullSerializer;
    protected JsonSerializer<Object> _serializer;
    protected final boolean _suppressNulls;
    protected final Object _suppressableValue;
    protected TypeSerializer _typeSerializer;
    protected final PropertyName _wrapperName;

    public BeanPropertyWriter(BeanPropertyDefinition beanPropertyDefinition, AnnotatedMember annotatedMember, Annotations annotations, JavaType javaType, JsonSerializer<?> jsonSerializer, TypeSerializer typeSerializer, JavaType javaType2, boolean z, Object obj) {
        this._member = annotatedMember;
        this._contextAnnotations = annotations;
        this._name = new SerializedString(beanPropertyDefinition.getName());
        this._wrapperName = beanPropertyDefinition.getWrapperName();
        this._declaredType = javaType;
        this._serializer = jsonSerializer;
        this._dynamicSerializers = jsonSerializer == null ? PropertySerializerMap.emptyMap() : null;
        this._typeSerializer = typeSerializer;
        this._cfgSerializationType = javaType2;
        this._isRequired = beanPropertyDefinition.isRequired();
        if (annotatedMember instanceof AnnotatedField) {
            this._accessorMethod = null;
            this._field = (Field) annotatedMember.getMember();
        } else if (annotatedMember instanceof AnnotatedMethod) {
            this._accessorMethod = (Method) annotatedMember.getMember();
            this._field = null;
        } else {
            throw new IllegalArgumentException("Can not pass member of type " + annotatedMember.getClass().getName());
        }
        this._suppressNulls = z;
        this._suppressableValue = obj;
        this._includeInViews = beanPropertyDefinition.findViews();
        this._nullSerializer = null;
    }

    protected BeanPropertyWriter(BeanPropertyWriter beanPropertyWriter) {
        this(beanPropertyWriter, beanPropertyWriter._name);
    }

    protected BeanPropertyWriter(BeanPropertyWriter beanPropertyWriter, SerializedString serializedString) {
        this._name = serializedString;
        this._wrapperName = beanPropertyWriter._wrapperName;
        this._member = beanPropertyWriter._member;
        this._contextAnnotations = beanPropertyWriter._contextAnnotations;
        this._declaredType = beanPropertyWriter._declaredType;
        this._accessorMethod = beanPropertyWriter._accessorMethod;
        this._field = beanPropertyWriter._field;
        this._serializer = beanPropertyWriter._serializer;
        this._nullSerializer = beanPropertyWriter._nullSerializer;
        if (beanPropertyWriter._internalSettings != null) {
            this._internalSettings = new HashMap<>(beanPropertyWriter._internalSettings);
        }
        this._cfgSerializationType = beanPropertyWriter._cfgSerializationType;
        this._dynamicSerializers = beanPropertyWriter._dynamicSerializers;
        this._suppressNulls = beanPropertyWriter._suppressNulls;
        this._suppressableValue = beanPropertyWriter._suppressableValue;
        this._includeInViews = beanPropertyWriter._includeInViews;
        this._typeSerializer = beanPropertyWriter._typeSerializer;
        this._nonTrivialBaseType = beanPropertyWriter._nonTrivialBaseType;
        this._isRequired = beanPropertyWriter._isRequired;
    }

    public BeanPropertyWriter rename(NameTransformer nameTransformer) {
        String transform = nameTransformer.transform(this._name.getValue());
        return transform.equals(this._name.toString()) ? this : new BeanPropertyWriter(this, new SerializedString(transform));
    }

    public void assignSerializer(JsonSerializer<Object> jsonSerializer) {
        if (this._serializer == null || this._serializer == jsonSerializer) {
            this._serializer = jsonSerializer;
            return;
        }
        throw new IllegalStateException("Can not override serializer");
    }

    public void assignNullSerializer(JsonSerializer<Object> jsonSerializer) {
        if (this._nullSerializer == null || this._nullSerializer == jsonSerializer) {
            this._nullSerializer = jsonSerializer;
            return;
        }
        throw new IllegalStateException("Can not override null serializer");
    }

    public BeanPropertyWriter unwrappingWriter(NameTransformer nameTransformer) {
        return new UnwrappingBeanPropertyWriter(this, nameTransformer);
    }

    public void setNonTrivialBaseType(JavaType javaType) {
        this._nonTrivialBaseType = javaType;
    }

    public String getName() {
        return this._name.getValue();
    }

    public JavaType getType() {
        return this._declaredType;
    }

    public PropertyName getWrapperName() {
        return this._wrapperName;
    }

    public boolean isRequired() {
        return this._isRequired;
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        return this._member.getAnnotation(cls);
    }

    public <A extends Annotation> A getContextAnnotation(Class<A> cls) {
        return this._contextAnnotations.get(cls);
    }

    public AnnotatedMember getMember() {
        return this._member;
    }

    public void depositSchemaProperty(JsonObjectFormatVisitor jsonObjectFormatVisitor) throws JsonMappingException {
        if (jsonObjectFormatVisitor == null) {
            return;
        }
        if (isRequired()) {
            jsonObjectFormatVisitor.property((BeanProperty) this);
        } else {
            jsonObjectFormatVisitor.optionalProperty((BeanProperty) this);
        }
    }

    public Object getInternalSetting(Object obj) {
        if (this._internalSettings == null) {
            return null;
        }
        return this._internalSettings.get(obj);
    }

    public Object setInternalSetting(Object obj, Object obj2) {
        if (this._internalSettings == null) {
            this._internalSettings = new HashMap<>();
        }
        return this._internalSettings.put(obj, obj2);
    }

    public Object removeInternalSetting(Object obj) {
        if (this._internalSettings == null) {
            return null;
        }
        Object remove = this._internalSettings.remove(obj);
        if (this._internalSettings.size() != 0) {
            return remove;
        }
        this._internalSettings = null;
        return remove;
    }

    public SerializedString getSerializedName() {
        return this._name;
    }

    public boolean hasSerializer() {
        return this._serializer != null;
    }

    public boolean hasNullSerializer() {
        return this._nullSerializer != null;
    }

    public boolean willSuppressNulls() {
        return this._suppressNulls;
    }

    public JsonSerializer<Object> getSerializer() {
        return this._serializer;
    }

    public JavaType getSerializationType() {
        return this._cfgSerializationType;
    }

    public Class<?> getRawSerializationType() {
        if (this._cfgSerializationType == null) {
            return null;
        }
        return this._cfgSerializationType.getRawClass();
    }

    public Class<?> getPropertyType() {
        if (this._accessorMethod != null) {
            return this._accessorMethod.getReturnType();
        }
        return this._field.getType();
    }

    public Type getGenericPropertyType() {
        if (this._accessorMethod != null) {
            return this._accessorMethod.getGenericReturnType();
        }
        return this._field.getGenericType();
    }

    public Class<?>[] getViews() {
        return this._includeInViews;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public boolean isRequired(AnnotationIntrospector annotationIntrospector) {
        return this._isRequired;
    }

    public void depositSchemaProperty(ObjectNode objectNode, SerializerProvider serializerProvider) throws JsonMappingException {
        JsonNode defaultSchemaNode;
        JavaType serializationType = getSerializationType();
        Type genericPropertyType = serializationType == null ? getGenericPropertyType() : serializationType.getRawClass();
        JsonSerializer<Object> serializer = getSerializer();
        if (serializer == null) {
            Class<?> rawSerializationType = getRawSerializationType();
            if (rawSerializationType == null) {
                rawSerializationType = getPropertyType();
            }
            serializer = serializerProvider.findValueSerializer(rawSerializationType, (BeanProperty) this);
        }
        boolean z = !isRequired();
        if (serializer instanceof SchemaAware) {
            defaultSchemaNode = ((SchemaAware) serializer).getSchema(serializerProvider, genericPropertyType, z);
        } else {
            defaultSchemaNode = JsonSchema.getDefaultSchemaNode();
        }
        objectNode.put(getName(), defaultSchemaNode);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001a, code lost:
        r2 = r1.getClass();
        r3 = r4._dynamicSerializers;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void serializeAsField(java.lang.Object r5, com.shaded.fasterxml.jackson.core.JsonGenerator r6, com.shaded.fasterxml.jackson.databind.SerializerProvider r7) throws java.lang.Exception {
        /*
            r4 = this;
            java.lang.Object r1 = r4.get(r5)
            if (r1 != 0) goto L_0x0016
            com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r0 = r4._nullSerializer
            if (r0 == 0) goto L_0x0015
            com.shaded.fasterxml.jackson.core.io.SerializedString r0 = r4._name
            r6.writeFieldName((com.shaded.fasterxml.jackson.core.SerializableString) r0)
            com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r0 = r4._nullSerializer
            r1 = 0
            r0.serialize(r1, r6, r7)
        L_0x0015:
            return
        L_0x0016:
            com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r0 = r4._serializer
            if (r0 != 0) goto L_0x002a
            java.lang.Class r2 = r1.getClass()
            com.shaded.fasterxml.jackson.databind.ser.impl.PropertySerializerMap r3 = r4._dynamicSerializers
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r3.serializerFor(r2)
            if (r0 != 0) goto L_0x002a
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4._findAndAddDynamic(r3, r2, r7)
        L_0x002a:
            java.lang.Object r2 = r4._suppressableValue
            if (r2 == 0) goto L_0x003a
            java.lang.Object r2 = MARKER_FOR_EMPTY
            java.lang.Object r3 = r4._suppressableValue
            if (r2 != r3) goto L_0x004c
            boolean r2 = r0.isEmpty(r1)
            if (r2 != 0) goto L_0x0015
        L_0x003a:
            if (r1 != r5) goto L_0x003f
            r4._handleSelfReference(r5, r0)
        L_0x003f:
            com.shaded.fasterxml.jackson.core.io.SerializedString r2 = r4._name
            r6.writeFieldName((com.shaded.fasterxml.jackson.core.SerializableString) r2)
            com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r2 = r4._typeSerializer
            if (r2 != 0) goto L_0x0055
            r0.serialize(r1, r6, r7)
            goto L_0x0015
        L_0x004c:
            java.lang.Object r2 = r4._suppressableValue
            boolean r2 = r2.equals(r1)
            if (r2 == 0) goto L_0x003a
            goto L_0x0015
        L_0x0055:
            com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r2 = r4._typeSerializer
            r0.serializeWithType(r1, r6, r7, r2)
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(java.lang.Object, com.shaded.fasterxml.jackson.core.JsonGenerator, com.shaded.fasterxml.jackson.databind.SerializerProvider):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
        r2 = r1.getClass();
        r3 = r4._dynamicSerializers;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void serializeAsColumn(java.lang.Object r5, com.shaded.fasterxml.jackson.core.JsonGenerator r6, com.shaded.fasterxml.jackson.databind.SerializerProvider r7) throws java.lang.Exception {
        /*
            r4 = this;
            java.lang.Object r1 = r4.get(r5)
            if (r1 != 0) goto L_0x0015
            com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r0 = r4._nullSerializer
            if (r0 == 0) goto L_0x0011
            com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r0 = r4._nullSerializer
            r1 = 0
            r0.serialize(r1, r6, r7)
        L_0x0010:
            return
        L_0x0011:
            r6.writeNull()
            goto L_0x0010
        L_0x0015:
            com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r0 = r4._serializer
            if (r0 != 0) goto L_0x0029
            java.lang.Class r2 = r1.getClass()
            com.shaded.fasterxml.jackson.databind.ser.impl.PropertySerializerMap r3 = r4._dynamicSerializers
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r3.serializerFor(r2)
            if (r0 != 0) goto L_0x0029
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4._findAndAddDynamic(r3, r2, r7)
        L_0x0029:
            java.lang.Object r2 = r4._suppressableValue
            if (r2 == 0) goto L_0x0049
            java.lang.Object r2 = MARKER_FOR_EMPTY
            java.lang.Object r3 = r4._suppressableValue
            if (r2 != r3) goto L_0x003d
            boolean r2 = r0.isEmpty(r1)
            if (r2 == 0) goto L_0x0049
            r4.serializeAsPlaceholder(r5, r6, r7)
            goto L_0x0010
        L_0x003d:
            java.lang.Object r2 = r4._suppressableValue
            boolean r2 = r2.equals(r1)
            if (r2 == 0) goto L_0x0049
            r4.serializeAsPlaceholder(r5, r6, r7)
            goto L_0x0010
        L_0x0049:
            if (r1 != r5) goto L_0x004e
            r4._handleSelfReference(r5, r0)
        L_0x004e:
            com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r2 = r4._typeSerializer
            if (r2 != 0) goto L_0x0056
            r0.serialize(r1, r6, r7)
            goto L_0x0010
        L_0x0056:
            com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r2 = r4._typeSerializer
            r0.serializeWithType(r1, r6, r7, r2)
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsColumn(java.lang.Object, com.shaded.fasterxml.jackson.core.JsonGenerator, com.shaded.fasterxml.jackson.databind.SerializerProvider):void");
    }

    public void serializeAsPlaceholder(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws Exception {
        if (this._nullSerializer != null) {
            this._nullSerializer.serialize(null, jsonGenerator, serializerProvider);
        } else {
            jsonGenerator.writeNull();
        }
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap propertySerializerMap, Class<?> cls, SerializerProvider serializerProvider) throws JsonMappingException {
        PropertySerializerMap.SerializerAndMapResult findAndAddSerializer;
        if (this._nonTrivialBaseType != null) {
            findAndAddSerializer = propertySerializerMap.findAndAddSerializer(serializerProvider.constructSpecializedType(this._nonTrivialBaseType, cls), serializerProvider, (BeanProperty) this);
        } else {
            findAndAddSerializer = propertySerializerMap.findAndAddSerializer(cls, serializerProvider, (BeanProperty) this);
        }
        if (propertySerializerMap != findAndAddSerializer.map) {
            this._dynamicSerializers = findAndAddSerializer.map;
        }
        return findAndAddSerializer.serializer;
    }

    public final Object get(Object obj) throws Exception {
        if (this._accessorMethod != null) {
            return this._accessorMethod.invoke(obj, new Object[0]);
        }
        return this._field.get(obj);
    }

    /* access modifiers changed from: protected */
    public void _handleSelfReference(Object obj, JsonSerializer<?> jsonSerializer) throws JsonMappingException {
        if (!jsonSerializer.usesObjectId()) {
            throw new JsonMappingException("Direct self-reference leading to cycle");
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append("property '").append(getName()).append("' (");
        if (this._accessorMethod != null) {
            sb.append("via method ").append(this._accessorMethod.getDeclaringClass().getName()).append("#").append(this._accessorMethod.getName());
        } else {
            sb.append("field \"").append(this._field.getDeclaringClass().getName()).append("#").append(this._field.getName());
        }
        if (this._serializer == null) {
            sb.append(", no static serializer");
        } else {
            sb.append(", static serializer of type " + this._serializer.getClass().getName());
        }
        sb.append(')');
        return sb.toString();
    }
}
