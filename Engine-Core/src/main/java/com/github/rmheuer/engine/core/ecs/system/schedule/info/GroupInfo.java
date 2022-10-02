package com.github.rmheuer.engine.core.ecs.system.schedule.info;

import com.github.rmheuer.engine.core.ecs.system.group.RootGroup;
import com.github.rmheuer.engine.core.ecs.system.group.SystemGroup;
import com.github.rmheuer.engine.core.util.LazyCache;

public final class GroupInfo extends NodeOrderingInfo {
    private static final LazyCache<Class<? extends SystemGroup>, GroupInfo> CACHE = new LazyCache<>(GroupInfo::new);
    public static GroupInfo get(Class<? extends SystemGroup> type) {
        return CACHE.get(type);
    }

    private GroupInfo(Class<? extends SystemGroup> type) {
        super(type, type, type.equals(RootGroup.class));
    }
}
