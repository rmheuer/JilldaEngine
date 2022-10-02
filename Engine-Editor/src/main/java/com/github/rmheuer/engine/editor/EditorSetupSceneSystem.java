package com.github.rmheuer.engine.editor;

import com.github.rmheuer.engine.core.ecs.entity.Entity;
import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.event.EventDispatcher;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.editor.event.FileDeleteEvent;
import com.github.rmheuer.engine.editor.event.FileTreeSelectionChangeEvent;
import com.github.rmheuer.engine.editor.file.TextFileEditor;
import com.github.rmheuer.engine.editor.window.FileContentWindow;
import com.github.rmheuer.engine.editor.window.FileTreeWindow;
import com.github.rmheuer.engine.editor.window.PropertiesWindow;
import com.github.rmheuer.engine.editor.window.SceneTreeWindow;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.component.GuiWindow;
import com.github.rmheuer.engine.render.camera.Camera;
import com.github.rmheuer.engine.render.camera.OrthographicProjection;

import java.io.IOException;

public final class EditorSetupSceneSystem implements GameSystem {
    // Temporary
    private TextFileEditor editor;

    @Override
    public void init(World world) {
        Entity root = world.getRoot();

        Entity camera = root.newChild("Camera");
        camera.addComponent(new Camera(new OrthographicProjection()));
        camera.addComponent(new Transform());

        makeWindow(root, "File Tree", new FileTreeWindow());

        SceneTreeWindow sceneTree = new SceneTreeWindow(world);
        makeWindow(root, "Scene Tree", sceneTree);
        makeWindow(root, "Properties", new PropertiesWindow(sceneTree));

        editor = new TextFileEditor();
        makeWindow(root, "File Editor", new FileContentWindow(editor));
    }

    private void makeWindow(Entity root, String title, GuiRenderable r) {
        Entity gui = root.newChild(title);
        gui.addComponent(new GuiWindow(r));
    }

    @OnEvent
    public void onFileTreeSelectionChange(World world, FileTreeSelectionChangeEvent e) {
        if (e.getSelection() == null || e.getSelection().isFile()) {
            try {
                if (e.isShouldSavePrevious()) editor.save();
                editor.open((ResourceFile) e.getSelection());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @OnEvent
    public void onFileDelete(World world, FileDeleteEvent e) {
        if (e.getFile().equals(editor.getCurrentFile())) {
            try {
                editor.open(null);
            } catch (IOException ex) {}
        }
    }

    @Override
    public void close(World world) {
        try {
            editor.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
