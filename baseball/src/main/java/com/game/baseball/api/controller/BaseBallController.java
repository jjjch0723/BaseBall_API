package com.game.baseball.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game.baseball.api.service.PyRunner;

@RestController
@RequestMapping("/api/v1")
public class BaseBallController {

    @Value("${spring.application.version}")
    private String appVersion;

    @Autowired
    @Qualifier("schedulePyRunnerImpl")
    private PyRunner schedulePyRunner;

    @Autowired
    @Qualifier("resultPyRunnerImpl")
    private PyRunner resultPyRunner;

    @GetMapping("/version")
    public String getAppVersion() {
        return "Application version: " + appVersion;
    }

    @PostMapping("/schedule/process")
    public String processSchedule() {
        schedulePyRunner.pyRunner();
        return "Schedule processing started";
    }

    @PostMapping("/results/process")
    public String processResults() {
        resultPyRunner.pyRunner();
        return "Results processing started";
    }
}
