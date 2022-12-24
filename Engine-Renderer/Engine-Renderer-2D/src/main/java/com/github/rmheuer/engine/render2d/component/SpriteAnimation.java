package com.github.rmheuer.engine.render2d.component;

import com.github.rmheuer.engine.core.ecs.component.Component;
import com.github.rmheuer.engine.render.texture.Subimage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SpriteAnimation implements Component {
    public static SpriteAnimation fromSpriteSheet(float framesPerSecond, Subimage img, int x, int y, int w, int h, int count) {
        List<Subimage> frames = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            frames.add(img.getSubImage(x + w * i, y, w, h));
        }
        return new SpriteAnimation(framesPerSecond, frames);
    }

    private List<Subimage> frames;
    private float framesPerSecond;

    private int currentFrameIndex;
    private float timeSinceLastFrame;

    public SpriteAnimation(float framesPerSecond, Subimage... frames) {
        this(framesPerSecond, Arrays.asList(frames));
    }

    public SpriteAnimation(float framesPerSecond, List<Subimage> frames) {
        this.frames = new ArrayList<>(frames);
        this.framesPerSecond = framesPerSecond;
        timeSinceLastFrame = 0;
    }

    public List<Subimage> getFrames() {
        return frames;
    }

    public void setFrames(List<Subimage> frames) {
        this.frames = frames;
    }

    public float getFramesPerSecond() {
        return framesPerSecond;
    }

    public void setFramesPerSecond(float framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    public Subimage getCurrentFrame() {
        return frames.get(currentFrameIndex);
    }

    public void setCurrentFrameIndex(int currentFrameIndex) {
        this.currentFrameIndex = currentFrameIndex;
        timeSinceLastFrame = 0;
    }

    public float getTimeSinceLastFrame() {
        return timeSinceLastFrame;
    }

    public void setTimeSinceLastFrame(float timeSinceLastFrame) {
        this.timeSinceLastFrame = timeSinceLastFrame;
    }
}
