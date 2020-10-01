package com.shaded.fasterxml.jackson.databind.introspect;

import com.shaded.fasterxml.jackson.annotation.JacksonAnnotation;
import com.shaded.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.shaded.fasterxml.jackson.annotation.JacksonInject;
import com.shaded.fasterxml.jackson.annotation.JsonAnyGetter;
import com.shaded.fasterxml.jackson.annotation.JsonAnySetter;
import com.shaded.fasterxml.jackson.annotation.JsonBackReference;
import com.shaded.fasterxml.jackson.annotation.JsonCreator;
import com.shaded.fasterxml.jackson.annotation.JsonFilter;
import com.shaded.fasterxml.jackson.annotation.JsonFormat;
import com.shaded.fasterxml.jackson.annotation.JsonGetter;
import com.shaded.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.shaded.fasterxml.jackson.annotation.JsonIdentityReference;
import com.shaded.fasterxml.jackson.annotation.JsonIgnore;
import com.shaded.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shaded.fasterxml.jackson.annotation.JsonIgnoreType;
import com.shaded.fasterxml.jackson.annotation.JsonInclude;
import com.shaded.fasterxml.jackson.annotation.JsonManagedReference;
import com.shaded.fasterxml.jackson.annotation.JsonProperty;
import com.shaded.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.shaded.fasterxml.jackson.annotation.JsonRawValue;
import com.shaded.fasterxml.jackson.annotation.JsonRootName;
import com.shaded.fasterxml.jackson.annotation.JsonSetter;
import com.shaded.fasterxml.jackson.annotation.JsonSubTypes;
import com.shaded.fasterxml.jackson.annotation.JsonTypeId;
import com.shaded.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shaded.fasterxml.jackson.annotation.JsonTypeName;
import com.shaded.fasterxml.jackson.annotation.JsonUnwrapped;
import com.shaded.fasterxml.jackson.annotation.JsonValue;
import com.shaded.fasterxml.jackson.annotation.JsonView;
import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.shaded.fasterxml.jackson.core.Version;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonDeserializer;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.KeyDeserializer;
import com.shaded.fasterxml.jackson.databind.PropertyName;
import com.shaded.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.shaded.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shaded.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.shaded.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaded.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.shaded.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.shaded.fasterxml.jackson.databind.annotation.JsonValueInstantiator;
import com.shaded.fasterxml.jackson.databind.annotation.NoClass;
import com.shaded.fasterxml.jackson.databind.cfg.MapperConfig;
import com.shaded.fasterxml.jackson.databind.cfg.PackageVersion;
import com.shaded.fasterxml.jackson.databind.jsontype.NamedType;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.shaded.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.shaded.fasterxml.jackson.databind.ser.std.RawSerializer;
import com.shaded.fasterxml.jackson.databind.util.Converter;
import com.shaded.fasterxml.jackson.databind.util.NameTransformer;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class JacksonAnnotationIntrospector extends AnnotationIntrospector implements Serializable {
    private static final long serialVersionUID = 1;

    public Version version() {
        return PackageVersion.VERSION;
    }

    @Deprecated
    public boolean isHandled(Annotation annotation) {
        return annotation.annotationType().getAnnotation(JacksonAnnotation.class) != null;
    }

    public boolean isAnnotationBundle(Annotation annotation) {
        return annotation.annotationType().getAnnotation(JacksonAnnotationsInside.class) != null;
    }

    public PropertyName findRootName(AnnotatedClass annotatedClass) {
        JsonRootName jsonRootName = (JsonRootName) annotatedClass.getAnnotation(JsonRootName.class);
        if (jsonRootName == null) {
            return null;
        }
        return new PropertyName(jsonRootName.value());
    }

    public String[] findPropertiesToIgnore(Annotated annotated) {
        JsonIgnoreProperties jsonIgnoreProperties = (JsonIgnoreProperties) annotated.getAnnotation(JsonIgnoreProperties.class);
        if (jsonIgnoreProperties == null) {
            return null;
        }
        return jsonIgnoreProperties.value();
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass annotatedClass) {
        JsonIgnoreProperties jsonIgnoreProperties = (JsonIgnoreProperties) annotatedClass.getAnnotation(JsonIgnoreProperties.class);
        if (jsonIgnoreProperties == null) {
            return null;
        }
        return Boolean.valueOf(jsonIgnoreProperties.ignoreUnknown());
    }

    public Boolean isIgnorableType(AnnotatedClass annotatedClass) {
        JsonIgnoreType jsonIgnoreType = (JsonIgnoreType) annotatedClass.getAnnotation(JsonIgnoreType.class);
        if (jsonIgnoreType == null) {
            return null;
        }
        return Boolean.valueOf(jsonIgnoreType.value());
    }

    public Object findFilterId(AnnotatedClass annotatedClass) {
        JsonFilter jsonFilter = (JsonFilter) annotatedClass.getAnnotation(JsonFilter.class);
        if (jsonFilter != null) {
            String value = jsonFilter.value();
            if (value.length() > 0) {
                return value;
            }
        }
        return null;
    }

    public Object findNamingStrategy(AnnotatedClass annotatedClass) {
        JsonNaming jsonNaming = (JsonNaming) annotatedClass.getAnnotation(JsonNaming.class);
        if (jsonNaming == null) {
            return null;
        }
        return jsonNaming.value();
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker, com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker<?>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker<?> findAutoDetectVisibility(com.shaded.fasterxml.jackson.databind.introspect.AnnotatedClass r2, com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker<?> r3) {
        /*
            r1 = this;
            java.lang.Class<com.shaded.fasterxml.jackson.annotation.JsonAutoDetect> r0 = com.shaded.fasterxml.jackson.annotation.JsonAutoDetect.class
            java.lang.annotation.Annotation r0 = r2.getAnnotation(r0)
            com.shaded.fasterxml.jackson.annotation.JsonAutoDetect r0 = (com.shaded.fasterxml.jackson.annotation.JsonAutoDetect) r0
            if (r0 != 0) goto L_0x000b
        L_0x000a:
            return r3
        L_0x000b:
            com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker r3 = r3.with((com.shaded.fasterxml.jackson.annotation.JsonAutoDetect) r0)
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector.findAutoDetectVisibility(com.shaded.fasterxml.jackson.databind.introspect.AnnotatedClass, com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker):com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker");
    }

    public AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember annotatedMember) {
        JsonManagedReference jsonManagedReference = (JsonManagedReference) annotatedMember.getAnnotation(JsonManagedReference.class);
        if (jsonManagedReference != null) {
            return AnnotationIntrospector.ReferenceProperty.managed(jsonManagedReference.value());
        }
        JsonBackReference jsonBackReference = (JsonBackReference) annotatedMember.getAnnotation(JsonBackReference.class);
        if (jsonBackReference != null) {
            return AnnotationIntrospector.ReferenceProperty.back(jsonBackReference.value());
        }
        return null;
    }

    public NameTransformer findUnwrappingNameTransformer(AnnotatedMember annotatedMember) {
        JsonUnwrapped jsonUnwrapped = (JsonUnwrapped) annotatedMember.getAnnotation(JsonUnwrapped.class);
        if (jsonUnwrapped == null || !jsonUnwrapped.enabled()) {
            return null;
        }
        return NameTransformer.simpleTransformer(jsonUnwrapped.prefix(), jsonUnwrapped.suffix());
    }

    public boolean hasIgnoreMarker(AnnotatedMember annotatedMember) {
        return _isIgnorable(annotatedMember);
    }

    public Boolean hasRequiredMarker(AnnotatedMember annotatedMember) {
        JsonProperty jsonProperty = (JsonProperty) annotatedMember.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return Boolean.valueOf(jsonProperty.required());
        }
        return null;
    }

    public Object findInjectableValueId(AnnotatedMember annotatedMember) {
        JacksonInject jacksonInject = (JacksonInject) annotatedMember.getAnnotation(JacksonInject.class);
        if (jacksonInject == null) {
            return null;
        }
        String value = jacksonInject.value();
        if (value.length() != 0) {
            return value;
        }
        if (!(annotatedMember instanceof AnnotatedMethod)) {
            return annotatedMember.getRawType().getName();
        }
        AnnotatedMethod annotatedMethod = (AnnotatedMethod) annotatedMember;
        if (annotatedMethod.getParameterCount() == 0) {
            return annotatedMember.getRawType().getName();
        }
        return annotatedMethod.getRawParameterType(0).getName();
    }

    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> mapperConfig, AnnotatedClass annotatedClass, JavaType javaType) {
        return _findTypeResolver(mapperConfig, annotatedClass, javaType);
    }

    public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> mapperConfig, AnnotatedMember annotatedMember, JavaType javaType) {
        if (javaType.isContainerType()) {
            return null;
        }
        return _findTypeResolver(mapperConfig, annotatedMember, javaType);
    }

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> mapperConfig, AnnotatedMember annotatedMember, JavaType javaType) {
        if (javaType.isContainerType()) {
            return _findTypeResolver(mapperConfig, annotatedMember, javaType);
        }
        throw new IllegalArgumentException("Must call method with a container type (got " + javaType + ")");
    }

    public List<NamedType> findSubtypes(Annotated annotated) {
        JsonSubTypes jsonSubTypes = (JsonSubTypes) annotated.getAnnotation(JsonSubTypes.class);
        if (jsonSubTypes == null) {
            return null;
        }
        JsonSubTypes.Type[] value = jsonSubTypes.value();
        ArrayList arrayList = new ArrayList(value.length);
        for (JsonSubTypes.Type type : value) {
            arrayList.add(new NamedType(type.value(), type.name()));
        }
        return arrayList;
    }

    public String findTypeName(AnnotatedClass annotatedClass) {
        JsonTypeName jsonTypeName = (JsonTypeName) annotatedClass.getAnnotation(JsonTypeName.class);
        if (jsonTypeName == null) {
            return null;
        }
        return jsonTypeName.value();
    }

    public Object findSerializer(Annotated annotated) {
        Class<? extends JsonSerializer<?>> using;
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize != null && (using = jsonSerialize.using()) != JsonSerializer.None.class) {
            return using;
        }
        JsonRawValue jsonRawValue = (JsonRawValue) annotated.getAnnotation(JsonRawValue.class);
        if (jsonRawValue == null || !jsonRawValue.value()) {
            return null;
        }
        return new RawSerializer(annotated.getRawType());
    }

    public Class<? extends JsonSerializer<?>> findKeySerializer(Annotated annotated) {
        Class<? extends JsonSerializer<?>> keyUsing;
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null || (keyUsing = jsonSerialize.keyUsing()) == JsonSerializer.None.class) {
            return null;
        }
        return keyUsing;
    }

    public Class<? extends JsonSerializer<?>> findContentSerializer(Annotated annotated) {
        Class<? extends JsonSerializer<?>> contentUsing;
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null || (contentUsing = jsonSerialize.contentUsing()) == JsonSerializer.None.class) {
            return null;
        }
        return contentUsing;
    }

    public JsonInclude.Include findSerializationInclusion(Annotated annotated, JsonInclude.Include include) {
        JsonInclude jsonInclude = (JsonInclude) annotated.getAnnotation(JsonInclude.class);
        if (jsonInclude != null) {
            return jsonInclude.value();
        }
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null) {
            return include;
        }
        switch (jsonSerialize.include()) {
            case ALWAYS:
                return JsonInclude.Include.ALWAYS;
            case NON_NULL:
                return JsonInclude.Include.NON_NULL;
            case NON_DEFAULT:
                return JsonInclude.Include.NON_DEFAULT;
            case NON_EMPTY:
                return JsonInclude.Include.NON_EMPTY;
            default:
                return include;
        }
    }

    public Class<?> findSerializationType(Annotated annotated) {
        Class<?> as;
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null || (as = jsonSerialize.mo14874as()) == NoClass.class) {
            return null;
        }
        return as;
    }

    public Class<?> findSerializationKeyType(Annotated annotated, JavaType javaType) {
        Class<?> keyAs;
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null || (keyAs = jsonSerialize.keyAs()) == NoClass.class) {
            return null;
        }
        return keyAs;
    }

    public Class<?> findSerializationContentType(Annotated annotated, JavaType javaType) {
        Class<?> contentAs;
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null || (contentAs = jsonSerialize.contentAs()) == NoClass.class) {
            return null;
        }
        return contentAs;
    }

    public JsonSerialize.Typing findSerializationTyping(Annotated annotated) {
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null) {
            return null;
        }
        return jsonSerialize.typing();
    }

    public Object findSerializationConverter(Annotated annotated) {
        Class<? extends Converter<?, ?>> converter;
        JsonSerialize jsonSerialize = (JsonSerialize) annotated.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null || (converter = jsonSerialize.converter()) == Converter.None.class) {
            return null;
        }
        return converter;
    }

    public Object findSerializationContentConverter(AnnotatedMember annotatedMember) {
        Class<? extends Converter<?, ?>> contentConverter;
        JsonSerialize jsonSerialize = (JsonSerialize) annotatedMember.getAnnotation(JsonSerialize.class);
        if (jsonSerialize == null || (contentConverter = jsonSerialize.contentConverter()) == Converter.None.class) {
            return null;
        }
        return contentConverter;
    }

    public Class<?>[] findViews(Annotated annotated) {
        JsonView jsonView = (JsonView) annotated.getAnnotation(JsonView.class);
        if (jsonView == null) {
            return null;
        }
        return jsonView.value();
    }

    public Boolean isTypeId(AnnotatedMember annotatedMember) {
        return Boolean.valueOf(annotatedMember.hasAnnotation(JsonTypeId.class));
    }

    public ObjectIdInfo findObjectIdInfo(Annotated annotated) {
        JsonIdentityInfo jsonIdentityInfo = (JsonIdentityInfo) annotated.getAnnotation(JsonIdentityInfo.class);
        if (jsonIdentityInfo == null || jsonIdentityInfo.generator() == ObjectIdGenerators.None.class) {
            return null;
        }
        return new ObjectIdInfo(jsonIdentityInfo.property(), jsonIdentityInfo.scope(), jsonIdentityInfo.generator());
    }

    public ObjectIdInfo findObjectReferenceInfo(Annotated annotated, ObjectIdInfo objectIdInfo) {
        JsonIdentityReference jsonIdentityReference = (JsonIdentityReference) annotated.getAnnotation(JsonIdentityReference.class);
        if (jsonIdentityReference != null) {
            return objectIdInfo.withAlwaysAsId(jsonIdentityReference.alwaysAsId());
        }
        return objectIdInfo;
    }

    public JsonFormat.Value findFormat(AnnotatedMember annotatedMember) {
        return findFormat(annotatedMember);
    }

    public JsonFormat.Value findFormat(Annotated annotated) {
        JsonFormat jsonFormat = (JsonFormat) annotated.getAnnotation(JsonFormat.class);
        if (jsonFormat == null) {
            return null;
        }
        return new JsonFormat.Value(jsonFormat);
    }

    public String[] findSerializationPropertyOrder(AnnotatedClass annotatedClass) {
        JsonPropertyOrder jsonPropertyOrder = (JsonPropertyOrder) annotatedClass.getAnnotation(JsonPropertyOrder.class);
        if (jsonPropertyOrder == null) {
            return null;
        }
        return jsonPropertyOrder.value();
    }

    public Boolean findSerializationSortAlphabetically(AnnotatedClass annotatedClass) {
        JsonPropertyOrder jsonPropertyOrder = (JsonPropertyOrder) annotatedClass.getAnnotation(JsonPropertyOrder.class);
        if (jsonPropertyOrder == null) {
            return null;
        }
        return Boolean.valueOf(jsonPropertyOrder.alphabetic());
    }

    public PropertyName findNameForSerialization(Annotated annotated) {
        String findSerializationName;
        if (annotated instanceof AnnotatedField) {
            findSerializationName = findSerializationName((AnnotatedField) annotated);
        } else {
            findSerializationName = annotated instanceof AnnotatedMethod ? findSerializationName((AnnotatedMethod) annotated) : null;
        }
        if (findSerializationName == null) {
            return null;
        }
        if (findSerializationName.length() == 0) {
            return PropertyName.USE_DEFAULT;
        }
        return new PropertyName(findSerializationName);
    }

    public String findSerializationName(AnnotatedField annotatedField) {
        JsonProperty jsonProperty = (JsonProperty) annotatedField.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedField.hasAnnotation(JsonSerialize.class) || annotatedField.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }

    public String findSerializationName(AnnotatedMethod annotatedMethod) {
        JsonGetter jsonGetter = (JsonGetter) annotatedMethod.getAnnotation(JsonGetter.class);
        if (jsonGetter != null) {
            return jsonGetter.value();
        }
        JsonProperty jsonProperty = (JsonProperty) annotatedMethod.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedMethod.hasAnnotation(JsonSerialize.class) || annotatedMethod.hasAnnotation(JsonView.class)) {
            return "";
        }
        return null;
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod annotatedMethod) {
        JsonValue jsonValue = (JsonValue) annotatedMethod.getAnnotation(JsonValue.class);
        return jsonValue != null && jsonValue.value();
    }

    public Class<? extends JsonDeserializer<?>> findDeserializer(Annotated annotated) {
        Class<? extends JsonDeserializer<?>> using;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (using = jsonDeserialize.using()) == JsonDeserializer.None.class) {
            return null;
        }
        return using;
    }

    public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated annotated) {
        Class<? extends KeyDeserializer> keyUsing;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (keyUsing = jsonDeserialize.keyUsing()) == KeyDeserializer.None.class) {
            return null;
        }
        return keyUsing;
    }

    public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated annotated) {
        Class<? extends JsonDeserializer<?>> contentUsing;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (contentUsing = jsonDeserialize.contentUsing()) == JsonDeserializer.None.class) {
            return null;
        }
        return contentUsing;
    }

    public Class<?> findDeserializationType(Annotated annotated, JavaType javaType) {
        Class<?> as;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (as = jsonDeserialize.mo14862as()) == NoClass.class) {
            return null;
        }
        return as;
    }

    public Class<?> findDeserializationKeyType(Annotated annotated, JavaType javaType) {
        Class<?> keyAs;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (keyAs = jsonDeserialize.keyAs()) == NoClass.class) {
            return null;
        }
        return keyAs;
    }

    public Class<?> findDeserializationContentType(Annotated annotated, JavaType javaType) {
        Class<?> contentAs;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (contentAs = jsonDeserialize.contentAs()) == NoClass.class) {
            return null;
        }
        return contentAs;
    }

    public Object findDeserializationConverter(Annotated annotated) {
        Class<? extends Converter<?, ?>> converter;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotated.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (converter = jsonDeserialize.converter()) == Converter.None.class) {
            return null;
        }
        return converter;
    }

    public Object findDeserializationContentConverter(AnnotatedMember annotatedMember) {
        Class<? extends Converter<?, ?>> contentConverter;
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotatedMember.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || (contentConverter = jsonDeserialize.contentConverter()) == Converter.None.class) {
            return null;
        }
        return contentConverter;
    }

    public Object findValueInstantiator(AnnotatedClass annotatedClass) {
        JsonValueInstantiator jsonValueInstantiator = (JsonValueInstantiator) annotatedClass.getAnnotation(JsonValueInstantiator.class);
        if (jsonValueInstantiator == null) {
            return null;
        }
        return jsonValueInstantiator.value();
    }

    public Class<?> findPOJOBuilder(AnnotatedClass annotatedClass) {
        JsonDeserialize jsonDeserialize = (JsonDeserialize) annotatedClass.getAnnotation(JsonDeserialize.class);
        if (jsonDeserialize == null || jsonDeserialize.builder() == NoClass.class) {
            return null;
        }
        return jsonDeserialize.builder();
    }

    public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass annotatedClass) {
        JsonPOJOBuilder jsonPOJOBuilder = (JsonPOJOBuilder) annotatedClass.getAnnotation(JsonPOJOBuilder.class);
        if (jsonPOJOBuilder == null) {
            return null;
        }
        return new JsonPOJOBuilder.Value(jsonPOJOBuilder);
    }

    public PropertyName findNameForDeserialization(Annotated annotated) {
        String findDeserializationName;
        if (annotated instanceof AnnotatedField) {
            findDeserializationName = findDeserializationName((AnnotatedField) annotated);
        } else if (annotated instanceof AnnotatedMethod) {
            findDeserializationName = findDeserializationName((AnnotatedMethod) annotated);
        } else {
            findDeserializationName = annotated instanceof AnnotatedParameter ? findDeserializationName((AnnotatedParameter) annotated) : null;
        }
        if (findDeserializationName == null) {
            return null;
        }
        if (findDeserializationName.length() == 0) {
            return PropertyName.USE_DEFAULT;
        }
        return new PropertyName(findDeserializationName);
    }

    public String findDeserializationName(AnnotatedMethod annotatedMethod) {
        JsonSetter jsonSetter = (JsonSetter) annotatedMethod.getAnnotation(JsonSetter.class);
        if (jsonSetter != null) {
            return jsonSetter.value();
        }
        JsonProperty jsonProperty = (JsonProperty) annotatedMethod.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedMethod.hasAnnotation(JsonDeserialize.class) || annotatedMethod.hasAnnotation(JsonView.class) || annotatedMethod.hasAnnotation(JsonBackReference.class) || annotatedMethod.hasAnnotation(JsonManagedReference.class)) {
            return "";
        }
        return null;
    }

    public String findDeserializationName(AnnotatedField annotatedField) {
        JsonProperty jsonProperty = (JsonProperty) annotatedField.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            return jsonProperty.value();
        }
        if (annotatedField.hasAnnotation(JsonDeserialize.class) || annotatedField.hasAnnotation(JsonView.class) || annotatedField.hasAnnotation(JsonBackReference.class) || annotatedField.hasAnnotation(JsonManagedReference.class)) {
            return "";
        }
        return null;
    }

    public String findDeserializationName(AnnotatedParameter annotatedParameter) {
        JsonProperty jsonProperty;
        if (annotatedParameter == null || (jsonProperty = (JsonProperty) annotatedParameter.getAnnotation(JsonProperty.class)) == null) {
            return null;
        }
        return jsonProperty.value();
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod annotatedMethod) {
        return annotatedMethod.hasAnnotation(JsonAnySetter.class);
    }

    public boolean hasAnyGetterAnnotation(AnnotatedMethod annotatedMethod) {
        return annotatedMethod.hasAnnotation(JsonAnyGetter.class);
    }

    public boolean hasCreatorAnnotation(Annotated annotated) {
        return annotated.hasAnnotation(JsonCreator.class);
    }

    /* access modifiers changed from: protected */
    public boolean _isIgnorable(Annotated annotated) {
        JsonIgnore jsonIgnore = (JsonIgnore) annotated.getAnnotation(JsonIgnore.class);
        return jsonIgnore != null && jsonIgnore.value();
    }

    /* access modifiers changed from: protected */
    public TypeResolverBuilder<?> _findTypeResolver(MapperConfig<?> mapperConfig, Annotated annotated, JavaType javaType) {
        TypeResolverBuilder _constructStdTypeResolverBuilder;
        TypeIdResolver typeIdResolver = null;
        JsonTypeInfo jsonTypeInfo = (JsonTypeInfo) annotated.getAnnotation(JsonTypeInfo.class);
        JsonTypeResolver jsonTypeResolver = (JsonTypeResolver) annotated.getAnnotation(JsonTypeResolver.class);
        if (jsonTypeResolver != null) {
            if (jsonTypeInfo == null) {
                return null;
            }
            _constructStdTypeResolverBuilder = mapperConfig.typeResolverBuilderInstance(annotated, jsonTypeResolver.value());
        } else if (jsonTypeInfo == null) {
            return null;
        } else {
            if (jsonTypeInfo.use() == JsonTypeInfo.C0807Id.NONE) {
                return _constructNoTypeResolverBuilder();
            }
            _constructStdTypeResolverBuilder = _constructStdTypeResolverBuilder();
        }
        JsonTypeIdResolver jsonTypeIdResolver = (JsonTypeIdResolver) annotated.getAnnotation(JsonTypeIdResolver.class);
        if (jsonTypeIdResolver != null) {
            typeIdResolver = mapperConfig.typeIdResolverInstance(annotated, jsonTypeIdResolver.value());
        }
        if (typeIdResolver != null) {
            typeIdResolver.init(javaType);
        }
        TypeResolverBuilder init = _constructStdTypeResolverBuilder.init(jsonTypeInfo.use(), typeIdResolver);
        JsonTypeInfo.C0806As include = jsonTypeInfo.include();
        if (include == JsonTypeInfo.C0806As.EXTERNAL_PROPERTY && (annotated instanceof AnnotatedClass)) {
            include = JsonTypeInfo.C0806As.PROPERTY;
        }
        TypeResolverBuilder typeProperty = init.inclusion(include).typeProperty(jsonTypeInfo.property());
        Class defaultImpl = jsonTypeInfo.defaultImpl();
        if (defaultImpl != JsonTypeInfo.None.class) {
            typeProperty = typeProperty.defaultImpl(defaultImpl);
        }
        return typeProperty.typeIdVisibility(jsonTypeInfo.visible());
    }

    /* access modifiers changed from: protected */
    public StdTypeResolverBuilder _constructStdTypeResolverBuilder() {
        return new StdTypeResolverBuilder();
    }

    /* access modifiers changed from: protected */
    public StdTypeResolverBuilder _constructNoTypeResolverBuilder() {
        return StdTypeResolverBuilder.noTypeInfoBuilder();
    }
}
