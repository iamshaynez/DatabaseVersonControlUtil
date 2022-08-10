package com.xiaowenz.scv;


import java.util.List;
import java.util.Properties;

import com.xiaowenz.scv.core.SCScript;

/**
 * Cli arg = H
 * 
 * If no option H, Variable Handle will be skipped.
 * 
 * The ${XXX} in the script will be replaced.
 */
public interface IScriptVariableHandler {

    public final String exp = "${%s}";
    public final String expKey = "${";

    public void init(Properties config) throws ScriptRuntimeException;

    public List<SCScript> handle(List<SCScript> scripts, Properties config) throws ScriptRuntimeException;
}
