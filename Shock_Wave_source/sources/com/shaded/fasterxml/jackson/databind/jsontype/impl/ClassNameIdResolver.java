package com.shaded.fasterxml.jackson.databind.jsontype.impl;

import com.shaded.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.type.TypeFactory;
import com.shaded.fasterxml.jackson.databind.util.ClassUtil;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class ClassNameIdResolver extends TypeIdResolverBase {
    public ClassNameIdResolver(JavaType javaType, TypeFactory typeFactory) {
        super(javaType, typeFactory);
    }

    public JsonTypeInfo.C0807Id getMechanism() {
        return JsonTypeInfo.C0807Id.CLASS;
    }

    public void registerSubtype(Class<?> cls, String str) {
    }

    public String idFromValue(Object obj) {
        return _idFrom(obj, obj.getClass());
    }

    public String idFromValueAndType(Object obj, Class<?> cls) {
        return _idFrom(obj, cls);
    }

    public JavaType typeFromId(String str) {
        if (str.indexOf(60) > 0) {
            return this._typeFactory.constructFromCanonical(str);
        }
        try {
            return this._typeFactory.constructSpecializedType(this._baseType, ClassUtil.findClass(str));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid type id '" + str + "' (for id type 'Id.class'): no such class found");
        } catch (Exception e2) {
            throw new IllegalArgumentException("Invalid type id '" + str + "' (for id type 'Id.class'): " + e2.getMessage(), e2);
        }
    }

    /* access modifiers changed from: protected */
    public final String _idFrom(Object obj, Class<?> cls) {
        boolean isAssignableFrom = Enum.class.isAssignableFrom(cls);
        Class<? super Object> cls2 = cls;
        if (isAssignableFrom) {
            boolean isEnum = cls.isEnum();
            cls2 = cls;
            if (!isEnum) {
                cls2 = cls.getSuperclass();
            }
        }
        String name = cls2.getName();
        if (name.startsWith("java.util")) {
            if (obj instanceof EnumSet) {
                return TypeFactory.defaultInstance().constructCollectionType((Class<? extends Collection>) EnumSet.class, (Class<?>) ClassUtil.findEnumType((EnumSet<?>) (EnumSet) obj)).toCanonical();
            } else if (obj instanceof EnumMap) {
                return TypeFactory.defaultInstance().constructMapType((Class<? extends Map>) EnumMap.class, (Class<?>) ClassUtil.findEnumType((EnumMap<?, ?>) (EnumMap) obj), (Class<?>) Object.class).toCanonical();
            } else {
                String substring = name.substring(9);
                if ((substring.startsWith(".Arrays$") || substring.startsWith(".Collections$")) && name.indexOf("List") >= 0) {
                    return "java.util.ArrayList";
                }
                return name;
            }
        } else if (name.indexOf(36) < 0 || ClassUtil.getOuterClass(cls2) == null || ClassUtil.getOuterClass(this._baseType.getRawClass()) != null) {
            return name;
        } else {
            return this._baseType.getRawClass().getName();
        }
    }
}
