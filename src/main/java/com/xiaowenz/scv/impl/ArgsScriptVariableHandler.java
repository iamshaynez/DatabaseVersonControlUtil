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

/**
 * Inject variables from cli args
 * 
 * if expKey still exists after mapping, error out.
 * 
 * Args(H)
 * 
 * - output: output folder
 * - any*(XXX=BBB) will be replaced
 * 
 */
public class ArgsScriptVariableHandler implements IScriptVariableHandler {

    protected Map<String, String> mapping = null;

    @Override
    public List<SCScript> handle(List<SCScript> scripts, Properties config) throws ScriptRuntimeException {
        
            String output = config.getProperty("output");
        
            if(output == null) {
                throw new ScriptRuntimeException("Missing location arg for FolderScriptLoader");
            }

            List<SCScript> newScripts = new ArrayList<SCScript>();

            // not the decent way to handle exception, throw a unchecked RuntimeException to avoid compile error
            
            scripts.forEach(script -> {
                Stream<String> lines = null;
                try {
                    Path filePath = Paths.get(script.getLocation() + File.separator + script.getFileName());
                    lines = Files.lines(filePath, Charset.forName("UTF-8")); 
                    List<String> replacedLine = lines.map(line -> replaceWords(line, mapping)).collect(Collectors.toList());
        
                    if(checkWords(replacedLine, expKey)) {
                        throw new Exception(String.format("Script still with %s", expKey)); 
                    }

                    String outputFileFullName = output + File.separator + script.getFileName();
                    Files.write(Paths.get(outputFileFullName), replacedLine, Charset.forName("UTF-8"));
                    newScripts.add(SCScript.create(script.getFileName(), output));
                    lines.close();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        lines.close();
                    } catch(Exception e) {
                        // eaten
                    }
                }
                
            });
        
        return newScripts;
    }

    /**
     * Replace all the variables from mapping
     * 
     * @param str
     * @param map
     * @return
     */
    protected static String replaceWords(String str, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (str.contains(entry.getKey())) {
                str = str.replace(entry.getKey(), entry.getValue());
            }
        }
        return str;
    }

    /**
     * Check if still special key word contains, if so, return true
     * 
     * @return
     */
    protected static boolean checkWords(List<String> strings, String key) {
        for (String str : strings) {
            if(str.contains(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * build mapping from properties config
     * 
     * @param config
     * @return
     */
    protected Map<String, String> buildMapping(Properties config) {
        Map<String, String> mapping = new HashMap<String, String>();

        config.forEach((k, v) -> mapping.put(String.format(this.exp, k.toString()), v.toString())); 

        return mapping;
    }

    @Override
    public void init(Properties config) throws ScriptRuntimeException {
        // output temp folder is mandatory
        String output = config.getProperty("output");
        
        if(output == null) {
            throw new ScriptRuntimeException("Missing output arg for Handler Output");
        } 

        File outputFolder = new File(output);
        if(!outputFolder.isDirectory()) {
            throw new ScriptRuntimeException("Output must be a folder: " + output);
        }

        // clean all files under folder
        this.cleanFolder(output);

        // build variable mapping from cli config
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
