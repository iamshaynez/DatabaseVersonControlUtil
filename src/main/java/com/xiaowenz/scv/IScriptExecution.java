package com.xiaowenz.scv;

import java.util.List;
import java.util.Map;

import com.xiaowenz.scv.core.SCScript;

public interface IScriptExecution {
    public void execute(List<SCScript> scripts, Map<String, String> config) ;
}
