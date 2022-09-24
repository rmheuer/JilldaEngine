package imgui.internal.stbtextedit.fn;

import imgui.internal.stbtextedit.StbTexteditRow;

@FunctionalInterface
public interface StbTexteditLayOutRowFn<StringType> {
    void layOut(StbTexteditRow r, StringType obj, int n);
}
