package com.github.rmheuer.engine.gui.system;

import com.github.rmheuer.engine.core.ecs.World;
import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.schedule.Stage;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.file.FileResourceFile;
import com.github.rmheuer.engine.core.serial.codec.bin.BinarySerialCodec;
import com.github.rmheuer.engine.core.serial.node.SerialObject;
import com.github.rmheuer.engine.gui.GuiRenderer;

import java.io.IOException;

@After(stage = Stage.INIT, after = GuiRenderSystem.class)
public final class GuiLayoutPersistenceSystem implements GameSystem {
    @Override
    public void init(World world) {
        GuiRenderer gui = world.getLocalSingleton(GuiRenderer.class);

        ResourceFile dataFile = new FileResourceFile("gui-layout.bin");
        if (!dataFile.exists())
            return;

        try {
            gui.load((SerialObject) BinarySerialCodec.get().decode(dataFile.readAsStream()));
        } catch (IOException e) {
            System.err.println("Failed to load persistent GUI layout:");
            e.printStackTrace();
        }
    }

    @Override
    public void close(World world) {
        GuiRenderer gui = world.getLocalSingleton(GuiRenderer.class);

        ResourceFile dataFile = new FileResourceFile("gui-layout.bin");
        try {
            BinarySerialCodec.get().encode(gui.save(), dataFile.writeAsStream());
        } catch (IOException e) {
            System.err.println("Failed to save persistent GUI layout:");
            e.printStackTrace();
        }
    }
}
