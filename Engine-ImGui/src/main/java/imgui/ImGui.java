package imgui;

import static imgui.ImGuiMacros.FLT_MIN;

public final class ImGui {
    // --- Context creation and access ---

    public static ImGuiContext createContext() { return createContext(null); }
    public static ImGuiContext createContext(ImFontAtlas sharedFontAtlas) {
        return null;
    }

    public static void destroyContext() { destroyContext(null); }
    public static void destroyContext(ImGuiContext ctx) {

    }

    public static ImGuiContext getCurrentContext() {
        return null;
    }

    public static void setCurrentContext(ImGuiContext ctx) {

    }

    // --- Main ---

    public static ImGuiIO getIO() {
        return null;
    }

    public static ImGuiStyle getStyle() {
        return null;
    }

    public static void newFrame() {

    }

    public static void endFrame() {

    }

    public static void render() {

    }

    public static ImDrawData getDrawData() {
        return null;
    }

    // --- Demo, Debug, Information ---

    public static void showDemoWindow() { showDemoWindow(null); }
    public static void showDemoWindow(boolean[] pOpen) {

    }

    public static void showMetricsWindow() { showMetricsWindow(null); }
    public static void showMetricsWindow(boolean[] pOpen) {

    }

    public static void showDebugLogWindow() { showDebugLogWindow(null); }
    public static void showDebugLogWindow(boolean[] pOpen) {

    }

    public static void showStackToolWindow() { showStackToolWindow(null); }
    public static void showStackToolWindow(boolean[] pOpen) {

    }

    public static void showAboutWindow() { showAboutWindow(null); }
    public static void showAboutWindow(boolean[] pOpen) {

    }

    public static void showStyleEditor() { showStyleEditor(null); }
    public static void showStyleEditor(ImGuiStyle ref) {

    }

    public static boolean showStyleSelector(String label) {
        return false;
    }

    public static void showFontSelector(String label) {

    }

    public static void showUserGuide() {

    }

    public static String getVersion() {
        return null;
    }

    // --- Styles ---

    public static void styleColorsDark() { styleColorsDark(null); }
    public static void styleColorsDark(ImGuiStyle dst) {

    }

    public static void styleColorsLight() { styleColorsLight(null); }
    public static void styleColorsLight(ImGuiStyle dst) {

    }

    public static void styleColorsClassic() { styleColorsClassic(null); }
    public static void styleColorsClassic(ImGuiStyle dst) {

    }

    // --- Windows ---

    public static boolean begin(String name) { return begin(name, null); }
    public static boolean begin(String name, boolean[] pOpen) { return begin(name, pOpen, 0); }
    public static boolean begin(String name, boolean[] pOpen, int flags) {
        return false;
    }

    public static void end() {

    }

    // --- Child Windows ---

    public static boolean beginChild(String strId) { return beginChild(strId, new ImVec2(0, 0)); }
    public static boolean beginChild(String strId, ImVec2 size) { return beginChild(strId, size, false); }
    public static boolean beginChild(String strId, ImVec2 size, boolean border) { return beginChild(strId, size, border, 0); }
    public static boolean beginChild(String strId, ImVec2 size, boolean border, int flags) {
        return false;
    }

    public static boolean beginChild(int id) { return beginChild(id, new ImVec2(0, 0)); }
    public static boolean beginChild(int id, ImVec2 size) { return beginChild(id, size, false); }
    public static boolean beginChild(int id, ImVec2 size, boolean border) { return beginChild(id, size, border, 0); }
    public static boolean beginChild(int id, ImVec2 size, boolean border, int flags) {
        return false;
    }

    public static void endChild() {

    }

    // --- Windows Utilities ---

    public static boolean isWindowAppearing() {
        return false;
    }

    public static boolean isWindowCollapsed() {
        return false;
    }

    public static boolean isWindowFocused() { return isWindowFocused(0); }
    public static boolean isWindowFocused(int flags) {
        return false;
    }

    public static boolean isWindowHovered() { return isWindowHovered(0); }
    public static boolean isWindowHovered(int flags) {
        return false;
    }

    public static ImDrawList getWindowDrawList() {
        return null;
    }

    public static float getWindowDpiScale() {
        return 0;
    }

    public static ImVec2 getWindowPos() {
        return null;
    }

    public static ImVec2 getWindowSize() {
        return null;
    }

    public static float getWindowWidth() {
        return 0;
    }

    public static float getWindowHeight() {
        return 0;
    }

    public static ImGuiViewport getWindowViewport() {
        return null;
    }

    // --- Window manipulation ---

    public static void setNextWindowPos(ImVec2 pos) { setNextWindowPos(pos, 0); }
    public static void setNextWindowPos(ImVec2 pos, int cond) { setNextWindowPos(pos, cond, new ImVec2(0, 0)); }
    public static void setNextWindowPos(ImVec2 pos, int cond, ImVec2 pivot) {

    }

    public static void setNextWindowSize(ImVec2 size) { setNextWindowSize(size, 0); }
    public static void setNextWindowSize(ImVec2 size, int cond) {

    }

    public static void setNextWindowSizeConstraints(ImVec2 sizeMin, ImVec2 sizeMax) { setNextWindowSizeConstraints(sizeMin, sizeMax, null); }
    public static void setNextWindowSizeConstraints(ImVec2 sizeMin, ImVec2 sizeMax, ImGuiSizeCallback customCallback) { setNextWindowSizeConstraints(sizeMin, sizeMax, customCallback, null); }
    public static void setNextWindowSizeConstraints(ImVec2 sizeMin, ImVec2 sizeMax, ImGuiSizeCallback customCallback, Object customCallbackData) {

    }

    public static void setNextWindowContentSize(ImVec2 size) {

    }

    public static void setNextWindowCollapsed(boolean collapsed) { setNextWindowCollapsed(collapsed, 0); }
    public static void setNextWindowCollapsed(boolean collapsed, int cond) {

    }

    public static void setNextWindowFocus() {

    }

    public static void setNextWindowBgAlpha(float alpha) {

    }

    public static void setNextWindowViewport(int viewportId) {

    }

    public static void setWindowPos(ImVec2 pos) { setWindowPos(pos, 0); }
    public static void setWindowPos(ImVec2 pos, int cond) {

    }

    public static void setWindowSize(ImVec2 size) { setWindowSize(size, 0); }
    public static void setWindowSize(ImVec2 size, int cond) {

    }

    public static void setWindowCollapsed(boolean collapsed) { setWindowCollapsed(collapsed, 0); }
    public static void setWindowCollapsed(boolean collapsed, int cond) {

    }

    public static void setWindowFocus() {

    }

    public static void setWindowFontScale(float scale) {

    }

    public static void setWindowPos(String name, ImVec2 pos) { setWindowPos(name, pos, 0); }
    public static void setWindowPos(String name, ImVec2 pos, int cond) {

    }

    public static void setWindowSize(String name, ImVec2 size) { setWindowSize(name, size, 0); }
    public static void setWindowSize(String name, ImVec2 size, int cond) {

    }

    public static void setWindowCollapsed(String name, boolean collapsed) { setWindowCollapsed(name, collapsed, 0); }
    public static void setWindowCollapsed(String name, boolean collapsed, int cond) {

    }

    public static void setWindowFocus(String name) {

    }

    // --- Content region ---

    public static ImVec2 getContentRegionAvail() {
        return null;
    }

    public static ImVec2 getContentRegionMax() {
        return null;
    }

    public static ImVec2 getWindowContentRegionMin() {
        return null;
    }

    public static ImVec2 getWindowContentRegionMax() {
        return null;
    }

    // --- Windows Scrolling ---

    public static float getScrollX() {
        return 0;
    }

    public static float getScrollY() {
        return 0;
    }

    public static void setScrollX(float scrollX) {

    }

    public static void setScrollY(float scrollY) {

    }

    public static float getScrollMaxX() {
        return 0;
    }

    public static float getScrollMaxY() {
        return 0;
    }

    public static void setScrollHereX() { setScrollHereX(0.5f); }
    public static void setScrollHereX(float centerXRatio) {

    }

    public static void setScrollHereY() { setScrollHereY(0.5f); }
    public static void setScrollHereY(float centerYRatio) {

    }

    public static void setScrollFromPosX(float localX) { setScrollFromPosX(localX, 0.5f); }
    public static void setScrollFromPosX(float localX, float centerXRatio) {

    }

    public static void setScrollFromPosY(float localY) { setScrollFromPosY(localY, 0.5f); }
    public static void setScrollFromPosY(float localY, float centerYRatio) {

    }

    // --- Parameters stacks (shared) ---

    public static void pushFont(ImFont font) {

    }

    public static void popFont() {

    }

    public static void pushStyleColor(int idx, int col) {

    }

    public static void pushStyleColor(int idx, ImVec4 col) {

    }

    public static void popStyleColor() { popStyleColor(1); }
    public static void popStyleColor(int count) {

    }

    public static void pushStyleVar(int idx, float val) {

    }

    public static void pushStyleVar(int idx, ImVec2 val) {

    }

    public static void popStyleVar() { popStyleVar(1); }
    public static void popStyleVar(int count) {

    }

    public static void pushAllowKeyboardFocus(boolean allowKeyboardFocus) {

    }

    public static void popAllowKeyboardFocus() {

    }

    public static void pushButtonRepeat(boolean repeat) {

    }

    public static void popButtonRepeat() {

    }

    // --- Parameters stacks (current window) ---

    public static void pushItemWidth(float itemWidth) {

    }

    public static void popItemWidth() {

    }

    public static void setNextItemWidth(float itemWidth) {

    }

    public static float calcItemWidth() {
        return 0;
    }

    public static void pushTextWrapPos() { pushTextWrapPos(0.0f); }
    public static void pushTextWrapPos(float wrapLocalPosX) {

    }

    public static void popTextWrapPos() {

    }

    // --- Style read access ---

    public static ImFont getFont() {
        return null;
    }

    public static float getFontSize() {
        return 0;
    }

    public static ImVec2 getFontTextUvWhitePixel() {
        return null;
    }

    public static int getColorU32Col(int idx) { return getColorU32Col(idx, 1.0f); }
    public static int getColorU32Col(int idx, float alphaMul) {
        return 0;
    }

    public static int getColorU32Vec(ImVec4 col) {
        return 0;
    }

    public static int getColorU32U32(int col) {
        return 0;
    }

    public static ImVec4 getStyleColorVec4(int idx) {
        return null;
    }

    // --- Cursor / Layout ---

    public static void separator() {

    }

    public static void sameLine() { sameLine(0.0f); }
    public static void sameLine(float offsetFromStartX) { sameLine(offsetFromStartX, -1.0f); }
    public static void sameLine(float offsetFromStartX, float spacing) {

    }

    public static void newLine() {

    }

    public static void spacing() {

    }

    public static void dummy(ImVec2 size) {

    }

    public static void indent() { indent(0.0f); }
    public static void indent(float indentW) {

    }

    public static void unindent() { unindent(0.0f); }
    public static void unindent(float indentW) {

    }

    public static void beginGroup() {

    }

    public static void endGroup() {

    }

    public static ImVec2 getCursorPos() {
        return null;
    }

    public static float getCursorPosX() {
        return 0;
    }

    public static float getCursorPosY() {
        return 0;
    }

    public static void setCursorPos(ImVec2 localPos) {

    }

    public static void setCursorPosX(float localX) {

    }

    public static void setCursorPosY(float localY) {

    }

    public static ImVec2 getCursorStartPos() {
        return null;
    }

    public static ImVec2 getCursorScreenPos() {
        return null;
    }

    public static void setCursorScreenPos(ImVec2 pos) {

    }

    public static void alignTextToFramePadding() {

    }

    public static float getTextLineHeight() {
        return 0;
    }

    public static float getTextLineHeightWithSpacing() {
        return 0;
    }

    public static float getFrameHeight() {
        return 0;
    }

    public static float getFrameHeightWithSpacing() {
        return 0;
    }

    // --- ID stack/scopes ---

    public static void pushID(String strId) {

    }

    public static void pushID(Object ptrId) {

    }

    public static void pushID(int intId) {

    }

    public static void popID() {

    }

    public static int getID(String strId) {
        return 0;
    }

    public static int getID(Object ptrId) {
        return 0;
    }

    // --- Widgets: Text ---

    public static void textUnformatted(String text) {

    }

    public static void text(String fmt, Object... args) {

    }

    public static void textColored(ImVec4 col, String fmt, Object... args) {

    }

    public static void textDisabled(String fmt, Object... args) {

    }

    public static void textWrapped(String fmt, Object... args) {

    }

    public static void labelText(String label, String fmt, Object... args) {

    }

    public static void bulletText(String fmt, Object... args) {

    }

    // --- Widgets: Main ---

    public static boolean button(String label) { return button(label, new ImVec2(0, 0)); }
    public static boolean button(String label, ImVec2 size) {
        return false;
    }

    public static boolean smallButton(String label) {
        return false;
    }

    public static boolean invisibleButton(String strId, ImVec2 size) { return invisibleButton(strId, size, 0); }
    public static boolean invisibleButton(String strId, ImVec2 size, int flags) {
        return false;
    }

    public static boolean arrowButton(String strId, int dir) {
        return false;
    }

    public static void image(Object userTextureId, ImVec2 size) { image(userTextureId, size, new ImVec2(0, 0)); }
    public static void image(Object userTextureId, ImVec2 size, ImVec2 uv0) { image(userTextureId, size, uv0, new ImVec2(1, 1)); }
    public static void image(Object userTextureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1) { image(userTextureId, size, uv0, uv1, new ImVec4(1, 1, 1, 1)); }
    public static void image(Object userTextureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol) { image(userTextureId, size, uv0, uv1, tintCol, new ImVec4(0, 0, 0, 0)); }
    public static void image(Object userTextureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1, ImVec4 tintCol, ImVec4 borderCol) {

    }

    public static boolean imageButton(Object userTextureId, ImVec2 size) { return imageButton(userTextureId, size, new ImVec2(0, 0)); }
    public static boolean imageButton(Object userTextureId, ImVec2 size, ImVec2 uv0) { return imageButton(userTextureId, size, uv0, new ImVec2(1, 1)); }
    public static boolean imageButton(Object userTextureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1) { return imageButton(userTextureId, size, uv0, uv1, -1); }
    public static boolean imageButton(Object userTextureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1, int framePadding) { return imageButton(userTextureId, size, uv0, uv1, framePadding, new ImVec4(0, 0, 0, 0)); }
    public static boolean imageButton(Object userTextureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1, int framePadding, ImVec4 bgCol) { return imageButton(userTextureId, size, uv0, uv1, framePadding, bgCol, new ImVec4(1, 1, 1, 1)); }
    public static boolean imageButton(Object userTextureId, ImVec2 size, ImVec2 uv0, ImVec2 uv1, int framePadding, ImVec4 bgCol, ImVec4 tintCol) {
        return false;
    }

    public static boolean checkbox(String label, boolean[] v) {
        return false;
    }

    public static boolean checkboxFlags(String label, int[] flags, int flagsValue) {
        return false;
    }

    public static boolean radioButton(String label, boolean active) {
        return false;
    }

    public static boolean radioButton(String label, int[] v, int vButton) {
        return false;
    }

    public static void progressBar(float fraction) { progressBar(fraction, new ImVec2(-FLT_MIN, 0)); }
    public static void progressBar(float fraction, ImVec2 sizeArg) { progressBar(fraction, sizeArg, null); }
    public static void progressBar(float fraction, ImVec2 sizeArg, String overlay) {

    }

    public static void bullet() {

    }

    // --- Widgets: Combo Box ---

    public static boolean beginCombo(String label, String previewValue) { return beginCombo(label, previewValue, 0); }
    public static boolean beginCombo(String label, String previewValue, int flags) {
        return false;
    }

    public static void endCombo() {

    }

    public static boolean combo(String label, int[] currentItem, String[] items) { return combo(label, currentItem, items, -1); }
    public static boolean combo(String label, int[] currentItem, String[] items, int popupMaxHeightInItems) {
        return false;
    }

    public static boolean combo(String label, int[] currentItem, ImGuiComboBoxItemsGetter itemsGetter, Object data, int itemsCount) { return combo(label, currentItem, itemsGetter, data, itemsCount, -1); }
    public static boolean combo(String label, int[] currentItem, ImGuiComboBoxItemsGetter itemsGetter, Object data, int itemsCount, int popupMaxHeightInItems) {
        return false;
    }

    // --- Widgets: Drag Sliders ---

    public static boolean dragFloat(String label, float[] v) { return dragFloat(label, v, 1.0f); }
    public static boolean dragFloat(String label, float[] v, float vSpeed) { return dragFloat(label, v, vSpeed, 0.0f); }
    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin) { return dragFloat(label, v, vSpeed, vMin, 0.0f); }
    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin, float vMax) { return dragFloat(label, v, vSpeed, vMin, vMax, "%.3f"); }
    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin, float vMax, String format) { return dragFloat(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragFloat(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean dragFloat2(String label, float[] v) { return dragFloat2(label, v, 1.0f); }
    public static boolean dragFloat2(String label, float[] v, float vSpeed) { return dragFloat2(label, v, vSpeed, 0.0f); }
    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin) { return dragFloat2(label, v, vSpeed, vMin, 0.0f); }
    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin, float vMax) { return dragFloat2(label, v, vSpeed, vMin, vMax, "%.3f"); }
    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin, float vMax, String format) { return dragFloat2(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragFloat2(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean dragFloat3(String label, float[] v) { return dragFloat3(label, v, 1.0f); }
    public static boolean dragFloat3(String label, float[] v, float vSpeed) { return dragFloat3(label, v, vSpeed, 0.0f); }
    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin) { return dragFloat3(label, v, vSpeed, vMin, 0.0f); }
    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin, float vMax) { return dragFloat3(label, v, vSpeed, vMin, vMax, "%.3f"); }
    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin, float vMax, String format) { return dragFloat3(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragFloat3(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean dragFloat4(String label, float[] v) { return dragFloat4(label, v, 1.0f); }
    public static boolean dragFloat4(String label, float[] v, float vSpeed) { return dragFloat4(label, v, vSpeed, 0.0f); }
    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin) { return dragFloat4(label, v, vSpeed, vMin, 0.0f); }
    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin, float vMax) { return dragFloat4(label, v, vSpeed, vMin, vMax, "%.3f"); }
    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin, float vMax, String format) { return dragFloat4(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragFloat4(String label, float[] v, float vSpeed, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax) { return dragFloatRange2(label, vCurrentMin, vCurrentMax, 1.0f); }
    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed) { return dragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, 0.0f); }
    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin) { return dragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, 0.0f); }
    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax) { return dragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, "%.3f"); }
    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax, String format) { return dragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, null); }
    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax, String format, String formatMax) { return dragFloatRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, formatMax, 0); }
    public static boolean dragFloatRange2(String label, float[] vCurrentMin, float[] vCurrentMax, float vSpeed, float vMin, float vMax, String format, String formatMax, int flags) {
        return false;
    }

    public static boolean dragInt(String label, int[] v) { return dragInt(label, v, 1.0f); }
    public static boolean dragInt(String label, int[] v, float vSpeed) { return dragInt(label, v, vSpeed, 0); }
    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin) { return dragInt(label, v, vSpeed, vMin, 0); }
    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin, int vMax) { return dragInt(label, v, vSpeed, vMin, vMax, "%.d"); }
    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin, int vMax, String format) { return dragInt(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragInt(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean dragInt2(String label, int[] v) { return dragInt2(label, v, 1.0f); }
    public static boolean dragInt2(String label, int[] v, float vSpeed) { return dragInt2(label, v, vSpeed, 0); }
    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin) { return dragInt2(label, v, vSpeed, vMin, 0); }
    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin, int vMax) { return dragInt2(label, v, vSpeed, vMin, vMax, "%d"); }
    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin, int vMax, String format) { return dragInt2(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragInt2(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean dragInt3(String label, int[] v) { return dragInt3(label, v, 1.0f); }
    public static boolean dragInt3(String label, int[] v, float vSpeed) { return dragInt3(label, v, vSpeed, 0); }
    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin) { return dragInt3(label, v, vSpeed, vMin, 0); }
    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin, int vMax) { return dragInt3(label, v, vSpeed, vMin, vMax, "%d"); }
    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin, int vMax, String format) { return dragInt3(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragInt3(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean dragInt4(String label, int[] v) { return dragInt4(label, v, 1.0f); }
    public static boolean dragInt4(String label, int[] v, float vSpeed) { return dragInt4(label, v, vSpeed, 0); }
    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin) { return dragInt4(label, v, vSpeed, vMin, 0); }
    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin, int vMax) { return dragInt4(label, v, vSpeed, vMin, vMax, "%d"); }
    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin, int vMax, String format) { return dragInt4(label, v, vSpeed, vMin, vMax, format, 0); }
    public static boolean dragInt4(String label, int[] v, float vSpeed, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax) { return dragIntRange2(label, vCurrentMin, vCurrentMax, 1.0f); }
    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed) { return dragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, 0); }
    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin) { return dragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, 0); }
    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax) { return dragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, "%d"); }
    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax, String format) { return dragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, null); }
    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax, String format, String formatMax) { return dragIntRange2(label, vCurrentMin, vCurrentMax, vSpeed, vMin, vMax, format, formatMax, 0); }
    public static boolean dragIntRange2(String label, int[] vCurrentMin, int[] vCurrentMax, float vSpeed, int vMin, int vMax, String format, String formatMax, int flags) {
        return false;
    }

    public static boolean dragScalar(String label, int dataType, Object[] pData) { return dragScalar(label, dataType, pData, 1.0f); }
    public static boolean dragScalar(String label, int dataType, Object[] pData, float vSpeed) { return dragScalar(label, dataType, pData, vSpeed, null); }
    public static boolean dragScalar(String label, int dataType, Object[] pData, float vSpeed, Object pMin) { return dragScalar(label, dataType, pData, vSpeed, pMin, null); }
    public static boolean dragScalar(String label, int dataType, Object[] pData, float vSpeed, Object pMin, Object pMax) { return dragScalar(label, dataType, pData, vSpeed, pMin, pMax, null); }
    public static boolean dragScalar(String label, int dataType, Object[] pData, float vSpeed, Object pMin, Object pMax, String format) { return dragScalar(label, dataType, pData, vSpeed, pMin, pMax, format, 0); }
    public static boolean dragScalar(String label, int dataType, Object[] pData, float vSpeed, Object pMin, Object pMax, String format, int flags) {
        return false;
    }

    public static boolean dragScalarN(String label, int dataType, Object[] pData, int components) { return dragScalarN(label, dataType, pData, components, 1.0f); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, int components, float vSpeed) { return dragScalarN(label, dataType, pData, components, vSpeed, null); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, int components, float vSpeed, Object pMin) { return dragScalarN(label, dataType, pData, components, vSpeed, pMin, null); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, int components, float vSpeed, Object pMin, Object pMax) { return dragScalarN(label, dataType, pData, components, vSpeed, pMin, pMax, null); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, int components, float vSpeed, Object pMin, Object pMax, String format) { return dragScalarN(label, dataType, pData, components, vSpeed, pMin, pMax, format, 0); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, int components, float vSpeed, Object pMin, Object pMax, String format, int flags) {
        return false;
    }

    // --- Widgets: Regular Sliders ---

    public static boolean sliderFloat(String label, float[] v) { return sliderFloat(label, v, 1.0f); }
    public static boolean sliderFloat(String label, float[] v, float vMin) { return sliderFloat(label, v, vMin, 0.0f); }
    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax) { return sliderFloat(label, v, vMin, vMax, "%.3f"); }
    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax, String format) { return sliderFloat(label, v, vMin, vMax, format, 0); }
    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderFloat2(String label, float[] v) { return sliderFloat2(label, v, 1.0f); }
    public static boolean sliderFloat2(String label, float[] v, float vMin) { return sliderFloat2(label, v, vMin, 0.0f); }
    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax) { return sliderFloat2(label, v, vMin, vMax, "%.3f"); }
    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax, String format) { return sliderFloat2(label, v, vMin, vMax, format, 0); }
    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderFloat3(String label, float[] v) { return sliderFloat3(label, v, 1.0f); }
    public static boolean sliderFloat3(String label, float[] v, float vMin) { return sliderFloat3(label, v, vMin, 0.0f); }
    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax) { return sliderFloat3(label, v, vMin, vMax, "%.3f"); }
    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax, String format) { return sliderFloat3(label, v, vMin, vMax, format, 0); }
    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderFloat4(String label, float[] v) { return sliderFloat4(label, v, 1.0f); }
    public static boolean sliderFloat4(String label, float[] v, float vMin) { return sliderFloat4(label, v, vMin, 0.0f); }
    public static boolean sliderFloat4(String label, float[] v, float vMin, float vMax) { return sliderFloat4(label, v, vMin, vMax, "%.3f"); }
    public static boolean sliderFloat4(String label, float[] v, float vMin, float vMax, String format) { return sliderFloat4(label, v, vMin, vMax, format, 0); }
    public static boolean sliderFloat4(String label, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderAngle(String label, float[] vRad) { return sliderAngle(label, vRad, -360.0f); }
    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin) { return sliderAngle(label, vRad, vDegreesMin, 360.0f); }
    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin, float vDegreesMax) { return sliderAngle(label, vRad, vDegreesMin, vDegreesMax, "%.0f deg"); }
    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin, float vDegreesMax, String format) { return sliderAngle(label, vRad, vDegreesMin, vDegreesMax, format, 0); }
    public static boolean sliderAngle(String label, float[] vRad, float vDegreesMin, float vDegreesMax, String format, int flags) {
        return false;
    }

    private ImGui() {
        throw new AssertionError();
    }
}
