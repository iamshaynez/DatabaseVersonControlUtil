package com.xiaowenz.scv.core;


import static org.junit.jupiter.api.Assertions.*;


class SCVersionTest {

    @org.junit.jupiter.api.Test
    void create() {
        // valid
        assertDoesNotThrow(() -> {
            SCVersion.create("1.0.0");
        });

        assertDoesNotThrow(() -> {
            SCVersion.create("12");
        });

        // invalid
        Exception exception = assertThrows(InvalidVersionException.class, () -> {
            SCVersion.create("1.0.R");
        });

        Exception exception1 = assertThrows(InvalidVersionException.class, () -> {
            SCVersion.create("1R.12.1");
        });

        Exception exception2 = assertThrows(InvalidVersionException.class, () -> {
            SCVersion.create("R10");
        });

    }

    @org.junit.jupiter.api.Test
    void getVersion() {
        assertDoesNotThrow(() -> {
            SCVersion scv = SCVersion.create("2.0.122");
            assertEquals(2, scv.getVersion(0));
            assertEquals(0, scv.getVersion(1));
            assertEquals(122, scv.getVersion(2));

        });

    }

    @org.junit.jupiter.api.Test
    void getDigits() throws InvalidVersionException {
        SCVersion scv = SCVersion.create("2.0.122");
        assertEquals(3, scv.getDigits());
        SCVersion scv1 = SCVersion.create("12321321");
        assertEquals(1, scv1.getDigits());
        SCVersion scv2 = SCVersion.create("2.0.122.32.012");
        assertEquals(5, scv2.getDigits());


    }

    @org.junit.jupiter.api.Test
    void compareTo() throws InvalidVersionException {

        // a > b
        assertEquals(1, SCVersion.create("2.0.122").compareTo(SCVersion.create("2.0.121")));
        assertEquals(1, SCVersion.create("2.1.122").compareTo(SCVersion.create("2.0.122")));
        assertEquals(1, SCVersion.create("12.0.122").compareTo(SCVersion.create("2.0.121")));
        assertEquals(1, SCVersion.create("2.0.122.1").compareTo(SCVersion.create("2.0.121.0")));

        // a = b
        assertEquals(0, SCVersion.create("2.0.122").compareTo(SCVersion.create("2.0.122")));
        assertEquals(0, SCVersion.create("2.0.122.0").compareTo(SCVersion.create("2.0.122")));
        assertEquals(0, SCVersion.create("2.0.122").compareTo(SCVersion.create("2.0.122.0")));

        // a < b
        assertEquals(-1, SCVersion.create("2.0.12").compareTo(SCVersion.create("2.0.121")));
        assertEquals(-1, SCVersion.create("2.0.122").compareTo(SCVersion.create("2.99.121")));
        assertEquals(-1, SCVersion.create("2.0.122.1").compareTo(SCVersion.create("2.0.122.1.1")));


    }
}
