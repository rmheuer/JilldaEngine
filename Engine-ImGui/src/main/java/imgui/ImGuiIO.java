package imgui;

import imgui.flag.ImGuiKey;
import imgui.fn.ImGuiGetClipboardTextFn;
import imgui.fn.ImGuiSetClipboardTextFn;
import imgui.fn.ImGuiSetPlatformImeDataFn;

public final class ImGuiIO {
    public int configFlags;
    public int backendFlags;
    public ImVec2 displaySize = new ImVec2();
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

    public ImFontAtlas fonts;
    public float fontGlobalScale;
    public boolean fontAllowUserScaling;
    public ImFont fontDefault;
    public ImVec2 displayFramebufferScale = new ImVec2();

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
    public float configMemoryCompactTimer;

    public String backendPlatformName;
    public String backendRendererName;
    public Object backendPlatformUserData;
    public Object backendRendererUserData;
    public Object backendLanguageUserData;

    public ImGuiGetClipboardTextFn getClipboardTextFn;
    public ImGuiSetClipboardTextFn setClipboardTextFn;
    public Object clipboardUserData;

    public ImGuiSetPlatformImeDataFn setPlatformImeDataFn;

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
    public int metricsActiveAllocations; // TODO: Likely remove
    public ImVec2 mouseDelta = new ImVec2();

    public ImVec2 mousePos = new ImVec2();
    public boolean[] mouseDown = new boolean[5];
    public float mouseWheel;
    public float mouseWheelH;
    public int mouseHoveredViewport;
    public boolean keyCtrl;
    public boolean keyShift;
    public boolean keyAlt;
    public boolean keySuper;

    public int keyMods;
    public ImGuiKeyData[] keysData = new ImGuiKeyData[ImGuiKey.KeysData_SIZE];
    public boolean wantCaptureMouseUnlessPopupClose;
    public ImVec2 mousePosPrev = new ImVec2();
    public ImVec2[] mouseClickedPos = new ImVec2[5];
    public double[] mouseClickedTime = new double[5];
    public boolean[] mouseClicked = new boolean[5];
    public boolean[] mouseDoubleClicked = new boolean[5];
    public short[] mouseClickedCount = new short[5];
    public short[] mouseClickedLastCount = new short[5];
    public boolean[] mouseReleased = new boolean[5];
    public boolean[] mouseDownOwned = new boolean[5];
    public boolean[] mouseDownOwnedUnlessPopupClose = new boolean[5];
    public float[] mouseDownDuration = new float[5];
    public float[] mouseDownDurationPrev = new float[5];
    public ImVec2[] mouseDragMaxDistanceAbs = new ImVec2[5];
    public float[] mouseDragMaxDistanceSqr = new float[5];
    public float penPressure;
    public boolean appFocusLost;
    public boolean appAcceptingEvents;
    public byte backendUsingLegacyKeyArrays;
    public boolean backendUsingLegacyNavInputArray;
    public char inputQueueSurrogate;
    public ImVector<Character> inputQueueCharacters = new ImVector<>();

    public ImGuiIO() {
        for (int i = 0; i < keysData.length; i++)
            keysData[i] = new ImGuiKeyData();
        for (int i = 0; i < mouseClickedPos.length; i++)
            mouseClickedPos[i] = new ImVec2();
        for (int i = 0; i < mouseDragMaxDistanceAbs.length; i++)
            mouseDragMaxDistanceAbs[i] = new ImVec2();
    }

    public void addKeyEvent(int key, boolean down) {}
    public void addKeyAnalogEvent(int key, boolean down, float v) {}
    public void addMousePosEvent(float x, float y) {}
    public void addMouseButtonEvent(int button, boolean down) {}
    public void addMouseWheelEvent(float whX, float whY) {}
    public void addMouseViewportEvent(int id) {}
    public void addFocusEvent(boolean focused) {}
    public void addInputCharacter(int c) {}
    public void addInputCharacterUTF16(char c) {}
    public void addInputCharactersUTF8(String str) {}

    public void setKeyEventNativeData(int key, int nativeKeycode, int nativeScancode) {setKeyEventNativeData(key, nativeKeycode, nativeScancode, -1); }
    public void setKeyEventNativeData(int key, int nativeKeycode, int nativeScancode, int nativeLegacyIndex) {}
    public void setAppAcceptingEvents(boolean acceptingEvents) {}
    public void clearInputCharacters() {}
    public void clearInputKeys() {}
}
