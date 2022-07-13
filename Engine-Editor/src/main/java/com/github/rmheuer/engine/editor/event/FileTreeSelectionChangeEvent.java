package com.github.rmheuer.engine.editor.event;

import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.resource.Resource;

public final class FileTreeSelectionChangeEvent implements Event {
    private final Resource selection;
    private final boolean shouldSavePrevious;

    public FileTreeSelectionChangeEvent(Resource selection, boolean shouldSavePrevious) {
        this.selection = selection;
        this.shouldSavePrevious = shouldSavePrevious;
    }

    public Resource getSelection() {
	return selection;
    }

    public boolean isShouldSavePrevious() {
        return shouldSavePrevious;
    }

    @Override
    public String toString() {
        return "FileTreeSelectionChangeEvent{" +
                "selection=" + selection +
                ", shouldSavePrevious=" + shouldSavePrevious +
                '}';
    }
}
