package com.jschapin.dregs;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappedRecordTest {

    record TestRecord(
            String aString
            , char aCharacter
            , byte aByte
            , short aShort
            , int anInteger
            , long aLong
            , float aFloat
            , double aDouble
            , boolean aBoolean
            , SubRecord subRecord
    ) {

    }

    record SubRecord(String aString, SubSubRecord aSubSubRecord) {
    }

    record SubSubRecord(int anInt) {
    }


    @Test
    public void test() {
        var map = new HashMap<String, String>();
        map.put("A_STRING", "foo");
        map.put("A_CHARACTER", "a");
        map.put("A_BYTE", "1");
        map.put("A_SHORT", "1");
        map.put("AN_INTEGER", "123");
        map.put("A_LONG", "123");
        map.put("A_FLOAT", "1.23");
        map.put("A_DOUBLE", "1.23");
        map.put("A_BOOLEAN", "true");
        map.put("SUB_RECORD_A_STRING", "bar");
        map.put("SUB_RECORD_A_SUB_SUB_RECORD_AN_INT", "456");
        var rec = MappedRecord.fromMap(map, TestRecord.class);
        assertEquals(
                new TestRecord(
                        "foo"
                        , 'a'
                        , (byte) 1
                        , (short) 1
                        , 123
                        , 123L
                        , 1.23F
                        , 1.23D
                        , true
                        , new SubRecord("bar", new SubSubRecord(456))
                ),
                rec
        );
    }

}