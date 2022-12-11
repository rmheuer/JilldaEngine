package com.github.rmheuer.engine.core.profile;

public enum FixedProfileStage {
    MAIN_LOOP("Main Loop"),
    INIT("Init"),
    UPDATE("Update"),
    FIXED_UPDATE("Fixed Update");

    private final String friendlyName;

    FixedProfileStage(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
