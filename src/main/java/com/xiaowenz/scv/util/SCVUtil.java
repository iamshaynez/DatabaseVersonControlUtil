package com.xiaowenz.scv.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.xiaowenz.scv.ScriptRuntimeException;

public class SCVUtil {
    
    public static Properties readPropertiesFile(String fileName) throws ScriptRuntimeException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(fileName);
            // create Properties class object
            prop = new Properties();
            // load properties file into it
            prop.load(fis);
 
        } catch (Exception e) {
            throw new ScriptRuntimeException(e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                // eaten
            }
        }
 
        return prop;
    }
    
}
