package com.shaded.fasterxml.jackson.databind.ser.impl;

import com.google.appinventor.components.common.PropertyTypeConstants;
import com.shaded.fasterxml.jackson.core.JsonGenerationException;
import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonNode;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.SerializationFeature;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.shaded.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;
import java.io.IOException;
import java.util.List;

@JacksonStdImpl
public final class IndexedStringListSerializer extends StaticListSerializerBase<List<String>> implements ContextualSerializer {
    public static final IndexedStringListSerializer instance = new IndexedStringListSerializer();
    protected final JsonSerializer<String> _serializer;

    protected IndexedStringListSerializer() {
        this((JsonSerializer<?>) null);
    }

    public IndexedStringListSerializer(JsonSerializer<?> jsonSerializer) {
        super(List.class);
        this._serializer = jsonSerializer;
    }

    /* access modifiers changed from: protected */
    public JsonNode contentSchema() {
        return createSchemaNode(PropertyTypeConstants.PROPERTY_TYPE_STRING, true);
    }

    /* access modifiers changed from: protected */
    public void acceptContentVisitor(JsonArrayFormatVisitor jsonArrayFormatVisitor) throws JsonMappingException {
        jsonArrayFormatVisitor.itemsFormat(JsonFormatTypes.STRING);
    }

    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        JsonSerializer jsonSerializer;
        AnnotatedMember member;
        Object findContentSerializer;
        if (beanProperty == null || (member = beanProperty.getMember()) == null || (findContentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(member)) == null) {
            jsonSerializer = null;
        } else {
            jsonSerializer = serializerProvider.serializerInstance(member, findContentSerializer);
        }
        if (jsonSerializer == null) {
            jsonSerializer = this._serializer;
        }
        JsonSerializer findConvertingContentSerializer = findConvertingContentSerializer(serializerProvider, beanProperty, jsonSerializer);
        if (findConvertingContentSerializer == null) {
            findConvertingContentSerializer = serializerProvider.findValueSerializer((Class<?>) String.class, beanProperty);
        } else if (findConvertingContentSerializer instanceof ContextualSerializer) {
            findConvertingContentSerializer = ((ContextualSerializer) findConvertingContentSerializer).createContextual(serializerProvider, beanProperty);
        }
        if (isDefaultSerializer(findConvertingContentSerializer)) {
            findConvertingContentSerializer = null;
        }
        return findConvertingContentSerializer == this._serializer ? this : new IndexedStringListSerializer(findConvertingContentSerializer);
    }

    public void serialize(List<String> list, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        int size = list.size();
        if (size != 1 || !serializerProvider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) {
            jsonGenerator.writeStartArray();
            if (this._serializer == null) {
                serializeContents(list, jsonGenerator, serializerProvider, size);
            } else {
                serializeUsingCustom(list, jsonGenerator, serializerProvider, size);
            }
            jsonGenerator.writeEndArray();
            return;
        }
        _serializeUnwrapped(list, jsonGenerator, serializerProvider);
    }

    private final void _serializeUnwrapped(List<String> list, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._serializer == null) {
            serializeContents(list, jsonGenerator, serializerProvider, 1);
        } else {
            serializeUsingCustom(list, jsonGenerator, serializerProvider, 1);
        }
    }

    public void serializeWithType(List<String> list, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        int size = list.size();
        typeSerializer.writeTypePrefixForArray(list, jsonGenerator);
        if (this._serializer == null) {
            serializeContents(list, jsonGenerator, serializerProvider, size);
        } else {
            serializeUsingCustom(list, jsonGenerator, serializerProvider, size);
        }
        typeSerializer.writeTypeSuffixForArray(list, jsonGenerator);
    }

    private final void serializeContents(List<String> list, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, int i) throws IOException, JsonGenerationException {
        int i2 = 0;
        while (i2 < i) {
            try {
                String str = list.get(i2);
                if (str == null) {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                } else {
                    jsonGenerator.writeString(str);
                }
                i2++;
            } catch (Exception e) {
                wrapAndThrow(serializerProvider, (Throwable) e, (Object) list, i2);
                return;
            }
        }
    }

    private final void serializeUsingCustom(List<String> list, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, int i) throws IOException, JsonGenerationException {
        try {
            JsonSerializer<String> jsonSerializer = this._serializer;
            for (int i2 = 0; i2 < i; i2++) {
                String str = list.get(i2);
                if (str == null) {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                } else {
                    jsonSerializer.serialize(str, jsonGenerator, serializerProvider);
                }
            }
        } catch (Exception e) {
            wrapAndThrow(serializerProvider, (Throwable) e, (Object) list, 0);
        }
    }
}
