package com.xiaowenz.scv;

import java.util.List;
import java.util.Properties;

import com.xiaowenz.scv.core.SCScript;

public interface IScriptExecutor {
    public void init(Properties config) throws ScriptConfigException;
    public void execute(List<SCScript> scripts, Properties config) throws ScriptRuntimeException ;
}
