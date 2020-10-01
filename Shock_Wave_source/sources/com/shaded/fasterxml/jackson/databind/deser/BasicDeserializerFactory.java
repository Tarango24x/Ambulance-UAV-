package com.shaded.fasterxml.jackson.databind.deser;

import com.shaded.fasterxml.jackson.core.JsonLocation;
import com.shaded.fasterxml.jackson.databind.AbstractTypeResolver;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.BeanDescription;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.DeserializationConfig;
import com.shaded.fasterxml.jackson.databind.DeserializationContext;
import com.shaded.fasterxml.jackson.databind.DeserializationFeature;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonDeserializer;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.JsonNode;
import com.shaded.fasterxml.jackson.databind.KeyDeserializer;
import com.shaded.fasterxml.jackson.databind.PropertyName;
import com.shaded.fasterxml.jackson.databind.annotation.NoClass;
import com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.shaded.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.shaded.fasterxml.jackson.databind.cfg.MapperConfig;
import com.shaded.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.shaded.fasterxml.jackson.databind.deser.std.ArrayBlockingQueueDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.shaded.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.EnumSetDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.JacksonDeserializers;
import com.shaded.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.shaded.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.shaded.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers;
import com.shaded.fasterxml.jackson.databind.deser.std.StdKeyDeserializers;
import com.shaded.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.shaded.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.shaded.fasterxml.jackson.databind.introspect.Annotated;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.shaded.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.shaded.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.shaded.fasterxml.jackson.databind.jsontype.NamedType;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.shaded.fasterxml.jackson.databind.type.ArrayType;
import com.shaded.fasterxml.jackson.databind.type.CollectionLikeType;
import com.shaded.fasterxml.jackson.databind.type.CollectionType;
import com.shaded.fasterxml.jackson.databind.type.MapLikeType;
import com.shaded.fasterxml.jackson.databind.type.MapType;
import com.shaded.fasterxml.jackson.databind.type.TypeFactory;
import com.shaded.fasterxml.jackson.databind.util.ClassUtil;
import com.shaded.fasterxml.jackson.databind.util.EnumResolver;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class BasicDeserializerFactory extends DeserializerFactory implements Serializable {
    private static final Class<?> CLASS_CHAR_BUFFER = CharSequence.class;
    private static final Class<?> CLASS_ITERABLE = Iterable.class;
    private static final Class<?> CLASS_OBJECT = Object.class;
    private static final Class<?> CLASS_STRING = String.class;
    static final HashMap<String, Class<? extends Collection>> _collectionFallbacks = new HashMap<>();
    static final HashMap<String, Class<? extends Map>> _mapFallbacks = new HashMap<>();
    protected final DeserializerFactoryConfig _factoryConfig;

    /* access modifiers changed from: protected */
    public abstract DeserializerFactory withConfig(DeserializerFactoryConfig deserializerFactoryConfig);

    static {
        _mapFallbacks.put(Map.class.getName(), LinkedHashMap.class);
        _mapFallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
        _mapFallbacks.put(SortedMap.class.getName(), TreeMap.class);
        _mapFallbacks.put("java.util.NavigableMap", TreeMap.class);
        try {
            _mapFallbacks.put(ConcurrentNavigableMap.class.getName(), ConcurrentSkipListMap.class);
        } catch (Throwable th) {
            System.err.println("Problems with (optional) types: " + th);
        }
        _collectionFallbacks.put(Collection.class.getName(), ArrayList.class);
        _collectionFallbacks.put(List.class.getName(), ArrayList.class);
        _collectionFallbacks.put(Set.class.getName(), HashSet.class);
        _collectionFallbacks.put(SortedSet.class.getName(), TreeSet.class);
        _collectionFallbacks.put(Queue.class.getName(), LinkedList.class);
        _collectionFallbacks.put("java.util.Deque", LinkedList.class);
        _collectionFallbacks.put("java.util.NavigableSet", TreeSet.class);
    }

    protected BasicDeserializerFactory(DeserializerFactoryConfig deserializerFactoryConfig) {
        this._factoryConfig = deserializerFactoryConfig;
    }

    public DeserializerFactoryConfig getFactoryConfig() {
        return this._factoryConfig;
    }

    public final DeserializerFactory withAdditionalDeserializers(Deserializers deserializers) {
        return withConfig(this._factoryConfig.withAdditionalDeserializers(deserializers));
    }

    public final DeserializerFactory withAdditionalKeyDeserializers(KeyDeserializers keyDeserializers) {
        return withConfig(this._factoryConfig.withAdditionalKeyDeserializers(keyDeserializers));
    }

    public final DeserializerFactory withDeserializerModifier(BeanDeserializerModifier beanDeserializerModifier) {
        return withConfig(this._factoryConfig.withDeserializerModifier(beanDeserializerModifier));
    }

    public final DeserializerFactory withAbstractTypeResolver(AbstractTypeResolver abstractTypeResolver) {
        return withConfig(this._factoryConfig.withAbstractTypeResolver(abstractTypeResolver));
    }

    public final DeserializerFactory withValueInstantiators(ValueInstantiators valueInstantiators) {
        return withConfig(this._factoryConfig.withValueInstantiators(valueInstantiators));
    }

    public JavaType mapAbstractType(DeserializationConfig deserializationConfig, JavaType javaType) throws JsonMappingException {
        JavaType _mapAbstractType2;
        while (true) {
            _mapAbstractType2 = _mapAbstractType2(deserializationConfig, javaType);
            if (_mapAbstractType2 == null) {
                return javaType;
            }
            Class<?> rawClass = javaType.getRawClass();
            Class<?> rawClass2 = _mapAbstractType2.getRawClass();
            if (rawClass != rawClass2 && rawClass.isAssignableFrom(rawClass2)) {
                javaType = _mapAbstractType2;
            }
        }
        throw new IllegalArgumentException("Invalid abstract type resolution from " + javaType + " to " + _mapAbstractType2 + ": latter is not a subtype of former");
    }

    private JavaType _mapAbstractType2(DeserializationConfig deserializationConfig, JavaType javaType) throws JsonMappingException {
        Class<?> rawClass = javaType.getRawClass();
        if (this._factoryConfig.hasAbstractTypeResolvers()) {
            for (AbstractTypeResolver findTypeMapping : this._factoryConfig.abstractTypeResolvers()) {
                JavaType findTypeMapping2 = findTypeMapping.findTypeMapping(deserializationConfig, javaType);
                if (findTypeMapping2 != null && findTypeMapping2.getRawClass() != rawClass) {
                    return findTypeMapping2;
                }
            }
        }
        return null;
    }

    public ValueInstantiator findValueInstantiator(DeserializationContext deserializationContext, BeanDescription beanDescription) throws JsonMappingException {
        ValueInstantiator valueInstantiator;
        DeserializationConfig config = deserializationContext.getConfig();
        ValueInstantiator valueInstantiator2 = null;
        AnnotatedClass classInfo = beanDescription.getClassInfo();
        Object findValueInstantiator = deserializationContext.getAnnotationIntrospector().findValueInstantiator(classInfo);
        if (findValueInstantiator != null) {
            valueInstantiator2 = _valueInstantiatorInstance(config, classInfo, findValueInstantiator);
        }
        if (valueInstantiator2 == null && (valueInstantiator2 = _findStdValueInstantiator(config, beanDescription)) == null) {
            valueInstantiator2 = _constructDefaultValueInstantiator(deserializationContext, beanDescription);
        }
        if (this._factoryConfig.hasValueInstantiators()) {
            valueInstantiator = valueInstantiator2;
            for (ValueInstantiators next : this._factoryConfig.valueInstantiators()) {
                valueInstantiator = next.findValueInstantiator(config, beanDescription, valueInstantiator);
                if (valueInstantiator == null) {
                    throw new JsonMappingException("Broken registered ValueInstantiators (of type " + next.getClass().getName() + "): returned null ValueInstantiator");
                }
            }
        } else {
            valueInstantiator = valueInstantiator2;
        }
        if (valueInstantiator.getIncompleteParameter() == null) {
            return valueInstantiator;
        }
        AnnotatedParameter incompleteParameter = valueInstantiator.getIncompleteParameter();
        throw new IllegalArgumentException("Argument #" + incompleteParameter.getIndex() + " of constructor " + incompleteParameter.getOwner() + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
    }

    private ValueInstantiator _findStdValueInstantiator(DeserializationConfig deserializationConfig, BeanDescription beanDescription) throws JsonMappingException {
        return JacksonDeserializers.findValueInstantiator(deserializationConfig, beanDescription);
    }

    /* access modifiers changed from: protected */
    public ValueInstantiator _constructDefaultValueInstantiator(DeserializationContext deserializationContext, BeanDescription beanDescription) throws JsonMappingException {
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, deserializationContext.canOverrideAccessModifiers());
        AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        DeserializationConfig config = deserializationContext.getConfig();
        VisibilityChecker<?> findAutoDetectVisibility = annotationIntrospector.findAutoDetectVisibility(beanDescription.getClassInfo(), config.getDefaultVisibilityChecker());
        _addDeserializerFactoryMethods(deserializationContext, beanDescription, findAutoDetectVisibility, annotationIntrospector, creatorCollector);
        if (beanDescription.getType().isConcrete()) {
            _addDeserializerConstructors(deserializationContext, beanDescription, findAutoDetectVisibility, annotationIntrospector, creatorCollector);
        }
        return creatorCollector.constructValueInstantiator(config);
    }

    public ValueInstantiator _valueInstantiatorInstance(DeserializationConfig deserializationConfig, Annotated annotated, Object obj) throws JsonMappingException {
        ValueInstantiator valueInstantiatorInstance;
        if (obj == null) {
            return null;
        }
        if (obj instanceof ValueInstantiator) {
            return (ValueInstantiator) obj;
        }
        if (!(obj instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + obj.getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
        }
        Class<NoClass> cls = (Class) obj;
        if (cls == NoClass.class) {
            return null;
        }
        if (!ValueInstantiator.class.isAssignableFrom(cls)) {
            throw new IllegalStateException("AnnotationIntrospector returned Class " + cls.getName() + "; expected Class<ValueInstantiator>");
        }
        HandlerInstantiator handlerInstantiator = deserializationConfig.getHandlerInstantiator();
        if (handlerInstantiator == null || (valueInstantiatorInstance = handlerInstantiator.valueInstantiatorInstance(deserializationConfig, annotated, cls)) == null) {
            return (ValueInstantiator) ClassUtil.createInstance(cls, deserializationConfig.canOverrideAccessModifiers());
        }
        return valueInstantiatorInstance;
    }

    /* access modifiers changed from: protected */
    public void _addDeserializerConstructors(DeserializationContext deserializationContext, BeanDescription beanDescription, VisibilityChecker<?> visibilityChecker, AnnotationIntrospector annotationIntrospector, CreatorCollector creatorCollector) throws JsonMappingException {
        AnnotatedConstructor annotatedConstructor;
        String[] strArr;
        AnnotatedConstructor findDefaultConstructor = beanDescription.findDefaultConstructor();
        if (findDefaultConstructor != null && (!creatorCollector.hasDefaultCreator() || annotationIntrospector.hasCreatorAnnotation(findDefaultConstructor))) {
            creatorCollector.setDefaultCreator(findDefaultConstructor);
        }
        String[] strArr2 = null;
        AnnotatedConstructor annotatedConstructor2 = null;
        for (BeanPropertyDefinition next : beanDescription.findProperties()) {
            if (next.getConstructorParameter() != null) {
                AnnotatedParameter constructorParameter = next.getConstructorParameter();
                AnnotatedWithParams owner = constructorParameter.getOwner();
                if (owner instanceof AnnotatedConstructor) {
                    if (annotatedConstructor2 == null) {
                        annotatedConstructor = (AnnotatedConstructor) owner;
                        strArr = new String[annotatedConstructor.getParameterCount()];
                    } else {
                        annotatedConstructor = annotatedConstructor2;
                        strArr = strArr2;
                    }
                    strArr[constructorParameter.getIndex()] = next.getName();
                    annotatedConstructor2 = annotatedConstructor;
                    strArr2 = strArr;
                }
            }
        }
        Iterator<AnnotatedConstructor> it = beanDescription.getConstructors().iterator();
        while (it.hasNext()) {
            AnnotatedConstructor next2 = it.next();
            int parameterCount = next2.getParameterCount();
            boolean z = annotationIntrospector.hasCreatorAnnotation(next2) || next2 == annotatedConstructor2;
            boolean isCreatorVisible = visibilityChecker.isCreatorVisible((AnnotatedMember) next2);
            if (parameterCount == 1) {
                _handleSingleArgumentConstructor(deserializationContext, beanDescription, visibilityChecker, annotationIntrospector, creatorCollector, next2, z, isCreatorVisible, next2 == annotatedConstructor2 ? strArr2[0] : null);
            } else if (z || isCreatorVisible) {
                AnnotatedParameter annotatedParameter = null;
                int i = 0;
                int i2 = 0;
                CreatorProperty[] creatorPropertyArr = new CreatorProperty[parameterCount];
                int i3 = 0;
                while (i3 < parameterCount) {
                    AnnotatedParameter parameter = next2.getParameter(i3);
                    String str = null;
                    if (next2 == annotatedConstructor2) {
                        str = strArr2[i3];
                    }
                    if (str == null) {
                        PropertyName findNameForDeserialization = parameter == null ? null : annotationIntrospector.findNameForDeserialization(parameter);
                        str = findNameForDeserialization == null ? null : findNameForDeserialization.getSimpleName();
                    }
                    Object findInjectableValueId = annotationIntrospector.findInjectableValueId(parameter);
                    if (str != null && str.length() > 0) {
                        i++;
                        creatorPropertyArr[i3] = constructCreatorProperty(deserializationContext, beanDescription, str, i3, parameter, findInjectableValueId);
                        parameter = annotatedParameter;
                    } else if (findInjectableValueId != null) {
                        i2++;
                        creatorPropertyArr[i3] = constructCreatorProperty(deserializationContext, beanDescription, str, i3, parameter, findInjectableValueId);
                        parameter = annotatedParameter;
                    } else if (annotatedParameter != null) {
                        parameter = annotatedParameter;
                    }
                    i3++;
                    annotatedParameter = parameter;
                }
                if (z || i > 0 || i2 > 0) {
                    if (i + i2 == parameterCount) {
                        creatorCollector.addPropertyCreator(next2, creatorPropertyArr);
                    } else if (i == 0 && i2 + 1 == parameterCount) {
                        creatorCollector.addDelegatingCreator(next2, creatorPropertyArr);
                    } else {
                        creatorCollector.addIncompeteParameter(annotatedParameter);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean _handleSingleArgumentConstructor(DeserializationContext deserializationContext, BeanDescription beanDescription, VisibilityChecker<?> visibilityChecker, AnnotationIntrospector annotationIntrospector, CreatorCollector creatorCollector, AnnotatedConstructor annotatedConstructor, boolean z, boolean z2, String str) throws JsonMappingException {
        String str2;
        AnnotatedParameter parameter = annotatedConstructor.getParameter(0);
        if (str == null) {
            PropertyName findNameForDeserialization = parameter == null ? null : annotationIntrospector.findNameForDeserialization(parameter);
            str2 = findNameForDeserialization == null ? null : findNameForDeserialization.getSimpleName();
        } else {
            str2 = str;
        }
        Object findInjectableValueId = annotationIntrospector.findInjectableValueId(parameter);
        if (findInjectableValueId != null || (str2 != null && str2.length() > 0)) {
            creatorCollector.addPropertyCreator(annotatedConstructor, new CreatorProperty[]{constructCreatorProperty(deserializationContext, beanDescription, str2, 0, parameter, findInjectableValueId)});
            return true;
        }
        Class<?> rawParameterType = annotatedConstructor.getRawParameterType(0);
        if (rawParameterType == String.class) {
            if (z || z2) {
                creatorCollector.addStringCreator(annotatedConstructor);
            }
            return true;
        } else if (rawParameterType == Integer.TYPE || rawParameterType == Integer.class) {
            if (z || z2) {
                creatorCollector.addIntCreator(annotatedConstructor);
            }
            return true;
        } else if (rawParameterType == Long.TYPE || rawParameterType == Long.class) {
            if (z || z2) {
                creatorCollector.addLongCreator(annotatedConstructor);
            }
            return true;
        } else if (rawParameterType == Double.TYPE || rawParameterType == Double.class) {
            if (z || z2) {
                creatorCollector.addDoubleCreator(annotatedConstructor);
            }
            return true;
        } else if (!z) {
            return false;
        } else {
            creatorCollector.addDelegatingCreator(annotatedConstructor, (CreatorProperty[]) null);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void _addDeserializerFactoryMethods(DeserializationContext deserializationContext, BeanDescription beanDescription, VisibilityChecker<?> visibilityChecker, AnnotationIntrospector annotationIntrospector, CreatorCollector creatorCollector) throws JsonMappingException {
        DeserializationConfig config = deserializationContext.getConfig();
        for (AnnotatedMethod next : beanDescription.getFactoryMethods()) {
            boolean hasCreatorAnnotation = annotationIntrospector.hasCreatorAnnotation(next);
            int parameterCount = next.getParameterCount();
            if (parameterCount != 0) {
                if (parameterCount == 1) {
                    AnnotatedParameter parameter = next.getParameter(0);
                    PropertyName findNameForDeserialization = parameter == null ? null : annotationIntrospector.findNameForDeserialization(parameter);
                    String simpleName = findNameForDeserialization == null ? null : findNameForDeserialization.getSimpleName();
                    if (annotationIntrospector.findInjectableValueId(parameter) == null && (simpleName == null || simpleName.length() == 0)) {
                        _handleSingleArgumentFactory(config, beanDescription, visibilityChecker, annotationIntrospector, creatorCollector, next, hasCreatorAnnotation);
                    }
                } else if (!annotationIntrospector.hasCreatorAnnotation(next)) {
                    continue;
                }
                AnnotatedParameter annotatedParameter = null;
                CreatorProperty[] creatorPropertyArr = new CreatorProperty[parameterCount];
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                while (i3 < parameterCount) {
                    AnnotatedParameter parameter2 = next.getParameter(i3);
                    PropertyName findNameForDeserialization2 = parameter2 == null ? null : annotationIntrospector.findNameForDeserialization(parameter2);
                    String simpleName2 = findNameForDeserialization2 == null ? null : findNameForDeserialization2.getSimpleName();
                    Object findInjectableValueId = annotationIntrospector.findInjectableValueId(parameter2);
                    if (simpleName2 != null && simpleName2.length() > 0) {
                        i++;
                        creatorPropertyArr[i3] = constructCreatorProperty(deserializationContext, beanDescription, simpleName2, i3, parameter2, findInjectableValueId);
                        parameter2 = annotatedParameter;
                    } else if (findInjectableValueId != null) {
                        i2++;
                        creatorPropertyArr[i3] = constructCreatorProperty(deserializationContext, beanDescription, simpleName2, i3, parameter2, findInjectableValueId);
                        parameter2 = annotatedParameter;
                    } else if (annotatedParameter != null) {
                        parameter2 = annotatedParameter;
                    }
                    i3++;
                    annotatedParameter = parameter2;
                }
                if (hasCreatorAnnotation || i > 0 || i2 > 0) {
                    if (i + i2 == parameterCount) {
                        creatorCollector.addPropertyCreator(next, creatorPropertyArr);
                    } else if (i == 0 && i2 + 1 == parameterCount) {
                        creatorCollector.addDelegatingCreator(next, creatorPropertyArr);
                    } else {
                        throw new IllegalArgumentException("Argument #" + annotatedParameter.getIndex() + " of factory method " + next + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
                    }
                }
            } else if (hasCreatorAnnotation) {
                creatorCollector.setDefaultCreator(next);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean _handleSingleArgumentFactory(DeserializationConfig deserializationConfig, BeanDescription beanDescription, VisibilityChecker<?> visibilityChecker, AnnotationIntrospector annotationIntrospector, CreatorCollector creatorCollector, AnnotatedMethod annotatedMethod, boolean z) throws JsonMappingException {
        Class<?> rawParameterType = annotatedMethod.getRawParameterType(0);
        if (rawParameterType == String.class) {
            if (!z && !visibilityChecker.isCreatorVisible((AnnotatedMember) annotatedMethod)) {
                return true;
            }
            creatorCollector.addStringCreator(annotatedMethod);
            return true;
        } else if (rawParameterType == Integer.TYPE || rawParameterType == Integer.class) {
            if (!z && !visibilityChecker.isCreatorVisible((AnnotatedMember) annotatedMethod)) {
                return true;
            }
            creatorCollector.addIntCreator(annotatedMethod);
            return true;
        } else if (rawParameterType == Long.TYPE || rawParameterType == Long.class) {
            if (!z && !visibilityChecker.isCreatorVisible((AnnotatedMember) annotatedMethod)) {
                return true;
            }
            creatorCollector.addLongCreator(annotatedMethod);
            return true;
        } else if (rawParameterType == Double.TYPE || rawParameterType == Double.class) {
            if (!z && !visibilityChecker.isCreatorVisible((AnnotatedMember) annotatedMethod)) {
                return true;
            }
            creatorCollector.addDoubleCreator(annotatedMethod);
            return true;
        } else if (rawParameterType == Boolean.TYPE || rawParameterType == Boolean.class) {
            if (!z && !visibilityChecker.isCreatorVisible((AnnotatedMember) annotatedMethod)) {
                return true;
            }
            creatorCollector.addBooleanCreator(annotatedMethod);
            return true;
        } else if (!annotationIntrospector.hasCreatorAnnotation(annotatedMethod)) {
            return false;
        } else {
            creatorCollector.addDelegatingCreator(annotatedMethod, (CreatorProperty[]) null);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public CreatorProperty constructCreatorProperty(DeserializationContext deserializationContext, BeanDescription beanDescription, String str, int i, AnnotatedParameter annotatedParameter, Object obj) throws JsonMappingException {
        BeanProperty.Std std;
        TypeDeserializer typeDeserializer;
        DeserializationConfig config = deserializationContext.getConfig();
        AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        Boolean hasRequiredMarker = annotationIntrospector == null ? null : annotationIntrospector.hasRequiredMarker(annotatedParameter);
        boolean booleanValue = hasRequiredMarker == null ? false : hasRequiredMarker.booleanValue();
        JavaType constructType = config.getTypeFactory().constructType(annotatedParameter.getParameterType(), beanDescription.bindingsForBeanType());
        BeanProperty.Std std2 = new BeanProperty.Std(str, constructType, annotationIntrospector.findWrapperName(annotatedParameter), beanDescription.getClassAnnotations(), annotatedParameter, booleanValue);
        JavaType resolveType = resolveType(deserializationContext, beanDescription, constructType, annotatedParameter);
        if (resolveType != constructType) {
            std = std2.withType(resolveType);
        } else {
            std = std2;
        }
        JsonDeserializer<Object> findDeserializerFromAnnotation = findDeserializerFromAnnotation(deserializationContext, annotatedParameter);
        JavaType modifyTypeByAnnotation = modifyTypeByAnnotation(deserializationContext, annotatedParameter, resolveType);
        TypeDeserializer typeDeserializer2 = (TypeDeserializer) modifyTypeByAnnotation.getTypeHandler();
        if (typeDeserializer2 == null) {
            typeDeserializer = findTypeDeserializer(config, modifyTypeByAnnotation);
        } else {
            typeDeserializer = typeDeserializer2;
        }
        CreatorProperty creatorProperty = new CreatorProperty(str, modifyTypeByAnnotation, std.getWrapperName(), typeDeserializer, beanDescription.getClassAnnotations(), annotatedParameter, i, obj, std.isRequired());
        if (findDeserializerFromAnnotation != null) {
            return creatorProperty.withValueDeserializer((JsonDeserializer<?>) findDeserializerFromAnnotation);
        }
        return creatorProperty;
    }

    public JsonDeserializer<?> createArrayDeserializer(DeserializationContext deserializationContext, ArrayType arrayType, BeanDescription beanDescription) throws JsonMappingException {
        TypeDeserializer typeDeserializer;
        DeserializationConfig config = deserializationContext.getConfig();
        JavaType contentType = arrayType.getContentType();
        JsonDeserializer jsonDeserializer = (JsonDeserializer) contentType.getValueHandler();
        TypeDeserializer typeDeserializer2 = (TypeDeserializer) contentType.getTypeHandler();
        if (typeDeserializer2 == null) {
            typeDeserializer = findTypeDeserializer(config, contentType);
        } else {
            typeDeserializer = typeDeserializer2;
        }
        JsonDeserializer<?> _findCustomArrayDeserializer = _findCustomArrayDeserializer(arrayType, config, beanDescription, typeDeserializer, jsonDeserializer);
        if (_findCustomArrayDeserializer == null) {
            if (jsonDeserializer == null) {
                Class<?> rawClass = contentType.getRawClass();
                if (contentType.isPrimitive()) {
                    return PrimitiveArrayDeserializers.forType(rawClass);
                }
                if (rawClass == String.class) {
                    return StringArrayDeserializer.instance;
                }
            }
            if (_findCustomArrayDeserializer == null) {
                _findCustomArrayDeserializer = new ObjectArrayDeserializer(arrayType, jsonDeserializer, typeDeserializer);
            }
        }
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return _findCustomArrayDeserializer;
        }
        Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            JsonDeserializer<?> jsonDeserializer2 = _findCustomArrayDeserializer;
            if (!it.hasNext()) {
                return jsonDeserializer2;
            }
            _findCustomArrayDeserializer = it.next().modifyArrayDeserializer(config, arrayType, beanDescription, jsonDeserializer2);
        }
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType arrayType, DeserializationConfig deserializationConfig, BeanDescription beanDescription, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        for (Deserializers findArrayDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findArrayDeserializer2 = findArrayDeserializer.findArrayDeserializer(arrayType, deserializationConfig, beanDescription, typeDeserializer, jsonDeserializer);
            if (findArrayDeserializer2 != null) {
                return findArrayDeserializer2;
            }
        }
        return null;
    }

    public JsonDeserializer<?> createCollectionDeserializer(DeserializationContext deserializationContext, CollectionType collectionType, BeanDescription beanDescription) throws JsonMappingException {
        TypeDeserializer typeDeserializer;
        CollectionType collectionType2;
        JsonDeserializer<?> jsonDeserializer;
        JavaType contentType = collectionType.getContentType();
        JsonDeserializer jsonDeserializer2 = (JsonDeserializer) contentType.getValueHandler();
        DeserializationConfig config = deserializationContext.getConfig();
        TypeDeserializer typeDeserializer2 = (TypeDeserializer) contentType.getTypeHandler();
        if (typeDeserializer2 == null) {
            typeDeserializer = findTypeDeserializer(config, contentType);
        } else {
            typeDeserializer = typeDeserializer2;
        }
        JsonDeserializer<?> _findCustomCollectionDeserializer = _findCustomCollectionDeserializer(collectionType, config, beanDescription, typeDeserializer, jsonDeserializer2);
        if (_findCustomCollectionDeserializer == null) {
            Class<?> rawClass = collectionType.getRawClass();
            if (jsonDeserializer2 == null && EnumSet.class.isAssignableFrom(rawClass)) {
                _findCustomCollectionDeserializer = new EnumSetDeserializer(contentType, (JsonDeserializer<?>) null);
            }
        }
        if (_findCustomCollectionDeserializer == null) {
            if (collectionType.isInterface() || collectionType.isAbstract()) {
                collectionType2 = _mapAbstractCollectionType(collectionType, config);
                if (collectionType2 == null) {
                    throw new IllegalArgumentException("Can not find a deserializer for non-concrete Collection type " + collectionType);
                }
                beanDescription = config.introspectForCreation(collectionType2);
            } else {
                collectionType2 = collectionType;
            }
            ValueInstantiator findValueInstantiator = findValueInstantiator(deserializationContext, beanDescription);
            if (!findValueInstantiator.canCreateUsingDefault() && collectionType2.getRawClass() == ArrayBlockingQueue.class) {
                return new ArrayBlockingQueueDeserializer(collectionType2, jsonDeserializer2, typeDeserializer, findValueInstantiator, (JsonDeserializer<Object>) null);
            }
            if (contentType.getRawClass() == String.class) {
                _findCustomCollectionDeserializer = new StringCollectionDeserializer(collectionType2, jsonDeserializer2, findValueInstantiator);
            } else {
                _findCustomCollectionDeserializer = new CollectionDeserializer(collectionType2, jsonDeserializer2, typeDeserializer, findValueInstantiator);
            }
        } else {
            collectionType2 = collectionType;
        }
        if (this._factoryConfig.hasDeserializerModifiers()) {
            Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                jsonDeserializer = _findCustomCollectionDeserializer;
                if (!it.hasNext()) {
                    break;
                }
                _findCustomCollectionDeserializer = it.next().modifyCollectionDeserializer(config, collectionType2, beanDescription, jsonDeserializer);
            }
        } else {
            jsonDeserializer = _findCustomCollectionDeserializer;
        }
        return jsonDeserializer;
    }

    /* access modifiers changed from: protected */
    public CollectionType _mapAbstractCollectionType(JavaType javaType, DeserializationConfig deserializationConfig) {
        Class cls = _collectionFallbacks.get(javaType.getRawClass().getName());
        if (cls == null) {
            return null;
        }
        return (CollectionType) deserializationConfig.constructSpecializedType(javaType, cls);
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType collectionType, DeserializationConfig deserializationConfig, BeanDescription beanDescription, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        for (Deserializers findCollectionDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findCollectionDeserializer2 = findCollectionDeserializer.findCollectionDeserializer(collectionType, deserializationConfig, beanDescription, typeDeserializer, jsonDeserializer);
            if (findCollectionDeserializer2 != null) {
                return findCollectionDeserializer2;
            }
        }
        return null;
    }

    public JsonDeserializer<?> createCollectionLikeDeserializer(DeserializationContext deserializationContext, CollectionLikeType collectionLikeType, BeanDescription beanDescription) throws JsonMappingException {
        TypeDeserializer typeDeserializer;
        JavaType contentType = collectionLikeType.getContentType();
        JsonDeserializer jsonDeserializer = (JsonDeserializer) contentType.getValueHandler();
        DeserializationConfig config = deserializationContext.getConfig();
        TypeDeserializer typeDeserializer2 = (TypeDeserializer) contentType.getTypeHandler();
        if (typeDeserializer2 == null) {
            typeDeserializer = findTypeDeserializer(config, contentType);
        } else {
            typeDeserializer = typeDeserializer2;
        }
        JsonDeserializer<?> _findCustomCollectionLikeDeserializer = _findCustomCollectionLikeDeserializer(collectionLikeType, config, beanDescription, typeDeserializer, jsonDeserializer);
        if (_findCustomCollectionLikeDeserializer == null || !this._factoryConfig.hasDeserializerModifiers()) {
            return _findCustomCollectionLikeDeserializer;
        }
        Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            JsonDeserializer<?> jsonDeserializer2 = _findCustomCollectionLikeDeserializer;
            if (!it.hasNext()) {
                return jsonDeserializer2;
            }
            _findCustomCollectionLikeDeserializer = it.next().modifyCollectionLikeDeserializer(config, collectionLikeType, beanDescription, jsonDeserializer2);
        }
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> _findCustomCollectionLikeDeserializer(CollectionLikeType collectionLikeType, DeserializationConfig deserializationConfig, BeanDescription beanDescription, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        for (Deserializers findCollectionLikeDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findCollectionLikeDeserializer2 = findCollectionLikeDeserializer.findCollectionLikeDeserializer(collectionLikeType, deserializationConfig, beanDescription, typeDeserializer, jsonDeserializer);
            if (findCollectionLikeDeserializer2 != null) {
                return findCollectionLikeDeserializer2;
            }
        }
        return null;
    }

    /* JADX WARNING: Failed to insert additional move for type inference */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.JsonDeserializer<?> createMapDeserializer(com.shaded.fasterxml.jackson.databind.DeserializationContext r16, com.shaded.fasterxml.jackson.databind.type.MapType r17, com.shaded.fasterxml.jackson.databind.BeanDescription r18) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r15 = this;
            com.shaded.fasterxml.jackson.databind.DeserializationConfig r4 = r16.getConfig()
            com.shaded.fasterxml.jackson.databind.JavaType r10 = r17.getKeyType()
            com.shaded.fasterxml.jackson.databind.JavaType r3 = r17.getContentType()
            java.lang.Object r8 = r3.getValueHandler()
            com.shaded.fasterxml.jackson.databind.JsonDeserializer r8 = (com.shaded.fasterxml.jackson.databind.JsonDeserializer) r8
            java.lang.Object r6 = r10.getValueHandler()
            com.shaded.fasterxml.jackson.databind.KeyDeserializer r6 = (com.shaded.fasterxml.jackson.databind.KeyDeserializer) r6
            java.lang.Object r2 = r3.getTypeHandler()
            com.shaded.fasterxml.jackson.databind.jsontype.TypeDeserializer r2 = (com.shaded.fasterxml.jackson.databind.jsontype.TypeDeserializer) r2
            if (r2 != 0) goto L_0x00e7
            com.shaded.fasterxml.jackson.databind.jsontype.TypeDeserializer r7 = r15.findTypeDeserializer(r4, r3)
        L_0x0024:
            r2 = r15
            r3 = r17
            r5 = r18
            com.shaded.fasterxml.jackson.databind.JsonDeserializer r9 = r2._findCustomMapDeserializer(r3, r4, r5, r6, r7, r8)
            if (r9 != 0) goto L_0x00bc
            java.lang.Class r2 = r17.getRawClass()
            java.lang.Class<java.util.EnumMap> r3 = java.util.EnumMap.class
            boolean r3 = r3.isAssignableFrom(r2)
            if (r3 == 0) goto L_0x0057
            java.lang.Class r3 = r10.getRawClass()
            if (r3 == 0) goto L_0x0047
            boolean r3 = r3.isEnum()
            if (r3 != 0) goto L_0x004f
        L_0x0047:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.String r3 = "Can not construct EnumMap; generic (key) type not available"
            r2.<init>(r3)
            throw r2
        L_0x004f:
            com.shaded.fasterxml.jackson.databind.deser.std.EnumMapDeserializer r9 = new com.shaded.fasterxml.jackson.databind.deser.std.EnumMapDeserializer
            r3 = 0
            r0 = r17
            r9.<init>(r0, r3, r8, r7)
        L_0x0057:
            if (r9 != 0) goto L_0x00bc
            boolean r3 = r17.isInterface()
            if (r3 != 0) goto L_0x0065
            boolean r3 = r17.isAbstract()
            if (r3 == 0) goto L_0x00e4
        L_0x0065:
            java.util.HashMap<java.lang.String, java.lang.Class<? extends java.util.Map>> r3 = _mapFallbacks
            java.lang.String r2 = r2.getName()
            java.lang.Object r2 = r3.get(r2)
            java.lang.Class r2 = (java.lang.Class) r2
            if (r2 != 0) goto L_0x008e
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Can not find a deserializer for non-concrete Map type "
            java.lang.StringBuilder r3 = r3.append(r4)
            r0 = r17
            java.lang.StringBuilder r3 = r3.append(r0)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        L_0x008e:
            r0 = r17
            com.shaded.fasterxml.jackson.databind.JavaType r2 = r4.constructSpecializedType(r0, r2)
            com.shaded.fasterxml.jackson.databind.type.MapType r2 = (com.shaded.fasterxml.jackson.databind.type.MapType) r2
            com.shaded.fasterxml.jackson.databind.BeanDescription r18 = r4.introspectForCreation(r2)
            r10 = r2
        L_0x009b:
            r0 = r16
            r1 = r18
            com.shaded.fasterxml.jackson.databind.deser.ValueInstantiator r11 = r15.findValueInstantiator(r0, r1)
            com.shaded.fasterxml.jackson.databind.deser.std.MapDeserializer r9 = new com.shaded.fasterxml.jackson.databind.deser.std.MapDeserializer
            r12 = r6
            r13 = r8
            r14 = r7
            r9.<init>((com.shaded.fasterxml.jackson.databind.JavaType) r10, (com.shaded.fasterxml.jackson.databind.deser.ValueInstantiator) r11, (com.shaded.fasterxml.jackson.databind.KeyDeserializer) r12, (com.shaded.fasterxml.jackson.databind.JsonDeserializer<java.lang.Object>) r13, (com.shaded.fasterxml.jackson.databind.jsontype.TypeDeserializer) r14)
            com.shaded.fasterxml.jackson.databind.AnnotationIntrospector r2 = r4.getAnnotationIntrospector()
            com.shaded.fasterxml.jackson.databind.introspect.AnnotatedClass r3 = r18.getClassInfo()
            java.lang.String[] r2 = r2.findPropertiesToIgnore(r3)
            r9.setIgnorableProperties(r2)
            r17 = r10
        L_0x00bc:
            com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig r2 = r15._factoryConfig
            boolean r2 = r2.hasDeserializerModifiers()
            if (r2 == 0) goto L_0x00e3
            com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig r2 = r15._factoryConfig
            java.lang.Iterable r2 = r2.deserializerModifiers()
            java.util.Iterator r3 = r2.iterator()
        L_0x00ce:
            boolean r2 = r3.hasNext()
            if (r2 == 0) goto L_0x00e3
            java.lang.Object r2 = r3.next()
            com.shaded.fasterxml.jackson.databind.deser.BeanDeserializerModifier r2 = (com.shaded.fasterxml.jackson.databind.deser.BeanDeserializerModifier) r2
            r0 = r17
            r1 = r18
            com.shaded.fasterxml.jackson.databind.JsonDeserializer r9 = r2.modifyMapDeserializer(r4, r0, r1, r9)
            goto L_0x00ce
        L_0x00e3:
            return r9
        L_0x00e4:
            r10 = r17
            goto L_0x009b
        L_0x00e7:
            r7 = r2
            goto L_0x0024
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.deser.BasicDeserializerFactory.createMapDeserializer(com.shaded.fasterxml.jackson.databind.DeserializationContext, com.shaded.fasterxml.jackson.databind.type.MapType, com.shaded.fasterxml.jackson.databind.BeanDescription):com.shaded.fasterxml.jackson.databind.JsonDeserializer");
    }

    public JsonDeserializer<?> createMapLikeDeserializer(DeserializationContext deserializationContext, MapLikeType mapLikeType, BeanDescription beanDescription) throws JsonMappingException {
        TypeDeserializer typeDeserializer;
        JavaType keyType = mapLikeType.getKeyType();
        JavaType contentType = mapLikeType.getContentType();
        DeserializationConfig config = deserializationContext.getConfig();
        JsonDeserializer jsonDeserializer = (JsonDeserializer) contentType.getValueHandler();
        KeyDeserializer keyDeserializer = (KeyDeserializer) keyType.getValueHandler();
        TypeDeserializer typeDeserializer2 = (TypeDeserializer) contentType.getTypeHandler();
        if (typeDeserializer2 == null) {
            typeDeserializer = findTypeDeserializer(config, contentType);
        } else {
            typeDeserializer = typeDeserializer2;
        }
        JsonDeserializer<?> _findCustomMapLikeDeserializer = _findCustomMapLikeDeserializer(mapLikeType, config, beanDescription, keyDeserializer, typeDeserializer, jsonDeserializer);
        if (_findCustomMapLikeDeserializer == null || !this._factoryConfig.hasDeserializerModifiers()) {
            return _findCustomMapLikeDeserializer;
        }
        Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            JsonDeserializer<?> jsonDeserializer2 = _findCustomMapLikeDeserializer;
            if (!it.hasNext()) {
                return jsonDeserializer2;
            }
            _findCustomMapLikeDeserializer = it.next().modifyMapLikeDeserializer(config, mapLikeType, beanDescription, jsonDeserializer2);
        }
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> _findCustomMapDeserializer(MapType mapType, DeserializationConfig deserializationConfig, BeanDescription beanDescription, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        for (Deserializers findMapDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findMapDeserializer2 = findMapDeserializer.findMapDeserializer(mapType, deserializationConfig, beanDescription, keyDeserializer, typeDeserializer, jsonDeserializer);
            if (findMapDeserializer2 != null) {
                return findMapDeserializer2;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> _findCustomMapLikeDeserializer(MapLikeType mapLikeType, DeserializationConfig deserializationConfig, BeanDescription beanDescription, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        for (Deserializers findMapLikeDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findMapLikeDeserializer2 = findMapLikeDeserializer.findMapLikeDeserializer(mapLikeType, deserializationConfig, beanDescription, keyDeserializer, typeDeserializer, jsonDeserializer);
            if (findMapLikeDeserializer2 != null) {
                return findMapLikeDeserializer2;
            }
        }
        return null;
    }

    public JsonDeserializer<?> createEnumDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        JsonDeserializer<?> jsonDeserializer;
        DeserializationConfig config = deserializationContext.getConfig();
        Class<?> rawClass = javaType.getRawClass();
        JsonDeserializer<?> _findCustomEnumDeserializer = _findCustomEnumDeserializer(rawClass, config, beanDescription);
        if (_findCustomEnumDeserializer == null) {
            Iterator<AnnotatedMethod> it = beanDescription.getFactoryMethods().iterator();
            while (true) {
                if (!it.hasNext()) {
                    jsonDeserializer = _findCustomEnumDeserializer;
                    break;
                }
                AnnotatedMethod next = it.next();
                if (deserializationContext.getAnnotationIntrospector().hasCreatorAnnotation(next)) {
                    if (next.getParameterCount() != 1 || !next.getRawReturnType().isAssignableFrom(rawClass)) {
                        throw new IllegalArgumentException("Unsuitable method (" + next + ") decorated with @JsonCreator (for Enum type " + rawClass.getName() + ")");
                    }
                    jsonDeserializer = EnumDeserializer.deserializerForCreator(config, rawClass, next);
                }
            }
            if (jsonDeserializer == null) {
                jsonDeserializer = new EnumDeserializer(constructEnumResolver(rawClass, config, beanDescription.findJsonValueMethod()));
            }
        } else {
            jsonDeserializer = _findCustomEnumDeserializer;
        }
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return jsonDeserializer;
        }
        Iterator<BeanDeserializerModifier> it2 = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            JsonDeserializer<?> jsonDeserializer2 = jsonDeserializer;
            if (!it2.hasNext()) {
                return jsonDeserializer2;
            }
            jsonDeserializer = it2.next().modifyEnumDeserializer(config, javaType, beanDescription, jsonDeserializer2);
        }
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> cls, DeserializationConfig deserializationConfig, BeanDescription beanDescription) throws JsonMappingException {
        for (Deserializers findEnumDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findEnumDeserializer2 = findEnumDeserializer.findEnumDeserializer(cls, deserializationConfig, beanDescription);
            if (findEnumDeserializer2 != null) {
                return findEnumDeserializer2;
            }
        }
        return null;
    }

    public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        Class<?> rawClass = javaType.getRawClass();
        JsonDeserializer<?> _findCustomTreeNodeDeserializer = _findCustomTreeNodeDeserializer(rawClass, deserializationConfig, beanDescription);
        return _findCustomTreeNodeDeserializer != null ? _findCustomTreeNodeDeserializer : JsonNodeDeserializer.getDeserializer(rawClass);
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> cls, DeserializationConfig deserializationConfig, BeanDescription beanDescription) throws JsonMappingException {
        for (Deserializers findTreeNodeDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findTreeNodeDeserializer2 = findTreeNodeDeserializer.findTreeNodeDeserializer(cls, deserializationConfig, beanDescription);
            if (findTreeNodeDeserializer2 != null) {
                return findTreeNodeDeserializer2;
            }
        }
        return null;
    }

    public TypeDeserializer findTypeDeserializer(DeserializationConfig deserializationConfig, JavaType javaType) throws JsonMappingException {
        JavaType mapAbstractType;
        Collection<NamedType> collection = null;
        AnnotatedClass classInfo = deserializationConfig.introspectClassAnnotations(javaType.getRawClass()).getClassInfo();
        AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder findTypeResolver = annotationIntrospector.findTypeResolver(deserializationConfig, classInfo, javaType);
        if (findTypeResolver == null) {
            findTypeResolver = deserializationConfig.getDefaultTyper(javaType);
            if (findTypeResolver == null) {
                return null;
            }
        } else {
            collection = deserializationConfig.getSubtypeResolver().collectAndResolveSubtypes(classInfo, (MapperConfig<?>) deserializationConfig, annotationIntrospector);
        }
        if (findTypeResolver.getDefaultImpl() == null && javaType.isAbstract() && (mapAbstractType = mapAbstractType(deserializationConfig, javaType)) != null && mapAbstractType.getRawClass() != javaType.getRawClass()) {
            findTypeResolver = findTypeResolver.defaultImpl(mapAbstractType.getRawClass());
        }
        return findTypeResolver.buildTypeDeserializer(deserializationConfig, javaType, collection);
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x001f A[LOOP:0: B:3:0x001f->B:6:0x002f, LOOP_START, PHI: r0 
      PHI: (r0v12 com.shaded.fasterxml.jackson.databind.KeyDeserializer) = (r0v0 com.shaded.fasterxml.jackson.databind.KeyDeserializer), (r0v15 com.shaded.fasterxml.jackson.databind.KeyDeserializer) binds: [B:2:0x000d, B:6:0x002f] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.shaded.fasterxml.jackson.databind.KeyDeserializer createKeyDeserializer(com.shaded.fasterxml.jackson.databind.DeserializationContext r6, com.shaded.fasterxml.jackson.databind.JavaType r7) throws com.shaded.fasterxml.jackson.databind.JsonMappingException {
        /*
            r5 = this;
            com.shaded.fasterxml.jackson.databind.DeserializationConfig r2 = r6.getConfig()
            r0 = 0
            com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig r1 = r5._factoryConfig
            boolean r1 = r1.hasKeyDeserializers()
            if (r1 == 0) goto L_0x0031
            java.lang.Class r1 = r7.getRawClass()
            com.shaded.fasterxml.jackson.databind.BeanDescription r1 = r2.introspectClassAnnotations((java.lang.Class<?>) r1)
            com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig r3 = r5._factoryConfig
            java.lang.Iterable r3 = r3.keyDeserializers()
            java.util.Iterator r3 = r3.iterator()
        L_0x001f:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0031
            java.lang.Object r0 = r3.next()
            com.shaded.fasterxml.jackson.databind.deser.KeyDeserializers r0 = (com.shaded.fasterxml.jackson.databind.deser.KeyDeserializers) r0
            com.shaded.fasterxml.jackson.databind.KeyDeserializer r0 = r0.findKeyDeserializer(r7, r2, r1)
            if (r0 == 0) goto L_0x001f
        L_0x0031:
            if (r0 != 0) goto L_0x0042
            boolean r0 = r7.isEnumType()
            if (r0 == 0) goto L_0x003e
            com.shaded.fasterxml.jackson.databind.KeyDeserializer r0 = r5._createEnumKeyDeserializer(r6, r7)
        L_0x003d:
            return r0
        L_0x003e:
            com.shaded.fasterxml.jackson.databind.KeyDeserializer r0 = com.shaded.fasterxml.jackson.databind.deser.std.StdKeyDeserializers.findStringBasedKeyDeserializer(r2, r7)
        L_0x0042:
            if (r0 == 0) goto L_0x003d
            com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig r1 = r5._factoryConfig
            boolean r1 = r1.hasDeserializerModifiers()
            if (r1 == 0) goto L_0x003d
            com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig r1 = r5._factoryConfig
            java.lang.Iterable r1 = r1.deserializerModifiers()
            java.util.Iterator r3 = r1.iterator()
            r1 = r0
        L_0x0057:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x0069
            java.lang.Object r0 = r3.next()
            com.shaded.fasterxml.jackson.databind.deser.BeanDeserializerModifier r0 = (com.shaded.fasterxml.jackson.databind.deser.BeanDeserializerModifier) r0
            com.shaded.fasterxml.jackson.databind.KeyDeserializer r0 = r0.modifyKeyDeserializer(r2, r7, r1)
            r1 = r0
            goto L_0x0057
        L_0x0069:
            r0 = r1
            goto L_0x003d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shaded.fasterxml.jackson.databind.deser.BasicDeserializerFactory.createKeyDeserializer(com.shaded.fasterxml.jackson.databind.DeserializationContext, com.shaded.fasterxml.jackson.databind.JavaType):com.shaded.fasterxml.jackson.databind.KeyDeserializer");
    }

    private KeyDeserializer _createEnumKeyDeserializer(DeserializationContext deserializationContext, JavaType javaType) throws JsonMappingException {
        DeserializationConfig config = deserializationContext.getConfig();
        BeanDescription introspect = config.introspect(javaType);
        JsonDeserializer<Object> findDeserializerFromAnnotation = findDeserializerFromAnnotation(deserializationContext, introspect.getClassInfo());
        if (findDeserializerFromAnnotation != null) {
            return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, javaType, findDeserializerFromAnnotation);
        }
        Class<?> rawClass = javaType.getRawClass();
        if (_findCustomEnumDeserializer(rawClass, config, introspect) != null) {
            return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, javaType, findDeserializerFromAnnotation);
        }
        EnumResolver<?> constructEnumResolver = constructEnumResolver(rawClass, config, introspect.findJsonValueMethod());
        for (AnnotatedMethod next : introspect.getFactoryMethods()) {
            if (config.getAnnotationIntrospector().hasCreatorAnnotation(next)) {
                if (next.getParameterCount() != 1 || !next.getRawReturnType().isAssignableFrom(rawClass)) {
                    throw new IllegalArgumentException("Unsuitable method (" + next + ") decorated with @JsonCreator (for Enum type " + rawClass.getName() + ")");
                } else if (next.getGenericParameterType(0) != String.class) {
                    throw new IllegalArgumentException("Parameter #0 type for factory method (" + next + ") not suitable, must be java.lang.String");
                } else {
                    if (config.canOverrideAccessModifiers()) {
                        ClassUtil.checkAndFixAccess(next.getMember());
                    }
                    return StdKeyDeserializers.constructEnumKeyDeserializer(constructEnumResolver, next);
                }
            }
        }
        return StdKeyDeserializers.constructEnumKeyDeserializer(constructEnumResolver);
    }

    public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, AnnotatedMember annotatedMember) throws JsonMappingException {
        AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder<?> findPropertyTypeResolver = annotationIntrospector.findPropertyTypeResolver(deserializationConfig, annotatedMember, javaType);
        if (findPropertyTypeResolver == null) {
            return findTypeDeserializer(deserializationConfig, javaType);
        }
        return findPropertyTypeResolver.buildTypeDeserializer(deserializationConfig, javaType, deserializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, deserializationConfig, annotationIntrospector, javaType));
    }

    public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig deserializationConfig, JavaType javaType, AnnotatedMember annotatedMember) throws JsonMappingException {
        AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder<?> findPropertyContentTypeResolver = annotationIntrospector.findPropertyContentTypeResolver(deserializationConfig, annotatedMember, javaType);
        JavaType contentType = javaType.getContentType();
        if (findPropertyContentTypeResolver == null) {
            return findTypeDeserializer(deserializationConfig, contentType);
        }
        return findPropertyContentTypeResolver.buildTypeDeserializer(deserializationConfig, contentType, deserializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, deserializationConfig, annotationIntrospector, contentType));
    }

    public JsonDeserializer<?> findDefaultDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        Class<?> rawClass = javaType.getRawClass();
        String name = rawClass.getName();
        if (rawClass.isPrimitive() || name.startsWith("java.")) {
            if (rawClass == CLASS_OBJECT) {
                return UntypedObjectDeserializer.instance;
            }
            if (rawClass == CLASS_STRING || rawClass == CLASS_CHAR_BUFFER) {
                return StringDeserializer.instance;
            }
            if (rawClass == CLASS_ITERABLE) {
                return createCollectionDeserializer(deserializationContext, deserializationContext.getTypeFactory().constructCollectionType((Class<? extends Collection>) Collection.class, javaType.containedTypeCount() > 0 ? javaType.containedType(0) : TypeFactory.unknownType()), beanDescription);
            }
            JsonDeserializer<?> find = NumberDeserializers.find(rawClass, name);
            if (find != null) {
                return find;
            }
            JsonDeserializer<?> find2 = DateDeserializers.find(rawClass, name);
            if (find2 == null) {
                return JdkDeserializers.find(rawClass, name);
            }
            return find2;
        } else if (name.startsWith("com.fasterxml.")) {
            return JacksonDeserializers.find(rawClass);
        } else {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext deserializationContext, Annotated annotated) throws JsonMappingException {
        Object findDeserializer = deserializationContext.getAnnotationIntrospector().findDeserializer(annotated);
        if (findDeserializer == null) {
            return null;
        }
        return deserializationContext.deserializerInstance(annotated, findDeserializer);
    }

    /* access modifiers changed from: protected */
    public <T extends JavaType> T modifyTypeByAnnotation(DeserializationContext deserializationContext, Annotated annotated, T t) throws JsonMappingException {
        T t2;
        T t3;
        JsonDeserializer<Object> deserializerInstance;
        KeyDeserializer keyDeserializerInstance;
        AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        Class<?> findDeserializationType = annotationIntrospector.findDeserializationType(annotated, t);
        if (findDeserializationType != null) {
            try {
                t2 = t.narrowBy(findDeserializationType);
            } catch (IllegalArgumentException e) {
                throw new JsonMappingException("Failed to narrow type " + t + " with concrete-type annotation (value " + findDeserializationType.getName() + "), method '" + annotated.getName() + "': " + e.getMessage(), (JsonLocation) null, e);
            }
        } else {
            t2 = t;
        }
        if (!t2.isContainerType()) {
            return t2;
        }
        Class<?> findDeserializationKeyType = annotationIntrospector.findDeserializationKeyType(annotated, t2.getKeyType());
        if (findDeserializationKeyType == null) {
            t3 = t2;
        } else if (!(t2 instanceof MapLikeType)) {
            throw new JsonMappingException("Illegal key-type annotation: type " + t2 + " is not a Map(-like) type");
        } else {
            try {
                t3 = ((MapLikeType) t2).narrowKey(findDeserializationKeyType);
            } catch (IllegalArgumentException e2) {
                throw new JsonMappingException("Failed to narrow key type " + t2 + " with key-type annotation (" + findDeserializationKeyType.getName() + "): " + e2.getMessage(), (JsonLocation) null, e2);
            }
        }
        JavaType keyType = t3.getKeyType();
        if (!(keyType == null || keyType.getValueHandler() != null || (keyDeserializerInstance = deserializationContext.keyDeserializerInstance(annotated, annotationIntrospector.findKeyDeserializer(annotated))) == null)) {
            t3 = ((MapLikeType) t3).withKeyValueHandler(keyDeserializerInstance);
            t3.getKeyType();
        }
        Class<?> findDeserializationContentType = annotationIntrospector.findDeserializationContentType(annotated, t3.getContentType());
        if (findDeserializationContentType != null) {
            try {
                t3 = t3.narrowContentsBy(findDeserializationContentType);
            } catch (IllegalArgumentException e3) {
                throw new JsonMappingException("Failed to narrow content type " + t3 + " with content-type annotation (" + findDeserializationContentType.getName() + "): " + e3.getMessage(), (JsonLocation) null, e3);
            }
        }
        if (t3.getContentType().getValueHandler() != null || (deserializerInstance = deserializationContext.deserializerInstance(annotated, annotationIntrospector.findContentDeserializer(annotated))) == null) {
            return t3;
        }
        return t3.withContentValueHandler(deserializerInstance);
    }

    /* access modifiers changed from: protected */
    public JavaType resolveType(DeserializationContext deserializationContext, BeanDescription beanDescription, JavaType javaType, AnnotatedMember annotatedMember) throws JsonMappingException {
        TypeDeserializer findTypeDeserializer;
        TypeDeserializer findPropertyContentTypeDeserializer;
        KeyDeserializer keyDeserializerInstance;
        if (javaType.isContainerType()) {
            AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
            if (!(javaType.getKeyType() == null || (keyDeserializerInstance = deserializationContext.keyDeserializerInstance(annotatedMember, annotationIntrospector.findKeyDeserializer(annotatedMember))) == null)) {
                javaType = ((MapLikeType) javaType).withKeyValueHandler(keyDeserializerInstance);
                javaType.getKeyType();
            }
            JsonDeserializer<Object> deserializerInstance = deserializationContext.deserializerInstance(annotatedMember, annotationIntrospector.findContentDeserializer(annotatedMember));
            if (deserializerInstance != null) {
                javaType = javaType.withContentValueHandler(deserializerInstance);
            }
            if ((annotatedMember instanceof AnnotatedMember) && (findPropertyContentTypeDeserializer = findPropertyContentTypeDeserializer(deserializationContext.getConfig(), javaType, annotatedMember)) != null) {
                javaType = javaType.withContentTypeHandler(findPropertyContentTypeDeserializer);
            }
        }
        if (annotatedMember instanceof AnnotatedMember) {
            findTypeDeserializer = findPropertyTypeDeserializer(deserializationContext.getConfig(), javaType, annotatedMember);
        } else {
            findTypeDeserializer = findTypeDeserializer(deserializationContext.getConfig(), javaType);
        }
        if (findTypeDeserializer != null) {
            return javaType.withTypeHandler(findTypeDeserializer);
        }
        return javaType;
    }

    /* access modifiers changed from: protected */
    public EnumResolver<?> constructEnumResolver(Class<?> cls, DeserializationConfig deserializationConfig, AnnotatedMethod annotatedMethod) {
        if (annotatedMethod != null) {
            Method annotated = annotatedMethod.getAnnotated();
            if (deserializationConfig.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(annotated);
            }
            return EnumResolver.constructUnsafeUsingMethod(cls, annotated);
        } else if (deserializationConfig.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING)) {
            return EnumResolver.constructUnsafeUsingToString(cls);
        } else {
            return EnumResolver.constructUnsafe(cls, deserializationConfig.getAnnotationIntrospector());
        }
    }

    /* access modifiers changed from: protected */
    public AnnotatedMethod _findJsonValueFor(DeserializationConfig deserializationConfig, JavaType javaType) {
        if (javaType == null) {
            return null;
        }
        return deserializationConfig.introspect(javaType).findJsonValueMethod();
    }
}
