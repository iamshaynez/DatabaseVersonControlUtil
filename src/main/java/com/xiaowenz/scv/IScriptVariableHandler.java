package com.xiaowenz.scv;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.xiaowenz.scv.core.SCScript;

public interface IScriptVariableHandler {

    public final String exp = "${%s}";

    public void init(Properties config) throws ScriptRuntimeException;

    public List<SCScript> handle(List<SCScript> scripts, Properties config) throws ScriptRuntimeException;
}
