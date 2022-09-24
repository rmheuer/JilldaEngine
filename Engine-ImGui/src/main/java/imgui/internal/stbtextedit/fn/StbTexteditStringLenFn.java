package imgui.internal.stbtextedit.fn;

@FunctionalInterface
public interface StbTexteditStringLenFn<StringType> {
    int get(StringType str);
}
