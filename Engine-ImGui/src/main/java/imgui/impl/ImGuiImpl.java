package imgui.impl;

import imgui.internal.ImGuiInputTextState;
import imgui.internal.stbtextedit.StbTextedit;

public final class ImGuiImpl {
    // FIXME
    public static final StbTextedit<ImGuiInputTextState> IMSTB = new StbTextedit.Builder<ImGuiInputTextState>()
            .build();

    private ImGuiImpl() {
        throw new AssertionError();
    }
}
