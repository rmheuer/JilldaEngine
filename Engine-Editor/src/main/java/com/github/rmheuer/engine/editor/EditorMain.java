package com.github.rmheuer.engine.editor;

import com.github.rmheuer.engine.core.main.EngineRuntime;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.gui.system.GuiModule;

public final class EditorMain {
    public static void main(String[] args) {
        if (EngineRuntime.restartForMacOS(args))
            return;

        Game game = Game.builder()
                .addModule(GuiModule.class)
                .build();

        game.worldBuilder()
                .addSystem(EditorSetupSceneSystem.class)
                .addSystem(EditorUpdateViewportSceneSystem.class)
                .build();

        game.run();
    }
}
