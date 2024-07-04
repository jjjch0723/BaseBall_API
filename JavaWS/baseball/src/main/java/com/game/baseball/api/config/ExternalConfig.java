package com.game.baseball.api.config;

public interface ExternalConfig {
    String getDbUrl();
    String getDbUserName();
    String getDbUserPass();
    String getScheduleCron();
    String getFilePath();
    String getMlbSchedulePyPath();
    String getKboSchedulePyPath();
    String getMlbResultPyPath();
    String getKboResultPyPath();
    String getGptExepectPyPath();
}