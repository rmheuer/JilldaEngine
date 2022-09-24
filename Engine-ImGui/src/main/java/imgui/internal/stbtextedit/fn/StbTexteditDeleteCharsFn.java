package imgui.internal.stbtextedit.fn;

@FunctionalInterface
public interface StbTexteditDeleteCharsFn<StringType> {
    void delete(StringType obj, int i, int n);
}
