package imgui.api;

import imgui.api.flag.ImGuiCol;

public final class ImGuiStyle {
    public float alpha;
    public float disabledAlpha;
    public final ImVec2 windowPadding = new ImVec2();
    public float windowRounding;
    public float windowBorderSize;
    public final ImVec2 windowMinSize = new ImVec2();
    public final ImVec2 windowTitleAlign = new ImVec2();
    public int windowMenuButtonPosition;
    public float childRounding;
    public float childBorderSize;
    public float popupRounding;
    public float popupBorderSize;
    public final ImVec2 framePadding = new ImVec2();
    public float frameRounding;
    public float frameBorderSize;
    public final ImVec2 itemSpacing = new ImVec2();
    public final ImVec2 itemInnerSpacing = new ImVec2();
    public final ImVec2 cellPadding = new ImVec2();
    public final ImVec2 touchExtraPadding = new ImVec2();
    public float indentSpacing;
    public float columnsMinSpacing;
    public float scrollbarSize;
    public float scrollbarRounding;
    public float grabMinSize;
    public float grabRounding;
    public float logSliderDeadzone;
    public float tabRounding;
    public float tabBorderSize;
    public float tabMinWidthForCloseButton;
    public int colorButtonPosition;
    public final ImVec2 buttonTextAlign = new ImVec2();
    public final ImVec2 selectableTextAlign = new ImVec2();
    public final ImVec2 displayWindowPadding = new ImVec2();
    public final ImVec2 displaySafeAreaPadding = new ImVec2();
    public float mouseCursorScale;
    public boolean antiAliasedLines;
    public boolean antiAliasedLinesUseTex;
    public boolean antiAliasedFill;
    public float curveTessellationTol;
    public float circleTessellationMaxError;
    private final ImVec4[] colors = new ImVec4[ImGuiCol.COUNT];

    public ImGuiStyle() {
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new ImVec4();
        }
    }

    public void scaleAllSizes(float scaleFactor) {

    }

    public ImVec4 getColor(int col) {
        return colors[col];
    }

    public void setColor(int col, ImVec4 val) {
        colors[col].set(val);
    }
}
