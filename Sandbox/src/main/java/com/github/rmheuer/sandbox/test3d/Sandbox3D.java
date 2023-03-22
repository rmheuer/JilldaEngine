package com.github.rmheuer.sandbox.test3d;

import com.github.rmheuer.engine.audio.system.AudioModule;
import com.github.rmheuer.engine.core.main.EngineRuntime;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.gui.system.GuiModule;
import com.github.rmheuer.engine.render.system.RenderModule;
import com.github.rmheuer.engine.render2d.system.Render2DModule;
import com.github.rmheuer.engine.render3d.system.Render3DModule;

public final class Sandbox3D {
    public static void main(String[] args) {
        if (EngineRuntime.restartForMacOS(args))
            return;

        Game game = Game.builder()
                .addModule(RenderModule.class)
                .addModule(Render2DModule.class)
                .addModule(Render3DModule.class)
                .addModule(GuiModule.class)
                .addModule(AudioModule.class)
                .build();

        game.worldBuilder()
                .addSystem(SandboxInitSystem.class)
                .build();

        game.run();
    }
}
