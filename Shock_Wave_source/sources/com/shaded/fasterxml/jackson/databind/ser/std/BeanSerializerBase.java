package com.shaded.fasterxml.jackson.databind.ser.std;

import com.shaded.fasterxml.jackson.annotation.JsonFormat;
import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.shaded.fasterxml.jackson.core.JsonGenerationException;
import com.shaded.fasterxml.jackson.core.JsonGenerator;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonNode;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.introspect.Annotated;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.shaded.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.shaded.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import com.shaded.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.shaded.fasterxml.jackson.databind.node.ObjectNode;
import com.shaded.fasterxml.jackson.databind.ser.AnyGetterWriter;
import com.shaded.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.shaded.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.shaded.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.shaded.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.shaded.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.shaded.fasterxml.jackson.databind.ser.FilterProvider;
import com.shaded.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.shaded.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.shaded.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
import com.shaded.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.shaded.fasterxml.jackson.databind.util.ArrayBuilders;
import com.shaded.fasterxml.jackson.databind.util.Converter;
import com.shaded.fasterxml.jackson.databind.util.NameTransformer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class BeanSerializerBase extends StdSerializer<Object> implements ContextualSerializer, ResolvableSerializer, JsonFormatVisitable, SchemaAware {
    protected static final BeanPropertyWriter[] NO_PROPS = new BeanPropertyWriter[0];
    protected final AnyGetterWriter _anyGetterWriter;
    protected final BeanPropertyWriter[] _filteredProps;
    protected final ObjectIdWriter _objectIdWriter;
    protected final Object _propertyFilterId;
    protected final BeanPropertyWriter[] _props;
    protected final JsonFormat.Shape _serializationShape;
    protected final AnnotatedMember _typeId;

    /* access modifiers changed from: protected */
    public abstract BeanSerializerBase asArraySerializer();

    public abstract void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException;

    /* access modifiers changed from: protected */
    public abstract BeanSerializerBase withIgnorals(String[] strArr);

    public abstract BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter);

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected BeanSerializerBase(JavaType javaType, BeanSerializerBuilder beanSerializerBuilder, BeanPropertyWriter[] beanPropertyWriterArr, BeanPropertyWriter[] beanPropertyWriterArr2) {
        super(javaType);
        JsonFormat.Shape shape = null;
        this._props = beanPropertyWriterArr;
        this._filteredProps = beanPropertyWriterArr2;
        if (beanSerializerBuilder == null) {
            this._typeId = null;
            this._anyGetterWriter = null;
            this._propertyFilterId = null;
            this._objectIdWriter = null;
            this._serializationShape = null;
            return;
        }
        this._typeId = beanSerializerBuilder.getTypeId();
        this._anyGetterWriter = beanSerializerBuilder.getAnyGetter();
        this._propertyFilterId = beanSerializerBuilder.getFilterId();
        this._objectIdWriter = beanSerializerBuilder.getObjectIdWriter();
        JsonFormat.Value findExpectedFormat = beanSerializerBuilder.getBeanDescription().findExpectedFormat((JsonFormat.Value) null);
        this._serializationShape = findExpectedFormat != null ? findExpectedFormat.getShape() : shape;
    }

    public BeanSerializerBase(BeanSerializerBase beanSerializerBase, BeanPropertyWriter[] beanPropertyWriterArr, BeanPropertyWriter[] beanPropertyWriterArr2) {
        super(beanSerializerBase._handledType);
        this._props = beanPropertyWriterArr;
        this._filteredProps = beanPropertyWriterArr2;
        this._typeId = beanSerializerBase._typeId;
        this._anyGetterWriter = beanSerializerBase._anyGetterWriter;
        this._objectIdWriter = beanSerializerBase._objectIdWriter;
        this._propertyFilterId = beanSerializerBase._propertyFilterId;
        this._serializationShape = beanSerializerBase._serializationShape;
    }

    protected BeanSerializerBase(BeanSerializerBase beanSerializerBase, ObjectIdWriter objectIdWriter) {
        super(beanSerializerBase._handledType);
        this._props = beanSerializerBase._props;
        this._filteredProps = beanSerializerBase._filteredProps;
        this._typeId = beanSerializerBase._typeId;
        this._anyGetterWriter = beanSerializerBase._anyGetterWriter;
        this._objectIdWriter = objectIdWriter;
        this._propertyFilterId = beanSerializerBase._propertyFilterId;
        this._serializationShape = beanSerializerBase._serializationShape;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected BeanSerializerBase(BeanSerializerBase beanSerializerBase, String[] strArr) {
        super(beanSerializerBase._handledType);
        BeanPropertyWriter[] beanPropertyWriterArr = null;
        HashSet arrayToSet = ArrayBuilders.arrayToSet(strArr);
        BeanPropertyWriter[] beanPropertyWriterArr2 = beanSerializerBase._props;
        BeanPropertyWriter[] beanPropertyWriterArr3 = beanSerializerBase._filteredProps;
        int length = beanPropertyWriterArr2.length;
        ArrayList arrayList = new ArrayList(length);
        ArrayList arrayList2 = beanPropertyWriterArr3 == null ? null : new ArrayList(length);
        for (int i = 0; i < length; i++) {
            BeanPropertyWriter beanPropertyWriter = beanPropertyWriterArr2[i];
            if (!arrayToSet.contains(beanPropertyWriter.getName())) {
                arrayList.add(beanPropertyWriter);
                if (beanPropertyWriterArr3 != null) {
                    arrayList2.add(beanPropertyWriterArr3[i]);
                }
            }
        }
        this._props = (BeanPropertyWriter[]) arrayList.toArray(new BeanPropertyWriter[arrayList.size()]);
        this._filteredProps = arrayList2 != null ? (BeanPropertyWriter[]) arrayList2.toArray(new BeanPropertyWriter[arrayList2.size()]) : beanPropertyWriterArr;
        this._typeId = beanSerializerBase._typeId;
        this._anyGetterWriter = beanSerializerBase._anyGetterWriter;
        this._objectIdWriter = beanSerializerBase._objectIdWriter;
        this._propertyFilterId = beanSerializerBase._propertyFilterId;
        this._serializationShape = beanSerializerBase._serializationShape;
    }

    protected BeanSerializerBase(BeanSerializerBase beanSerializerBase) {
        this(beanSerializerBase, beanSerializerBase._props, beanSerializerBase._filteredProps);
    }

    protected BeanSerializerBase(BeanSerializerBase beanSerializerBase, NameTransformer nameTransformer) {
        this(beanSerializerBase, rename(beanSerializerBase._props, nameTransformer), rename(beanSerializerBase._filteredProps, nameTransformer));
    }

    private static final BeanPropertyWriter[] rename(BeanPropertyWriter[] beanPropertyWriterArr, NameTransformer nameTransformer) {
        if (beanPropertyWriterArr == null || beanPropertyWriterArr.length == 0 || nameTransformer == null || nameTransformer == NameTransformer.NOP) {
            return beanPropertyWriterArr;
        }
        int length = beanPropertyWriterArr.length;
        BeanPropertyWriter[] beanPropertyWriterArr2 = new BeanPropertyWriter[length];
        for (int i = 0; i < length; i++) {
            BeanPropertyWriter beanPropertyWriter = beanPropertyWriterArr[i];
            if (beanPropertyWriter != null) {
                beanPropertyWriterArr2[i] = beanPropertyWriter.rename(nameTransformer);
            }
        }
        return beanPropertyWriterArr2;
    }

    public void resolve(SerializerProvider serializerProvider) throws JsonMappingException {
        int length;
        BeanPropertyWriter beanPropertyWriter;
        TypeSerializer typeSerializer;
        JsonSerializer<Object> findNullValueSerializer;
        BeanPropertyWriter beanPropertyWriter2;
        if (this._filteredProps == null) {
            length = 0;
        } else {
            length = this._filteredProps.length;
        }
        int length2 = this._props.length;
        for (int i = 0; i < length2; i++) {
            BeanPropertyWriter beanPropertyWriter3 = this._props[i];
            if (!beanPropertyWriter3.willSuppressNulls() && !beanPropertyWriter3.hasNullSerializer() && (findNullValueSerializer = serializerProvider.findNullValueSerializer(beanPropertyWriter3)) != null) {
                beanPropertyWriter3.assignNullSerializer(findNullValueSerializer);
                if (i < length && (beanPropertyWriter2 = this._filteredProps[i]) != null) {
                    beanPropertyWriter2.assignNullSerializer(findNullValueSerializer);
                }
            }
            if (!beanPropertyWriter3.hasSerializer()) {
                JsonSerializer findConvertingSerializer = findConvertingSerializer(serializerProvider, beanPropertyWriter3);
                if (findConvertingSerializer == null) {
                    JavaType serializationType = beanPropertyWriter3.getSerializationType();
                    if (serializationType == null) {
                        serializationType = serializerProvider.constructType(beanPropertyWriter3.getGenericPropertyType());
                        if (!serializationType.isFinal()) {
                            if (serializationType.isContainerType() || serializationType.containedTypeCount() > 0) {
                                beanPropertyWriter3.setNonTrivialBaseType(serializationType);
                            }
                        }
                    }
                    findConvertingSerializer = serializerProvider.findValueSerializer(serializationType, (BeanProperty) beanPropertyWriter3);
                    if (serializationType.isContainerType() && (typeSerializer = (TypeSerializer) serializationType.getContentType().getTypeHandler()) != null && (findConvertingSerializer instanceof ContainerSerializer)) {
                        findConvertingSerializer = ((ContainerSerializer) findConvertingSerializer).withValueTypeSerializer(typeSerializer);
                    }
                }
                beanPropertyWriter3.assignSerializer(findConvertingSerializer);
                if (i < length && (beanPropertyWriter = this._filteredProps[i]) != null) {
                    beanPropertyWriter.assignSerializer(findConvertingSerializer);
                }
            }
        }
        if (this._anyGetterWriter != null) {
            this._anyGetterWriter.resolve(serializerProvider);
        }
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<Object> findConvertingSerializer(SerializerProvider serializerProvider, BeanPropertyWriter beanPropertyWriter) throws JsonMappingException {
        Object findSerializationConverter;
        AnnotationIntrospector annotationIntrospector = serializerProvider.getAnnotationIntrospector();
        if (annotationIntrospector == null || (findSerializationConverter = annotationIntrospector.findSerializationConverter(beanPropertyWriter.getMember())) == null) {
            return null;
        }
        Converter<Object, Object> converterInstance = serializerProvider.converterInstance(beanPropertyWriter.getMember(), findSerializationConverter);
        JavaType outputType = converterInstance.getOutputType(serializerProvider.getTypeFactory());
        return new StdDelegatingSerializer(converterInstance, outputType, serializerProvider.findValueSerializer(outputType, (BeanProperty) beanPropertyWriter));
    }

    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        ObjectIdWriter objectIdWriter;
        String[] strArr;
        BeanSerializerBase beanSerializerBase;
        JsonFormat.Value findFormat;
        ObjectIdWriter withSerializer;
        JsonFormat.Shape shape = null;
        ObjectIdWriter objectIdWriter2 = this._objectIdWriter;
        AnnotationIntrospector annotationIntrospector = serializerProvider.getAnnotationIntrospector();
        AnnotatedMember member = (beanProperty == null || annotationIntrospector == null) ? null : beanProperty.getMember();
        if (member != null) {
            String[] findPropertiesToIgnore = annotationIntrospector.findPropertiesToIgnore(member);
            ObjectIdInfo findObjectIdInfo = annotationIntrospector.findObjectIdInfo(member);
            if (findObjectIdInfo != null) {
                ObjectIdInfo findObjectReferenceInfo = annotationIntrospector.findObjectReferenceInfo(member, findObjectIdInfo);
                Class<? extends ObjectIdGenerator<?>> generatorType = findObjectReferenceInfo.getGeneratorType();
                JavaType javaType = serializerProvider.getTypeFactory().findTypeParameters(serializerProvider.constructType(generatorType), (Class<?>) ObjectIdGenerator.class)[0];
                if (generatorType == ObjectIdGenerators.PropertyGenerator.class) {
                    String propertyName = findObjectReferenceInfo.getPropertyName();
                    int length = this._props.length;
                    int i = 0;
                    while (i != length) {
                        BeanPropertyWriter beanPropertyWriter = this._props[i];
                        if (propertyName.equals(beanPropertyWriter.getName())) {
                            if (i > 0) {
                                System.arraycopy(this._props, 0, this._props, 1, i);
                                this._props[0] = beanPropertyWriter;
                                if (this._filteredProps != null) {
                                    BeanPropertyWriter beanPropertyWriter2 = this._filteredProps[i];
                                    System.arraycopy(this._filteredProps, 0, this._filteredProps, 1, i);
                                    this._filteredProps[0] = beanPropertyWriter2;
                                }
                            }
                            String[] strArr2 = findPropertiesToIgnore;
                            objectIdWriter = ObjectIdWriter.construct(beanPropertyWriter.getType(), (String) null, new PropertyBasedObjectIdGenerator(findObjectReferenceInfo, beanPropertyWriter), findObjectReferenceInfo.getAlwaysAsId());
                            strArr = strArr2;
                        } else {
                            i++;
                        }
                    }
                    throw new IllegalArgumentException("Invalid Object Id definition for " + this._handledType.getName() + ": can not find property with name '" + propertyName + "'");
                }
                String[] strArr3 = findPropertiesToIgnore;
                objectIdWriter = ObjectIdWriter.construct(javaType, findObjectReferenceInfo.getPropertyName(), serializerProvider.objectIdGeneratorInstance(member, findObjectReferenceInfo), findObjectReferenceInfo.getAlwaysAsId());
                strArr = strArr3;
            } else if (objectIdWriter2 != null) {
                String[] strArr4 = findPropertiesToIgnore;
                objectIdWriter = this._objectIdWriter.withAlwaysAsId(annotationIntrospector.findObjectReferenceInfo(member, new ObjectIdInfo("", (Class<?>) null, (Class<? extends ObjectIdGenerator<?>>) null)).getAlwaysAsId());
                strArr = strArr4;
            } else {
                String[] strArr5 = findPropertiesToIgnore;
                objectIdWriter = objectIdWriter2;
                strArr = strArr5;
            }
        } else {
            objectIdWriter = objectIdWriter2;
            strArr = null;
        }
        if (objectIdWriter == null || (withSerializer = objectIdWriter.withSerializer(serializerProvider.findValueSerializer(objectIdWriter.idType, beanProperty))) == this._objectIdWriter) {
            beanSerializerBase = this;
        } else {
            beanSerializerBase = withObjectIdWriter(withSerializer);
        }
        if (!(strArr == null || strArr.length == 0)) {
            beanSerializerBase = beanSerializerBase.withIgnorals(strArr);
        }
        if (!(member == null || (findFormat = annotationIntrospector.findFormat((Annotated) member)) == null)) {
            shape = findFormat.getShape();
        }
        if (shape == null) {
            shape = this._serializationShape;
        }
        if (shape == JsonFormat.Shape.ARRAY) {
            return beanSerializerBase.asArraySerializer();
        }
        return beanSerializerBase;
    }

    public boolean usesObjectId() {
        return this._objectIdWriter != null;
    }

    public void serializeWithType(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        if (this._objectIdWriter != null) {
            _serializeWithObjectId(obj, jsonGenerator, serializerProvider, typeSerializer);
            return;
        }
        String _customTypeId = this._typeId == null ? null : _customTypeId(obj);
        if (_customTypeId == null) {
            typeSerializer.writeTypePrefixForObject(obj, jsonGenerator);
        } else {
            typeSerializer.writeCustomTypePrefixForObject(obj, jsonGenerator, _customTypeId);
        }
        if (this._propertyFilterId != null) {
            serializeFieldsFiltered(obj, jsonGenerator, serializerProvider);
        } else {
            serializeFields(obj, jsonGenerator, serializerProvider);
        }
        if (_customTypeId == null) {
            typeSerializer.writeTypeSuffixForObject(obj, jsonGenerator);
        } else {
            typeSerializer.writeCustomTypeSuffixForObject(obj, jsonGenerator, _customTypeId);
        }
    }

    /* access modifiers changed from: protected */
    public final void _serializeWithObjectId(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, boolean z) throws IOException, JsonGenerationException {
        ObjectIdWriter objectIdWriter = this._objectIdWriter;
        WritableObjectId findObjectId = serializerProvider.findObjectId(obj, objectIdWriter.generator);
        if (!findObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter)) {
            Object generateId = findObjectId.generateId(obj);
            if (objectIdWriter.alwaysAsId) {
                objectIdWriter.serializer.serialize(generateId, jsonGenerator, serializerProvider);
                return;
            }
            if (z) {
                jsonGenerator.writeStartObject();
            }
            findObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
            if (this._propertyFilterId != null) {
                serializeFieldsFiltered(obj, jsonGenerator, serializerProvider);
            } else {
                serializeFields(obj, jsonGenerator, serializerProvider);
            }
            if (z) {
                jsonGenerator.writeEndObject();
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void _serializeWithObjectId(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        ObjectIdWriter objectIdWriter = this._objectIdWriter;
        WritableObjectId findObjectId = serializerProvider.findObjectId(obj, objectIdWriter.generator);
        if (!findObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter)) {
            Object generateId = findObjectId.generateId(obj);
            if (objectIdWriter.alwaysAsId) {
                objectIdWriter.serializer.serialize(generateId, jsonGenerator, serializerProvider);
                return;
            }
            String _customTypeId = this._typeId == null ? null : _customTypeId(obj);
            if (_customTypeId == null) {
                typeSerializer.writeTypePrefixForObject(obj, jsonGenerator);
            } else {
                typeSerializer.writeCustomTypePrefixForObject(obj, jsonGenerator, _customTypeId);
            }
            findObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
            if (this._propertyFilterId != null) {
                serializeFieldsFiltered(obj, jsonGenerator, serializerProvider);
            } else {
                serializeFields(obj, jsonGenerator, serializerProvider);
            }
            if (_customTypeId == null) {
                typeSerializer.writeTypeSuffixForObject(obj, jsonGenerator);
            } else {
                typeSerializer.writeCustomTypeSuffixForObject(obj, jsonGenerator, _customTypeId);
            }
        }
    }

    private final String _customTypeId(Object obj) {
        Object value = this._typeId.getValue(obj);
        if (value == null) {
            return "";
        }
        return value instanceof String ? (String) value : value.toString();
    }

    /* access modifiers changed from: protected */
    public void serializeFields(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        BeanPropertyWriter[] beanPropertyWriterArr;
        if (this._filteredProps == null || serializerProvider.getActiveView() == null) {
            beanPropertyWriterArr = this._props;
        } else {
            beanPropertyWriterArr = this._filteredProps;
        }
        int i = 0;
        try {
            int length = beanPropertyWriterArr.length;
            while (i < length) {
                BeanPropertyWriter beanPropertyWriter = beanPropertyWriterArr[i];
                if (beanPropertyWriter != null) {
                    beanPropertyWriter.serializeAsField(obj, jsonGenerator, serializerProvider);
                }
                i++;
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndSerialize(obj, jsonGenerator, serializerProvider);
            }
        } catch (Exception e) {
            wrapAndThrow(serializerProvider, (Throwable) e, obj, i == beanPropertyWriterArr.length ? "[anySetter]" : beanPropertyWriterArr[i].getName());
        } catch (StackOverflowError e2) {
            JsonMappingException jsonMappingException = new JsonMappingException("Infinite recursion (StackOverflowError)", (Throwable) e2);
            jsonMappingException.prependPath(new JsonMappingException.Reference(obj, i == beanPropertyWriterArr.length ? "[anySetter]" : beanPropertyWriterArr[i].getName()));
            throw jsonMappingException;
        }
    }

    /* access modifiers changed from: protected */
    public void serializeFieldsFiltered(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        BeanPropertyWriter[] beanPropertyWriterArr;
        if (this._filteredProps == null || serializerProvider.getActiveView() == null) {
            beanPropertyWriterArr = this._props;
        } else {
            beanPropertyWriterArr = this._filteredProps;
        }
        BeanPropertyFilter findFilter = findFilter(serializerProvider);
        if (findFilter == null) {
            serializeFields(obj, jsonGenerator, serializerProvider);
            return;
        }
        try {
            for (BeanPropertyWriter beanPropertyWriter : beanPropertyWriterArr) {
                if (beanPropertyWriter != null) {
                    findFilter.serializeAsField(obj, jsonGenerator, serializerProvider, beanPropertyWriter);
                }
            }
            if (this._anyGetterWriter != null) {
                this._anyGetterWriter.getAndSerialize(obj, jsonGenerator, serializerProvider);
            }
        } catch (Exception e) {
            wrapAndThrow(serializerProvider, (Throwable) e, obj, 0 == beanPropertyWriterArr.length ? "[anySetter]" : beanPropertyWriterArr[0].getName());
        } catch (StackOverflowError e2) {
            JsonMappingException jsonMappingException = new JsonMappingException("Infinite recursion (StackOverflowError)", (Throwable) e2);
            jsonMappingException.prependPath(new JsonMappingException.Reference(obj, 0 == beanPropertyWriterArr.length ? "[anySetter]" : beanPropertyWriterArr[0].getName()));
            throw jsonMappingException;
        }
    }

    /* access modifiers changed from: protected */
    public BeanPropertyFilter findFilter(SerializerProvider serializerProvider) throws JsonMappingException {
        Object obj = this._propertyFilterId;
        FilterProvider filterProvider = serializerProvider.getFilterProvider();
        if (filterProvider != null) {
            return filterProvider.findFilter(obj);
        }
        throw new JsonMappingException("Can not resolve BeanPropertyFilter with id '" + obj + "'; no FilterProvider configured");
    }

    public JsonNode getSchema(SerializerProvider serializerProvider, Type type) throws JsonMappingException {
        BeanPropertyFilter beanPropertyFilter;
        String id;
        ObjectNode createSchemaNode = createSchemaNode("object", true);
        JsonSerializableSchema jsonSerializableSchema = (JsonSerializableSchema) this._handledType.getAnnotation(JsonSerializableSchema.class);
        if (!(jsonSerializableSchema == null || (id = jsonSerializableSchema.mo15649id()) == null || id.length() <= 0)) {
            createSchemaNode.put("id", id);
        }
        ObjectNode objectNode = createSchemaNode.objectNode();
        if (this._propertyFilterId != null) {
            beanPropertyFilter = findFilter(serializerProvider);
        } else {
            beanPropertyFilter = null;
        }
        for (BeanPropertyWriter beanPropertyWriter : this._props) {
            if (beanPropertyFilter == null) {
                beanPropertyWriter.depositSchemaProperty(objectNode, serializerProvider);
            } else {
                beanPropertyFilter.depositSchemaProperty(beanPropertyWriter, objectNode, serializerProvider);
            }
        }
        createSchemaNode.put("properties", (JsonNode) objectNode);
        return createSchemaNode;
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper jsonFormatVisitorWrapper, JavaType javaType) throws JsonMappingException {
        int i = 0;
        JsonObjectFormatVisitor expectObjectFormat = jsonFormatVisitorWrapper == null ? null : jsonFormatVisitorWrapper.expectObjectFormat(javaType);
        if (expectObjectFormat == null) {
            return;
        }
        if (this._propertyFilterId != null) {
            BeanPropertyFilter findFilter = findFilter(jsonFormatVisitorWrapper.getProvider());
            while (i < this._props.length) {
                findFilter.depositSchemaProperty(this._props[i], expectObjectFormat, jsonFormatVisitorWrapper.getProvider());
                i++;
            }
            return;
        }
        while (i < this._props.length) {
            this._props[i].depositSchemaProperty(expectObjectFormat);
            i++;
        }
    }
}
