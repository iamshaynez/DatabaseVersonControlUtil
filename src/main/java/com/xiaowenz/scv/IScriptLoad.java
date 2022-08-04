package com.xiaowenz.scv;

import java.util.Map;

import com.xiaowenz.scv.core.SCIndex;

public interface IScriptLoad {
    public SCIndex load(Map<String, String> config);
}
