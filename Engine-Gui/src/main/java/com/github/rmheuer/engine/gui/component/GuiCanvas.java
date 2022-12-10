package com.github.rmheuer.engine.gui.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GuiCanvas implements Component {
    private final GuiRenderer renderer;
    private final List<GuiWindow> windows;

    public GuiCanvas() {
        renderer = new GuiRenderer();
        windows = new ArrayList<>();
    }

    public GuiRenderer getRenderer() {
        return renderer;
    }

    public void addWindow(GuiWindow window) {
        windows.add(window);
    }

    public List<GuiWindow> getWindows() {
        return Collections.unmodifiableList(windows);
    }
}
