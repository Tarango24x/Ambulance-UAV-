package com.shaded.fasterxml.jackson.databind.deser.impl;

import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerators;

public class PropertyBasedObjectIdGenerator extends ObjectIdGenerators.PropertyGenerator {
    private static final long serialVersionUID = 1;

    public PropertyBasedObjectIdGenerator(Class<?> cls) {
        super(cls);
    }

    public Object generateId(Object obj) {
        throw new UnsupportedOperationException();
    }

    public ObjectIdGenerator<Object> forScope(Class<?> cls) {
        return cls == this._scope ? this : new PropertyBasedObjectIdGenerator(cls);
    }

    public ObjectIdGenerator<Object> newForSerialization(Object obj) {
        return this;
    }

    public ObjectIdGenerator.IdKey key(Object obj) {
        return new ObjectIdGenerator.IdKey(getClass(), this._scope, obj);
    }
}
