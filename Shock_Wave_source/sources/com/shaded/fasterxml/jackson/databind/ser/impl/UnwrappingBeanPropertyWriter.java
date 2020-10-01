package com.shaded.fasterxml.jackson.databind.ser.impl;

import com.shaded.fasterxml.jackson.core.p004io.SerializedString;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.shaded.fasterxml.jackson.databind.util.NameTransformer;

public class UnwrappingBeanPropertyWriter extends BeanPropertyWriter {
    protected final NameTransformer _nameTransformer;

    public UnwrappingBeanPropertyWriter(BeanPropertyWriter beanPropertyWriter, NameTransformer nameTransformer) {
        super(beanPropertyWriter);
        this._nameTransformer = nameTransformer;
    }

    private UnwrappingBeanPropertyWriter(UnwrappingBeanPropertyWriter unwrappingBeanPropertyWriter, NameTransformer nameTransformer, SerializedString serializedString) {
        super(unwrappingBeanPropertyWriter, serializedString);
        this._nameTransformer = nameTransformer;
    }

    public UnwrappingBeanPropertyWriter rename(NameTransformer nameTransformer) {
        return new UnwrappingBeanPropertyWriter(this, NameTransformer.chainedTransformer(nameTransformer, this._nameTransformer), new SerializedString(nameTransformer.transform(this._name.getValue())));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000b, code lost:
        r2 = r1.getClass();
        r3 = r4._dynamicSerializers;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void serializeAsField(java.lang.Object r5, com.shaded.fasterxml.jackson.core.JsonGenerator r6, com.shaded.fasterxml.jackson.databind.SerializerProvider r7) throws java.lang.Exception {
        /*
            r4 = this;
            java.lang.Object r1 = r4.get(r5)
            if (r1 != 0) goto L_0x0007
        L_0x0006:
            return
        L_0x0007:
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4._serializer
            if (r0 != 0) goto L_0x001b
            java.lang.Class r2 = r1.getClass()
            com.shaded.fasterxml.jackson.databind.ser.impl.PropertySerializerMap r3 = r4._dynamicSerializers
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r3.serializerFor(r2)
            if (r0 != 0) goto L_0x001b
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r4._findAndAddDynamic(r3, r2, r7)
        L_0x001b:
            java.lang.Object r2 = r4._suppressableValue
            if (r2 == 0) goto L_0x002b
            java.lang.Object r2 = MARKER_FOR_EMPTY
            java.lang.Object r3 = r4._suppressableValue
            if (r2 != r3) goto L_0x0043
            boolean r2 = r0.isEmpty(r1)
            if (r2 != 0) goto L_0x0006
        L_0x002b:
            if (r1 != r5) goto L_0x0030
            r4._handleSelfReference(r5, r0)
        L_0x0030:
            boolean r2 = r0.isUnwrappingSerializer()
            if (r2 != 0) goto L_0x003b
            com.shaded.fasterxml.jackson.core.io.SerializedString r2 = r4._name
            r6.writeFieldName((com.shaded.fasterxml.jackson.core.SerializableString) r2)
        L_0x003b:
            com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r2 = r4._typeSerializer
            if (r2 != 0) goto L_0x004c
            r0.serialize(r1, r6, r7)
            goto L_0x0006
        L_0x0043:
            java.lang.Object r2 = r4._suppressableValue
            boolean r2 = r2.equals(r1)
            if (r2 == 0) goto L_0x002b
            goto L_0x0006
        L_0x004c:
            com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r2 = r4._typeSerializer
            r0.serializeWithType(r1, r6, r7, r2)
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter.serializeAsField(java.lang.Object, com.shaded.fasterxml.jackson.core.JsonGenerator, com.shaded.fasterxml.jackson.databind.SerializerProvider):void");
    }

    public void assignSerializer(JsonSerializer<Object> jsonSerializer) {
        NameTransformer nameTransformer;
        super.assignSerializer(jsonSerializer);
        if (this._serializer != null) {
            NameTransformer nameTransformer2 = this._nameTransformer;
            if (this._serializer.isUnwrappingSerializer()) {
                nameTransformer = NameTransformer.chainedTransformer(nameTransformer2, ((UnwrappingBeanSerializer) this._serializer)._nameTransformer);
            } else {
                nameTransformer = nameTransformer2;
            }
            this._serializer = this._serializer.unwrappingSerializer(nameTransformer);
        }
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap propertySerializerMap, Class<?> cls, SerializerProvider serializerProvider) throws JsonMappingException {
        JsonSerializer<Object> findValueSerializer;
        NameTransformer nameTransformer;
        if (this._nonTrivialBaseType != null) {
            findValueSerializer = serializerProvider.findValueSerializer(serializerProvider.constructSpecializedType(this._nonTrivialBaseType, cls), (BeanProperty) this);
        } else {
            findValueSerializer = serializerProvider.findValueSerializer(cls, (BeanProperty) this);
        }
        NameTransformer nameTransformer2 = this._nameTransformer;
        if (findValueSerializer.isUnwrappingSerializer()) {
            nameTransformer = NameTransformer.chainedTransformer(nameTransformer2, ((UnwrappingBeanSerializer) findValueSerializer)._nameTransformer);
        } else {
            nameTransformer = nameTransformer2;
        }
        JsonSerializer<Object> unwrappingSerializer = findValueSerializer.unwrappingSerializer(nameTransformer);
        this._dynamicSerializers = this._dynamicSerializers.newWith(cls, unwrappingSerializer);
        return unwrappingSerializer;
    }
}
