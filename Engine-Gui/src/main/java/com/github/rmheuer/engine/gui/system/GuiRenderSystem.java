package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.entity.Name;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.OnEvent;
import com.github.rmheuer.engine.core.math.Vector2i;
import com.github.rmheuer.engine.core.module.ModuleRegistry;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.component.GuiWindow;
import com.github.rmheuer.engine.render.event.RenderSceneEvent;
import com.github.rmheuer.engine.render.system.RenderModule;
import com.github.rmheuer.engine.render2d.DrawList2D;
import com.github.rmheuer.engine.render2d.RenderContext2D;
import com.github.rmheuer.engine.render2d.system.RenderEndFrame2DSystem;

public final class GuiRenderSystem implements GameSystem {
    @Override
    public void init(World world) {
        world.setLocalSingleton(new GuiRenderer());
    }

    @OnEvent
    @After(RenderEndFrame2DSystem.class)
    public void onRenderScene(World world, RenderSceneEvent e) {
        RenderContext2D ctx2d = world.getLocalSingleton(RenderContext2D.class);
        GuiRenderer gui = world.getLocalSingleton(GuiRenderer.class);

        // TODO: Get this from target framebuffer
        Vector2i size = ModuleRegistry.getInstance(RenderModule.class).getWindow().getSize();
        gui.beginFrame(size.x, size.y);

        world.forEach(GuiWindow.class, Name.class, (win, name) -> {
            gui.beginWindow(name.getName());
            win.getContent().drawGui(gui);
            gui.endWindow();
        });
        gui.endFrame();

        DrawList2D drawList = gui.getDrawList();
        ctx2d.getRenderer().draw(drawList);
    }
}
