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
import java.util.Collection;

@JacksonStdImpl
public class StringCollectionSerializer extends StaticListSerializerBase<Collection<String>> implements ContextualSerializer {
    public static final StringCollectionSerializer instance = new StringCollectionSerializer();
    protected final JsonSerializer<String> _serializer;

    protected StringCollectionSerializer() {
        this((JsonSerializer<?>) null);
    }

    protected StringCollectionSerializer(JsonSerializer<?> jsonSerializer) {
        super(Collection.class);
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
        return findConvertingContentSerializer == this._serializer ? this : new StringCollectionSerializer(findConvertingContentSerializer);
    }

    public void serialize(Collection<String> collection, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (collection.size() != 1 || !serializerProvider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) {
            jsonGenerator.writeStartArray();
            if (this._serializer == null) {
                serializeContents(collection, jsonGenerator, serializerProvider);
            } else {
                serializeUsingCustom(collection, jsonGenerator, serializerProvider);
            }
            jsonGenerator.writeEndArray();
            return;
        }
        _serializeUnwrapped(collection, jsonGenerator, serializerProvider);
    }

    private final void _serializeUnwrapped(Collection<String> collection, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._serializer == null) {
            serializeContents(collection, jsonGenerator, serializerProvider);
        } else {
            serializeUsingCustom(collection, jsonGenerator, serializerProvider);
        }
    }

    public void serializeWithType(Collection<String> collection, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForArray(collection, jsonGenerator);
        if (this._serializer == null) {
            serializeContents(collection, jsonGenerator, serializerProvider);
        } else {
            serializeUsingCustom(collection, jsonGenerator, serializerProvider);
        }
        typeSerializer.writeTypeSuffixForArray(collection, jsonGenerator);
    }

    private final void serializeContents(Collection<String> collection, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        int i;
        if (this._serializer != null) {
            serializeUsingCustom(collection, jsonGenerator, serializerProvider);
            return;
        }
        int i2 = 0;
        for (String next : collection) {
            if (next == null) {
                try {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                } catch (Exception e) {
                    wrapAndThrow(serializerProvider, (Throwable) e, (Object) collection, i2);
                    i = i2;
                }
            } else {
                jsonGenerator.writeString(next);
            }
            i = i2 + 1;
            i2 = i;
        }
    }

    private void serializeUsingCustom(Collection<String> collection, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        JsonSerializer<String> jsonSerializer = this._serializer;
        for (String next : collection) {
            if (next == null) {
                try {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                } catch (Exception e) {
                    wrapAndThrow(serializerProvider, (Throwable) e, (Object) collection, 0);
                }
            } else {
                jsonSerializer.serialize(next, jsonGenerator, serializerProvider);
            }
        }
    }
}
