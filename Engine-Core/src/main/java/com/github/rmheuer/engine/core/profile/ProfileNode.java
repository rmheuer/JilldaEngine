package com.github.rmheuer.engine.core.profile;

import java.util.ArrayList;
import java.util.List;

public final class ProfileNode {
    private final String name;
    private final ProfileNode parent;
    private final List<ProfileNode> children;

    private long totalTime;
    private long selfTime;

    private long startTime;
    private long pauseTime;

    public ProfileNode(String name, ProfileNode parent) {
        this.name = name;
        this.parent = parent;

        children = new ArrayList<>();
        totalTime = 0;
        selfTime = 0;
    }

    public void begin() {
        startTime = System.nanoTime();
        unpause();
    }

    public void pause() {
        selfTime += System.nanoTime() - pauseTime;
    }

    public void unpause() {
        pauseTime = System.nanoTime();
    }

    public void end() {
        pause();
        totalTime = System.nanoTime() - startTime;
    }

    public void addChild(ProfileNode child) {
        children.add(child);
    }

    public String getName() {
        return name;
    }

    public ProfileNode getParent() {
        return parent;
    }

    public List<ProfileNode> getChildren() {
        return children;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public long getSelfTime() {
        return selfTime;
    }
}
