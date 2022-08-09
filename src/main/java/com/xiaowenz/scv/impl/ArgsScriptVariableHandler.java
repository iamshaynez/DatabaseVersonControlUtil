package com.xiaowenz.scv.impl;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.xiaowenz.scv.IScriptVariableHandler;
import com.xiaowenz.scv.ScriptRuntimeException;
import com.xiaowenz.scv.core.SCScript;

public class ArgsScriptVariableHandler implements IScriptVariableHandler {

    private Map<String, String> mapping = null;

    @Override
    public List<SCScript> handle(List<SCScript> scripts, Properties config) throws ScriptRuntimeException {
        
   
            String output = config.getProperty("output");
        
            if(output == null) {
                throw new ScriptRuntimeException("Missing location arg for FolderScriptLoader");
            }

            List<SCScript> newScripts = new ArrayList<SCScript>();

            // not the decent way to handle exception
            scripts.forEach(script -> {
                try {
                    Path filePath = Paths.get(script.getLocation() + File.separator + script.getFileName());
                    Stream<String> lines = Files.lines(filePath, Charset.forName("UTF-8")); 
                    List<String> replacedLine = lines.map(line -> replaceWords(line, mapping)).collect(Collectors.toList());
        
                    String outputFileFullName = output + File.separator + script.getFileName();
                    Files.write(Paths.get(outputFileFullName), replacedLine, Charset.forName("UTF-8"));
                    newScripts.add(SCScript.create(script.getFileName(), output));
                    lines.close();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
                
            });
        
        return newScripts;
    }

    protected static String replaceWords(String str, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
    
            if (str.contains(entry.getKey())) {
                str = str.replace(entry.getKey(), entry.getValue());
            }
        }
        return str;
    }

    protected Map<String, String> buildMapping(Properties config) {
        Map<String, String> mapping = new HashMap<String, String>();

        config.forEach((k, v) -> mapping.put(String.format(this.exp, k.toString()), v.toString())); 

        return mapping;
    }

    @Override
    public void init(Properties config) throws ScriptRuntimeException {
        String output = config.getProperty("output");
        
        if(output == null) {
            throw new ScriptRuntimeException("Missing output arg for Handler Output");
        } 

        File outputFolder = new File(output);
        if(!outputFolder.isDirectory()) {
            throw new ScriptRuntimeException("Output must be a folder: " + output);
        }

        this.cleanFolder(output);
        
        mapping = this.buildMapping(config);
    }

    /**
     * Clean all files under folder
     * 
     * @param path
     */
    protected void cleanFolder(String path) {
        File file = new File(path);

        if(file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for(File temp: files) {
                if (temp.isFile()) {
                    temp.delete();
                }
            }
        }
        
        
    }
}
