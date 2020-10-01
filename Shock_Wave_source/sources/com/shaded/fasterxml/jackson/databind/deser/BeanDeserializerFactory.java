package com.shaded.fasterxml.jackson.databind.deser;

import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.shaded.fasterxml.jackson.databind.AbstractTypeResolver;
import com.shaded.fasterxml.jackson.databind.AnnotationIntrospector;
import com.shaded.fasterxml.jackson.databind.BeanDescription;
import com.shaded.fasterxml.jackson.databind.BeanProperty;
import com.shaded.fasterxml.jackson.databind.DeserializationConfig;
import com.shaded.fasterxml.jackson.databind.DeserializationContext;
import com.shaded.fasterxml.jackson.databind.JavaType;
import com.shaded.fasterxml.jackson.databind.JsonDeserializer;
import com.shaded.fasterxml.jackson.databind.JsonMappingException;
import com.shaded.fasterxml.jackson.databind.MapperFeature;
import com.shaded.fasterxml.jackson.databind.PropertyName;
import com.shaded.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.shaded.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.shaded.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.shaded.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.shaded.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.shaded.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator;
import com.shaded.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.shaded.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.shaded.fasterxml.jackson.databind.deser.std.ThrowableDeserializer;
import com.shaded.fasterxml.jackson.databind.ext.OptionalHandlerFactory;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.shaded.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.shaded.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.shaded.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.shaded.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.shaded.fasterxml.jackson.databind.type.TypeFactory;
import com.shaded.fasterxml.jackson.databind.util.ArrayBuilders;
import com.shaded.fasterxml.jackson.databind.util.ClassUtil;
import com.shaded.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class BeanDeserializerFactory extends BasicDeserializerFactory implements Serializable {
    private static final Class<?>[] INIT_CAUSE_PARAMS = {Throwable.class};
    private static final Class<?>[] NO_VIEWS = new Class[0];
    public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
    private static final long serialVersionUID = 1;

    public BeanDeserializerFactory(DeserializerFactoryConfig deserializerFactoryConfig) {
        super(deserializerFactoryConfig);
    }

    public DeserializerFactory withConfig(DeserializerFactoryConfig deserializerFactoryConfig) {
        if (this._factoryConfig == deserializerFactoryConfig) {
            return this;
        }
        if (getClass() == BeanDeserializerFactory.class) {
            return new BeanDeserializerFactory(deserializerFactoryConfig);
        }
        throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType javaType, DeserializationConfig deserializationConfig, BeanDescription beanDescription) throws JsonMappingException {
        for (Deserializers findBeanDeserializer : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> findBeanDeserializer2 = findBeanDeserializer.findBeanDeserializer(javaType, deserializationConfig, beanDescription);
            if (findBeanDeserializer2 != null) {
                return findBeanDeserializer2;
            }
        }
        return null;
    }

    public JsonDeserializer<Object> createBeanDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        JavaType materializeAbstractType;
        DeserializationConfig config = deserializationContext.getConfig();
        JsonDeserializer<Object> _findCustomBeanDeserializer = _findCustomBeanDeserializer(javaType, config, beanDescription);
        if (_findCustomBeanDeserializer != null) {
            return _findCustomBeanDeserializer;
        }
        if (javaType.isThrowable()) {
            return buildThrowableDeserializer(deserializationContext, javaType, beanDescription);
        }
        if (javaType.isAbstract() && (materializeAbstractType = materializeAbstractType(deserializationContext, javaType, beanDescription)) != null) {
            return buildBeanDeserializer(deserializationContext, materializeAbstractType, config.introspect(materializeAbstractType));
        }
        JsonDeserializer<?> findStdDeserializer = findStdDeserializer(deserializationContext, javaType, beanDescription);
        if (findStdDeserializer != null) {
            return findStdDeserializer;
        }
        if (!isPotentialBeanType(javaType.getRawClass())) {
            return null;
        }
        return buildBeanDeserializer(deserializationContext, javaType, beanDescription);
    }

    public JsonDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription, Class<?> cls) throws JsonMappingException {
        return buildBuilderBasedDeserializer(deserializationContext, javaType, deserializationContext.getConfig().introspectForBuilder(deserializationContext.constructType(cls)));
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> findStdDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        JavaType javaType2;
        JsonDeserializer<?> findDefaultDeserializer = findDefaultDeserializer(deserializationContext, javaType, beanDescription);
        if (findDefaultDeserializer != null) {
            return findDefaultDeserializer;
        }
        if (!AtomicReference.class.isAssignableFrom(javaType.getRawClass())) {
            return findOptionalStdDeserializer(deserializationContext, javaType, beanDescription);
        }
        JavaType[] findTypeParameters = deserializationContext.getTypeFactory().findTypeParameters(javaType, (Class<?>) AtomicReference.class);
        if (findTypeParameters == null || findTypeParameters.length < 1) {
            javaType2 = TypeFactory.unknownType();
        } else {
            javaType2 = findTypeParameters[0];
        }
        return new JdkDeserializers.AtomicReferenceDeserializer(javaType2);
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<?> findOptionalStdDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        return OptionalHandlerFactory.instance.findDeserializer(javaType, deserializationContext.getConfig(), beanDescription);
    }

    /* access modifiers changed from: protected */
    public JavaType materializeAbstractType(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        JavaType type = beanDescription.getType();
        for (AbstractTypeResolver resolveAbstractType : this._factoryConfig.abstractTypeResolvers()) {
            JavaType resolveAbstractType2 = resolveAbstractType.resolveAbstractType(deserializationContext.getConfig(), type);
            if (resolveAbstractType2 != null) {
                return resolveAbstractType2;
            }
        }
        return null;
    }

    public JsonDeserializer<Object> buildBeanDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        BeanDeserializerBuilder beanDeserializerBuilder;
        JsonDeserializer<?> build;
        ValueInstantiator findValueInstantiator = findValueInstantiator(deserializationContext, beanDescription);
        BeanDeserializerBuilder constructBeanDeserializerBuilder = constructBeanDeserializerBuilder(deserializationContext, beanDescription);
        constructBeanDeserializerBuilder.setValueInstantiator(findValueInstantiator);
        addBeanProps(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        addObjectIdReader(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        addReferenceProperties(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        addInjectables(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        DeserializationConfig config = deserializationContext.getConfig();
        if (this._factoryConfig.hasDeserializerModifiers()) {
            Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                beanDeserializerBuilder = constructBeanDeserializerBuilder;
                if (!it.hasNext()) {
                    break;
                }
                constructBeanDeserializerBuilder = it.next().updateBuilder(config, beanDescription, beanDeserializerBuilder);
            }
        } else {
            beanDeserializerBuilder = constructBeanDeserializerBuilder;
        }
        if (!javaType.isAbstract() || findValueInstantiator.canInstantiate()) {
            build = beanDeserializerBuilder.build();
        } else {
            build = beanDeserializerBuilder.buildAbstract();
        }
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return build;
        }
        Iterator<BeanDeserializerModifier> it2 = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            JsonDeserializer<?> jsonDeserializer = build;
            if (!it2.hasNext()) {
                return jsonDeserializer;
            }
            build = it2.next().modifyDeserializer(config, beanDescription, jsonDeserializer);
        }
    }

    /* access modifiers changed from: protected */
    public JsonDeserializer<Object> buildBuilderBasedDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        ValueInstantiator findValueInstantiator = findValueInstantiator(deserializationContext, beanDescription);
        DeserializationConfig config = deserializationContext.getConfig();
        BeanDeserializerBuilder constructBeanDeserializerBuilder = constructBeanDeserializerBuilder(deserializationContext, beanDescription);
        constructBeanDeserializerBuilder.setValueInstantiator(findValueInstantiator);
        addBeanProps(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        addObjectIdReader(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        addReferenceProperties(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        addInjectables(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        JsonPOJOBuilder.Value findPOJOBuilderConfig = beanDescription.findPOJOBuilderConfig();
        String str = findPOJOBuilderConfig == null ? "build" : findPOJOBuilderConfig.buildMethodName;
        AnnotatedMethod findMethod = beanDescription.findMethod(str, (Class<?>[]) null);
        if (findMethod != null && config.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(findMethod.getMember());
        }
        constructBeanDeserializerBuilder.setPOJOBuilder(findMethod, findPOJOBuilderConfig);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier updateBuilder : this._factoryConfig.deserializerModifiers()) {
                constructBeanDeserializerBuilder = updateBuilder.updateBuilder(config, beanDescription, constructBeanDeserializerBuilder);
            }
        }
        JsonDeserializer<?> buildBuilderBased = constructBeanDeserializerBuilder.buildBuilderBased(javaType, str);
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return buildBuilderBased;
        }
        Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            JsonDeserializer<?> jsonDeserializer = buildBuilderBased;
            if (!it.hasNext()) {
                return jsonDeserializer;
            }
            buildBuilderBased = it.next().modifyDeserializer(config, beanDescription, jsonDeserializer);
        }
    }

    /* access modifiers changed from: protected */
    public void addObjectIdReader(DeserializationContext deserializationContext, BeanDescription beanDescription, BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        JavaType javaType;
        SettableBeanProperty settableBeanProperty;
        ObjectIdGenerator objectIdGeneratorInstance;
        ObjectIdInfo objectIdInfo = beanDescription.getObjectIdInfo();
        if (objectIdInfo != null) {
            Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
            if (generatorType == ObjectIdGenerators.PropertyGenerator.class) {
                String propertyName = objectIdInfo.getPropertyName();
                settableBeanProperty = beanDeserializerBuilder.findProperty(propertyName);
                if (settableBeanProperty == null) {
                    throw new IllegalArgumentException("Invalid Object Id definition for " + beanDescription.getBeanClass().getName() + ": can not find property with name '" + propertyName + "'");
                }
                javaType = settableBeanProperty.getType();
                objectIdGeneratorInstance = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
            } else {
                javaType = deserializationContext.getTypeFactory().findTypeParameters(deserializationContext.constructType(generatorType), (Class<?>) ObjectIdGenerator.class)[0];
                settableBeanProperty = null;
                objectIdGeneratorInstance = deserializationContext.objectIdGeneratorInstance(beanDescription.getClassInfo(), objectIdInfo);
            }
            beanDeserializerBuilder.setObjectIdReader(ObjectIdReader.construct(javaType, objectIdInfo.getPropertyName(), objectIdGeneratorInstance, deserializationContext.findRootValueDeserializer(javaType), settableBeanProperty));
        }
    }

    public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationContext deserializationContext, JavaType javaType, BeanDescription beanDescription) throws JsonMappingException {
        BeanDeserializerBuilder beanDeserializerBuilder;
        SettableBeanProperty constructSettableProperty;
        DeserializationConfig config = deserializationContext.getConfig();
        BeanDeserializerBuilder constructBeanDeserializerBuilder = constructBeanDeserializerBuilder(deserializationContext, beanDescription);
        constructBeanDeserializerBuilder.setValueInstantiator(findValueInstantiator(deserializationContext, beanDescription));
        addBeanProps(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        AnnotatedMethod findMethod = beanDescription.findMethod("initCause", INIT_CAUSE_PARAMS);
        if (!(findMethod == null || (constructSettableProperty = constructSettableProperty(deserializationContext, beanDescription, SimpleBeanPropertyDefinition.construct(deserializationContext.getConfig(), findMethod, "cause"), findMethod.getGenericParameterType(0))) == null)) {
            constructBeanDeserializerBuilder.addOrReplaceProperty(constructSettableProperty, true);
        }
        constructBeanDeserializerBuilder.addIgnorable("localizedMessage");
        constructBeanDeserializerBuilder.addIgnorable("suppressed");
        constructBeanDeserializerBuilder.addIgnorable("message");
        if (this._factoryConfig.hasDeserializerModifiers()) {
            Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                beanDeserializerBuilder = constructBeanDeserializerBuilder;
                if (!it.hasNext()) {
                    break;
                }
                constructBeanDeserializerBuilder = it.next().updateBuilder(config, beanDescription, beanDeserializerBuilder);
            }
        } else {
            beanDeserializerBuilder = constructBeanDeserializerBuilder;
        }
        JsonDeserializer<?> build = beanDeserializerBuilder.build();
        if (build instanceof BeanDeserializer) {
            build = new ThrowableDeserializer((BeanDeserializer) build);
        }
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return build;
        }
        Iterator<BeanDeserializerModifier> it2 = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            JsonDeserializer<?> jsonDeserializer = build;
            if (!it2.hasNext()) {
                return jsonDeserializer;
            }
            build = it2.next().modifyDeserializer(config, beanDescription, jsonDeserializer);
        }
    }

    /* access modifiers changed from: protected */
    public BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext deserializationContext, BeanDescription beanDescription) {
        return new BeanDeserializerBuilder(beanDescription, deserializationContext.getConfig());
    }

    /* access modifiers changed from: protected */
    public void addBeanProps(DeserializationContext deserializationContext, BeanDescription beanDescription, BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        List<BeanPropertyDefinition> list;
        Set<String> ignoredPropertyNames;
        SettableBeanProperty[] fromObjectArguments = beanDeserializerBuilder.getValueInstantiator().getFromObjectArguments(deserializationContext.getConfig());
        AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        Boolean findIgnoreUnknownProperties = annotationIntrospector.findIgnoreUnknownProperties(beanDescription.getClassInfo());
        if (findIgnoreUnknownProperties != null) {
            beanDeserializerBuilder.setIgnoreUnknownProperties(findIgnoreUnknownProperties.booleanValue());
        }
        HashSet<String> arrayToSet = ArrayBuilders.arrayToSet(annotationIntrospector.findPropertiesToIgnore(beanDescription.getClassInfo()));
        for (String addIgnorable : arrayToSet) {
            beanDeserializerBuilder.addIgnorable(addIgnorable);
        }
        AnnotatedMethod findAnySetter = beanDescription.findAnySetter();
        if (findAnySetter != null) {
            beanDeserializerBuilder.setAnySetter(constructAnySetter(deserializationContext, beanDescription, findAnySetter));
        }
        if (findAnySetter == null && (ignoredPropertyNames = beanDescription.getIgnoredPropertyNames()) != null) {
            for (String addIgnorable2 : ignoredPropertyNames) {
                beanDeserializerBuilder.addIgnorable(addIgnorable2);
            }
        }
        boolean z = deserializationContext.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS) && deserializationContext.isEnabled(MapperFeature.AUTO_DETECT_GETTERS);
        List<BeanPropertyDefinition> filterBeanProps = filterBeanProps(deserializationContext, beanDescription, beanDeserializerBuilder, beanDescription.findProperties(), arrayToSet);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            Iterator<BeanDeserializerModifier> it = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                list = filterBeanProps;
                if (!it.hasNext()) {
                    break;
                }
                filterBeanProps = it.next().updateProperties(deserializationContext.getConfig(), beanDescription, list);
            }
        } else {
            list = filterBeanProps;
        }
        for (BeanPropertyDefinition next : list) {
            SettableBeanProperty settableBeanProperty = null;
            if (next.hasConstructorParameter()) {
                String name = next.getName();
                if (fromObjectArguments != null) {
                    int length = fromObjectArguments.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        SettableBeanProperty settableBeanProperty2 = fromObjectArguments[i];
                        if (name.equals(settableBeanProperty2.getName())) {
                            settableBeanProperty = settableBeanProperty2;
                            break;
                        }
                        i++;
                    }
                }
                if (settableBeanProperty == null) {
                    throw deserializationContext.mappingException("Could not find creator property with name '" + name + "' (in class " + beanDescription.getBeanClass().getName() + ")");
                }
                beanDeserializerBuilder.addCreatorProperty(settableBeanProperty);
            } else {
                if (next.hasSetter()) {
                    settableBeanProperty = constructSettableProperty(deserializationContext, beanDescription, next, next.getSetter().getGenericParameterType(0));
                } else if (next.hasField()) {
                    settableBeanProperty = constructSettableProperty(deserializationContext, beanDescription, next, next.getField().getGenericType());
                } else if (z && next.hasGetter()) {
                    Class<?> rawType = next.getGetter().getRawType();
                    if (Collection.class.isAssignableFrom(rawType) || Map.class.isAssignableFrom(rawType)) {
                        settableBeanProperty = constructSetterlessProperty(deserializationContext, beanDescription, next);
                    }
                }
                if (settableBeanProperty != null) {
                    Class<?>[] findViews = next.findViews();
                    if (findViews == null && !deserializationContext.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
                        findViews = NO_VIEWS;
                    }
                    settableBeanProperty.setViews(findViews);
                    beanDeserializerBuilder.addProperty(settableBeanProperty);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public List<BeanPropertyDefinition> filterBeanProps(DeserializationContext deserializationContext, BeanDescription beanDescription, BeanDeserializerBuilder beanDeserializerBuilder, List<BeanPropertyDefinition> list, Set<String> set) throws JsonMappingException {
        ArrayList arrayList = new ArrayList(Math.max(4, list.size()));
        HashMap hashMap = new HashMap();
        for (BeanPropertyDefinition next : list) {
            String name = next.getName();
            if (!set.contains(name)) {
                if (!next.hasConstructorParameter()) {
                    Class<?> cls = null;
                    if (next.hasSetter()) {
                        cls = next.getSetter().getRawParameterType(0);
                    } else if (next.hasField()) {
                        cls = next.getField().getRawType();
                    }
                    if (cls != null && isIgnorableType(deserializationContext.getConfig(), beanDescription, cls, hashMap)) {
                        beanDeserializerBuilder.addIgnorable(name);
                    }
                }
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public void addReferenceProperties(DeserializationContext deserializationContext, BeanDescription beanDescription, BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        Type rawType;
        Map<String, AnnotatedMember> findBackReferenceProperties = beanDescription.findBackReferenceProperties();
        if (findBackReferenceProperties != null) {
            for (Map.Entry next : findBackReferenceProperties.entrySet()) {
                String str = (String) next.getKey();
                AnnotatedMember annotatedMember = (AnnotatedMember) next.getValue();
                if (annotatedMember instanceof AnnotatedMethod) {
                    rawType = ((AnnotatedMethod) annotatedMember).getGenericParameterType(0);
                } else {
                    rawType = annotatedMember.getRawType();
                }
                beanDeserializerBuilder.addBackReferenceProperty(str, constructSettableProperty(deserializationContext, beanDescription, SimpleBeanPropertyDefinition.construct(deserializationContext.getConfig(), annotatedMember), rawType));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addInjectables(DeserializationContext deserializationContext, BeanDescription beanDescription, BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        Map<Object, AnnotatedMember> findInjectables = beanDescription.findInjectables();
        if (findInjectables != null) {
            boolean canOverrideAccessModifiers = deserializationContext.canOverrideAccessModifiers();
            for (Map.Entry next : findInjectables.entrySet()) {
                AnnotatedMember annotatedMember = (AnnotatedMember) next.getValue();
                if (canOverrideAccessModifiers) {
                    annotatedMember.fixAccess();
                }
                beanDeserializerBuilder.addInjectable(annotatedMember.getName(), beanDescription.resolveType(annotatedMember.getGenericType()), beanDescription.getClassAnnotations(), annotatedMember, next.getKey());
            }
        }
    }

    /* access modifiers changed from: protected */
    public SettableAnyProperty constructAnySetter(DeserializationContext deserializationContext, BeanDescription beanDescription, AnnotatedMethod annotatedMethod) throws JsonMappingException {
        if (deserializationContext.canOverrideAccessModifiers()) {
            annotatedMethod.fixAccess();
        }
        JavaType resolveType = beanDescription.bindingsForBeanType().resolveType(annotatedMethod.getGenericParameterType(1));
        BeanProperty.Std std = new BeanProperty.Std(annotatedMethod.getName(), resolveType, (PropertyName) null, beanDescription.getClassAnnotations(), annotatedMethod, false);
        JavaType resolveType2 = resolveType(deserializationContext, beanDescription, resolveType, annotatedMethod);
        JsonDeserializer<Object> findDeserializerFromAnnotation = findDeserializerFromAnnotation(deserializationContext, annotatedMethod);
        if (findDeserializerFromAnnotation != null) {
            return new SettableAnyProperty((BeanProperty) std, annotatedMethod, resolveType2, findDeserializerFromAnnotation);
        }
        return new SettableAnyProperty((BeanProperty) std, annotatedMethod, modifyTypeByAnnotation(deserializationContext, annotatedMethod, resolveType2), (JsonDeserializer<Object>) null);
    }

    /* access modifiers changed from: protected */
    public SettableBeanProperty constructSettableProperty(DeserializationContext deserializationContext, BeanDescription beanDescription, BeanPropertyDefinition beanPropertyDefinition, Type type) throws JsonMappingException {
        SettableBeanProperty fieldProperty;
        AnnotatedMember mutator = beanPropertyDefinition.getMutator();
        if (deserializationContext.canOverrideAccessModifiers()) {
            mutator.fixAccess();
        }
        JavaType resolveType = beanDescription.resolveType(type);
        BeanProperty.Std std = new BeanProperty.Std(beanPropertyDefinition.getName(), resolveType, beanPropertyDefinition.getWrapperName(), beanDescription.getClassAnnotations(), mutator, beanPropertyDefinition.isRequired());
        JavaType resolveType2 = resolveType(deserializationContext, beanDescription, resolveType, mutator);
        if (resolveType2 != resolveType) {
            std.withType(resolveType2);
        }
        JsonDeserializer<Object> findDeserializerFromAnnotation = findDeserializerFromAnnotation(deserializationContext, mutator);
        JavaType modifyTypeByAnnotation = modifyTypeByAnnotation(deserializationContext, mutator, resolveType2);
        TypeDeserializer typeDeserializer = (TypeDeserializer) modifyTypeByAnnotation.getTypeHandler();
        if (mutator instanceof AnnotatedMethod) {
            fieldProperty = new MethodProperty(beanPropertyDefinition, modifyTypeByAnnotation, typeDeserializer, beanDescription.getClassAnnotations(), (AnnotatedMethod) mutator);
        } else {
            fieldProperty = new FieldProperty(beanPropertyDefinition, modifyTypeByAnnotation, typeDeserializer, beanDescription.getClassAnnotations(), (AnnotatedField) mutator);
        }
        if (findDeserializerFromAnnotation != null) {
            fieldProperty = fieldProperty.withValueDeserializer(findDeserializerFromAnnotation);
        }
        AnnotationIntrospector.ReferenceProperty findReferenceType = beanPropertyDefinition.findReferenceType();
        if (findReferenceType != null && findReferenceType.isManagedReference()) {
            fieldProperty.setManagedReferenceName(findReferenceType.getName());
        }
        return fieldProperty;
    }

    /* access modifiers changed from: protected */
    public SettableBeanProperty constructSetterlessProperty(DeserializationContext deserializationContext, BeanDescription beanDescription, BeanPropertyDefinition beanPropertyDefinition) throws JsonMappingException {
        AnnotatedMethod getter = beanPropertyDefinition.getGetter();
        if (deserializationContext.canOverrideAccessModifiers()) {
            getter.fixAccess();
        }
        JavaType type = getter.getType(beanDescription.bindingsForBeanType());
        JsonDeserializer<Object> findDeserializerFromAnnotation = findDeserializerFromAnnotation(deserializationContext, getter);
        JavaType modifyTypeByAnnotation = modifyTypeByAnnotation(deserializationContext, getter, type);
        SetterlessProperty setterlessProperty = new SetterlessProperty(beanPropertyDefinition, modifyTypeByAnnotation, (TypeDeserializer) modifyTypeByAnnotation.getTypeHandler(), beanDescription.getClassAnnotations(), getter);
        if (findDeserializerFromAnnotation != null) {
            return setterlessProperty.withValueDeserializer(findDeserializerFromAnnotation);
        }
        return setterlessProperty;
    }

    /* access modifiers changed from: protected */
    public boolean isPotentialBeanType(Class<?> cls) {
        String canBeABeanType = ClassUtil.canBeABeanType(cls);
        if (canBeABeanType != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + cls.getName() + " (of type " + canBeABeanType + ") as a Bean");
        } else if (ClassUtil.isProxyType(cls)) {
            throw new IllegalArgumentException("Can not deserialize Proxy class " + cls.getName() + " as a Bean");
        } else {
            String isLocalType = ClassUtil.isLocalType(cls, true);
            if (isLocalType == null) {
                return true;
            }
            throw new IllegalArgumentException("Can not deserialize Class " + cls.getName() + " (of type " + isLocalType + ") as a Bean");
        }
    }

    /* access modifiers changed from: protected */
    public boolean isIgnorableType(DeserializationConfig deserializationConfig, BeanDescription beanDescription, Class<?> cls, Map<Class<?>, Boolean> map) {
        Boolean bool = map.get(cls);
        if (bool == null) {
            bool = deserializationConfig.getAnnotationIntrospector().isIgnorableType(deserializationConfig.introspectClassAnnotations(cls).getClassInfo());
            if (bool == null) {
                bool = Boolean.FALSE;
            }
        }
        return bool.booleanValue();
    }
}
