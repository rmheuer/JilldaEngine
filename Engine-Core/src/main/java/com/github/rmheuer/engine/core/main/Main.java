package com.github.rmheuer.engine.core.main;

import com.github.rmheuer.engine.core.serial.node.SerialNode;
import com.github.rmheuer.engine.core.serial.obj.ObjectSerializer;

import java.util.Arrays;

public final class Main {
    public static abstract class TestParent {

    }

    public static class Test extends TestParent {
        public String str;
        public int[] intArray;
        public TestParent child, child2;
        private TestParent self;

        public Test() {}

        @Override
        public String toString() {
            return "Test{" +
                    "str='" + str + '\'' +
                    ", intArray=" + Arrays.toString(intArray) +
                    ", child=" + child +
                    '}';
        }
    }

    public static void main(String[] args) {
        //Test test = new Test();
        //test.str = "Hello, world!";
        //test.intArray = new int[] {1, 2, 3, 4, 5};

        //Test child = new Test();
        //child.str = "I am the child :)";
        //child.self = child;
        //test.child = test.child2 = child;

        //System.out.println("Input: " + test);
        //SerialNode node = ObjectSerializer.get().serialize(test);
        //System.out.println(node);
        //Test out = ObjectSerializer.get().deserialize(node, Test.class);
        //System.out.println("Output: " + out);

        Game.get().run();
    }

    private Main() {
        throw new AssertionError();
    }
}
