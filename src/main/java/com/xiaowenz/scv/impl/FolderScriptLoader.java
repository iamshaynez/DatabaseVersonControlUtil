package com.xiaowenz.scv.impl;

import java.io.File;
import java.util.List;
import java.util.Properties;

import com.xiaowenz.scv.IScriptLoader;
import com.xiaowenz.scv.ScriptConfigException;
import com.xiaowenz.scv.ScriptRuntimeException;
import com.xiaowenz.scv.core.SCIndex;

public class FolderScriptLoader implements IScriptLoader {

    private static final List<String> ext = new ArrayList<String>(){["",""]};

    @Override
    public void init(Properties config) throws ScriptConfigException {
        String location = config.getProperty("location");
        if(location == null) {
            throw new ScriptConfigException("Missing location arg for FolderScriptLoader");
        }
        
        File file = new File(location);
        if(file.exists() && !file.isFile()){
            
        } else {
            throw new ScriptConfigException("Invalid folder location: " + location);
        }

    }


    @Override
    public SCIndex load(Properties config) throws ScriptRuntimeException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    public static void isDirectory(File file) {
		if(file.exists()){
			if (file.isFile()) {
			System.out.println("file is ==>>" + file.getAbsolutePath());
			}else{
				File[] list = file.listFiles();
				if (list.length == 0) {
					System.out.println(file.getAbsolutePath() + " is null");
				} else {
					for (int i = 0; i < list.length; i++) {
						isDirectory(list[i]);//递归调用
					}
				}
			}
		}else{
			System.out.println("文件不存在！");
		}
	}
}
