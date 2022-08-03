package com.xiaowenz.scv.core;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SCIndexTest {

    private static SCIndex index;

    @BeforeAll
    static void initIndex() throws InvalidVersionException {
        index = SCIndex.create();
        index.add(SCScript.create("R2.0.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R2.0.2_DDL_abc.sql","dummy"));
        index.add(SCScript.create("U2.0.2_DDL_abc.sql","dummy"));

        index.add(SCScript.create("R1.0.0_DDL_abc.sql","dummy"));
        index.add(SCScript.create("U1.0.0_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R1.0.0_DML_abc.sql","dummy"));
        index.add(SCScript.create("U1.0.0_DML_abc.sql","dummy"));
        index.add(SCScript.create("R1.0.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R1.0.1_DML_abc.sql","dummy"));
        index.add(SCScript.create("R1.0.1_PATCH_abc.sql","dummy"));
        index.add(SCScript.create("U1.0.1_PATCH_abc.sql","dummy"));
        index.add(SCScript.create("R1.1.0_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R1.1.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R1.1.1_DML_abc.sql","dummy"));


    }

    @Test
    void loadScripts() throws InvalidVersionException {
        SCIndex newIndex = this.index.loadScripts(SCAction.R, SCVersion.create("1.0.0"),  SCVersion.create("1.0.1"));
        assertEquals(newIndex.listScripts().get(0), SCScript.create("R1.0.0_DDL_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(1), SCScript.create("R1.0.0_DML_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(2), SCScript.create("R1.0.1_DDL_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(3), SCScript.create("R1.0.1_DML_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(4), SCScript.create("R1.0.1_PATCH_abc.sql","dummy"));

        SCIndex newIndex2 = this.index.loadScripts(SCAction.U, SCVersion.create("2.0.1"),  SCVersion.create("2.0.2"));
        assertEquals(newIndex2.listScripts().get(0), SCScript.create("U2.0.2_DDL_abc.sql","dummy"));
    }

    @Test
    void add() throws InvalidVersionException {
        SCIndex newIndex = SCIndex.create();
        newIndex.add(SCScript.create("R1.1.1_DML_abc.sql","dummy"));
        newIndex.add(SCScript.create("R1.1.1_DDL_abc.sql","dummy"));
        newIndex.add(SCScript.create("R1.1.1_PATCH_abc.sql","dummy"));

        newIndex.add(SCScript.create("R22.1.1_DML_abc.sql","dummy"));
        newIndex.add(SCScript.create("U1.0.1_DML_abc.sql","dummy"));

        assertEquals(newIndex.listScripts().get(0), SCScript.create("U1.0.1_DML_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(1), SCScript.create("R1.1.1_DDL_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(2), SCScript.create("R1.1.1_DML_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(3), SCScript.create("R1.1.1_PATCH_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(4), SCScript.create("R22.1.1_DML_abc.sql","dummy"));
    }
}