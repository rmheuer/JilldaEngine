package com.github.rmheuer.engine.editor.file;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.gui.GuiRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TextFileEditor implements FileEditor {
    private final List<String> lines;
    private ResourceFile res;

    public TextFileEditor() {
	lines = new ArrayList<>();
    }

    @Override
    public void open(ResourceFile res) throws IOException {
	this.res = res;

	String content = res.readAsString();
	String[] split = content.split("\n");

	lines.clear();
	for (String line : split) {
	    lines.add(line);
	}
    }

    @Override
    public void save() throws IOException {
	// TODO
    }

    @Override
    public void showGui(GuiRenderer g) {
	// TODO: Make lines editable
	for (String line : lines) {
	    g.text(line);
	}
    }
}
