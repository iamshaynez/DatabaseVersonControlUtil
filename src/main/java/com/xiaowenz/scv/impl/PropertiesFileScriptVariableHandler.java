package com.xiaowenz.scv.impl;

import java.io.File;

import java.util.Properties;

import com.xiaowenz.scv.ScriptRuntimeException;

import com.xiaowenz.scv.util.SCVUtil;

/**
 * Load variable mapping from local properties file
 * 
 * args(V):
 * 
 * - output: output folder
 * - mapping: properties file
 * - class: this class
 */
public class PropertiesFileScriptVariableHandler extends ArgsScriptVariableHandler {

    

    @Override
    public void init(Properties config) throws ScriptRuntimeException {
        // init output folder
        String output = config.getProperty("output");
        
        if(output == null) {
            throw new ScriptRuntimeException("Missing output arg for Handler Output");
        } 

        File outputFolder = new File(output);
        if(!outputFolder.isDirectory()) {
            throw new ScriptRuntimeException("Output must be a folder: " + output);
        }

        this.cleanFolder(output);
        
        // init mapping from properties file
        String mappingFile = config.getProperty("mapping");
        if(mappingFile == null) {
            throw new ScriptRuntimeException("Missing output arg for Handler Config File");
        } 
        mapping = this.buildMapping(SCVUtil.readPropertiesFile(mappingFile));
    }
    
}
