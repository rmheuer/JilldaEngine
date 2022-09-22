package imgui.api;

import imgui.api.fn.*;

import java.util.ArrayList;
import java.util.List;

public final class ImGuiPlatformIO {
    public PlatformCreateWindowFn platformCreateWindow;
    public PlatformDestroyWindowFn platformDestroyWindow;
    public PlatformShowWindowFn platformShowWindow;
    public PlatformSetWindowPosFn platformSetWindowPos;
    public PlatformGetWindowPosFn platformGetWindowPos;
    public PlatformSetWindowSizeFn platformSetWindowSize;
    public PlatformGetWindowSizeFn platformGetWindowSize;
    public PlatformSetWindowFocusFn platformSetWindowFocus;
    public PlatformGetWindowFocusFn platformGetWindowFocus;
    public PlatformGetWindowMinimizedFn platformGetWindowMinimized;
    public PlatformSetWindowTitleFn platformSetWindowTitle;
    public PlatformSetWindowAlphaFn platformSetWindowAlpha;
    public PlatformUpdateWindowFn platformUpdateWindow;
    public PlatformRenderWindowFn platformRenderWindow;
    public PlatformSwapBuffersFn platformSwapBuffers;
    public PlatformGetWindowDpiScaleFn platformGetWindowDpiScale;
    public PlatformOnChangedViewportFn platformOnChangedViewport;
    public PlatformCreateVkSurfaceFn platformCreateVkSurface;

    public RendererCreateWindowFn rendererCreateWindow;
    public RendererDestroyWindowFn rendererDestroyWindow;
    public RendererSetWindowSizeFn rendererSetWindowSize;
    public RendererRenderWindowFn rendererRenderWindow;
    public RendererSwapBuffersFn rendererSwapBuffers;

    public final List<ImGuiPlatformMonitor> monitors = new ArrayList<>();

    public final List<ImGuiViewport/*ptr*/> viewports = new ArrayList<>();
}
