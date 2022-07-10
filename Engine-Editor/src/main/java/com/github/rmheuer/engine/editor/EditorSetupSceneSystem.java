package com.github.rmheuer.engine.editor;

import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.math.Transform;
import com.github.rmheuer.engine.editor.window.FileTreeWindow;
import com.github.rmheuer.engine.editor.window.PropertiesWindow;
import com.github.rmheuer.engine.editor.window.SceneTreeWindow;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.component.GuiWindow;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.OrthographicProjection;

public final class EditorSetupSceneSystem implements GameSystem {
    @Override
    public void init(World world) {
        Entity root = world.getRoot();

        Entity camera = root.newChild("Camera");
        camera.addComponent(new Camera(new OrthographicProjection(800, 600)));
        camera.addComponent(new Transform());

        makeWindow(root, "File Tree", new FileTreeWindow());

        SceneTreeWindow sceneTree = new SceneTreeWindow(world);
        makeWindow(root, "Scene Tree", sceneTree);
        makeWindow(root, "Properties", new PropertiesWindow(sceneTree));
    }

    private void makeWindow(Entity root, String title, GuiRenderable r) {
        Entity gui = root.newChild(title);
        gui.addComponent(new GuiWindow(r));
    }
}
