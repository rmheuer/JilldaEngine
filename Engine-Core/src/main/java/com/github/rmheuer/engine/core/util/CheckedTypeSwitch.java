package com.github.rmheuer.engine.core.util;

public final class CheckedTypeSwitch<P> {
    @FunctionalInterface
    public interface Case<C, E extends Throwable> {
	void apply(C c) throws E;
    }
    
    private final P parameter;

    public CheckedTypeSwitch(P parameter) {
	this.parameter = parameter;
    }

    public <C extends P, E extends Throwable> CheckedTypeSwitch<P> doCase(Class<C> type, Case<C, E> caseFn) throws E {
	if (type.isInstance(parameter)) {
	    caseFn.apply(type.cast(parameter));
	}
	return this;
    }
}
