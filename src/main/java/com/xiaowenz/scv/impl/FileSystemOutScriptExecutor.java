package com.xiaowenz.scv.impl;

import java.util.List;
import java.util.Properties;

import com.xiaowenz.scv.IScriptExecutor;
import com.xiaowenz.scv.ScriptConfigException;
import com.xiaowenz.scv.ScriptRuntimeException;
import com.xiaowenz.scv.core.SCScript;

public class FileSystemOutScriptExecutor implements IScriptExecutor {

    public FileSystemOutScriptExecutor() {

    }
    
    @Override
    public void init(Properties config) throws ScriptConfigException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void execute(List<SCScript> scripts, Properties config) throws ScriptRuntimeException {
        
        System.out.println("Start Executing List of Scripts");
        for(SCScript script: scripts) {
            System.out.println("Executing ... " + script.getFileName());
        }
        System.out.println("Execution completed...");
    }
    
}
