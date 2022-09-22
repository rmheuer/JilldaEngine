package imgui.api;

import imgui.api.flag.ImGuiKey;
import imgui.api.fn.ImGuiGetClipboardTextFn;
import imgui.api.fn.ImGuiSetClipboardTextFn;
import imgui.api.fn.ImGuiSetPlatformImeDataFn;

import java.util.ArrayList;
import java.util.List;

public final class ImGuiIO {
    public int configFlags;
    public int backendFlags;
    public final ImVec2 displaySize = new ImVec2();
    public float deltaTime;
    public float iniSavingRate;
    public String iniFilename;
    public String logFilename;
    public float mouseDoubleClickTime;
    public float mouseDoubleClickMaxDist;
    public float mouseDragThreshold;
    public float keyRepeatDelay;
    public float keyRepeatRate;
    public Object userData;

    public ImFontAtlas/*ptr*/ fonts;
    public float fontGlobalScale;
    public boolean fontAllowUserScaling;
    public ImFont/*ptr*/ fontDefault;
    public final ImVec2 displayFramebufferScale = new ImVec2();

    public boolean configDockingNoSplit;
    public boolean configDockingWithShift;
    public boolean configDockingAlwaysTabBar;
    public boolean configDockingTransparentPayload;

    public boolean configViewportsNoAutoMerge;
    public boolean configViewportsNoTaskBarIcon;
    public boolean configViewportsNoDecoration;
    public boolean configViewportsNoDefaultParent;

    public boolean mouseDrawCursor;
    public boolean configMacOSXBehaviors;
    public boolean configInputTrickleEventQueue;
    public boolean configInputTextCursorBlink;
    public boolean configInputTextEnterKeepActive;
    public boolean configDragClickToInputText;
    public boolean configWindowsResizeFromEdges;
    public boolean configWindowsMoveFromTitleBarOnly;
    public float configMemoryCompactTimer; // TODO: Is this necessary?

    public String backendPlatformName;
    public String backendRendererName;
    public Object backendPlatformUserData;
    public Object backendRendererUserData;
    public Object backendLanguageUserData;

    public ImGuiGetClipboardTextFn getClipboardTextFn;
    public ImGuiSetClipboardTextFn setClipboardTextFn;
    public Object clipboardUserData;

    public ImGuiSetPlatformImeDataFn setPlatformImeDataFn;

    public void addKeyEvent(int key, boolean down) {

    }

    public void addKeyAnalogEvent(int key, boolean down, float v) {

    }

    public void addMousePosEvent(float x, float y) {

    }

    public void addMouseButtonEvent(int button, boolean down) {

    }

    public void addMouseWheelEvent(float whX, float whY) {

    }

    public void addMouseViewportEvent(int id) {

    }

    public void addFocusEvent(boolean focused) {

    }

    public void addInputCharacter(int c) {

    }

    public void setKeyEventNativeData(int key, int nativeKeycode, int nativeScancode) { setKeyEventNativeData(key, nativeKeycode, nativeScancode, -1); }
    public void setKeyEventNativeData(int key, int nativeKeycode, int nativeScancode, int nativeLegacyIndex) {

    }

    public void setAppAcceptingEvents(boolean acceptingEvents) {

    }

    public void clearInputCharacters() {

    }

    public void clearInputKeys() {

    }

    public boolean wantCaptureMouse;
    public boolean wantCaptureKeyboard;
    public boolean wantTextInput;
    public boolean wantSetMousePos;
    public boolean wantSaveIniSettings;
    public boolean navActive;
    public boolean navVisible;
    public float framerate;
    public int metricsRenderVertices;
    public int metricsRenderIndices;
    public int metricsRenderWindows;
    public int metricsActiveWindows;
    public int metricsActiveAllocations; // Probably remove
    public final ImVec2 mouseDelta = new ImVec2();

    public final ImVec2 mousePos = new ImVec2();
    public final boolean[] mouseDown = new boolean[5];
    public float mouseWheel;
    public float mouseWheelH;
    public int mouseHoveredViewport;
    public boolean keyCtrl;
    public boolean keyShift;
    public boolean keyAlt;
    public boolean keySuper;

    public int keyMods;
    private final ImGuiKeyData[] keysData = new ImGuiKeyData[ImGuiKey.KeysData_SIZE];
    public boolean wantCaptureMouseUnlessPopupClose;
    public final ImVec2 mousePosPrev = new ImVec2();
    private final ImVec2[] mouseClickedPos = new ImVec2[5];
    public final double[] mouseClickedTime = new double[5];
    public final boolean[] mouseClicked = new boolean[5];
    public final boolean[] mouseDoubleClicked = new boolean[5];
    public final short[] mouseClickedCount = new short[5];
    public final short[] mouseClickedLastCount = new short[5];
    public final boolean[] mouseReleased = new boolean[5];
    public final boolean[] mouseDownOwned = new boolean[5];
    public final boolean[] mouseDownOwnedUnlessPopupClose = new boolean[5];
    public final float[] mouseDownDuration = new float[5];
    public final float[] mouseDownDurationPrev = new float[5];
    private final ImVec2[] mouseDragMaxDistanceAbs = new ImVec2[5];
    public final float[] mouseDragMaxDistanceSqr = new float[5];
    public float penPressure;
    public boolean appFocusLost;
    public boolean appAcceptingEvents;
    public byte backendUsingLegacyKeyArrays;
    public boolean backendUsingLegacyNavInputArray;
    public char inputQueueSurrogate;
    public final List<Character> inputQueueCharacters = new ArrayList<>();

    public ImGuiIO() {
        for (int i = 0; i < keysData.length; i++)
            keysData[i] = new ImGuiKeyData();
        for (int i = 0; i < mouseClickedPos.length; i++)
            mouseClickedPos[i] = new ImVec2();
        for (int i = 0; i < mouseDragMaxDistanceAbs.length; i++)
            mouseDragMaxDistanceAbs[i] = new ImVec2();
    }

    public ImGuiKeyData getKeyData(int idx) {
        return keysData[idx];
    }

    public void setKeyData(int idx, ImGuiKeyData data) {
        keysData[idx].set(data);
    }

    public ImVec2 getMouseClickedPos(int idx) {
        return mouseClickedPos[idx];
    }

    public void setMouseClickedPos(int idx, ImVec2 pos) {
        mouseClickedPos[idx].set(pos);
    }

    public ImVec2 getMouseDragMaxDistanceAbs(int idx) {
        return mouseDragMaxDistanceAbs[idx];
    }

    public void setMouseDragMaxDistanceAbs(int idx, ImVec2 v) {
        mouseDragMaxDistanceAbs[idx].set(v);
    }
}
