package imgui.internal.stbtextedit.fn;

@FunctionalInterface
public interface StbTexteditInsertCharsFn<StringType> {
    boolean insert(StringType obj, int i, String str);
}
