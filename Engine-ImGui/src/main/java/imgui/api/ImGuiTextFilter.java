package imgui.api;

import java.util.ArrayList;
import java.util.List;

public final class ImGuiTextFilter {
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
        return !filters.isEmpty();
    }

    public static final class ImGuiTextRange {
        private final String text;

        public ImGuiTextRange() {
            text = null;
        }

        public ImGuiTextRange(String text) {
            this.text = text;
        }

        public boolean empty() {
            return text.isEmpty();
        }

        public void split(char separator, List<ImGuiTextRange>/*ptr*/ out) {

        }
    }

    public final char[] inputBuf = new char[256]; // TODO: Should be String or nonexistent?
    private final List<ImGuiTextRange> filters = new ArrayList<>(); // TODO: Implement accessors as needed
    public int countGrep;
}
