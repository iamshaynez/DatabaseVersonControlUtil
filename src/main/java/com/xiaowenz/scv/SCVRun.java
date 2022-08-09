package com.xiaowenz.scv;

import com.xiaowenz.scv.core.InvalidVersionException;
import com.xiaowenz.scv.core.SCIndex;
import com.xiaowenz.scv.core.SCScript;
import com.xiaowenz.scv.core.SCVersion;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SCVRun {

    private static Logger logger = Logger.getLogger(SCVRun.class.getName());

    public static void main(String[] args) {

        // Load all parameters
        CommandLineParser parser = new DefaultParser();
        try {
            // Load all parameters
            // parse the command line arguments
            logger.info("Start Runtime, reading args...");
            CommandLine line = parser.parse(buildOptions(), args);

            String start = line.getOptionValue("start");
            String end = line.getOptionValue("end");
            String action = line.getOptionValue("action");

            // init Script Load instance
            logger.info("Start Initializing Script Loader...");
            Properties loaderConfig = line.getOptionProperties("L");

            if(loaderConfig.getProperty("class") == null) {
                throw new ScriptConfigException("Class for Loader is not assigned. Use -Lclass= to assign class name.");
            }

            Class<IScriptLoader> loadClass = (Class<IScriptLoader>) Class.forName(loaderConfig.getProperty("class"));
            IScriptLoader loader = loadClass.getDeclaredConstructor().newInstance();

            loader.init(loaderConfig);
            SCIndex index = loader.load(loaderConfig);
            List<SCScript> scripts = null;

            if("R".equals(action)) {
                scripts = index.release(SCVersion.create(start), SCVersion.create(end));
            } else if("U".equals(action)) {
                scripts = index.undo(SCVersion.create(start), SCVersion.create(end));
            } else if("P".equals(action)) {
                scripts = index.patch(SCVersion.create(start), SCVersion.create(end));
            } else {
                // rest goes to R
                scripts = index.release(SCVersion.create(start), SCVersion.create(end));
            }


            // init Script Variable Injection Handler instance
            Properties handleConfig = line.getOptionProperties("V");
            if(handleConfig.getProperty("class") == null) {
                logger.info("Variable Handler Skipped...");
            } else {
                logger.info("Start Initializing Script Variable Handler...");
                Class<IScriptVariableHandler> handleClass = (Class<IScriptVariableHandler>) Class.forName(handleConfig.getProperty("class"));
                IScriptVariableHandler handler = handleClass.getDeclaredConstructor().newInstance();

                handler.init(handleConfig);

                List<SCScript> newScripts = handler.handle(scripts, handleConfig);

                scripts = newScripts;
            }

            // init Script Execution instance
            logger.info("Start Initializing Script Executor...");
            Properties execConfig = line.getOptionProperties("E");

            if(execConfig.getProperty("class") == null) {
                throw new ScriptConfigException("Class for Executor is not assigned. Use -Eclass= to assign class name.");
            }

            Class<IScriptExecutor> execClass = (Class<IScriptExecutor>) Class.forName(execConfig.getProperty("class"));
            IScriptExecutor executor = execClass.getDeclaredConstructor().newInstance();

            executor.init(execConfig);

            // Run time
            logger.info("Start Executing Scripts from Loader...");
            if(scripts == null) {
                logger.log(Level.SEVERE, "No scripts will be executed, you might verify the version number or action code.");
            } else {
                executor.execute(scripts, execConfig);
            }
            
        } catch (ParseException exp) {
            // oops, something went wrong
            logger.log(Level.SEVERE, "Parsing failed.  Reason: " + exp.getMessage());
        } catch (ScriptConfigException configException) {
            logger.log(Level.SEVERE, "Config failed.  Reason: " + configException.getMessage());
        } catch (InvalidVersionException versionException) {
            logger.log(Level.SEVERE, "Version invalid failed.  Reason: " + versionException.getMessage());
        } catch (ScriptRuntimeException runtimeException) {
            logger.log(Level.SEVERE, "Runtime failed.  Reason: " + runtimeException.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Critical runtime failed.  Reason: " + e.getMessage());
        }


        // Happy ending
        logger.log(Level.INFO, "Completed successfully...");
    }

    private static Options buildOptions() {
        Options options = new Options();

        // Properties for Loader Module
        Option loader = Option.builder("L")
                                .hasArgs()
                                .valueSeparator('=').required()
                                .build();

        // Properties for Executor Module
        Option executor = Option.builder("E").hasArgs().valueSeparator('=').required().build();

        // Properties for Handler Module
        Option handler = Option.builder("V").hasArgs().valueSeparator('=').build();

        // Version start from
        Option start  = Option.builder("start").argName("version").required().hasArg().desc("version number start from").build();
        // Version end at
        Option end  = Option.builder("end").argName("version").hasArg().required().desc("version number end at").build();

        // Action Type
        Option action  = Option.builder("action").argName("R|U|P").hasArg().required().desc("action type").build();
        
        options.addOption(loader);
        options.addOption(executor);
        options.addOption(handler);
        options.addOption(start);
        options.addOption(end);
        options.addOption(action);

        return options;
    }
}
