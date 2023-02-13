package com.github.rmheuer.engine.core.serial2.obj;

public interface SerialMapperFactory {
    /**
     * @param type type to map
     * @return mapper if supported, null otherwise
     */
    SerialMapper<?, ?> getMapperFor(Class<?> type);
}
