package com.github.rmheuer.engine.core.serial2.obj;

/**
 * Indicates that a class can be serialized by its fields. This means that
 * all non-transient fields must be any of:
 *   Primitive
 *   Array
 *   Enum
 *   Collection (no wildcards)
 *   Object that either:
 *     Implements AutoSerializable
 *     Is annotated with @SerializeWith on the field or type declaration
 *     Have a mapper provided by a SerialMapperFactory
 * and that the class must not have any type parameters.
 *
 * Objects serialized multiple times will be stored once, with references added
 * afterward to preserve object graphs.
 *
 * A serializable class must have a no-args constructor, which may be private.
 */
public interface AutoSerializable {
}
