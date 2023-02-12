package com.github.rmheuer.sandbox.test2d;

import com.github.rmheuer.engine.audio.system.AudioModule;
import com.github.rmheuer.engine.core.main.EngineRuntime;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.gui.system.GuiModule;
import com.github.rmheuer.engine.physics2d.Physics2DModule;
import com.github.rmheuer.engine.render.system.RenderModule;
import com.github.rmheuer.engine.render2d.system.Render2DModule;

public final class Sandbox2D {
    public static void main(String[] args) {
        if (EngineRuntime.restartForMacOS(args))
            return;

        Game game = Game.builder()
                .addModule(RenderModule.class)
                .addModule(Render2DModule.class)
                .addModule(Physics2DModule.class)
                .addModule(GuiModule.class)
                .addModule(AudioModule.class)
                .build();

        game.worldBuilder()
                .addSystem(Sandbox2dInitSystem.class)
                .addSystem(KeyboardControlSystem.class)
                .build();

        game.run();
    }
}
