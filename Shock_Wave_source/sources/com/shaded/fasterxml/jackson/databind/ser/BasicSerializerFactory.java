package com.shaded.fasterxml.jackson.databind.ser;

import com.shaded.fasterxml.jackson.annotation.JsonFormat;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.BeanDescription;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonSerializable;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.MapperFeature;
import com.shaded.fasterxml.jackson.databind.SerializationConfig;
import com.shaded.fasterxml.jackson.databind.SerializerProvider;
import com.shaded.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaded.fasterxml.jackson.databind.annotation.NoClass;
import com.shaded.fasterxml.jackson.databind.cfg.MapperConfig;
import com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.shaded.fasterxml.jackson.databind.ext.OptionalHandlerFactory;
import com.shaded.fasterxml.jackson.databind.introspect.Annotated;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.shaded.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.shaded.fasterxml.jackson.databind.jsontype.NamedType;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.BooleanSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.InetAddressSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.shaded.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.SqlDateSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.SqlTimeSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.StdContainerSerializers;
import com.shaded.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.StdJdkSerializers;
import com.shaded.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.TimeZoneSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shaded.fasterxml.jackson.databind.ser.std.TokenBufferSerializer;
import com.shaded.fasterxml.jackson.databind.type.ArrayType;
import com.shaded.fasterxml.jackson.databind.type.CollectionLikeType;
import com.shaded.fasterxml.jackson.databind.type.CollectionType;
import com.shaded.fasterxml.jackson.databind.type.MapLikeType;
import com.shaded.fasterxml.jackson.databind.type.MapType;
import com.shaded.fasterxml.jackson.databind.type.TypeFactory;
import com.shaded.fasterxml.jackson.databind.util.ClassUtil;
import com.shaded.fasterxml.jackson.databind.util.Converter;
import com.shaded.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.TimeZone;

public abstract class BasicSerializerFactory extends SerializerFactory implements Serializable {
    protected static final HashMap<String, JsonSerializer<?>> _concrete = new HashMap<>();
    protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy = new HashMap<>();
    protected final SerializerFactoryConfig _factoryConfig;

    public abstract JsonSerializer<Object> createSerializer(SerializerProvider serializerProvider, JavaType javaType) throws JsonMappingException;

    /* access modifiers changed from: protected */
    public abstract Iterable<Serializers> customSerializers();

    public abstract SerializerFactory withConfig(SerializerFactoryConfig serializerFactoryConfig);

    static {
        _concrete.put(String.class.getName(), new StringSerializer());
        ToStringSerializer toStringSerializer = ToStringSerializer.instance;
        _concrete.put(StringBuffer.class.getName(), toStringSerializer);
        _concrete.put(StringBuilder.class.getName(), toStringSerializer);
        _concrete.put(Character.class.getName(), toStringSerializer);
        _concrete.put(Character.TYPE.getName(), toStringSerializer);
        NumberSerializers.addAll(_concrete);
        _concrete.put(Boolean.TYPE.getName(), new BooleanSerializer(true));
        _concrete.put(Boolean.class.getName(), new BooleanSerializer(false));
        NumberSerializers.NumberSerializer numberSerializer = new NumberSerializers.NumberSerializer();
        _concrete.put(BigInteger.class.getName(), numberSerializer);
        _concrete.put(BigDecimal.class.getName(), numberSerializer);
        _concrete.put(Calendar.class.getName(), CalendarSerializer.instance);
        DateSerializer dateSerializer = DateSerializer.instance;
        _concrete.put(Date.class.getName(), dateSerializer);
        _concrete.put(Timestamp.class.getName(), dateSerializer);
        _concreteLazy.put(java.sql.Date.class.getName(), SqlDateSerializer.class);
        _concreteLazy.put(Time.class.getName(), SqlTimeSerializer.class);
        for (Map.Entry next : StdJdkSerializers.all()) {
            Object value = next.getValue();
            if (value instanceof JsonSerializer) {
                _concrete.put(((Class) next.getKey()).getName(), (JsonSerializer) value);
            } else if (value instanceof Class) {
                _concreteLazy.put(((Class) next.getKey()).getName(), (Class) value);
            } else {
                throw new IllegalStateException("Internal error: unrecognized value of type " + next.getClass().getName());
            }
        }
        _concreteLazy.put(TokenBuffer.class.getName(), TokenBufferSerializer.class);
    }

    protected BasicSerializerFactory(SerializerFactoryConfig serializerFactoryConfig) {
        this._factoryConfig = serializerFactoryConfig == null ? new SerializerFactoryConfig() : serializerFactoryConfig;
    }

    public SerializerFactoryConfig getFactoryConfig() {
        return this._factoryConfig;
    }

    public final SerializerFactory withAdditionalSerializers(Serializers serializers) {
        return withConfig(this._factoryConfig.withAdditionalSerializers(serializers));
    }

    public final SerializerFactory withAdditionalKeySerializers(Serializers serializers) {
        return withConfig(this._factoryConfig.withAdditionalKeySerializers(serializers));
    }

    public final SerializerFactory withSerializerModifier(BeanSerializerModifier beanSerializerModifier) {
        return withConfig(this._factoryConfig.withSerializerModifier(beanSerializerModifier));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0059, code lost:
        r7 = r0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:3:0x001b A[LOOP:0: B:3:0x001b->B:6:0x002b, LOOP_START, PHI: r0 
      PHI: (r0v10 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) = (r0v1 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>), (r0v13 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) binds: [B:2:0x0011, B:6:0x002b] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> createKeySerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig r5, com.shaded.fasterxml.jackson.databind.JavaType r6, com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r7) {
        /*
            r4 = this;
            java.lang.Class r0 = r6.getRawClass()
            com.shaded.fasterxml.jackson.databind.BeanDescription r1 = r5.introspectClassAnnotations((java.lang.Class<?>) r0)
            r0 = 0
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r2 = r4._factoryConfig
            boolean r2 = r2.hasKeySerializers()
            if (r2 == 0) goto L_0x002d
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r2 = r4._factoryConfig
            java.lang.Iterable r2 = r2.keySerializers()
            java.util.Iterator r2 = r2.iterator()
        L_0x001b:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x002d
            java.lang.Object r0 = r2.next()
            com.shaded.fasterxml.jackson.databind.ser.Serializers r0 = (com.shaded.fasterxml.jackson.databind.ser.Serializers) r0
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.findSerializer(r5, r6, r1)
            if (r0 == 0) goto L_0x001b
        L_0x002d:
            if (r0 != 0) goto L_0x0059
            if (r7 != 0) goto L_0x0035
            com.shaded.fasterxml.jackson.databind.JsonSerializer r7 = com.shaded.fasterxml.jackson.databind.ser.std.StdKeySerializers.getStdKeySerializer(r6)
        L_0x0035:
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r0 = r4._factoryConfig
            boolean r0 = r0.hasSerializerModifiers()
            if (r0 == 0) goto L_0x0058
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r0 = r4._factoryConfig
            java.lang.Iterable r0 = r0.serializerModifiers()
            java.util.Iterator r2 = r0.iterator()
        L_0x0047:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x0058
            java.lang.Object r0 = r2.next()
            com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier r0 = (com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier) r0
            com.shaded.fasterxml.jackson.databind.JsonSerializer r7 = r0.modifyKeySerializer(r5, r6, r1, r7)
            goto L_0x0047
        L_0x0058:
            return r7
        L_0x0059:
            r7 = r0
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.BasicSerializerFactory.createKeySerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig, com.shaded.fasterxml.jackson.databind.JavaType, com.shaded.fasterxml.jackson.databind.JsonSerializer):com.shaded.fasterxml.jackson.databind.JsonSerializer");
    }

    public TypeSerializer createTypeSerializer(SerializationConfig serializationConfig, JavaType javaType) {
        Collection<NamedType> collectAndResolveSubtypes;
        AnnotatedClass classInfo = serializationConfig.introspectClassAnnotations(javaType.getRawClass()).getClassInfo();
        AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder<?> findTypeResolver = annotationIntrospector.findTypeResolver(serializationConfig, classInfo, javaType);
        if (findTypeResolver == null) {
            findTypeResolver = serializationConfig.getDefaultTyper(javaType);
            collectAndResolveSubtypes = null;
        } else {
            collectAndResolveSubtypes = serializationConfig.getSubtypeResolver().collectAndResolveSubtypes(classInfo, (MapperConfig<?>) serializationConfig, annotationIntrospector);
        }
        if (findTypeResolver == null) {
            return null;
        }
        return findTypeResolver.buildTypeSerializer(serializationConfig, javaType, collectAndResolveSubtypes);
    }

    public final JsonSerializer<?> getNullSerializer() {
        return NullSerializer.instance;
    }

    /* access modifiers changed from: protected */
    public final JsonSerializer<?> findSerializerByLookup(JavaType javaType, SerializationConfig serializationConfig, BeanDescription beanDescription, boolean z) {
        Class cls;
        String name = javaType.getRawClass().getName();
        JsonSerializer<?> jsonSerializer = _concrete.get(name);
        if (jsonSerializer != null || (cls = _concreteLazy.get(name)) == null) {
            return jsonSerializer;
        }
        try {
            return (JsonSerializer) cls.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate standard serializer (of type " + cls.getName() + "): " + e.getMessage(), e);
        }
    }

    /* access modifiers changed from: protected */
    public final JsonSerializer<?> findSerializerByAnnotations(SerializerProvider serializerProvider, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        if (JsonSerializable.class.isAssignableFrom(javaType.getRawClass())) {
            return SerializableSerializer.instance;
        }
        AnnotatedMethod findJsonValueMethod = beanDescription.findJsonValueMethod();
        if (findJsonValueMethod == null) {
            return null;
        }
        Method annotated = findJsonValueMethod.getAnnotated();
        if (serializerProvider.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(annotated);
        }
        return new JsonValueSerializer(annotated, findSerializerFromAnnotation(serializerProvider, findJsonValueMethod));
    }

    /* access modifiers changed from: protected */
    public final JsonSerializer<?> findSerializerByPrimaryType(SerializerProvider serializerProvider, JavaType javaType, BeanDescription beanDescription, boolean z) throws JsonMappingException {
        Class<?> rawClass = javaType.getRawClass();
        if (InetAddress.class.isAssignableFrom(rawClass)) {
            return InetAddressSerializer.instance;
        }
        if (TimeZone.class.isAssignableFrom(rawClass)) {
            return TimeZoneSerializer.instance;
        }
        if (Charset.class.isAssignableFrom(rawClass)) {
            return ToStringSerializer.instance;
        }
        JsonSerializer<?> findOptionalStdSerializer = findOptionalStdSerializer(serializerProvider, javaType, beanDescription, z);
        if (findOptionalStdSerializer != null) {
            return findOptionalStdSerializer;
        }
        if (Number.class.isAssignableFrom(rawClass)) {
            return NumberSerializers.NumberSerializer.instance;
        }
        if (Enum.class.isAssignableFrom(rawClass)) {
            return buildEnumSerializer(serializerProvider.getConfig(), javaType, beanDescription);
        }
        if (Calendar.class.isAssignableFrom(rawClass)) {
            return CalendarSerializer.instance;
        }
        if (Date.class.isAssignableFrom(rawClass)) {
            return DateSerializer.instance;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<?> findOptionalStdSerializer(SerializerProvider serializerProvider, JavaType javaType, BeanDescription beanDescription, boolean z) throws JsonMappingException {
        return OptionalHandlerFactory.instance.findSerializer(serializerProvider.getConfig(), javaType, beanDescription);
    }

    /* access modifiers changed from: protected */
    public final JsonSerializer<?> findSerializerByAddonType(SerializationConfig serializationConfig, JavaType javaType, BeanDescription beanDescription, boolean z) throws JsonMappingException {
        Class<?> rawClass = javaType.getRawClass();
        if (Iterator.class.isAssignableFrom(rawClass)) {
            return buildIteratorSerializer(serializationConfig, javaType, beanDescription, z);
        }
        if (Iterable.class.isAssignableFrom(rawClass)) {
            return buildIterableSerializer(serializationConfig, javaType, beanDescription, z);
        }
        if (CharSequence.class.isAssignableFrom(rawClass)) {
            return ToStringSerializer.instance;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<Object> findSerializerFromAnnotation(SerializerProvider serializerProvider, Annotated annotated) throws JsonMappingException {
        Object findSerializer = serializerProvider.getAnnotationIntrospector().findSerializer(annotated);
        if (findSerializer == null) {
            return null;
        }
        return findConvertingSerializer(serializerProvider, annotated, serializerProvider.serializerInstance(annotated, findSerializer));
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<?> findConvertingSerializer(SerializerProvider serializerProvider, Annotated annotated, JsonSerializer<?> jsonSerializer) throws JsonMappingException {
        Converter<Object, Object> findConverter = findConverter(serializerProvider, annotated);
        return findConverter == null ? jsonSerializer : new StdDelegatingSerializer(findConverter, findConverter.getOutputType(serializerProvider.getTypeFactory()), jsonSerializer);
    }

    /* access modifiers changed from: protected */
    public Converter<Object, Object> findConverter(SerializerProvider serializerProvider, Annotated annotated) throws JsonMappingException {
        Object findSerializationConverter = serializerProvider.getAnnotationIntrospector().findSerializationConverter(annotated);
        if (findSerializationConverter == null) {
            return null;
        }
        return serializerProvider.converterInstance(annotated, findSerializationConverter);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final JsonSerializer<?> buildContainerSerializer(SerializerProvider serializerProvider, JavaType javaType, BeanDescription beanDescription, BeanProperty beanProperty, boolean z) throws JsonMappingException {
        return buildContainerSerializer(serializerProvider, javaType, beanDescription, z);
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<?> buildContainerSerializer(SerializerProvider serializerProvider, JavaType javaType, BeanDescription beanDescription, boolean z) throws JsonMappingException {
        boolean z2;
        JsonSerializer<?> jsonSerializer;
        SerializationConfig config = serializerProvider.getConfig();
        if (!z && javaType.useStaticType() && (!javaType.isContainerType() || javaType.getContentType().getRawClass() != Object.class)) {
            z = true;
        }
        TypeSerializer createTypeSerializer = createTypeSerializer(config, javaType.getContentType());
        if (createTypeSerializer != null) {
            z2 = false;
        } else {
            z2 = z;
        }
        JsonSerializer<Object> _findContentSerializer = _findContentSerializer(serializerProvider, beanDescription.getClassInfo());
        if (javaType.isMapLikeType()) {
            MapLikeType mapLikeType = (MapLikeType) javaType;
            JsonSerializer<Object> _findKeySerializer = _findKeySerializer(serializerProvider, beanDescription.getClassInfo());
            if (mapLikeType.isTrueMapType()) {
                return buildMapSerializer(config, (MapType) mapLikeType, beanDescription, z2, _findKeySerializer, createTypeSerializer, _findContentSerializer);
            }
            for (Serializers findMapLikeSerializer : customSerializers()) {
                MapLikeType mapLikeType2 = (MapLikeType) javaType;
                jsonSerializer = findMapLikeSerializer.findMapLikeSerializer(config, mapLikeType2, beanDescription, _findKeySerializer, createTypeSerializer, _findContentSerializer);
                if (jsonSerializer != null) {
                    if (this._factoryConfig.hasSerializerModifiers()) {
                        Iterator<BeanSerializerModifier> it = this._factoryConfig.serializerModifiers().iterator();
                        while (true) {
                            JsonSerializer<?> jsonSerializer2 = jsonSerializer;
                            if (!it.hasNext()) {
                                return jsonSerializer2;
                            }
                            jsonSerializer = it.next().modifyMapLikeSerializer(config, mapLikeType2, beanDescription, jsonSerializer2);
                        }
                    }
                }
            }
            return null;
        } else if (javaType.isCollectionLikeType()) {
            CollectionLikeType collectionLikeType = (CollectionLikeType) javaType;
            if (collectionLikeType.isTrueCollectionType()) {
                return buildCollectionSerializer(config, (CollectionType) collectionLikeType, beanDescription, z2, createTypeSerializer, _findContentSerializer);
            }
            CollectionLikeType collectionLikeType2 = (CollectionLikeType) javaType;
            for (Serializers findCollectionLikeSerializer : customSerializers()) {
                jsonSerializer = findCollectionLikeSerializer.findCollectionLikeSerializer(config, collectionLikeType2, beanDescription, createTypeSerializer, _findContentSerializer);
                if (jsonSerializer != null) {
                    if (this._factoryConfig.hasSerializerModifiers()) {
                        Iterator<BeanSerializerModifier> it2 = this._factoryConfig.serializerModifiers().iterator();
                        while (true) {
                            JsonSerializer<?> jsonSerializer3 = jsonSerializer;
                            if (!it2.hasNext()) {
                                return jsonSerializer3;
                            }
                            jsonSerializer = it2.next().modifyCollectionLikeSerializer(config, collectionLikeType2, beanDescription, jsonSerializer3);
                        }
                    }
                }
            }
            return null;
        } else if (!javaType.isArrayType()) {
            return null;
        } else {
            return buildArraySerializer(config, (ArrayType) javaType, beanDescription, z2, createTypeSerializer, _findContentSerializer);
        }
        return jsonSerializer;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final JsonSerializer<?> buildCollectionSerializer(SerializationConfig serializationConfig, CollectionType collectionType, BeanDescription beanDescription, BeanProperty beanProperty, boolean z, TypeSerializer typeSerializer, JsonSerializer<Object> jsonSerializer) throws JsonMappingException {
        return buildCollectionSerializer(serializationConfig, collectionType, beanDescription, z, typeSerializer, jsonSerializer);
    }

    /* JADX WARNING: type inference failed for: r14v0, types: [com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object>, java.lang.Object, com.shaded.fasterxml.jackson.databind.JsonSerializer] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:1:0x000a A[LOOP:0: B:1:0x000a->B:4:0x001f, LOOP_START, PHI: r0 
      PHI: (r0v2 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) = (r0v1 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>), (r0v21 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) binds: [B:0:0x0000, B:4:0x001f] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonSerializer<?> buildCollectionSerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig r9, com.shaded.fasterxml.jackson.databind.type.CollectionType r10, com.shaded.fasterxml.jackson.databind.BeanDescription r11, boolean r12, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r13, com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r14) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r8 = this;
            r6 = 0
            java.lang.Iterable r0 = r8.customSerializers()
            java.util.Iterator r7 = r0.iterator()
            r0 = r6
        L_0x000a:
            boolean r1 = r7.hasNext()
            if (r1 == 0) goto L_0x0021
            java.lang.Object r0 = r7.next()
            com.shaded.fasterxml.jackson.databind.ser.Serializers r0 = (com.shaded.fasterxml.jackson.databind.ser.Serializers) r0
            r1 = r9
            r2 = r10
            r3 = r11
            r4 = r13
            r5 = r14
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.findCollectionSerializer(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L_0x000a
        L_0x0021:
            if (r0 != 0) goto L_0x004c
            com.shaded.fasterxml.jackson.annotation.JsonFormat$Value r1 = r11.findExpectedFormat(r6)
            if (r1 == 0) goto L_0x0032
            com.shaded.fasterxml.jackson.annotation.JsonFormat$Shape r1 = r1.getShape()
            com.shaded.fasterxml.jackson.annotation.JsonFormat$Shape r2 = com.shaded.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT
            if (r1 != r2) goto L_0x0032
        L_0x0031:
            return r6
        L_0x0032:
            java.lang.Class r1 = r10.getRawClass()
            java.lang.Class<java.util.EnumSet> r2 = java.util.EnumSet.class
            boolean r2 = r2.isAssignableFrom(r1)
            if (r2 == 0) goto L_0x0071
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r10.getContentType()
            boolean r1 = r0.isEnumType()
            if (r1 != 0) goto L_0x00b4
        L_0x0048:
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = com.shaded.fasterxml.jackson.databind.ser.std.StdContainerSerializers.enumSetSerializer(r6)
        L_0x004c:
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r8._factoryConfig
            boolean r1 = r1.hasSerializerModifiers()
            if (r1 == 0) goto L_0x00b0
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r8._factoryConfig
            java.lang.Iterable r1 = r1.serializerModifiers()
            java.util.Iterator r2 = r1.iterator()
            r1 = r0
        L_0x005f:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x00b1
            java.lang.Object r0 = r2.next()
            com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier r0 = (com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier) r0
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.modifyCollectionSerializer(r9, r10, r11, r1)
            r1 = r0
            goto L_0x005f
        L_0x0071:
            com.shaded.fasterxml.jackson.databind.JavaType r2 = r10.getContentType()
            java.lang.Class r2 = r2.getRawClass()
            boolean r1 = r8.isIndexedList(r1)
            if (r1 == 0) goto L_0x00a1
            java.lang.Class<java.lang.String> r1 = java.lang.String.class
            if (r2 != r1) goto L_0x0098
            if (r14 == 0) goto L_0x008b
            boolean r1 = com.shaded.fasterxml.jackson.databind.util.ClassUtil.isJacksonStdImpl((java.lang.Object) r14)
            if (r1 == 0) goto L_0x008d
        L_0x008b:
            com.shaded.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer r0 = com.shaded.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer.instance
        L_0x008d:
            if (r0 != 0) goto L_0x004c
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r10.getContentType()
            com.shaded.fasterxml.jackson.databind.ser.ContainerSerializer r0 = com.shaded.fasterxml.jackson.databind.ser.std.StdContainerSerializers.collectionSerializer(r0, r12, r13, r14)
            goto L_0x004c
        L_0x0098:
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r10.getContentType()
            com.shaded.fasterxml.jackson.databind.ser.ContainerSerializer r0 = com.shaded.fasterxml.jackson.databind.ser.std.StdContainerSerializers.indexedListSerializer(r0, r12, r13, r14)
            goto L_0x008d
        L_0x00a1:
            java.lang.Class<java.lang.String> r1 = java.lang.String.class
            if (r2 != r1) goto L_0x008d
            if (r14 == 0) goto L_0x00ad
            boolean r1 = com.shaded.fasterxml.jackson.databind.util.ClassUtil.isJacksonStdImpl((java.lang.Object) r14)
            if (r1 == 0) goto L_0x008d
        L_0x00ad:
            com.shaded.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer r0 = com.shaded.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer.instance
            goto L_0x008d
        L_0x00b0:
            r1 = r0
        L_0x00b1:
            r6 = r1
            goto L_0x0031
        L_0x00b4:
            r6 = r0
            goto L_0x0048
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.BasicSerializerFactory.buildCollectionSerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig, com.shaded.fasterxml.jackson.databind.type.CollectionType, com.shaded.fasterxml.jackson.databind.BeanDescription, boolean, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer, com.shaded.fasterxml.jackson.databind.JsonSerializer):com.shaded.fasterxml.jackson.databind.JsonSerializer");
    }

    /* access modifiers changed from: protected */
    public boolean isIndexedList(Class<?> cls) {
        return RandomAccess.class.isAssignableFrom(cls);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:1:0x0009 A[LOOP:0: B:1:0x0009->B:4:0x001f, LOOP_START, PHI: r0 
      PHI: (r0v1 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) = (r0v0 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>), (r0v19 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) binds: [B:0:0x0000, B:4:0x001f] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonSerializer<?> buildMapSerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig r9, com.shaded.fasterxml.jackson.databind.type.MapType r10, com.shaded.fasterxml.jackson.databind.BeanDescription r11, boolean r12, com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r13, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r14, com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r15) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r8 = this;
            r0 = 0
            java.lang.Iterable r1 = r8.customSerializers()
            java.util.Iterator r7 = r1.iterator()
        L_0x0009:
            boolean r1 = r7.hasNext()
            if (r1 == 0) goto L_0x0021
            java.lang.Object r0 = r7.next()
            com.shaded.fasterxml.jackson.databind.ser.Serializers r0 = (com.shaded.fasterxml.jackson.databind.ser.Serializers) r0
            r1 = r9
            r2 = r10
            r3 = r11
            r4 = r13
            r5 = r14
            r6 = r15
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.findMapSerializer(r1, r2, r3, r4, r5, r6)
            if (r0 == 0) goto L_0x0009
        L_0x0021:
            if (r0 != 0) goto L_0x0052
            java.lang.Class<java.util.EnumMap> r0 = java.util.EnumMap.class
            java.lang.Class r1 = r10.getRawClass()
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 == 0) goto L_0x0077
            com.shaded.fasterxml.jackson.databind.JavaType r0 = r10.getKeyType()
            r3 = 0
            boolean r1 = r0.isEnumType()
            if (r1 == 0) goto L_0x0046
            java.lang.Class r0 = r0.getRawClass()
            com.shaded.fasterxml.jackson.databind.AnnotationIntrospector r1 = r9.getAnnotationIntrospector()
            com.shaded.fasterxml.jackson.databind.util.EnumValues r3 = com.shaded.fasterxml.jackson.databind.util.EnumValues.construct(r0, r1)
        L_0x0046:
            com.shaded.fasterxml.jackson.databind.ser.std.EnumMapSerializer r0 = new com.shaded.fasterxml.jackson.databind.ser.std.EnumMapSerializer
            com.shaded.fasterxml.jackson.databind.JavaType r1 = r10.getContentType()
            r2 = r12
            r4 = r14
            r5 = r15
            r0.<init>(r1, r2, r3, r4, r5)
        L_0x0052:
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r8._factoryConfig
            boolean r1 = r1.hasSerializerModifiers()
            if (r1 == 0) goto L_0x008d
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r8._factoryConfig
            java.lang.Iterable r1 = r1.serializerModifiers()
            java.util.Iterator r2 = r1.iterator()
            r1 = r0
        L_0x0065:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x008e
            java.lang.Object r0 = r2.next()
            com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier r0 = (com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier) r0
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.modifyMapSerializer(r9, r10, r11, r1)
            r1 = r0
            goto L_0x0065
        L_0x0077:
            com.shaded.fasterxml.jackson.databind.AnnotationIntrospector r0 = r9.getAnnotationIntrospector()
            com.shaded.fasterxml.jackson.databind.introspect.AnnotatedClass r1 = r11.getClassInfo()
            java.lang.String[] r0 = r0.findPropertiesToIgnore(r1)
            r1 = r10
            r2 = r12
            r3 = r14
            r4 = r13
            r5 = r15
            com.shaded.fasterxml.jackson.databind.ser.std.MapSerializer r0 = com.shaded.fasterxml.jackson.databind.ser.std.MapSerializer.construct(r0, r1, r2, r3, r4, r5)
            goto L_0x0052
        L_0x008d:
            r1 = r0
        L_0x008e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.BasicSerializerFactory.buildMapSerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig, com.shaded.fasterxml.jackson.databind.type.MapType, com.shaded.fasterxml.jackson.databind.BeanDescription, boolean, com.shaded.fasterxml.jackson.databind.JsonSerializer, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer, com.shaded.fasterxml.jackson.databind.JsonSerializer):com.shaded.fasterxml.jackson.databind.JsonSerializer");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:1:0x0009 A[LOOP:0: B:1:0x0009->B:4:0x001e, LOOP_START, PHI: r0 
      PHI: (r0v1 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) = (r0v0 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>), (r0v16 com.shaded.fasterxml.jackson.databind.JsonSerializer<?>) binds: [B:0:0x0000, B:4:0x001e] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonSerializer<?> buildArraySerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig r8, com.shaded.fasterxml.jackson.databind.type.ArrayType r9, com.shaded.fasterxml.jackson.databind.BeanDescription r10, boolean r11, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer r12, com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object> r13) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r7 = this;
            r0 = 0
            java.lang.Iterable r1 = r7.customSerializers()
            java.util.Iterator r6 = r1.iterator()
        L_0x0009:
            boolean r1 = r6.hasNext()
            if (r1 == 0) goto L_0x0020
            java.lang.Object r0 = r6.next()
            com.shaded.fasterxml.jackson.databind.ser.Serializers r0 = (com.shaded.fasterxml.jackson.databind.ser.Serializers) r0
            r1 = r8
            r2 = r9
            r3 = r10
            r4 = r12
            r5 = r13
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.findArraySerializer(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L_0x0009
        L_0x0020:
            if (r0 != 0) goto L_0x003f
            java.lang.Class r1 = r9.getRawClass()
            if (r13 == 0) goto L_0x002e
            boolean r2 = com.shaded.fasterxml.jackson.databind.util.ClassUtil.isJacksonStdImpl((java.lang.Object) r13)
            if (r2 == 0) goto L_0x0034
        L_0x002e:
            java.lang.Class<java.lang.String[]> r0 = java.lang.String[].class
            if (r0 != r1) goto L_0x0064
            com.shaded.fasterxml.jackson.databind.ser.impl.StringArraySerializer r0 = com.shaded.fasterxml.jackson.databind.ser.impl.StringArraySerializer.instance
        L_0x0034:
            if (r0 != 0) goto L_0x003f
            com.shaded.fasterxml.jackson.databind.ser.std.ObjectArraySerializer r0 = new com.shaded.fasterxml.jackson.databind.ser.std.ObjectArraySerializer
            com.shaded.fasterxml.jackson.databind.JavaType r1 = r9.getContentType()
            r0.<init>((com.shaded.fasterxml.jackson.databind.JavaType) r1, (boolean) r11, (com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer) r12, (com.shaded.fasterxml.jackson.databind.JsonSerializer<java.lang.Object>) r13)
        L_0x003f:
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r7._factoryConfig
            boolean r1 = r1.hasSerializerModifiers()
            if (r1 == 0) goto L_0x0069
            com.shaded.fasterxml.jackson.databind.cfg.SerializerFactoryConfig r1 = r7._factoryConfig
            java.lang.Iterable r1 = r1.serializerModifiers()
            java.util.Iterator r2 = r1.iterator()
            r1 = r0
        L_0x0052:
            boolean r0 = r2.hasNext()
            if (r0 == 0) goto L_0x006a
            java.lang.Object r0 = r2.next()
            com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier r0 = (com.shaded.fasterxml.jackson.databind.ser.BeanSerializerModifier) r0
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = r0.modifyArraySerializer(r8, r9, r10, r1)
            r1 = r0
            goto L_0x0052
        L_0x0064:
            com.shaded.fasterxml.jackson.databind.JsonSerializer r0 = com.shaded.fasterxml.jackson.databind.ser.std.StdArraySerializers.findStandardImpl(r1)
            goto L_0x0034
        L_0x0069:
            r1 = r0
        L_0x006a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ser.BasicSerializerFactory.buildArraySerializer(com.shaded.fasterxml.jackson.databind.SerializationConfig, com.shaded.fasterxml.jackson.databind.type.ArrayType, com.shaded.fasterxml.jackson.databind.BeanDescription, boolean, com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer, com.shaded.fasterxml.jackson.databind.JsonSerializer):com.shaded.fasterxml.jackson.databind.JsonSerializer");
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<?> buildIteratorSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanDescription beanDescription, boolean z) throws JsonMappingException {
        JavaType containedType = javaType.containedType(0);
        if (containedType == null) {
            containedType = TypeFactory.unknownType();
        }
        TypeSerializer createTypeSerializer = createTypeSerializer(serializationConfig, containedType);
        return StdContainerSerializers.iteratorSerializer(containedType, usesStaticTyping(serializationConfig, beanDescription, createTypeSerializer), createTypeSerializer);
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<?> buildIterableSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanDescription beanDescription, boolean z) throws JsonMappingException {
        JavaType containedType = javaType.containedType(0);
        if (containedType == null) {
            containedType = TypeFactory.unknownType();
        }
        TypeSerializer createTypeSerializer = createTypeSerializer(serializationConfig, containedType);
        return StdContainerSerializers.iterableSerializer(containedType, usesStaticTyping(serializationConfig, beanDescription, createTypeSerializer), createTypeSerializer);
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<?> buildEnumSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        JsonFormat.Value findExpectedFormat = beanDescription.findExpectedFormat((JsonFormat.Value) null);
        if (findExpectedFormat == null || findExpectedFormat.getShape() != JsonFormat.Shape.OBJECT) {
            JsonSerializer<?> construct = EnumSerializer.construct(javaType.getRawClass(), serializationConfig, beanDescription, findExpectedFormat);
            if (!this._factoryConfig.hasSerializerModifiers()) {
                return construct;
            }
            Iterator<BeanSerializerModifier> it = this._factoryConfig.serializerModifiers().iterator();
            while (true) {
                JsonSerializer<?> jsonSerializer = construct;
                if (!it.hasNext()) {
                    return jsonSerializer;
                }
                construct = it.next().modifyEnumSerializer(serializationConfig, javaType, beanDescription, jsonSerializer);
            }
        } else {
            ((BasicBeanDescription) beanDescription).removeProperty("declaringClass");
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public <T extends JavaType> T modifyTypeByAnnotation(SerializationConfig serializationConfig, Annotated annotated, T t) {
        Class<?> findSerializationType = serializationConfig.getAnnotationIntrospector().findSerializationType(annotated);
        if (findSerializationType != null) {
            try {
                t = t.widenBy(findSerializationType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to widen type " + t + " with concrete-type annotation (value " + findSerializationType.getName() + "), method '" + annotated.getName() + "': " + e.getMessage());
            }
        }
        return modifySecondaryTypesByAnnotation(serializationConfig, annotated, t);
    }

    protected static <T extends JavaType> T modifySecondaryTypesByAnnotation(SerializationConfig serializationConfig, Annotated annotated, T t) {
        AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        if (!t.isContainerType()) {
            return t;
        }
        Class<?> findSerializationKeyType = annotationIntrospector.findSerializationKeyType(annotated, t.getKeyType());
        if (findSerializationKeyType != null) {
            if (!(t instanceof MapType)) {
                throw new IllegalArgumentException("Illegal key-type annotation: type " + t + " is not a Map type");
            }
            try {
                t = ((MapType) t).widenKey(findSerializationKeyType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Failed to narrow key type " + t + " with key-type annotation (" + findSerializationKeyType.getName() + "): " + e.getMessage());
            }
        }
        Class<?> findSerializationContentType = annotationIntrospector.findSerializationContentType(annotated, t.getContentType());
        if (findSerializationContentType == null) {
            return t;
        }
        try {
            return t.widenContentsBy(findSerializationContentType);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("Failed to narrow content type " + t + " with content-type annotation (" + findSerializationContentType.getName() + "): " + e2.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<Object> _findKeySerializer(SerializerProvider serializerProvider, Annotated annotated) throws JsonMappingException {
        Object findKeySerializer = serializerProvider.getAnnotationIntrospector().findKeySerializer(annotated);
        if (findKeySerializer != null) {
            return serializerProvider.serializerInstance(annotated, findKeySerializer);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public JsonSerializer<Object> _findContentSerializer(SerializerProvider serializerProvider, Annotated annotated) throws JsonMappingException {
        Object findContentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(annotated);
        if (findContentSerializer != null) {
            return serializerProvider.serializerInstance(annotated, findContentSerializer);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final boolean usesStaticTyping(SerializationConfig serializationConfig, BeanDescription beanDescription, TypeSerializer typeSerializer, BeanProperty beanProperty) {
        return usesStaticTyping(serializationConfig, beanDescription, typeSerializer);
    }

    /* access modifiers changed from: protected */
    public boolean usesStaticTyping(SerializationConfig serializationConfig, BeanDescription beanDescription, TypeSerializer typeSerializer) {
        if (typeSerializer != null) {
            return false;
        }
        JsonSerialize.Typing findSerializationTyping = serializationConfig.getAnnotationIntrospector().findSerializationTyping(beanDescription.getClassInfo());
        if (findSerializationTyping == null) {
            return serializationConfig.isEnabled(MapperFeature.USE_STATIC_TYPING);
        }
        if (findSerializationTyping == JsonSerialize.Typing.STATIC) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public Class<?> _verifyAsClass(Object obj, String str, Class<?> cls) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector." + str + "() returned value of type " + obj.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
        }
        Class<NoClass> cls2 = (Class) obj;
        if (cls2 == cls || cls2 == NoClass.class) {
            return null;
        }
        return cls2;
    }
}
