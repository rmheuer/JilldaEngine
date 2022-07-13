package com.github.rmheuer.engine.editor.event;

import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.resource.Resource;

public final class FileDeleteEvent implements Event {
    private final Resource file;

    public FileDeleteEvent(Resource file) {
        this.file = file;
    }

    public Resource getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "FileDeleteEvent{" +
                "file=" + file +
                '}';
    }
}
