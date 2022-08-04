package com.xiaowenz.scv.core;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class SCIndexTest {

    private static SCIndex index;

    @BeforeAll
    static void initIndex() throws InvalidVersionException {
        index = SCIndex.create();
        index.add(SCScript.create("R2.0.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("U2.0.1_DDL_abc.sql","dummy"));
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



    /**
     * Test Add Script to Index
     * 
     * @throws InvalidVersionException
     */
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

    /**
     * Test Load Script from a Index
     * 
     * @throws InvalidVersionException
     */
    @Test
    void loadScripts() throws InvalidVersionException {
        SCIndex newIndex = SCIndexTest.index.loadScripts(SCAction.R, SCVersion.create("1.0.0"),  SCVersion.create("1.0.1"));
        assertEquals(newIndex.listScripts().get(0), SCScript.create("R1.0.0_DDL_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(1), SCScript.create("R1.0.0_DML_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(2), SCScript.create("R1.0.1_DDL_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(3), SCScript.create("R1.0.1_DML_abc.sql","dummy"));
        assertEquals(newIndex.listScripts().get(4), SCScript.create("R1.0.1_PATCH_abc.sql","dummy"));

        SCIndex newIndex2 = SCIndexTest.index.loadScripts(SCAction.U, SCVersion.create("2.0.1"),  SCVersion.create("2.0.2"));
        assertEquals(newIndex2.listScripts().get(0), SCScript.create("U2.0.1_DDL_abc.sql","dummy"));
        assertEquals(newIndex2.listScripts().get(1), SCScript.create("U2.0.2_DDL_abc.sql","dummy"));
    }

    @Test
    void release() throws InvalidVersionException {
        List<SCScript> list = SCIndexTest.index.release(SCVersion.create("1.0.0"), SCVersion.create("1.0.1"));
        assertEquals(list.get(0), SCScript.create("R1.0.0_DDL_abc.sql","dummy"));
        assertEquals(list.get(1), SCScript.create("R1.0.0_DML_abc.sql","dummy"));
        assertEquals(list.get(2), SCScript.create("R1.0.1_DDL_abc.sql","dummy"));
        assertEquals(list.get(3), SCScript.create("R1.0.1_DML_abc.sql","dummy"));
    }

    @Test
    void undo() throws InvalidVersionException {
        List<SCScript> list = SCIndexTest.index.undo(SCVersion.create("2.0.0"), SCVersion.create("2.0.2"));
        assertEquals(list.get(0), SCScript.create("U2.0.2_DDL_abc.sql","dummy"));
        assertEquals(list.get(1), SCScript.create("U2.0.1_DDL_abc.sql","dummy"));
    }

    @Test
    void patch() throws InvalidVersionException {
        List<SCScript> list = SCIndexTest.index.patch(SCVersion.create("1.0.0"), SCVersion.create("1.0.1"));
        assertEquals(list.get(0), SCScript.create("R1.0.1_PATCH_abc.sql","dummy"));
    }
}
