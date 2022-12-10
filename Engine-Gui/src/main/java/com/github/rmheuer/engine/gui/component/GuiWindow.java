package com.github.rmheuer.engine.gui.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.render2d.DrawList2D;

import java.util.function.Supplier;

// TODO: Default to screen-space draw getter
public final class GuiWindow implements Component {
    private GuiRenderable content;
    private Supplier<DrawList2D> drawListGetter;

    public GuiWindow(Supplier<DrawList2D> drawListGetter) {
        this(null, drawListGetter);
    }

    public GuiWindow(GuiRenderable content, Supplier<DrawList2D> drawListGetter) {
        this.content = content;
        this.drawListGetter = drawListGetter;
    }

    public GuiRenderable getContent() {
        return content;
    }

    public void setContent(GuiRenderable content) {
        this.content = content;
    }

    public Supplier<DrawList2D> getDrawListGetter() {
        return drawListGetter;
    }

    public void setDrawListGetter(Supplier<DrawList2D> drawListGetter) {
        this.drawListGetter = drawListGetter;
    }
}
