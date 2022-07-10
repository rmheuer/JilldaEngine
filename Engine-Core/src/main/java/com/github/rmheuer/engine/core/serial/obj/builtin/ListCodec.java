package com.github.rmheuer.engine.core.serial.obj.builtin;

import com.github.rmheuer.engine.core.serial.node.SerialArray;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.DeserializationContext;
import com.github.rmheuer.engine.core.serial.obj.SerializationContext;
import com.github.rmheuer.engine.core.serial.obj.TypeCodec;

import java.util.ArrayList;
import java.util.List;

public final class ListCodec<T> implements TypeCodec<List<T>> {
    private final Class<T> elemType;

    public ListCodec(Class<T> elemType) {
	this.elemType = elemType;
    }

    @Override
    public SerialNode serialize(List<T> list, SerializationContext ctx) {
	SerialArray arr = new SerialArray();
	for (T t : list) {
	    arr.add(ctx.serialize(t, elemType));
	}
	return arr;
    }

    @Override
    public List<T> deserialize(SerialNode node, DeserializationContext ctx) {
	List<T> list = new ArrayList<>();
	SerialArray arr = (SerialArray) node;
	for (SerialNode child : arr) {
	    list.add(ctx.deserialize(child, elemType));
	}
	return list;
    }
}
