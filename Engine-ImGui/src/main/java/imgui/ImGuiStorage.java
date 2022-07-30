package imgui;

import imgui.util.ValueRef;

public final class ImGuiStorage {
    public static final class ImGuiStoragePair {
        public int key;
        public Object val;

        public ImGuiStoragePair(int key, int valI) {
            this.key = key;
            val = valI;
        }

        public ImGuiStoragePair(int key, float valF) {
            this.key = key;
            val = valF;
        }

        public ImGuiStoragePair(int key, Object valP) {
            this.key = key;
            val = valP;
        }

        public int valI() { return (int) val; }
        public float valF() { return (float) val; }
        public Object valP() { return val; }
    }

    public ImVector<ImGuiStoragePair> data = new ImVector<>();

    public void clear() { data.clear(); }
    public int getInt(int key) { return getInt(key, 0); }
    public int getInt(int key, int defaultVal) { return 0; }
    public void setInt(int key, int val) {}
    public boolean getBool(int key) { return getBool(key, false); }
    public boolean getBool(int key, boolean defaultVal) { return false; }
    public void setBool(int key, boolean val) {}
    public float getFloat(int key) { return getFloat(key, 0.0f); }
    public float getFloat(int key, float defaultVal) { return 0; }
    public void setFloat(int key, float val) {}
    public Object getObject(int key) { return null; }
    public void setObject(int key, Object val) {}

    public ValueRef<Integer> getIntRef(int key) { return getIntRef(key, 0); }
    public ValueRef<Integer> getIntRef(int key, int defaultVal) { return null; }
    public ValueRef<Boolean> getBoolRef(int key) { return getBoolRef(key, false); }
    public ValueRef<Boolean> getBoolRef(int key, boolean defaultVal) { return null; }
    public ValueRef<Float> getFloatRef(int key) { return getFloatRef(key, 0.0f); }
    public ValueRef<Float> getFloatRef(int key, float defaultVal) { return null; }
    public ValueRef<Object> getObjectRef(int key) { return getObjectRef(key, null); }
    public ValueRef<Object> getObjectRef(int key, Object defaultVal) { return null; }

    public void setAllInt(int val) {

    }

    public void buildSortByKey() {

    }
}
