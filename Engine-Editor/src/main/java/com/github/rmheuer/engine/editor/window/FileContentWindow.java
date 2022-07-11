package com.github.rmheuer.engine.editor.window;

import com.github.rmheuer.engine.editor.file.FileEditor;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.GuiRenderer;

public final class FileContentWindow implements GuiRenderable {
    private final FileEditor editor;

    public FileContentWindow(FileEditor editor) {
	this.editor = editor;
    }

    @Override
    public void drawGui(GuiRenderer g) {
	editor.showGui(g);
    }
}

