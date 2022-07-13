package com.github.rmheuer.engine.editor.file;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.gui.GuiRenderer;

import java.io.IOException;

// TODO: Add ability to check if file type is supported
public interface FileEditor {
    void open(ResourceFile res) throws IOException;
    ResourceFile getCurrentFile();
    void save() throws IOException;

    void showGui(GuiRenderer g);
}
