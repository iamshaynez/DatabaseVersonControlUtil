package com.xiaowenz.scv;

import java.util.Properties;

import com.xiaowenz.scv.core.SCIndex;

public interface IScriptLoader {
    public void init(Properties config) throws ScriptConfigException;
    public SCIndex load(Properties config) throws ScriptRuntimeException;
}
