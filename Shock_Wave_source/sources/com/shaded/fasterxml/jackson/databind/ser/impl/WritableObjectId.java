package com.shaded.fasterxml.jackson.databind.ser.impl;

import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.shaded.fasterxml.jackson.core.JsonGenerationException;
import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.core.SerializableString;
import com.shaded.fasterxml.jackson.core.p004io.SerializedString;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public final class WritableObjectId {
    public final ObjectIdGenerator<?> generator;

    /* renamed from: id */
    public Object f274id;
    protected boolean idWritten = false;

    public WritableObjectId(ObjectIdGenerator<?> objectIdGenerator) {
        this.generator = objectIdGenerator;
    }

    public boolean writeAsId(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, ObjectIdWriter objectIdWriter) throws IOException, JsonGenerationException {
        if (this.f274id == null || (!this.idWritten && !objectIdWriter.alwaysAsId)) {
            return false;
        }
        objectIdWriter.serializer.serialize(this.f274id, jsonGenerator, serializerProvider);
        return true;
    }

    public Object generateId(Object obj) {
        Object generateId = this.generator.generateId(obj);
        this.f274id = generateId;
        return generateId;
    }

    public void writeAsField(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, ObjectIdWriter objectIdWriter) throws IOException, JsonGenerationException {
        SerializedString serializedString = objectIdWriter.propertyName;
        this.idWritten = true;
        if (serializedString != null) {
            jsonGenerator.writeFieldName((SerializableString) serializedString);
            objectIdWriter.serializer.serialize(this.f274id, jsonGenerator, serializerProvider);
        }
    }
}
