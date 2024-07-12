package com.game.baseball.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalConfigImpl implements ExternalConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPass;

    @Value("${schedule.cron}")
    private String scheduleCron;

    @Value("${file.path}")
    private String filePath;

    @Value("${py.paths.mlb.schedule}")
    private String mlbSchedulePyPath;

    @Value("${py.paths.kbo.schedule}")
    private String kboSchedulePyPath;

    @Value("${py.paths.mlb.result}")
    private String mlbResultPyPath;

    @Value("${py.paths.kbo.result}")
    private String kboResultPyPath;
    
    @Value("${py.paths.gpt.exepect}")
    private String gptExepectPyPath;

    @Override
    public String getDbUrl() {
        return dbUrl;
    }

    @Override
    public String getDbUserName() {
        return dbUserName;
    }
    
    @Override
    public String getDbUserPass() {
        return dbUserPass;
    }
    
    @Override
    public String getScheduleCron() {
        return scheduleCron;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }
    
    @Override
    public String getMlbSchedulePyPath() {
        return mlbSchedulePyPath;
    }

    @Override
    public String getKboSchedulePyPath() {
        return kboSchedulePyPath;
    }

    @Override
    public String getMlbResultPyPath() {
        return mlbResultPyPath;
    }

    @Override
    public String getKboResultPyPath() {
        return kboResultPyPath;
    }

    @Override
    public String getGptExepectPyPath() {
        return gptExepectPyPath;
    }
}
