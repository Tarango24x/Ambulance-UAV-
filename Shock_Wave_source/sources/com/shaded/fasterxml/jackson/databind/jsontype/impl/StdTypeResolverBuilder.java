package com.shaded.fasterxml.jackson.databind.jsontype.impl;

import com.shaded.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.DeserializationConfig;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.SerializationConfig;
import com.shaded.fasterxml.jackson.databind.cfg.MapperConfig;
import com.shaded.fasterxml.jackson.databind.jsontype.NamedType;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeSerializer;
import java.util.Collection;

public class StdTypeResolverBuilder implements TypeResolverBuilder<StdTypeResolverBuilder> {
    protected TypeIdResolver _customIdResolver;
    protected Class<?> _defaultImpl;
    protected JsonTypeInfo.C0807Id _idType;
    protected JsonTypeInfo.C0806As _includeAs;
    protected boolean _typeIdVisible = false;
    protected String _typeProperty;

    public static StdTypeResolverBuilder noTypeInfoBuilder() {
        return new StdTypeResolverBuilder().init(JsonTypeInfo.C0807Id.NONE, (TypeIdResolver) null);
    }

    public StdTypeResolverBuilder init(JsonTypeInfo.C0807Id id, TypeIdResolver typeIdResolver) {
        if (id == null) {
            throw new IllegalArgumentException("idType can not be null");
        }
        this._idType = id;
        this._customIdResolver = typeIdResolver;
        this._typeProperty = id.getDefaultPropertyName();
        return this;
    }

    public TypeSerializer buildTypeSerializer(SerializationConfig serializationConfig, JavaType javaType, Collection<NamedType> collection) {
        if (this._idType == JsonTypeInfo.C0807Id.NONE) {
            return null;
        }
        TypeIdResolver idResolver = idResolver(serializationConfig, javaType, collection, true, false);
        switch (this._includeAs) {
            case WRAPPER_ARRAY:
                return new AsArrayTypeSerializer(idResolver, (BeanProperty) null);
            case PROPERTY:
                return new AsPropertyTypeSerializer(idResolver, (BeanProperty) null, this._typeProperty);
            case WRAPPER_OBJECT:
                return new AsWrapperTypeSerializer(idResolver, (BeanProperty) null);
            case EXTERNAL_PROPERTY:
                return new AsExternalTypeSerializer(idResolver, (BeanProperty) null, this._typeProperty);
            default:
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
        }
    }

    public TypeDeserializer buildTypeDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, Collection<NamedType> collection) {
        if (this._idType == JsonTypeInfo.C0807Id.NONE) {
            return null;
        }
        TypeIdResolver idResolver = idResolver(deserializationConfig, javaType, collection, false, true);
        switch (this._includeAs) {
            case WRAPPER_ARRAY:
                return new AsArrayTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            case PROPERTY:
                return new AsPropertyTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            case WRAPPER_OBJECT:
                return new AsWrapperTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            case EXTERNAL_PROPERTY:
                return new AsExternalTypeDeserializer(javaType, idResolver, this._typeProperty, this._typeIdVisible, this._defaultImpl);
            default:
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
        }
    }

    public StdTypeResolverBuilder inclusion(JsonTypeInfo.C0806As as) {
        if (as == null) {
            throw new IllegalArgumentException("includeAs can not be null");
        }
        this._includeAs = as;
        return this;
    }

    public StdTypeResolverBuilder typeProperty(String str) {
        if (str == null || str.length() == 0) {
            str = this._idType.getDefaultPropertyName();
        }
        this._typeProperty = str;
        return this;
    }

    public StdTypeResolverBuilder defaultImpl(Class<?> cls) {
        this._defaultImpl = cls;
        return this;
    }

    public StdTypeResolverBuilder typeIdVisibility(boolean z) {
        this._typeIdVisible = z;
        return this;
    }

    public String getTypeProperty() {
        return this._typeProperty;
    }

    public Class<?> getDefaultImpl() {
        return this._defaultImpl;
    }

    public boolean isTypeIdVisible() {
        return this._typeIdVisible;
    }

    /* access modifiers changed from: protected */
    public TypeIdResolver idResolver(MapperConfig<?> mapperConfig, JavaType javaType, Collection<NamedType> collection, boolean z, boolean z2) {
        if (this._customIdResolver != null) {
            return this._customIdResolver;
        }
        if (this._idType == null) {
            throw new IllegalStateException("Can not build, 'init()' not yet called");
        }
        switch (this._idType) {
            case CLASS:
                return new ClassNameIdResolver(javaType, mapperConfig.getTypeFactory());
            case MINIMAL_CLASS:
                return new MinimalClassNameIdResolver(javaType, mapperConfig.getTypeFactory());
            case NAME:
                return TypeNameIdResolver.construct(mapperConfig, javaType, collection, z, z2);
            case NONE:
                return null;
            default:
                throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
        }
    }
}
