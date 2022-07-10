package com.github.rmheuer.engine.editor.window;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.entity.Name;
import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;
import com.github.rmheuer.engine.editor.view.SerialDataView;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.GuiRenderer;

public final class PropertiesWindow implements GuiRenderable {
    private final SceneTreeWindow sceneTree;

    public PropertiesWindow(SceneTreeWindow sceneTree) {
        this.sceneTree = sceneTree;
    }

    private void showComponent(GuiRenderer g, Entity e, Component c) {
        Class<? extends Component> clazz = c.getClass();

        // Temporary
        if (!clazz.equals(Name.class))
            return;

        SerialNode serial = ObjectSerializer.get().serialize(c);
        if (SerialDataView.show(g, c.getClass().getSimpleName(), serial)) {
            Component n = ObjectSerializer.get().deserialize(serial, clazz);

            e.removeComponent(clazz);
            e.addComponent(n);
        }
    }

    @Override
    public void drawGui(GuiRenderer g) {
        Entity selection = sceneTree.getSelection();
        if (selection == null) {
            g.text("No entity selected");
        } else {
            for (Component c : selection.getComponents()) {
                showComponent(g, selection, c);
            }
        }
    }
}
