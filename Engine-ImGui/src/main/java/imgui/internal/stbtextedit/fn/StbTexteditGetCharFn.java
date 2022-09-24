package imgui.internal.stbtextedit.fn;

@FunctionalInterface
public interface StbTexteditGetCharFn<StringType> {
    char get(StringType obj, int i);
}
