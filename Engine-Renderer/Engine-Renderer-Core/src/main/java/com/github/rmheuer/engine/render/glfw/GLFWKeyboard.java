package com.github.rmheuer.engine.render.glfw;

import com.github.rmheuer.engine.core.input.keyboard.Key;
import com.github.rmheuer.engine.core.input.keyboard.Keyboard;
import com.github.rmheuer.engine.core.util.BiMap;

import static org.lwjgl.glfw.GLFW.*;

public final class GLFWKeyboard implements Keyboard {
    private static final BiMap<Integer, Key> KEYS = new BiMap<>();

    public static Key getKey(int id) {
        return KEYS.getBOrDefault(id, Key.UNKNOWN);
    }

    public static int getGlfwId(Key key) {
        return KEYS.getAOrDefault(key, GLFW_KEY_UNKNOWN);
    }

    private final long window;

    public GLFWKeyboard(long window) {
        this.window = window;
    }

    @Override
    public boolean isKeyPressed(Key key) {
        return glfwGetKey(window, getGlfwId(key)) == GLFW_PRESS;
    }

    static {
        KEYS.put(GLFW_KEY_UNKNOWN, Key.UNKNOWN);
        KEYS.put(GLFW_KEY_SPACE, Key.SPACE);
        KEYS.put(GLFW_KEY_APOSTROPHE, Key.APOSTROPHE);
        KEYS.put(GLFW_KEY_COMMA, Key.COMMA);
        KEYS.put(GLFW_KEY_MINUS, Key.MINUS);
        KEYS.put(GLFW_KEY_PERIOD, Key.PERIOD);
        KEYS.put(GLFW_KEY_SLASH, Key.SLASH);
        KEYS.put(GLFW_KEY_0, Key.ZERO);
        KEYS.put(GLFW_KEY_1, Key.ONE);
        KEYS.put(GLFW_KEY_2, Key.TWO);
        KEYS.put(GLFW_KEY_3, Key.THREE);
        KEYS.put(GLFW_KEY_4, Key.FOUR);
        KEYS.put(GLFW_KEY_5, Key.FIVE);
        KEYS.put(GLFW_KEY_6, Key.SIX);
        KEYS.put(GLFW_KEY_7, Key.SEVEN);
        KEYS.put(GLFW_KEY_8, Key.EIGHT);
        KEYS.put(GLFW_KEY_9, Key.NINE);
        KEYS.put(GLFW_KEY_SEMICOLON, Key.SEMICOLON);
        KEYS.put(GLFW_KEY_EQUAL, Key.EQUALS);
        KEYS.put(GLFW_KEY_A, Key.A);
        KEYS.put(GLFW_KEY_B, Key.B);
        KEYS.put(GLFW_KEY_C, Key.C);
        KEYS.put(GLFW_KEY_D, Key.D);
        KEYS.put(GLFW_KEY_E, Key.E);
        KEYS.put(GLFW_KEY_F, Key.F);
        KEYS.put(GLFW_KEY_G, Key.G);
        KEYS.put(GLFW_KEY_H, Key.H);
        KEYS.put(GLFW_KEY_I, Key.I);
        KEYS.put(GLFW_KEY_J, Key.J);
        KEYS.put(GLFW_KEY_K, Key.K);
        KEYS.put(GLFW_KEY_L, Key.L);
        KEYS.put(GLFW_KEY_M, Key.M);
        KEYS.put(GLFW_KEY_N, Key.N);
        KEYS.put(GLFW_KEY_O, Key.O);
        KEYS.put(GLFW_KEY_P, Key.P);
        KEYS.put(GLFW_KEY_Q, Key.Q);
        KEYS.put(GLFW_KEY_R, Key.R);
        KEYS.put(GLFW_KEY_S, Key.S);
        KEYS.put(GLFW_KEY_T, Key.T);
        KEYS.put(GLFW_KEY_U, Key.U);
        KEYS.put(GLFW_KEY_V, Key.V);
        KEYS.put(GLFW_KEY_W, Key.W);
        KEYS.put(GLFW_KEY_X, Key.X);
        KEYS.put(GLFW_KEY_Y, Key.Y);
        KEYS.put(GLFW_KEY_Z, Key.Z);
        KEYS.put(GLFW_KEY_LEFT_BRACKET, Key.LEFT_BRACKET);
        KEYS.put(GLFW_KEY_RIGHT_BRACKET, Key.RIGHT_BRACKET);
        KEYS.put(GLFW_KEY_GRAVE_ACCENT, Key.GRAVE_ACCENT);
        KEYS.put(GLFW_KEY_WORLD_1, Key.WORLD_1);
        KEYS.put(GLFW_KEY_WORLD_2, Key.WORLD_2);
        KEYS.put(GLFW_KEY_ESCAPE, Key.ESCAPE);
        KEYS.put(GLFW_KEY_ENTER, Key.ENTER);
        KEYS.put(GLFW_KEY_TAB, Key.TAB);
        KEYS.put(GLFW_KEY_BACKSPACE, Key.BACKSPACE);
        KEYS.put(GLFW_KEY_INSERT, Key.INSERT);
        KEYS.put(GLFW_KEY_DELETE, Key.DELETE);
        KEYS.put(GLFW_KEY_RIGHT, Key.RIGHT);
        KEYS.put(GLFW_KEY_LEFT, Key.LEFT);
        KEYS.put(GLFW_KEY_DOWN, Key.DOWN);
        KEYS.put(GLFW_KEY_UP, Key.UP);
        KEYS.put(GLFW_KEY_PAGE_UP, Key.PAGE_UP);
        KEYS.put(GLFW_KEY_PAGE_DOWN, Key.PAGE_DOWN);
        KEYS.put(GLFW_KEY_HOME, Key.HOME);
        KEYS.put(GLFW_KEY_END, Key.END);
        KEYS.put(GLFW_KEY_CAPS_LOCK, Key.CAPS_LOCK);
        KEYS.put(GLFW_KEY_SCROLL_LOCK, Key.SCROLL_LOCK);
        KEYS.put(GLFW_KEY_NUM_LOCK, Key.NUM_LOCK);
        KEYS.put(GLFW_KEY_PRINT_SCREEN, Key.PRINT_SCREEN);
        KEYS.put(GLFW_KEY_PAUSE, Key.PAUSE);
        KEYS.put(GLFW_KEY_F1, Key.F1);
        KEYS.put(GLFW_KEY_F2, Key.F2);
        KEYS.put(GLFW_KEY_F3, Key.F3);
        KEYS.put(GLFW_KEY_F4, Key.F4);
        KEYS.put(GLFW_KEY_F5, Key.F5);
        KEYS.put(GLFW_KEY_F6, Key.F6);
        KEYS.put(GLFW_KEY_F7, Key.F7);
        KEYS.put(GLFW_KEY_F8, Key.F8);
        KEYS.put(GLFW_KEY_F9, Key.F9);
        KEYS.put(GLFW_KEY_F10, Key.F10);
        KEYS.put(GLFW_KEY_F11, Key.F11);
        KEYS.put(GLFW_KEY_F12, Key.F12);
        KEYS.put(GLFW_KEY_F13, Key.F13);
        KEYS.put(GLFW_KEY_F14, Key.F14);
        KEYS.put(GLFW_KEY_F15, Key.F15);
        KEYS.put(GLFW_KEY_F16, Key.F16);
        KEYS.put(GLFW_KEY_F17, Key.F17);
        KEYS.put(GLFW_KEY_F18, Key.F18);
        KEYS.put(GLFW_KEY_F19, Key.F19);
        KEYS.put(GLFW_KEY_F20, Key.F20);
        KEYS.put(GLFW_KEY_F21, Key.F21);
        KEYS.put(GLFW_KEY_F22, Key.F22);
        KEYS.put(GLFW_KEY_F23, Key.F23);
        KEYS.put(GLFW_KEY_F24, Key.F24);
        KEYS.put(GLFW_KEY_F25, Key.F25);
        KEYS.put(GLFW_KEY_KP_0, Key.NUMPAD_ZERO);
        KEYS.put(GLFW_KEY_KP_1, Key.NUMPAD_ONE);
        KEYS.put(GLFW_KEY_KP_2, Key.NUMPAD_TWO);
        KEYS.put(GLFW_KEY_KP_3, Key.NUMPAD_THREE);
        KEYS.put(GLFW_KEY_KP_4, Key.NUMPAD_FOUR);
        KEYS.put(GLFW_KEY_KP_5, Key.NUMPAD_FIVE);
        KEYS.put(GLFW_KEY_KP_6, Key.NUMPAD_SIX);
        KEYS.put(GLFW_KEY_KP_7, Key.NUMPAD_SEVEN);
        KEYS.put(GLFW_KEY_KP_8, Key.NUMPAD_EIGHT);
        KEYS.put(GLFW_KEY_KP_9, Key.NUMPAD_NINE);
        KEYS.put(GLFW_KEY_KP_DECIMAL, Key.NUMPAD_DECIMAL);
        KEYS.put(GLFW_KEY_KP_DIVIDE, Key.NUMPAD_DIVIDE);
        KEYS.put(GLFW_KEY_KP_MULTIPLY, Key.NUMPAD_MULTIPLY);
        KEYS.put(GLFW_KEY_KP_SUBTRACT, Key.NUMPAD_SUBTRACT);
        KEYS.put(GLFW_KEY_KP_ADD, Key.NUMPAD_ADD);
        KEYS.put(GLFW_KEY_KP_ENTER, Key.NUMPAD_ENTER);
        KEYS.put(GLFW_KEY_KP_EQUAL, Key.NUMPAD_EQUALS);
        KEYS.put(GLFW_KEY_LEFT_SHIFT, Key.LEFT_SHIFT);
        KEYS.put(GLFW_KEY_LEFT_CONTROL, Key.LEFT_CONTROL);
        KEYS.put(GLFW_KEY_LEFT_ALT, Key.LEFT_ALT);
        KEYS.put(GLFW_KEY_LEFT_SUPER, Key.LEFT_SUPER);
        KEYS.put(GLFW_KEY_RIGHT_SHIFT, Key.RIGHT_SHIFT);
        KEYS.put(GLFW_KEY_RIGHT_CONTROL, Key.RIGHT_CONTROL);
        KEYS.put(GLFW_KEY_RIGHT_ALT, Key.RIGHT_ALT);
        KEYS.put(GLFW_KEY_RIGHT_SUPER, Key.RIGHT_SUPER);
        KEYS.put(GLFW_KEY_MENU, Key.MENU);
    }
}
