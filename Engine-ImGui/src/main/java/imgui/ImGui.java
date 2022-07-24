package imgui;

import imgui.fn.ImGuiInputTextCallback;
import imgui.fn.ImGuiItemsGetter;
import imgui.fn.ImGuiSizeCallback;

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

    public static boolean combo(String label, int[] currentItem, ImGuiItemsGetter itemsGetter, Object data, int itemsCount) { return combo(label, currentItem, itemsGetter, data, itemsCount, -1); }
    public static boolean combo(String label, int[] currentItem, ImGuiItemsGetter itemsGetter, Object data, int itemsCount, int popupMaxHeightInItems) {
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

    public static boolean dragScalarN(String label, int dataType, Object[] pData) { return dragScalarN(label, dataType, pData, 1.0f); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, float vSpeed) { return dragScalarN(label, dataType, pData, vSpeed, null); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, float vSpeed, Object pMin) { return dragScalarN(label, dataType, pData, vSpeed, pMin, null); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, float vSpeed, Object pMin, Object pMax) { return dragScalarN(label, dataType, pData, vSpeed, pMin, pMax, null); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, float vSpeed, Object pMin, Object pMax, String format) { return dragScalarN(label, dataType, pData, vSpeed, pMin, pMax, format, 0); }
    public static boolean dragScalarN(String label, int dataType, Object[] pData, float vSpeed, Object pMin, Object pMax, String format, int flags) {
        return false;
    }

    // --- Widgets: Regular Sliders ---

    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax) { return sliderFloat(label, v, vMin, vMax, "%.3f"); }
    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax, String format) { return sliderFloat(label, v, vMin, vMax, format, 0); }
    public static boolean sliderFloat(String label, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax) { return sliderFloat2(label, v, vMin, vMax, "%.3f"); }
    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax, String format) { return sliderFloat2(label, v, vMin, vMax, format, 0); }
    public static boolean sliderFloat2(String label, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax) { return sliderFloat3(label, v, vMin, vMax, "%.3f"); }
    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax, String format) { return sliderFloat3(label, v, vMin, vMax, format, 0); }
    public static boolean sliderFloat3(String label, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

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

    public static boolean sliderInt(String label, int[] v, int vMin, int vMax) { return sliderInt(label, v, vMin, vMax, "%d"); }
    public static boolean sliderInt(String label, int[] v, int vMin, int vMax, String format) { return sliderInt(label, v, vMin, vMax, format, 0); }
    public static boolean sliderInt(String label, int[] v, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderInt2(String label, int[] v, int vMin, int vMax) { return sliderInt2(label, v, vMin, vMax, "%d"); }
    public static boolean sliderInt2(String label, int[] v, int vMin, int vMax, String format) { return sliderInt2(label, v, vMin, vMax, format, 0); }
    public static boolean sliderInt2(String label, int[] v, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderInt3(String label, int[] v, int vMin, int vMax) { return sliderInt3(label, v, vMin, vMax, "%d"); }
    public static boolean sliderInt3(String label, int[] v, int vMin, int vMax, String format) { return sliderInt3(label, v, vMin, vMax, format, 0); }
    public static boolean sliderInt3(String label, int[] v, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderInt4(String label, int[] v, int vMin, int vMax) { return sliderInt4(label, v, vMin, vMax, "%d"); }
    public static boolean sliderInt4(String label, int[] v, int vMin, int vMax, String format) { return sliderInt4(label, v, vMin, vMax, format, 0); }
    public static boolean sliderInt4(String label, int[] v, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean sliderScalar(String label, int dataType, Object[] pData, Object pMin, Object pMax) { return sliderScalar(label, dataType, pData, pMin, pMax, null); }
    public static boolean sliderScalar(String label, int dataType, Object[] pData, Object pMin, Object pMax, String format) { return sliderScalar(label, dataType, pData, pMin, pMax, format, 0); }
    public static boolean sliderScalar(String label, int dataType, Object[] pData, Object pMin, Object pMax, String format, int flags) {
        return false;
    }

    public static boolean sliderScalarN(String label, int dataType, Object[] pData, Object pMin, Object pMax) { return sliderScalarN(label, dataType, pData, pMin, pMax, null); }
    public static boolean sliderScalarN(String label, int dataType, Object[] pData, Object pMin, Object pMax, String format) { return sliderScalarN(label, dataType, pData, pMin, pMax, format, 0); }
    public static boolean sliderScalarN(String label, int dataType, Object[] pData, Object pMin, Object pMax, String format, int flags) {
        return false;
    }

    public static boolean vSliderFloat(String label, ImVec2 size, float[] v, float vMin, float vMax) { return vSliderFloat(label, size, v, vMin, vMax, "%.3f"); }
    public static boolean vSliderFloat(String label, ImVec2 size, float[] v, float vMin, float vMax, String format) { return vSliderFloat(label, size, v, vMin, vMax, format, 0); }
    public static boolean vSliderFloat(String label, ImVec2 size, float[] v, float vMin, float vMax, String format, int flags) {
        return false;
    }

    public static boolean vSliderInt(String label, ImVec2 size, int[] v, int vMin, int vMax) { return vSliderInt(label, size, v, vMin, vMax, "%d"); }
    public static boolean vSliderInt(String label, ImVec2 size, int[] v, int vMin, int vMax, String format) { return vSliderInt(label, size, v, vMin, vMax, format, 0); }
    public static boolean vSliderInt(String label, ImVec2 size, int[] v, int vMin, int vMax, String format, int flags) {
        return false;
    }

    public static boolean vSliderScalar(String label, ImVec2 size, int dataType, Object[] pData, Object pMin, Object pMax) { return vSliderScalar(label, size, dataType, pData, pMin, pMax, null); }
    public static boolean vSliderScalar(String label, ImVec2 size, int dataType, Object[] pData, Object pMin, Object pMax, String format) { return vSliderScalar(label, size, dataType, pData, pMin, pMax, format, 0); }
    public static boolean vSliderScalar(String label, ImVec2 size, int dataType, Object[] pData, Object pMin, Object pMax, String format, int flags) {
        return false;
    }

    // --- Widgets: Input with Keyboard ---

    public static boolean inputText(String label, char[] buf) { return inputText(label, buf, 0); }
    public static boolean inputText(String label, char[] buf, int flags) { return inputText(label, buf, flags, null); }
    public static boolean inputText(String label, char[] buf, int flags, ImGuiInputTextCallback callback) { return inputText(label, buf, flags, callback, null); }
    public static boolean inputText(String label, char[] buf, int flags, ImGuiInputTextCallback callback, Object userData) {
        return false;
    }

    public static boolean inputTextMultiline(String label, char[] buf) { return inputTextMultiline(label, buf, new ImVec2(0, 0)); }
    public static boolean inputTextMultiline(String label, char[] buf, ImVec2 size) { return inputTextMultiline(label, buf, size, 0); }
    public static boolean inputTextMultiline(String label, char[] buf, ImVec2 size, int flags) { return inputTextMultiline(label, buf, size, flags, null); }
    public static boolean inputTextMultiline(String label, char[] buf, ImVec2 size, int flags, ImGuiInputTextCallback callback) { return inputTextMultiline(label, buf, size, flags, callback, null); }
    public static boolean inputTextMultiline(String label, char[] buf, ImVec2 size, int flags, ImGuiInputTextCallback callback, Object userData) {
        return false;
    }

    public static boolean inputTextWithHint(String label, String hint, char[] buf) { return inputTextWithHint(label, hint, buf, 0); }
    public static boolean inputTextWithHint(String label, String hint, char[] buf, int flags) { return inputTextWithHint(label, hint, buf, flags, null); }
    public static boolean inputTextWithHint(String label, String hint, char[] buf, int flags, ImGuiInputTextCallback callback) { return inputTextWithHint(label, hint, buf, flags, callback, null); }
    public static boolean inputTextWithHint(String label, String hint, char[] buf, int flags, ImGuiInputTextCallback callback, Object userData) {
        return false;
    }

    public static boolean inputFloat(String label, float[] v) { return inputFloat(label, v, 0.0f); }
    public static boolean inputFloat(String label, float[] v, float step) { return inputFloat(label, v, step, 0.0f); }
    public static boolean inputFloat(String label, float[] v, float step, float stepFast) { return inputFloat(label, v, step, stepFast, "%.3f"); }
    public static boolean inputFloat(String label, float[] v, float step, float stepFast, String format) { return inputFloat(label, v, step, stepFast, format, 0); }
    public static boolean inputFloat(String label, float[] v, float step, float stepFast, String format, int flags) {
        return false;
    }

    public static boolean inputFloat2(String label, float[] v) { return inputFloat2(label, v, "%.3f"); }
    public static boolean inputFloat2(String label, float[] v, String format) { return inputFloat2(label, v, format, 0); }
    public static boolean inputFloat2(String label, float[] v, String format, int flags) {
        return false;
    }

    public static boolean inputFloat3(String label, float[] v) { return inputFloat3(label, v, "%.3f"); }
    public static boolean inputFloat3(String label, float[] v, String format) { return inputFloat3(label, v, format, 0); }
    public static boolean inputFloat3(String label, float[] v, String format, int flags) {
        return false;
    }

    public static boolean inputFloat4(String label, float[] v) { return inputFloat4(label, v, "%.3f"); }
    public static boolean inputFloat4(String label, float[] v, String format) { return inputFloat4(label, v, format, 0); }
    public static boolean inputFloat4(String label, float[] v, String format, int flags) {
        return false;
    }

    public static boolean inputInt(String label, int[] v) { return inputInt(label, v, 1); }
    public static boolean inputInt(String label, int[] v, int step) { return inputInt(label, v, step, 100); }
    public static boolean inputInt(String label, int[] v, int step, int stepFast) { return inputInt(label, v, step, stepFast, 0); }
    public static boolean inputInt(String label, int[] v, int step, int stepFast, int flags) {
        return false;
    }

    public static boolean inputInt2(String label, int[] v) { return inputInt2(label, v, 0); }
    public static boolean inputInt2(String label, int[] v, int flags) {
        return false;
    }

    public static boolean inputInt3(String label, int[] v) { return inputInt3(label, v, 0); }
    public static boolean inputInt3(String label, int[] v, int flags) {
        return false;
    }

    public static boolean inputInt4(String label, int[] v) { return inputInt4(label, v, 0); }
    public static boolean inputInt4(String label, int[] v, int flags) {
        return false;
    }

    public static boolean inputDouble(String label, double[] v) { return inputDouble(label, v, 0.0); }
    public static boolean inputDouble(String label, double[] v, double step) { return inputDouble(label, v, step, 0.0); }
    public static boolean inputDouble(String label, double[] v, double step, double stepFast) { return inputDouble(label, v, step, stepFast, "%.6f"); }
    public static boolean inputDouble(String label, double[] v, double step, double stepFast, String format) { return inputDouble(label, v, step, stepFast, format, 0); }
    public static boolean inputDouble(String label, double[] v, double step, double stepFast, String format, int flags) {
        return false;
    }

    public static boolean inputScalar(String label, int dataType, double[] v) { return inputScalar(label, dataType, v, 0.0); }
    public static boolean inputScalar(String label, int dataType, double[] v, double step) { return inputScalar(label, dataType, v, step, 0.0); }
    public static boolean inputScalar(String label, int dataType, double[] v, double step, double stepFast) { return inputScalar(label, dataType, v, step, stepFast, "%.6f"); }
    public static boolean inputScalar(String label, int dataType, double[] v, double step, double stepFast, String format) { return inputScalar(label, dataType, v, step, stepFast, format, 0); }
    public static boolean inputScalar(String label, int dataType, double[] v, double step, double stepFast, String format, int flags) {
        return false;
    }

    public static boolean inputScalarN(String label, int dataType, double[] v) { return inputScalarN(label, dataType, v, 0.0); }
    public static boolean inputScalarN(String label, int dataType, double[] v, double step) { return inputScalarN(label, dataType, v, step, 0.0); }
    public static boolean inputScalarN(String label, int dataType, double[] v, double step, double stepFast) { return inputScalarN(label, dataType, v, step, stepFast, "%.6f"); }
    public static boolean inputScalarN(String label, int dataType, double[] v, double step, double stepFast, String format) { return inputScalarN(label, dataType, v, step, stepFast, format, 0); }
    public static boolean inputScalarN(String label, int dataType, double[] v, double step, double stepFast, String format, int flags) {
        return false;
    }

    // --- Widgets: Color Editor/Picker ---

    public static boolean colorEdit3(String label, float[] col) { return colorEdit3(label, col, 0); }
    public static boolean colorEdit3(String label, float[] col, int flags) {
        return false;
    }

    public static boolean colorEdit4(String label, float[] col) { return colorEdit4(label, col, 0); }
    public static boolean colorEdit4(String label, float[] col, int flags) {
        return false;
    }

    public static boolean colorPicker3(String label, float[] col) { return colorPicker3(label, col, 0); }
    public static boolean colorPicker3(String label, float[] col, int flags) {
        return false;
    }

    public static boolean colorPicker4(String label, float[] col) { return colorPicker4(label, col, 0); }
    public static boolean colorPicker4(String label, float[] col, int flags) { return colorPicker4(label, col, flags, null); }
    public static boolean colorPicker4(String label, float[] col, int flags, float[] refCol) {
        return false;
    }

    public static boolean colorButton(String descId, ImVec4 col) { return colorButton(descId, col, 0); }
    public static boolean colorButton(String descId, ImVec4 col, int flags) { return colorButton(descId, col, flags, new ImVec2(0, 0)); }
    public static boolean colorButton(String descId, ImVec4 col, int flags, ImVec2 size) {
        return false;
    }

    public static void setColorEditOptions(int flags) {

    }

    // --- Widgets: Trees ---

    public static boolean treeNode(String label) {
        return false;
    }

    public static boolean treeNode(String strId, String fmt, Object... args) {
        return false;
    }

    public static boolean treeNode(Object ptrId, String fmt, Object... args) {
        return false;
    }

    public static boolean treeNodeEx(String label) { return treeNodeEx(label, 0); }
    public static boolean treeNodeEx(String label, int flags) {
        return false;
    }

    public static boolean treeNodeEx(String strId, int flags, String fmt, Object... args) {
        return false;
    }

    public static boolean treeNodeEx(Object ptrId, int flags, String fmt, Object... args) {
        return false;
    }

    public static void treePush(String strId) {

    }

    public static void treePush() { treePush(null); }
    public static void treePush(Object ptrId) {

    }

    public static void treePop() {

    }

    public static float getTreeNodeToLabelSpacing() {
        return 0;
    }

    public static boolean collapsingHeader(String label) { return collapsingHeader(label, 0); }
    public static boolean collapsingHeader(String label, int flags) {
        return false;
    }

    public static boolean collapsingHeader(String label, boolean[] pVisible) { return collapsingHeader(label, pVisible, 0); }
    public static boolean collapsingHeader(String label, boolean[] pVisible, int flags) {
        return false;
    }

    public static void setNextItemOpen(boolean isOpen) { setNextItemOpen(isOpen, 0); }
    public static void setNextItemOpen(boolean isOpen, int cond) {

    }

    // --- Widgets: Selectables ---

    public static boolean selectable(String label) { return selectable(label, false); }
    public static boolean selectable(String label, boolean selected) { return selectable(label, selected, 0); }
    public static boolean selectable(String label, boolean selected, int flags) { return selectable(label, selected, flags, new ImVec2(0, 0)); }
    public static boolean selectable(String label, boolean selected, int flags, ImVec2 size) {
        return false;
    }

    public static boolean selectable(String label, boolean[] pSelected) { return selectable(label, pSelected, 0); }
    public static boolean selectable(String label, boolean[] pSelected, int flags) { return selectable(label, pSelected, flags, new ImVec2(0, 0)); }
    public static boolean selectable(String label, boolean[] pSelected, int flags, ImVec2 size) {
        return false;
    }

    // --- Widgets: List Boxes ---

    public static boolean beginListBox(String label) { return beginListBox(label, new ImVec2(0, 0)); }
    public static boolean beginListBox(String label, ImVec2 size) {
        return false;
    }

    public static void endListBox() {

    }

    public static boolean listBox(String label, int[] currentItem, String[] items) { return listBox(label, currentItem, items, -1); }
    public static boolean listBox(String label, int[] currentItem, String[] items, int heightInItems) {
        return false;
    }

    public static boolean listBox(String label, int[] currentItem, ImGuiItemsGetter itemsGetter, Object data, int itemsCount) { return listBox(label, currentItem, itemsGetter, data, itemsCount, -1); }
    public static boolean listBox(String label, int[] currentItem, ImGuiItemsGetter itemsGetter, Object data, int itemsCount, int heightInItems) {
        return false;
    }

    // --- Widgets: Data Plotting ---
    // TODO

    // --- Widgets: Value() Helpers ---

    public static void value(String prefix, boolean b) {

    }

    public static void value(String prefix, int v) {

    }

    public static void valueU(String prefix, int v) {

    }

    public static void value(String prefix, float v) { value(prefix, v, null); }
    public static void value(String prefix, float v, String floatFormat) {

    }

    // --- Widgets: Menus ---

    public static boolean beginMenuBar() {
        return false;
    }

    public static void endMenuBar() {

    }

    public static boolean beginMainMenuBar() {
        return false;
    }

    public static void endMainMenuBar() {

    }

    public static boolean beginMenu(String label) { return beginMenu(label, true); }
    public static boolean beginMenu(String label, boolean enabled) {
        return false;
    }

    public static void endMenu() {

    }

    public static boolean menuItem(String label) { return menuItem(label, null); }
    public static boolean menuItem(String label, String shortcut) { return menuItem(label, shortcut, false); }
    public static boolean menuItem(String label, String shortcut, boolean selected) { return menuItem(label, shortcut, selected, true); }
    public static boolean menuItem(String label, String shortcut, boolean selected, boolean enabled) {
        return false;
    }

    public static boolean menuItem(String label, String shortcut, boolean[] selected) { return menuItem(label, shortcut, selected, true); }
    public static boolean menuItem(String label, String shortcut, boolean[] selected, boolean enabled) {
        return false;
    }

    // --- Tooltips ---

    public static void beginTooltip() {

    }

    public static void endTooltip() {

    }

    public static void setTooltip(String fmt, Object... args) {

    }

    // --- Popups, Modals ---

    public static boolean beginPopup(String strId) { return beginPopup(strId, 0); }
    public static boolean beginPopup(String strId, int flags) {
        return false;
    }

    public static boolean beginPopupModal(String name) { return beginPopupModal(name, null); }
    public static boolean beginPopupModal(String name, boolean[] pOpen) { return beginPopupModal(name, pOpen, 0); }
    public static boolean beginPopupModal(String name, boolean[] pOpen, int flags) {
        return false;
    }

    public static void endPopup() {

    }

    // ---- Popups: open/close functions ---

    public static void openPopup(String strId) { openPopup(strId, 0); }
    public static void openPopup(String strId, int popupFlags) {

    }

    public static void openPopup(int id) { openPopup(id, 0); }
    public static void openPopup(int id, int popupFlags) {

    }

    public static void openPopupOnItemClick(String strId) { openPopupOnItemClick(strId, 1); }
    public static void openPopupOnItemClick(String strId, int popupFlags) {

    }

    public static void closeCurrentPopup() {

    }

    // --- Popups: open+begin combined functions helpers ---

    public static boolean beginPopupContextItem() { return beginPopupContextItem(null); }
    public static boolean beginPopupContextItem(String strId) { return beginPopupContextItem(strId, 1); }
    public static boolean beginPopupContextItem(String strId, int popupFlags) {
        return false;
    }

    public static boolean beginPopupContextWindow() { return beginPopupContextWindow(null); }
    public static boolean beginPopupContextWindow(String strId) { return beginPopupContextWindow(strId, 1); }
    public static boolean beginPopupContextWindow(String strId, int popupFlags) {
        return false;
    }

    public static boolean beginPopupContextVoid() { return beginPopupContextVoid(null); }
    public static boolean beginPopupContextVoid(String strId) { return beginPopupContextVoid(strId, 1); }
    public static boolean beginPopupContextVoid(String strId, int popupFlags) {
        return false;
    }

    // --- Popups: query functions ---

    public static boolean isPopupOpen(String strId) { return isPopupOpen(strId, 0); }
    public static boolean isPopupOpen(String strId, int flags) {
        return false;
    }

    // --- Tables ---

    public static boolean beginTable(String strId, int column) { return beginTable(strId, column, 0); }
    public static boolean beginTable(String strId, int column, int flags) { return beginTable(strId, column, flags, new ImVec2(0.0f, 0.0f)); }
    public static boolean beginTable(String strId, int column, int flags, ImVec2 outerSize) { return beginTable(strId, column, flags, outerSize, 0.0f); }
    public static boolean beginTable(String strId, int column, int flags, ImVec2 outerSize, float innerWidth) {
        return false;
    }

    public static void endTable() {

    }

    public static void tableNextRow() { tableNextRow(0); }
    public static void tableNextRow(int rowFlags) { tableNextRow(rowFlags, 0.0f); }
    public static void tableNextRow(int rowFlags, float minRowHeight) {

    }

    public static boolean tableNextColumn() {
        return false;
    }

    public static boolean tableSetColumnIndex(int columnN) {
        return false;
    }

    // --- Tables: Headers & Columns declaration ---

    public static void tableSetupColumn(String label) { tableSetupColumn(label, 0); }
    public static void tableSetupColumn(String label, int flags) { tableSetupColumn(label, flags, 0.0f); }
    public static void tableSetupColumn(String label, int flags, float initWidthOrHeight) { tableSetupColumn(label, flags, initWidthOrHeight, 0); }
    public static void tableSetupColumn(String label, int flags, float initWidthOrHeight, int userId) {

    }

    public static void tableSetupScrollFreeze(int cols, int rows) {

    }

    public static void tableHeadersRow() {

    }

    public static void tableHeader(String label) {

    }

    // --- Tables: Sorting & Miscellaneous functions ---

    public static ImGuiTableSortSpecs tableGetSortSpecs() {
        return null;
    }

    public static int tableGetColumnCount() {
        return 0;
    }

    public static int tableGetColumnIndex() {
        return 0;
    }

    public static int tableGetRowIndex() {
        return 0;
    }

    public static String tableGetColumnName() { return tableGetColumnName(-1); }
    public static String tableGetColumnName(int columnN) {
        return null;
    }

    public static int tableGetColumnFlags() { return tableGetColumnFlags(-1); }
    public static int tableGetColumnFlags(int columnN) {
        return 0;
    }

    public static void tableSetColumnEnabled(int columnN, boolean v) {

    }

    public static void tableSetBgColor(int target, int color) { tableSetBgColor(target, color, -1); }
    public static void tableSetBgColor(int target, int color, int columnN) {

    }

    // --- Legacy Columns API (prefer using Tables!) ---

    public static void columns() { columns(1); }
    public static void columns(int count) { columns(count, null); }
    public static void columns(int count, String id) { columns(count, id, true); }
    public static void columns(int count, String id, boolean border) {

    }

    public static void nextColumn() {

    }

    public static int getColumnIndex() {
        return 0;
    }

    public static float getColumnWidth() { return getColumnWidth(-1); }
    public static float getColumnWidth(int columnIndex) {
        return 0;
    }

    public static void setColumnWidth(int columnIndex, float width) {

    }

    public static float getColumnOffset() { return getColumnOffset(-1); }
    public static float getColumnOffset(int columnIndex) {
        return 0;
    }

    public static void setColumnOffset(int columnIndex, float offsetX) {

    }

    public static int getColumnsCount() {
        return 0;
    }

    // --- Tab Bars, Tabs ---

    public static boolean beginTabBar(String strId) { return beginTabBar(strId, 0); }
    public static boolean beginTabBar(String strId, int flags) {
        return false;
    }

    public static void endTabBar() {

    }

    public static boolean beginTabItem(String label) { return beginTabItem(label, null); }
    public static boolean beginTabItem(String label, boolean[] pOpen) { return beginTabItem(label, pOpen, 0); }
    public static boolean beginTabItem(String label, boolean[] pOpen, int flags) {
        return false;
    }

    public static void endTabItem() {

    }

    public static boolean tabItemButton(String label) { return tabItemButton(label, 0); }
    public static boolean tabItemButton(String label, int flags) {
        return false;
    }

    public static void setTabItemClosed(String tabOrDockedWindowLabel) {

    }

    // --- Docking ---

    public static int dockSpace(int id) { return dockSpace(id, new ImVec2(0, 0)); }
    public static int dockSpace(int id, ImVec2 size) { return dockSpace(id, size, 0); }
    public static int dockSpace(int id, ImVec2 size, int flags) { return dockSpace(id, size, flags, null); }
    public static int dockSpace(int id, ImVec2 size, int flags, ImGuiWindowClass windowClass) {
        return 0;
    }

    public static int dockSpaceOverViewport(ImGuiViewport viewport) { return dockSpaceOverViewport(viewport, 0); }
    public static int dockSpaceOverViewport(ImGuiViewport viewport, int flags) { return dockSpaceOverViewport(viewport, flags, null); }
    public static int dockSpaceOverViewport(ImGuiViewport viewport, int flags, ImGuiWindowClass windowClass) {
        return 0;
    }

    public static void setNextWindowDockID(int dockId) { setNextWindowDockID(dockId, 0); }
    public static void setNextWindowDockID(int dockId, int cond) {

    }

    public static void setNextWindowClass(ImGuiWindowClass windowClass) {

    }

    public static int getWindowDockID() {
        return 0;
    }

    public static boolean isWindowDocked() {
        return false;
    }

    // --- Logging/Capture ---

    public static void logToTTY() { logToTTY(-1); }
    public static void logToTTY(int autoOpenDepth) {

    }

    public static void logToFile() { logToFile(-1); }
    public static void logToFile(int autoOpenDepth) { logToFile(autoOpenDepth, null); }
    public static void logToFile(int autoOpenDepth, String filename) {

    }

    public static void logToClipboard() { logToClipboard(-1); }
    public static void logToClipboard(int autoOpenDepth) {

    }

    public static void logFinish() {

    }

    public static void logButtons() {

    }

    public static void logText(String fmt, Object... args) {

    }

    // --- Drag and Drop ---

    public static boolean beginDragDropSource() { return beginDragDropSource(0); }
    public static boolean beginDragDropSource(int flags) {
        return false;
    }

    public static boolean setDragDropPayload(String type, Object data) { return setDragDropPayload(type, data, 0); }
    public static boolean setDragDropPayload(String type, Object data, int cond) {
        return false;
    }

    public static void endDragDropSource() {

    }

    public static boolean beginDragDropTarget() {
        return false;
    }

    public static ImGuiPayload acceptDragDropPayload(String type) { return acceptDragDropPayload(type, 0); }
    public static ImGuiPayload acceptDragDropPayload(String type, int flags) {
        return null;
    }

    public static void endDragDropTarget() {

    }

    public static ImGuiPayload getDragDropPayload() {
        return null;
    }

    // --- Disabling ---

    public static void beginDisabled() { beginDisabled(true); }
    public static void beginDisabled(boolean disabled) {

    }

    public static void endDisabled() {

    }

    // --- Clipping ---

    public static void pushClipRect(ImVec2 clipRectMin, ImVec2 clipRectMax, boolean intersectWithCurrentClipRect) {

    }

    public static void popClipRect() {

    }

    // --- Focus, Activation ---

    public static void setItemDefaultFocus() {

    }

    public static void setKeyboardFocusHere() { setKeyboardFocusHere(0); }
    public static void setKeyboardFocusHere(int offset) {

    }

    // --- Item/Widgets Utilities and Query Functions ---

    public static boolean isItemHovered() { return isItemHovered(0); }
    public static boolean isItemHovered(int flags) {
        return false;
    }

    public static boolean isItemActive() {
        return false;
    }

    public static boolean isItemFocused() {
        return false;
    }

    public static boolean isItemClicked() { return isItemClicked(0); }
    public static boolean isItemClicked(int mouseButton) {
        return false;
    }

    public static boolean isItemVisible() {
        return false;
    }

    public static boolean isItemEdited() {
        return false;
    }

    public static boolean isItemActivated() {
        return false;
    }

    public static boolean isItemDeactivated() {
        return false;
    }

    public static boolean isItemDeactivatedAfterEdit() {
        return false;
    }

    public static boolean isItemToggledOpen() {
        return false;
    }

    public static boolean isAnyItemHovered() {
        return false;
    }

    public static boolean isAnyItemActive() {
        return false;
    }

    public static boolean isAnyItemFocused() {
        return false;
    }

    public static ImVec2 getItemRectMin() {
        return null;
    }

    public static ImVec2 getItemRectMax() {
        return null;
    }

    public static ImVec2 getItemRectSize() {
        return null;
    }

    public static void setItemAllowOverlap() {

    }

    // --- Viewports ---

    public static ImGuiViewport getMainViewport() {
        return null;
    }

    // --- Background/Foreground Draw Lists ---

    public static ImDrawList getBackgroundDrawList() {
        return null;
    }

    public static ImDrawList getForegroundDrawList() {
        return null;
    }

    public static ImDrawList getBackgroundDrawList(ImGuiViewport viewport) {
        return null;
    }

    public static ImDrawList getForegroundDrawList(ImGuiViewport viewport) {
        return null;
    }

    // --- Miscellaneous Utilities ---

    public static boolean isRectVisible(ImVec2 size) {
        return false;
    }

    public static boolean isRectVisible(ImVec2 rectMin, ImVec2 rectMax) {
        return false;
    }

    public static double getTime() {
        return 0;
    }

    public static int getFrameCount() {
        return 0;
    }

    public static ImDrawListSharedData getDrawListSharedData() {
        return null;
    }

    public static String getStyleColorName(int idx) {
        return null;
    }

    public static void setStateStorage(ImGuiStorage storage) {

    }

    public static ImGuiStorage getStateStorage() {
        return null;
    }

    public static boolean beginChildFrame(int id, ImVec2 size) { return beginChildFrame(id, size, 0); }
    public static boolean beginChildFrame(int id, ImVec2 size, int flags) {
        return false;
    }

    public static void endChildFrame() {

    }

    // --- Text Utilities ---

    public static ImVec2 calcTextSize(String text) { return calcTextSize(text, false); }
    public static ImVec2 calcTextSize(String text, boolean hideTextAfterDoubleHash) { return calcTextSize(text, hideTextAfterDoubleHash, -1.0f); }
    public static ImVec2 calcTextSize(String text, boolean hideTextAfterDoubleHash, float wrapWidth) {
        return null;
    }

    // --- Color Utilities ---

    public static ImVec4 colorConvertU32ToFloat4(int in) {
        return null;
    }

    public static int colorConvertFloat4ToU32(ImVec4 in) {
        return 0;
    }

    public static void colorConvertRGBtoHSV(float r, float g, float b, float[] outH, float[] outS, float[] outV) {

    }

    public static void colorConvertHSVtoRGB(float h, float s, float v, float[] outR, float[] outG, float[] outV) {

    }

    // --- Inputs Utilities: Keyboard ---

    public static boolean isKeyDown(int key) {
        return false;
    }

    public static boolean isKeyPressed(int key) { return isKeyPressed(key, true); }
    public static boolean isKeyPressed(int key, boolean repeat) {
        return false;
    }

    public static boolean isKeyReleased(int key) {
        return false;
    }

    public static int getKeyPressedAmount(int key, float repeatDelay, float rate) {
        return 0;
    }

    public static String getKeyName(int key) {
        return null;
    }

    public static void setNextFrameWantCaptureKeyboard(boolean wantCaptureKeyboard) {

    }

    // --- Inputs Utilities: Mouse ---

    public static boolean isMouseDown(int button) {
        return false;
    }

    public static boolean isMouseClicked(int button) { return isMouseClicked(button, false); }
    public static boolean isMouseClicked(int button, boolean repeat) {
        return false;
    }

    public static boolean isMouseReleased(int button) {
        return false;
    }

    public static boolean isMouseDoubleClicked(int button) {
        return false;
    }

    public static int getMouseClickedCount(int button) {
        return 0;
    }

    public static boolean isMouseHoveringRect(ImVec2 rMin, ImVec2 rMax) { return isMouseHoveringRect(rMin, rMax, true); }
    public static boolean isMouseHoveringRect(ImVec2 rMin, ImVec2 rMax, boolean clip) {
        return false;
    }

    public static boolean isMousePosValid() { return isMousePosValid(null); }
    public static boolean isMousePosValid(ImVec2 mousePos) {
        return false;
    }

    public static boolean isAnyMouseDown() {
        return false;
    }

    public static ImVec2 getMousePos() {
        return null;
    }

    public static ImVec2 getMousePosOnOpeningCurrentPopup() {
        return null;
    }

    public static boolean isMouseDragging(int button) { return isMouseDragging(button, -1.0f); }
    public static boolean isMouseDragging(int button, float lockThreshold) {
        return false;
    }

    public static ImVec2 getMouseDragDelta() { return getMouseDragDelta(0); }
    public static ImVec2 getMouseDragDelta(int button) { return getMouseDragDelta(button, -1.0f); }
    public static ImVec2 getMouseDragDelta(int button, float lockThreshold) {
        return null;
    }

    public static void resetMouseDragDelta() { resetMouseDragDelta(0); }
    public static void resetMouseDragDelta(int button) {

    }

    public static int getMouseCursor() {
        return 0;
    }

    public static void setMouseCursor(int cursorType) {

    }

    public static void setNextFrameWantCaptureMouse(boolean wantCaptureMouse) {

    }

    // --- Clipboard Utilities ---

    public static String getClipboardText() {
        return null;
    }

    public static void setClipboardText(String text) {

    }

    // --- Settings/.Ini Utilities ---

    public static void loadIniSettingsFromDisk(String iniFilename) {

    }

    public static void loadIniSettingsFromMemory(String iniData) {

    }

    public static void saveIniSettingsToDisk(String iniFilename) {

    }

    public static String saveIniSettingsToMemory() {
        return null;
    }

    // --- Platform/OS interface for multi-viewport support ---

    public static ImGuiPlatformIO getPlatformIO() {
        return null;
    }

    public static void updatePlatformWindows() {

    }

    public static void renderPlatformWindowsDefault() { renderPlatformWindowsDefault(null); }
    public static void renderPlatformWindowsDefault(Object platformRenderArg) { renderPlatformWindowsDefault(platformRenderArg, null); }
    public static void renderPlatformWindowsDefault(Object platformRenderArg, Object rendererRenderArg) {

    }

    public static void destroyPlatformWindows() {

    }

    public static ImGuiViewport findViewportByID(int id) {
        return null;
    }

    public static ImGuiViewport findViewportByPlatformHandle(Object platformHandle) {
        return null;
    }

    private ImGui() {
        throw new AssertionError();
    }
}
