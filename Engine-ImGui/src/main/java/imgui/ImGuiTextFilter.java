package imgui;

public final class ImGuiTextFilter {
    public static final class ImGuiTextRange {
        public String src;
        public int b;
        public int e;

        public ImGuiTextRange() {
            src = null;
            b = e = 0;
        }

        public ImGuiTextRange(String src, int b, int e) {
            this.src = src;
            this.b = b;
            this.e = e;
        }

        public boolean empty() {
            return b == e;
        }

        public void split(char separator, ImVector<ImGuiTextRange> out) {}
    }

    public char[] inputBuf = new char[256];
    public ImVector<ImGuiTextRange> filters = new ImVector<>();
    public int countGrep;

    public ImGuiTextFilter() { this(""); }
    public ImGuiTextFilter(String defaultFilter) {

    }

    public boolean draw() { return draw("Filter (inc,-exc)"); }
    public boolean draw(String label) { return draw(label, 0.0f); }
    public boolean draw(String label, float width) {
        return false;
    }

    public boolean passFilter(String text) {
        return false;
    }

    public void build() {

    }

    public void clear() {
        inputBuf[0] = 0;
        build();
    }

    public boolean isActive() {
        return !filters.empty();
    }
}
