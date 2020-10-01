package com.shaded.fasterxml.jackson.databind.ser.std;

import com.shaded.fasterxml.jackson.core.JsonGenerationException;
import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonSerializable;
import com.shaded.fasterxml.jackson.databind.ObjectMapper;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@JacksonStdImpl
public class SerializableSerializer extends StdSerializer<JsonSerializable> {
    private static final AtomicReference<ObjectMapper> _mapperReference = new AtomicReference<>();
    public static final SerializableSerializer instance = new SerializableSerializer();

    protected SerializableSerializer() {
        super(JsonSerializable.class);
    }

    public void serialize(JsonSerializable jsonSerializable, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonSerializable.serialize(jsonGenerator, serializerProvider);
    }

    public final void serializeWithType(JsonSerializable jsonSerializable, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        jsonSerializable.serializeWithType(jsonGenerator, serializerProvider, typeSerializer);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0058  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonNode getSchema(com.shaded.fasterxml.jackson.databind.SerializerProvider r8, java.lang.reflect.Type r9) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r7 = this;
            r2 = 0
            com.shaded.fasterxml.jackson.databind.node.ObjectNode r4 = r7.createObjectNode()
            java.lang.String r0 = "any"
            if (r9 == 0) goto L_0x007c
            java.lang.Class r1 = com.shaded.fasterxml.jackson.databind.type.TypeFactory.rawClass(r9)
            java.lang.Class<com.shaded.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema> r3 = com.shaded.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema.class
            boolean r3 = r1.isAnnotationPresent(r3)
            if (r3 == 0) goto L_0x007c
            java.lang.Class<com.shaded.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema> r0 = com.shaded.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema.class
            java.lang.annotation.Annotation r0 = r1.getAnnotation(r0)
            com.shaded.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema r0 = (com.shaded.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema) r0
            java.lang.String r3 = r0.schemaType()
            java.lang.String r1 = "##irrelevant"
            java.lang.String r5 = r0.schemaObjectPropertiesDefinition()
            boolean r1 = r1.equals(r5)
            if (r1 != 0) goto L_0x007a
            java.lang.String r1 = r0.schemaObjectPropertiesDefinition()
        L_0x0031:
            java.lang.String r5 = "##irrelevant"
            java.lang.String r6 = r0.schemaItemDefinition()
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L_0x0078
            java.lang.String r2 = r0.schemaItemDefinition()
            r0 = r3
        L_0x0042:
            java.lang.String r3 = "type"
            r4.put((java.lang.String) r3, (java.lang.String) r0)
            if (r1 == 0) goto L_0x0056
            java.lang.String r0 = "properties"
            com.shaded.fasterxml.jackson.databind.ObjectMapper r3 = _getObjectMapper()     // Catch:{ IOException -> 0x0066 }
            com.shaded.fasterxml.jackson.databind.JsonNode r1 = r3.readTree((java.lang.String) r1)     // Catch:{ IOException -> 0x0066 }
            r4.put((java.lang.String) r0, (com.shaded.fasterxml.jackson.databind.JsonNode) r1)     // Catch:{ IOException -> 0x0066 }
        L_0x0056:
            if (r2 == 0) goto L_0x0065
            java.lang.String r0 = "items"
            com.shaded.fasterxml.jackson.databind.ObjectMapper r1 = _getObjectMapper()     // Catch:{ IOException -> 0x006f }
            com.shaded.fasterxml.jackson.databind.JsonNode r1 = r1.readTree((java.lang.String) r2)     // Catch:{ IOException -> 0x006f }
            r4.put((java.lang.String) r0, (com.shaded.fasterxml.jackson.databind.JsonNode) r1)     // Catch:{ IOException -> 0x006f }
        L_0x0065:
            return r4
        L_0x0066:
            r0 = move-exception
            com.shaded.fasterxml.jackson.databind.JsonMappingException r0 = new com.shaded.fasterxml.jackson.databind.JsonMappingException
            java.lang.String r1 = "Failed to parse @JsonSerializableSchema.schemaObjectPropertiesDefinition value"
            r0.<init>(r1)
            throw r0
        L_0x006f:
            r0 = move-exception
            com.shaded.fasterxml.jackson.databind.JsonMappingException r0 = new com.shaded.fasterxml.jackson.databind.JsonMappingException
            java.lang.String r1 = "Failed to parse @JsonSerializableSchema.schemaItemDefinition value"
            r0.<init>(r1)
            throw r0
        L_0x0078:
            r0 = r3
            goto L_0x0042
        L_0x007a:
            r1 = r2
            goto L_0x0031
        L_0x007c:
            r1 = r2
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.std.SerializableSerializer.getSchema(com.shaded.fasterxml.jackson.databind.SerializerProvider, java.lang.reflect.Type):com.shaded.fasterxml.jackson.databind.JsonNode");
    }

    private static final synchronized ObjectMapper _getObjectMapper() {
        ObjectMapper objectMapper;
        synchronized (SerializableSerializer.class) {
            objectMapper = _mapperReference.get();
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
                _mapperReference.set(objectMapper);
            }
        }
        return objectMapper;
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper jsonFormatVisitorWrapper, JavaType javaType) throws JsonMappingException {
        jsonFormatVisitorWrapper.expectAnyFormat(javaType);
    }
}
