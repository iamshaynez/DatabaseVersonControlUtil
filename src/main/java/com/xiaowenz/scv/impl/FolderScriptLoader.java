package com.xiaowenz.scv.impl;

import java.io.File;
import java.util.Properties;

import com.xiaowenz.scv.IScriptLoader;
import com.xiaowenz.scv.ScriptConfigException;
import com.xiaowenz.scv.ScriptRuntimeException;
import com.xiaowenz.scv.core.InvalidVersionException;
import com.xiaowenz.scv.core.SCIndex;
import com.xiaowenz.scv.core.SCScript;


/**
 * Load Script from local folder
 * 
 * args(L)
 * 
 * - location: local root folder for scripts
 * - class: this class
 */

public class FolderScriptLoader implements IScriptLoader {

    //private static final List<String> ext = null;

    private File folder = null;

    public FolderScriptLoader(){
        
    }

    @Override
    public void init(Properties config) throws ScriptConfigException {
        String location = config.getProperty("location");
        if(location == null) {
            throw new ScriptConfigException("Missing location arg for FolderScriptLoader");
        }

        File file = new File(location);
        if(file.exists() && !file.isFile()){
            this.folder = file;
        } else {
            throw new ScriptConfigException("Invalid folder location: " + location);
        }
    }


    @Override
    public SCIndex load(Properties config) throws ScriptRuntimeException {
        SCIndex index = SCIndex.create();

        try {
            addFiles(folder, index);
        } catch (InvalidVersionException e) {
            throw new ScriptRuntimeException(e.getMessage());
        }
        
        return index;
    }
    
    
    public static void addFiles(File file, SCIndex index) throws InvalidVersionException {
		if(file.exists()){
			if (file.isFile()) {
                index.add(SCScript.create(file.getName(), file.getAbsoluteFile().getParent()));
			}else{
				File[] list = file.listFiles();
				if (list.length == 0) {
					// nothing
				} else {
					for (int i = 0; i < list.length; i++) {
						addFiles(list[i], index);
					}
				}
			}
		}else{
			// nothing
		}
	}
}
