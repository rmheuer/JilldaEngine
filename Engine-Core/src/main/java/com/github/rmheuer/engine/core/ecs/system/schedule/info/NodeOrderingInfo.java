package com.github.rmheuer.engine.core.ecs.system.schedule.info;

import com.github.rmheuer.engine.core.ecs.system.SystemNode;
import com.github.rmheuer.engine.core.ecs.system.annotation.After;
import com.github.rmheuer.engine.core.ecs.system.annotation.Before;
import com.github.rmheuer.engine.core.ecs.system.annotation.RunInGroup;
import com.github.rmheuer.engine.core.ecs.system.group.SimulationGroup;
import com.github.rmheuer.engine.core.ecs.system.group.SystemGroup;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

public class NodeOrderingInfo {
    private static final Class<? extends SystemGroup> DEFAULT_GROUP = SimulationGroup.class;

    private final Class<? extends SystemNode> sourceType;
    private final boolean isRoot;
    private final GroupInfo group;
    private final List<Class<? extends SystemNode>> before;
    private final List<Class<? extends SystemNode>> after;

    public NodeOrderingInfo(Class<? extends SystemNode> sourceType, AnnotatedElement elem, boolean isRoot) {
        this.sourceType = sourceType;
        this.isRoot = isRoot;
        before = new ArrayList<>();
        after = new ArrayList<>();

        Before[] aBefore = elem.getAnnotationsByType(Before.class);
        After[] aAfters = elem.getAnnotationsByType(After.class);
        for (Before b : aBefore)
            before.add(b.value());
        for (After a : aAfters)
            after.add(a.value());

        RunInGroup aGroup = elem.getAnnotation(RunInGroup.class);
        if (aGroup != null) {
            group = GroupInfo.get(aGroup.value());
        } else if (isRoot) {
            group = null;
        } else {
            group = GroupInfo.get(DEFAULT_GROUP);
        }
    }

    public Class<? extends SystemNode> getSourceType() {
        return sourceType;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public GroupInfo getGroup() {
        return group;
    }

    public List<Class<? extends SystemNode>> getBefore() {
        return before;
    }

    public List<Class<? extends SystemNode>> getAfter() {
        return after;
    }
}
