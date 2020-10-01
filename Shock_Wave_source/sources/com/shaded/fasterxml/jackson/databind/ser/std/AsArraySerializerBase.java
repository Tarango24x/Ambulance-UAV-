package com.shaded.fasterxml.jackson.databind.ser.std;

import com.shaded.fasterxml.jackson.core.JsonGenerationException;
import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.SerializationFeature;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.shaded.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.shaded.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.shaded.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import java.io.IOException;

public abstract class AsArraySerializerBase<T> extends ContainerSerializer<T> implements ContextualSerializer {
    protected PropertySerializerMap _dynamicSerializers;
    protected final JsonSerializer<Object> _elementSerializer;
    protected final JavaType _elementType;
    protected final BeanProperty _property;
    protected final boolean _staticTyping;
    protected final TypeSerializer _valueTypeSerializer;

    /* access modifiers changed from: protected */
    public abstract void serializeContents(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException;

    public abstract AsArraySerializerBase<T> withResolved(BeanProperty beanProperty, TypeSerializer typeSerializer, JsonSerializer<?> jsonSerializer);

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected AsArraySerializerBase(Class<?> cls, JavaType javaType, boolean z, TypeSerializer typeSerializer, BeanProperty beanProperty, JsonSerializer<Object> jsonSerializer) {
        super(cls, false);
        boolean z2 = false;
        this._elementType = javaType;
        if (z || (javaType != null && javaType.isFinal())) {
            z2 = true;
        }
        this._staticTyping = z2;
        this._valueTypeSerializer = typeSerializer;
        this._property = beanProperty;
        this._elementSerializer = jsonSerializer;
        this._dynamicSerializers = PropertySerializerMap.emptyMap();
    }

    protected AsArraySerializerBase(AsArraySerializerBase<?> asArraySerializerBase, BeanProperty beanProperty, TypeSerializer typeSerializer, JsonSerializer<?> jsonSerializer) {
        super((ContainerSerializer<?>) asArraySerializerBase);
        this._elementType = asArraySerializerBase._elementType;
        this._staticTyping = asArraySerializerBase._staticTyping;
        this._valueTypeSerializer = typeSerializer;
        this._property = beanProperty;
        this._elementSerializer = jsonSerializer;
        this._dynamicSerializers = asArraySerializerBase._dynamicSerializers;
    }

    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        TypeSerializer typeSerializer;
        AnnotatedMember member;
        Object findContentSerializer;
        TypeSerializer typeSerializer2 = this._valueTypeSerializer;
        if (typeSerializer2 != null) {
            typeSerializer = typeSerializer2.forProperty(beanProperty);
        } else {
            typeSerializer = typeSerializer2;
        }
        JsonSerializer jsonSerializer = null;
        if (!(beanProperty == null || (member = beanProperty.getMember()) == null || (findContentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(member)) == null)) {
            jsonSerializer = serializerProvider.serializerInstance(member, findContentSerializer);
        }
        if (jsonSerializer == null) {
            jsonSerializer = this._elementSerializer;
        }
        JsonSerializer findConvertingContentSerializer = findConvertingContentSerializer(serializerProvider, beanProperty, jsonSerializer);
        if (findConvertingContentSerializer == null) {
            if (findConvertingContentSerializer == null && this._elementType != null && (this._staticTyping || hasContentTypeAnnotation(serializerProvider, beanProperty))) {
                findConvertingContentSerializer = serializerProvider.findValueSerializer(this._elementType, beanProperty);
            }
        } else if (findConvertingContentSerializer instanceof ContextualSerializer) {
            findConvertingContentSerializer = ((ContextualSerializer) findConvertingContentSerializer).createContextual(serializerProvider, beanProperty);
        }
        if (findConvertingContentSerializer == this._elementSerializer && beanProperty == this._property && this._valueTypeSerializer == typeSerializer) {
            return this;
        }
        return withResolved(beanProperty, typeSerializer, findConvertingContentSerializer);
    }

    public JavaType getContentType() {
        return this._elementType;
    }

    public JsonSerializer<?> getContentSerializer() {
        return this._elementSerializer;
    }

    public final void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (!serializerProvider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) || !hasSingleElement(t)) {
            jsonGenerator.writeStartArray();
            serializeContents(t, jsonGenerator, serializerProvider);
            jsonGenerator.writeEndArray();
            return;
        }
        serializeContents(t, jsonGenerator, serializerProvider);
    }

    public final void serializeWithType(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForArray(t, jsonGenerator);
        serializeContents(t, jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForArray(t, jsonGenerator);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x004c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonNode getSchema(com.shaded.fasterxml.jackson.databind.SerializerProvider r7, java.lang.reflect.Type r8) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r6 = this;
            r5 = 1
            r1 = 0
            java.lang.String r0 = "array"
            com.shaded.fasterxml.jackson.databind.node.ObjectNode r2 = r6.createSchemaNode(r0, r5)
            if (r8 == 0) goto L_0x0058
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r7.constructType(r8)
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r0.getContentType()
            if (r0 != 0) goto L_0x0028
            boolean r3 = r8 instanceof java.lang.reflect.ParameterizedType
            if (r3 == 0) goto L_0x0028
            java.lang.reflect.ParameterizedType r8 = (java.lang.reflect.ParameterizedType) r8
            java.lang.reflect.Type[] r3 = r8.getActualTypeArguments()
            int r4 = r3.length
            if (r4 != r5) goto L_0x0028
            r0 = 0
            r0 = r3[r0]
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r7.constructType(r0)
        L_0x0028:
            if (r0 != 0) goto L_0x0030
            com.shaded.fasterxml.jackson.databind.JavaType r3 = r6._elementType
            if (r3 == 0) goto L_0x0030
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r6._elementType
        L_0x0030:
            if (r0 == 0) goto L_0x0055
            java.lang.Class r3 = r0.getRawClass()
            java.lang.Class<java.lang.Object> r4 = java.lang.Object.class
            if (r3 == r4) goto L_0x0056
            com.shaded.fasterxml.jackson.databind.BeanProperty r3 = r6._property
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r7.findValueSerializer((com.shaded.fasterxml.jackson.databind.JavaType) r0, (com.shaded.fasterxml.jackson.databind.BeanProperty) r3)
            boolean r3 = r0 instanceof com.shaded.fasterxml.jackson.databind.jsonschema.SchemaAware
            if (r3 == 0) goto L_0x0056
            com.shaded.fasterxml.jackson.databind.jsonschema.SchemaAware r0 = (com.shaded.fasterxml.jackson.databind.jsonschema.SchemaAware) r0
            com.shaded.fasterxml.jackson.databind.JsonNode r0 = r0.getSchema(r7, r1)
        L_0x004a:
            if (r0 != 0) goto L_0x0050
            com.shaded.fasterxml.jackson.databind.JsonNode r0 = com.shaded.fasterxml.jackson.databind.jsonschema.JsonSchema.getDefaultSchemaNode()
        L_0x0050:
            java.lang.String r1 = "items"
            r2.put((java.lang.String) r1, (com.shaded.fasterxml.jackson.databind.JsonNode) r0)
        L_0x0055:
            return r2
        L_0x0056:
            r0 = r1
            goto L_0x004a
        L_0x0058:
            r0 = r1
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.std.AsArraySerializerBase.getSchema(com.shaded.fasterxml.jackson.databind.SerializerProvider, java.lang.reflect.Type):com.shaded.fasterxml.jackson.databind.JsonNode");
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper jsonFormatVisitorWrapper, JavaType javaType) throws JsonMappingException {
        JsonArrayFormatVisitor expectArrayFormat = jsonFormatVisitorWrapper == null ? null : jsonFormatVisitorWrapper.expectArrayFormat(javaType);
        if (expectArrayFormat != null) {
            JavaType moreSpecificType = jsonFormatVisitorWrapper.getProvider().getTypeFactory().moreSpecificType(this._elementType, javaType.getContentType());
            if (moreSpecificType == null) {
                throw new JsonMappingException("Could not resolve type");
            }
            JsonSerializer<Object> jsonSerializer = this._elementSerializer;
            if (jsonSerializer == null) {
                jsonSerializer = jsonFormatVisitorWrapper.getProvider().findValueSerializer(moreSpecificType, this._property);
            }
            expectArrayFormat.itemsFormat(jsonSerializer, moreSpecificType);
        }
    }

    /* access modifiers changed from: protected */
    public final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap propertySerializerMap, Class<?> cls, SerializerProvider serializerProvider) throws JsonMappingException {
        PropertySerializerMap.SerializerAndMapResult findAndAddSerializer = propertySerializerMap.findAndAddSerializer(cls, serializerProvider, this._property);
        if (propertySerializerMap != findAndAddSerializer.map) {
            this._dynamicSerializers = findAndAddSerializer.map;
        }
        return findAndAddSerializer.serializer;
    }

    /* access modifiers changed from: protected */
    public final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap propertySerializerMap, JavaType javaType, SerializerProvider serializerProvider) throws JsonMappingException {
        PropertySerializerMap.SerializerAndMapResult findAndAddSerializer = propertySerializerMap.findAndAddSerializer(javaType, serializerProvider, this._property);
        if (propertySerializerMap != findAndAddSerializer.map) {
            this._dynamicSerializers = findAndAddSerializer.map;
        }
        return findAndAddSerializer.serializer;
    }
}
