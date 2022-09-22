package imgui.api;

import java.util.ArrayList;
import java.util.List;

public final class ImGuiStorage {
    public static final class ImGuiStoragePair {
        public int key;
        public Object val;

        public ImGuiStoragePair(int key, Object val) {
            this.key = key;
            this.val = val;
        }
    }

    private final List<ImGuiStoragePair> data = new ArrayList<>();

    public void clear() {
        data.clear();
    }

    public int getInt(int key) { return getInt(key, 0); }
    public int getInt(int key, int defaultVal) {
        return 0;
    }

    public void setInt(int key, int val) {

    }

    public boolean getBool(int key) { return getBool(key, false); }
    public boolean getBool(int key, boolean defaultVal) {
        return false;
    }

    public void setBool(int key, boolean val) {

    }

    public float getFloat(int key) { return getFloat(key, 0.0f); }
    public float getFloat(int key, float defaultVal) {
        return 0;
    }

    public void setFloat(int key, float val) {

    }

    public Object getObject(int key) {
        return null;
    }

    public void setObject(int key, Object val) {

    }

    // TODO: Get***Ref() if necessary

    public void setAllInt(int val) {

    }

    public void buildSortByKey() {

    }
}
