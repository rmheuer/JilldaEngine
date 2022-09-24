package imgui.internal.stbtextedit.fn;

@FunctionalInterface
public interface StbTexteditKeyToTextFn {
    int convert(int key);
}
