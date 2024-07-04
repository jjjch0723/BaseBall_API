package com.game.baseball.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FilePathsUtil {

    private final ExternalConfig externalConfig;

    @Autowired
    public FilePathsUtil(ExternalConfig externalConfig) {
        this.externalConfig = externalConfig;
    }

    // -------------경기 일정용 json파일-------------
    public String getMLBFilePath(String day) {
        return externalConfig.getFilePath() + "/" + day + "MLBgames.json";
    }

    public String getKBOFilePath(String day) {
		return externalConfig.getFilePath() + "/" + day + "KBOgames.json";
    }
    // -----------------------------------------
    
    // -------------경기 기록용 json파일-------------
    public String getMLBResultFilePath(String day) {
        return externalConfig.getFilePath() + "/" + day + "MLBresult.json";
    }

    public String getKBOResultFilePath(String day) {
        return externalConfig.getFilePath() + "/" + day + "KBOresult.json";
    }
    // -----------------------------------------
    
    // -------------경기 예상용 json파일-------------
    public String getGPTexecptFilePath() {
    	return externalConfig.getFilePath() + "/game_analysis.json";
    }
    // -----------------------------------------
    
    // --------------파이썬 파일 경로----------------
    public String getMlbSchedulePyPath() {
        return externalConfig.getMlbSchedulePyPath();
    }

    public String getKboSchedulePyPath() {
        return externalConfig.getKboSchedulePyPath();
    }

    public String getMlbResultPyPath() {
        return externalConfig.getMlbResultPyPath();
    }

    public String getKboResultPyPath() {
        return externalConfig.getKboResultPyPath();
    }
    
    public String getGptExecptPyPath() {
    	return externalConfig.getGptExepectPyPath();
    }
    // -----------------------------------------
}
