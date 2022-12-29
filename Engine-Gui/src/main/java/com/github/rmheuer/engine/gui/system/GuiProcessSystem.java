package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.input.mouse.Mouse;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.math.Matrix4f;
import com.github.rmheuer.engine.core.math.Ray3f;
import com.github.rmheuer.engine.core.math.Vector3f;
import com.github.rmheuer.engine.core.transform.Transform;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiWindow;
import com.github.rmheuer.engine.gui.component.GuiCanvas;
import com.github.rmheuer.engine.render.camera.PrimaryCamera;
import com.github.rmheuer.engine.render.system.RenderContext;
import com.github.rmheuer.engine.render2d.component.Canvas2D;

public final class GuiProcessSystem implements GameSystem {
    @Override
    public void update(World world, float delta) {
        world.forEach(GuiCanvas.class, Canvas2D.class, Transform.class, (canvas, canvas2d, transform) -> {
            GuiRenderer gui = canvas.getRenderer();
            Mouse mouse = Game.get().getInputManager().getSource(Mouse.class);
            if (canvas2d.isScreenSpace()) {
                gui.getInput().mouseMoved(mouse.getCursorX(), mouse.getCursorY());
            } else {
                PrimaryCamera cam = world.getLocalSingleton(RenderContext.class).getPrimaryCamera();
                if (cam != null) {
                    Ray3f ray = cam.getRayAtScreenPos(mouse.getCursorX(), mouse.getCursorY());

                    Matrix4f invTxf = transform.getGlobalInverseMatrix();
                    Vector3f orig = invTxf.transformPosition(ray.getOrigin());
                    Vector3f dir = invTxf.transformDirection(ray.getDirection());

                    float zDelta = -orig.z;
                    float dirScale = zDelta / dir.z;

                    float x = orig.x + dir.x * dirScale;
                    float y = orig.y + dir.y * dirScale;
                    gui.getInput().mouseMoved(x, y);
                }
            }

            gui.beginFrame();

            for (GuiWindow window : canvas.getWindows()) {
                gui.beginWindow(window.getTitle());
                window.getContent().drawGui(gui);
                gui.endWindow();
            }

            gui.endFrame();

            canvas2d.getDrawList().join(gui.getDrawList());
        });
    }
}
