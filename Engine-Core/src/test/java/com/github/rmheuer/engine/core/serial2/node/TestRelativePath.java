package com.github.rmheuer.engine.core.serial2.node;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(Lifecycle.PER_CLASS)
public final class TestRelativePath {
    private SerialObject root;
    private SerialArray foo;
    private SerialObject bar;
    private SerialObject baz;

    @BeforeAll
    public void setUpNodes() {
        root = new SerialObject();
        foo = new SerialArray();
        foo.add(new SerialInt(1));
        foo.add(new SerialInt(2));
        baz = new SerialObject();
        foo.add(baz);
        root.put("foo", foo);
        bar = new SerialObject();
        bar.put("baz", new SerialByte((byte) 42));
        root.put("bar", bar);
    }

    @Test
    public void testParentToChild() {
        assertEquals(root.getRelativePathTo(foo), "foo");
    }

    @Test
    public void testChildToParent() {
        assertEquals(foo.getRelativePathTo(root), "..");
    }

    @Test
    public void testSibling() {
        assertEquals(foo.getRelativePathTo(bar), "../bar");
    }

    @Test
    public void testCompound() {
        assertEquals(root.getRelativePathTo(baz), "foo/2");
    }

    @Test
    public void testSelf() {
        assertThrows(IllegalArgumentException.class, () -> {
            root.getRelativePathTo(root);
        });
    }
}
