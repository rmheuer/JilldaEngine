package com.github.rmheuer.engine.core.serial.node;

import java.util.Collection;
import java.util.Collections;

public abstract class SerialNode {
    private SerialNode parent;

    public SerialNode() {
	parent = null;
    }

    public SerialNode getParent() {
	return parent;
    }

    public void setParent(SerialNode parent) {
	this.parent = parent;
    }

    public boolean hasParent() {
	return parent != null;
    }

    // Optional override
    public Collection<SerialNode> getChildren() {
	return Collections.emptySet();
    }
}
