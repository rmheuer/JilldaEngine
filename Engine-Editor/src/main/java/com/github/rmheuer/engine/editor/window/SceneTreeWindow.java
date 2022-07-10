package com.github.rmheuer.engine.editor.window;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiTreeFlags;

import java.util.List;

public final class SceneTreeWindow implements GuiRenderable {
    private final World world;

    private Entity selected;

    public SceneTreeWindow(World world) {
        this.world = world;
    }

    private void showEntity(GuiRenderer g, Entity e, boolean root) {
        String name = e.getName();

        List<Entity> children = e.getChildren();

        int flags = GuiTreeFlags.BackgroundFillsAvailX | GuiTreeFlags.ToggleRequiresSelection;
        if (children.isEmpty())
            flags |= GuiTreeFlags.Leaf;
        if (e.equals(selected))
            flags |= GuiTreeFlags.Selected;
        if (root)
            flags |= GuiTreeFlags.DefaultOpen;

        boolean open = g.pushTree(name, e, flags);
        if (g.isWidgetClicked(MouseButton.LEFT))
            selected = e;

        if (open) {
            for (Entity child : children) {
                showEntity(g, child, false);
            }
            g.popTree();
        }
    }

    @Override
    public void drawGui(GuiRenderer g) {
        g.setLineSpacingEnabled(false);
        showEntity(g, world.getRoot(), true);
        g.setLineSpacingEnabled(true);
    }

    public Entity getSelection() {
        return selected;
    }
}
