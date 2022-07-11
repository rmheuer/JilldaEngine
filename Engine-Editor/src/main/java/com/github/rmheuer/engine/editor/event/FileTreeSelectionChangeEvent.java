package com.github.rmheuer.engine.editor.event;

import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.resource.Resource;

public final class FileTreeSelectionChangeEvent implements Event {
    private final Resource selection;

    public FileTreeSelectionChangeEvent(Resource selection) {
	this.selection = selection;
    }

    public Resource getSelection() {
	return selection;
    }

    @Override
    public String toString() {
	return "FileTreeSelectionChangeEvent{"
	    + "selection=" + selection
	    + "}";
    }
}
