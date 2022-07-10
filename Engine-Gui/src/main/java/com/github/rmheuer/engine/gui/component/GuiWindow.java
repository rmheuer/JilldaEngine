package com.github.rmheuer.engine.gui.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.gui.GuiRenderable;

public final class GuiWindow implements Component {
    private GuiRenderable content;

    public GuiWindow() {}

    public GuiWindow(GuiRenderable content) {
        this.content = content;
    }

    public GuiRenderable getContent() {
        return content;
    }

    public void setContent(GuiRenderable content) {
        this.content = content;
    }
}
