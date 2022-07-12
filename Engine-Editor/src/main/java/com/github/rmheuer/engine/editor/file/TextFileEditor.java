package com.github.rmheuer.engine.editor.file;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.jar.JarResourceFile;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.render.RendererAPI;
import com.github.rmheuer.engine.render2d.font.Font;
import com.github.rmheuer.engine.render2d.font.TrueTypeFont;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TextFileEditor implements FileEditor {
    private static final String FONT_PATH = "com/github/rmheuer/engine/editor/fonts/JetBrainsMono-Regular.ttf";

    private final List<String> lines;
    private final Font font;
    private ResourceFile res;

    public TextFileEditor() {
        lines = new ArrayList<>();

        try {
            font = new TrueTypeFont(RendererAPI.getBackend(), new JarResourceFile(FONT_PATH), 14);
            font.claim();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font");
        }
    }

    @Override
    public void open(ResourceFile res) throws IOException {
        this.res = res;

        String content = res.readAsString();
        String[] split = content
                .replace("\r", "")
                .replace("\t", "        ")
                .split("\n");

        lines.clear();
        lines.addAll(Arrays.asList(split));
    }

    @Override
    public void save() throws IOException {
        // TODO
    }

    @Override
    public void showGui(GuiRenderer g) {
        // TODO: Make lines editable
        g.getStyle().pushFont(font);
        for (String line : lines) {
            g.text(line);
        }
        g.getStyle().popFont();
    }
}
