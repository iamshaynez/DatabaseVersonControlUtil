package com.xiaowenz.scv.core;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchemaScriptTest {

    @Test
    void create() {
        // valid cases
        assertDoesNotThrow(() -> {
            SCScript.create("R1.2.0.12_DDL_ABC.sql","dummy");
        });

        assertDoesNotThrow(() -> {
            SCScript.create("U1.2.0.12_DML_ABC.sql","dummy");
        });

        assertDoesNotThrow(() -> {
            SCScript.create("R1.2.0.12_PATCH_ABC.sql","dummy");
        });

        assertDoesNotThrow(() -> {
            SCScript.create("R1.2.0.12.100_DDL_ABC.sql","dummy");
        });

        // invalid cases
        Exception exception = assertThrows(InvalidVersionException.class, () -> {
            SCScript.create("R1.0.R_DDL_ABC.sql","dummy");
        });

        exception = assertThrows(InvalidVersionException.class, () -> {
            SCScript.create("C1.0.0_DDL_ABC.sql","dummy");
        });

        exception = assertThrows(InvalidVersionException.class, () -> {
            SCScript.create("1.0.12_DDL_ABC.sql","dummy");
        });

        exception = assertThrows(InvalidVersionException.class, () -> {
            SCScript.create("R1.0.12_DDC_AAA.sql","dummy");
        });

        exception = assertThrows(InvalidVersionException.class, () -> {
            SCScript.create("R1.0.13_PATCHABC.sql","dummy");
        });
    }

    @Test
    void compareTo() throws InvalidVersionException {
        assertTrue(SCScript.create("R1.2.0.13_PATCH_ABC.sql","dummy").
                compareTo(SCScript.create("R1.2.0.12_PATCH_ABC.sql","dummy")) > 0);

        assertTrue(SCScript.create("R1.2.0.13_PATCH_ABC.sql","dummy").
                compareTo(SCScript.create("R1.2.0.14_PATCH_ABC.sql","dummy")) < 0);

        assertTrue(SCScript.create("R1.2.0.13_DDL_ABC.sql","dummy").
                compareTo(SCScript.create("R1.2.0.13_PATCH_ABC.sql","dummy")) < 0);

        assertTrue(SCScript.create("R1.2.0.13_DDL_ABC.sql","dummy").
                compareTo(SCScript.create("R1.2.0.13_DML_ABC.sql","dummy")) < 0);

        assertTrue(SCScript.create("R1.2.0.13_DML_ABC.sql","dummy").
                compareTo(SCScript.create("R1.2.0.13_PATCH_ABC.sql","dummy")) < 0);

        assertTrue(SCScript.create("R1.2.0.13_PATCH_ABC222.sql","dummy").
                compareTo(SCScript.create("R1.2.0.13_PATCH_ABC.sql","dummy")) == 0);

        assertTrue(SCScript.create("R1.2.0.13_PATCH_ABC222.sql","dummy").
                compareTo(SCScript.create("U1.2.0.13_PATCH_ABC.sql","dummy")) < 0);

        assertTrue(SCScript.create("R1.2.0.13_PATCH_ABC222.sql","dummy").
                compareTo(SCScript.create("U1.2.0.13_DDL_ABC.sql","dummy")) < 0);

        assertTrue(SCScript.create("U1.2.0.13_PATCH_ABC222.sql","dummy").
                compareTo(SCScript.create("U1.2.0.13_DDL_ABC.sql","dummy")) > 0);
    }
}
