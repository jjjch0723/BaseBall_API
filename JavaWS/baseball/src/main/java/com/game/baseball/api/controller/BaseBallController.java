package com.game.baseball.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game.baseball.api.config.BatchConfig;
import com.game.baseball.api.service.PyRunner;

@RestController
@RequestMapping("/api/v1")
public class BaseBallController {

    private static final Logger logger = LoggerFactory.getLogger(BaseBallController.class);

    @Value("${spring.application.version}")
    private String appVersion;

    @Autowired
    @Qualifier("schedulePyRunnerImpl")
    private PyRunner schedulePyRunner;

    @Autowired
    @Qualifier("resultPyRunnerImpl")
    private PyRunner resultPyRunner;

    @Autowired
    private BatchConfig batchConfig;

    @GetMapping("/version")
    public String getAppVersion() {
        logger.info("getAppVersion() called");
        return "Application version: " + appVersion;
    }

    @PostMapping("/schedule/process")
    public String processSchedule() {
        logger.info("processSchedule() called");
        schedulePyRunner.pyRunner();
        return "Schedule processing started";
    }

    @PostMapping("/results/process")
    public String processResults() {
        logger.info("processResults() called");
        resultPyRunner.pyRunner();
        return "Results processing started";
    }

    @PostMapping("/batch/run")
    public String runBatchJob() {
        logger.info("runBatchJob() called");
        batchConfig.perform();
        return "Batch job started";
    }
}
