package com.game.baseball.api.runPy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class runPyfileImpl implements runPyfile {

    private static final Logger logger = LoggerFactory.getLogger(runPyfileImpl.class);

    @Override
    public void runMLBpy(String path) {
        pyRunner(path);
    }

    @Override
    public void runKBOpy(String path) {
        pyRunner(path);
    }

    @Override
    public void runMLBresultpy(String path) {
        pyRunner(path);
    }

    @Override
    public void runKBOresultpy(String path) {
        pyRunner(path);
    }

	@Override
	public void runGPTexepectpy(String path) {
		pyRunner(path);
	}
	
	@Override
	public void pyRunner(String path) {
	    try {
	        ProcessBuilder pb = new ProcessBuilder("/home/baseball/venv/bin/python3.12", path);
	        logger.info("Running Python script: {}", path);
	        
	        Process process = pb.start();

	        BufferedReader stdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        BufferedReader stdErrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

	        String line;
	        while ((line = stdOutReader.readLine()) != null) {
	            logger.info("Python stdout: {}", line);
	        }

	        while ((line = stdErrReader.readLine()) != null) {
	            logger.error("Python stderr: {}", line);
	        }

	        int exitCode = process.waitFor();
	        if (exitCode == 0) {
	            logger.info("Python script executed successfully with exit code: {}", exitCode);
	        } else {
	            logger.error("Python script execution failed with exit code: {}", exitCode);
	        }
	    } catch (Exception e) {
	        logger.error("Error running Python script: {}", path, e);
	    }
	}
}
