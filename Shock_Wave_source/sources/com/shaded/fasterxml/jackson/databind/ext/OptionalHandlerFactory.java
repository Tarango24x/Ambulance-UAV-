package com.shaded.fasterxml.jackson.databind.ext;

import com.shaded.fasterxml.jackson.databind.BeanDescription;
import com.shaded.fasterxml.jackson.databind.DeserializationConfig;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonDeserializer;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonSerializer;
import com.shaded.fasterxml.jackson.databind.SerializationConfig;
import com.shaded.fasterxml.jackson.databind.deser.Deserializers;
import com.shaded.fasterxml.jackson.databind.ser.Serializers;
import java.io.Serializable;

public class OptionalHandlerFactory implements Serializable {
    private static final String CLASS_NAME_DOM_DOCUMENT = "org.w3c.dom.Node";
    private static final String CLASS_NAME_DOM_NODE = "org.w3c.dom.Node";
    private static final String DESERIALIZERS_FOR_JAVAX_XML = "com.shaded.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
    private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "com.shaded.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer";
    private static final String DESERIALIZER_FOR_DOM_NODE = "com.shaded.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer";
    private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
    private static final String SERIALIZERS_FOR_JAVAX_XML = "com.shaded.fasterxml.jackson.databind.ext.CoreXMLSerializers";
    private static final String SERIALIZER_FOR_DOM_NODE = "com.shaded.fasterxml.jackson.databind.ext.DOMSerializer";
    public static final OptionalHandlerFactory instance = new OptionalHandlerFactory();
    private static final long serialVersionUID = 1;

    protected OptionalHandlerFactory() {
    }

    public JsonSerializer<?> findSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanDescription beanDescription) {
        Class<?> rawClass = javaType.getRawClass();
        if (rawClass.getName().startsWith(PACKAGE_PREFIX_JAVAX_XML) || hasSupertypeStartingWith(rawClass, PACKAGE_PREFIX_JAVAX_XML)) {
            Object instantiate = instantiate(SERIALIZERS_FOR_JAVAX_XML);
            if (instantiate == null) {
                return null;
            }
            return ((Serializers) instantiate).findSerializer(serializationConfig, javaType, beanDescription);
        } else if (doesImplement(rawClass, "org.w3c.dom.Node")) {
            return (JsonSerializer) instantiate(SERIALIZER_FOR_DOM_NODE);
        } else {
            return null;
        }
    }

    public JsonDeserializer<?> findDeserializer(JavaType javaType, DeserializationConfig deserializationConfig, BeanDescription beanDescription) throws JsonMappingException {
        Class<?> rawClass = javaType.getRawClass();
        if (rawClass.getName().startsWith(PACKAGE_PREFIX_JAVAX_XML) || hasSupertypeStartingWith(rawClass, PACKAGE_PREFIX_JAVAX_XML)) {
            Object instantiate = instantiate(DESERIALIZERS_FOR_JAVAX_XML);
            if (instantiate == null) {
                return null;
            }
            return ((Deserializers) instantiate).findBeanDeserializer(javaType, deserializationConfig, beanDescription);
        } else if (doesImplement(rawClass, "org.w3c.dom.Node")) {
            return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_DOCUMENT);
        } else {
            if (doesImplement(rawClass, "org.w3c.dom.Node")) {
                return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_NODE);
            }
            return null;
        }
    }

    private Object instantiate(String str) {
        try {
            return Class.forName(str).newInstance();
        } catch (Exception | LinkageError e) {
            return null;
        }
    }

    private boolean doesImplement(Class<?> cls, String str) {
        for (Class<? super Object> cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
            if (cls2.getName().equals(str) || hasInterface(cls2, str)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInterface(Class<?> cls, String str) {
        Class[] interfaces = cls.getInterfaces();
        for (Class name : interfaces) {
            if (name.getName().equals(str)) {
                return true;
            }
        }
        for (Class hasInterface : interfaces) {
            if (hasInterface(hasInterface, str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean hasSupertypeStartingWith(java.lang.Class<?> r4, java.lang.String r5) {
        /*
            r3 = this;
            r0 = 1
            java.lang.Class r1 = r4.getSuperclass()
        L_0x0005:
            if (r1 == 0) goto L_0x001b
            java.lang.String r2 = r1.getName()
            boolean r2 = r2.startsWith(r5)
            if (r2 == 0) goto L_0x0012
        L_0x0011:
            return r0
        L_0x0012:
            java.lang.Class r1 = r1.getSuperclass()
            goto L_0x0005
        L_0x0017:
            java.lang.Class r4 = r4.getSuperclass()
        L_0x001b:
            if (r4 == 0) goto L_0x0024
            boolean r1 = r3.hasInterfaceStartingWith(r4, r5)
            if (r1 == 0) goto L_0x0017
            goto L_0x0011
        L_0x0024:
            r0 = 0
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.ext.OptionalHandlerFactory.hasSupertypeStartingWith(java.lang.Class, java.lang.String):boolean");
    }

    private boolean hasInterfaceStartingWith(Class<?> cls, String str) {
        Class[] interfaces = cls.getInterfaces();
        for (Class name : interfaces) {
            if (name.getName().startsWith(str)) {
                return true;
            }
        }
        for (Class hasInterfaceStartingWith : interfaces) {
            if (hasInterfaceStartingWith(hasInterfaceStartingWith, str)) {
                return true;
            }
        }
        return false;
    }
}
