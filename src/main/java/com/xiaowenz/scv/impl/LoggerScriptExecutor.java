package com.xiaowenz.scv.impl;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.xiaowenz.scv.IScriptExecutor;
import com.xiaowenz.scv.ScriptConfigException;
import com.xiaowenz.scv.ScriptRuntimeException;
import com.xiaowenz.scv.core.SCScript;

/**
 * Mock the script execution by printing into logger
 * 
 * args(E)
 * 
 * - class: this class
 */
public class LoggerScriptExecutor implements IScriptExecutor {

    private static Logger logger = Logger.getLogger(LoggerScriptExecutor.class.getName());

    public LoggerScriptExecutor() {

    } 
    
    @Override
    public void init(Properties config) throws ScriptConfigException {
        // nothing to init
    }

    @Override
    public void execute(List<SCScript> scripts, Properties config) throws ScriptRuntimeException {
        
        logger.info("Start Executing List of Scripts");
        for(SCScript script: scripts) {
            logger.info("Executing ... " + script.getFileName() + " at " + script.getLocation());
        }
        logger.info("Execution completed...");
    }
    
}
