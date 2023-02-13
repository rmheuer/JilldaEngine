package com.github.rmheuer.engine.core.serial2.obj;

public abstract class SerialMapper<T, S extends AutoSerializable> {
    private final Class<S> serialType;

    public SerialMapper(Class<S> serialType) {
        this.serialType = serialType;
    }

    public abstract S toSerializable(T t);
    public abstract T fromSerializable(S s);

    public Class<S> getSerialType() {
        return serialType;
    }
}
