package com.github.rmheuer.sandbox;

import com.github.rmheuer.engine.core.serial2.json.JsonCodec;
import com.github.rmheuer.engine.core.serial2.node.SerialNode;
import com.github.rmheuer.engine.core.serial2.obj.AutoSerializable;
import com.github.rmheuer.engine.core.serial2.obj.ObjectSerializer;
import com.github.rmheuer.engine.core.serial2.obj.SerialMapper;
import com.github.rmheuer.engine.core.serial2.obj.SerializeWith;

public class SerialTest {
    @SerializeWith(MapperTest.class)
    private static class NotSerializable {
        private int a = 42;
    }

    private static class Serializable implements AutoSerializable {
        private int meaningOfEverything;
    }

    private static class MapperTest extends SerialMapper<NotSerializable, Serializable> {
        public MapperTest() {
            super(Serializable.class);
        }

        @Override
        public Serializable toSerializable(NotSerializable notSerializable) {
            Serializable s = new Serializable();
            s.meaningOfEverything = notSerializable.a;
            return s;
        }

        @Override
        public NotSerializable fromSerializable(Serializable serializable) {
            throw new UnsupportedOperationException();
        }
    }

    private static class Test implements AutoSerializable {
        public int foo;
        public String bar;
        public boolean[] bools = {true, false};
        public NotSerializable notSerializable = new NotSerializable();
        public NotSerializable refTest = notSerializable;
    }

    public static void main(String[] args) {
        ObjectSerializer serializer = new ObjectSerializer();
        SerialNode serial = serializer.serialize(new Test(), Test.class);

        System.out.println(JsonCodec.get().encode(serial));
        return;
    }
}
