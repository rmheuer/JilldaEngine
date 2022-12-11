package com.github.rmheuer.engine.core.profile;

public final class Profiler {
    private ProfileNode root;
    private ProfileNode current;
    private ProfileNode last;

    public void begin(String rootName) {
        root = new ProfileNode(rootName, null);
        current = root;
        root.begin();
    }

    public void end() {
        current.end();
        last = root;
    }

    public void push(String name) {
        ProfileNode child = new ProfileNode(name, current);
        current.pause();
        current.addChild(child);
        current = child;
        child.begin();
    }

    public void pop() { pop(1); }
    public void pop(int count) {
        for (int i = 0; i < count; i++) {
            current.end();
            current = current.getParent();
            current.unpause();
        }
    }

    public ProfileNode getData() {
        return last;
    }
}
