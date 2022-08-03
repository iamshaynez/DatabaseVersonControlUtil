package com.xiaowenz.scv;

import com.xiaowenz.scv.core.InvalidVersionException;
import com.xiaowenz.scv.core.SCVersion;

public class SCVRun {

    public static void main(String[] args) throws InvalidVersionException {

        SCVersion scv = SCVersion.create("1.0.0.1");
        System.out.println(scv);
    }
}
