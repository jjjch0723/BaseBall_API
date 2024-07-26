package com.game.baseball.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.game.baseball.api.config.BatchConfig;
import com.game.baseball.api.jsonFileManager.getDay;
import com.game.baseball.api.jsonFileManager.readManager.readJSONImpl;
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
    @Qualifier("gptPyRunnerImpl")
    private PyRunner gptPyRunner; 
    
    @Autowired
    private BatchConfig batchConfig;

    @Autowired
    private getDay getDay;
    
    @Autowired
    private readJSONImpl readJSONImpl;
    
    @GetMapping("/version")
    public String getAppVersion() {
        logger.info("getAppVersion() called");
        return "Application version: " + appVersion;
    }

    @PostMapping("/schedule/process")
    public ResponseEntity<List<Map<String, Object>>> processSchedule() {
        logger.info("processSchedule() called");
        schedulePyRunner.pyRunner();
        
        String tdy = getDay.getTodaydate();
        String tmr = getDay.getTomorrowdate();
        
        List<Map<String, Object>> kboList = readJSONImpl.readKBOfile(tdy);
        List<Map<String, Object>> mlbList = readJSONImpl.readMLBfile(tmr);
        if ((kboList == null || kboList.isEmpty()) && (mlbList == null || mlbList.isEmpty())) {
            return ResponseEntity.noContent().build(); // 데이터가 없을 경우 204 No Content 반환
        }
        
        List<Map<String, Object>> fullSchedule = new ArrayList<>();
        
        if(kboList != null) {
        	fullSchedule.addAll(kboList);
        }
        if(mlbList != null) {
        	fullSchedule.addAll(mlbList);
        }
        logger.info("fullScheulde : " + fullSchedule.toString());
        System.out.println(fullSchedule);
        return ResponseEntity.ok(fullSchedule);
    }

    @PostMapping("/results/process")
    public ResponseEntity<List<Map<String, Object>>> processResults() {
        logger.info("processResults() called");
        resultPyRunner.pyRunner();
        
        String tdy = getDay.getTodaydate();
        String ysd = getDay.getYesterdaydate();
        
        List<Map<String, Object>> kboList = readJSONImpl.readKBOrsltfile(ysd);
        List<Map<String, Object>> mlbList = readJSONImpl.readMLBrsltfile(tdy);
        
        if ((kboList == null || kboList.isEmpty()) && (mlbList == null || mlbList.isEmpty())) {
            return ResponseEntity.noContent().build(); // 데이터가 없을 경우 204 No Content 반환
        }
        
        List<Map<String, Object>> fullSchedule = new ArrayList<>();
        
        if(kboList != null) {
        	fullSchedule.addAll(kboList);
        }
        if(mlbList != null) {
        	fullSchedule.addAll(mlbList);
        }
        logger.info("fullScheulde : " + fullSchedule.toString());
        System.out.println(fullSchedule);
        return ResponseEntity.ok(fullSchedule);
    }

    @PostMapping("/exepect/process")
    public String processExepect() {
    	logger.info("processExepect() called");
    	gptPyRunner.pyRunner();
    	return "Exepect processing started";
    }
    
    @PostMapping("/batch/run")
    public String runBatchJob() {
        logger.info("runBatchJob() called");
        batchConfig.perform();
        return "Batch job started";
    }
}
