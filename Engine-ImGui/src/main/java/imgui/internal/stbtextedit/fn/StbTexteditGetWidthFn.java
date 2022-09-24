package imgui.internal.stbtextedit.fn;

@FunctionalInterface
public interface StbTexteditGetWidthFn<StringType> {
    float get(StringType obj, int n, int i);
}
