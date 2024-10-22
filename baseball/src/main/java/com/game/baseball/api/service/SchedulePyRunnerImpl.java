package com.game.baseball.api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.baseball.api.config.FilePathsUtil;
import com.game.baseball.api.dao.BaseBallDaoImpl;
import com.game.baseball.api.jsonFileManager.getDay;
import com.game.baseball.api.jsonFileManager.readManager.readJSONImpl;
import com.game.baseball.api.runPy.runPyfileImpl;

@Service
public class SchedulePyRunnerImpl implements PyRunner {

    private static final Logger logger = LoggerFactory.getLogger(SchedulePyRunnerImpl.class);

    @Autowired
    private BaseBallDaoImpl bbd;

    @Autowired
    private getDay gd;

    @Autowired
    private readJSONImpl rji;

    @Autowired
    private runPyfileImpl rpi;

    @Autowired
    private FilePathsUtil filePathsUtil;

    @Override
    public void pyRunner() {
        try {
            executePythonScripts();
            processSchedules();
        } catch (Exception e) {
            logger.error("Error occurred during schedule batch processing", e);
        }
    }

    private void executePythonScripts() {
        String mlbPyPath = filePathsUtil.getMlbSchedulePyPath();
        String kboPyPath = filePathsUtil.getKboSchedulePyPath();

        try {
            logger.info("Running MLB schedule script at path: {}", mlbPyPath);
            rpi.pyRunner(mlbPyPath);
            logger.info("MLB schedule script completed");
        } catch (Exception e) {
            logger.error("Failed to run MLB schedule script", e);
        }

        try {
            logger.info("Running KBO schedule script at path: {}", kboPyPath);
            rpi.pyRunner(kboPyPath);
            logger.info("KBO schedule script completed");
        } catch (Exception e) {
            logger.error("Failed to run KBO schedule script", e);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread sleep interrupted", e);
        }
    }

    private void processSchedules() {
        String tdy = gd.getTodaydate();
        String tmr = gd.getTomorrowdate();

        List<Map<String, Object>> mlbList = rji.readMLBfile(tmr);
        List<Map<String, Object>> kboList = rji.readKBOfile(tdy);

        logger.info("Dropping and recreating MLB schedule table");
        bbd.dropMLBtbl();
        bbd.createTemporaryMLBtbl();

        logger.info("Dropping and recreating KBO schedule table");
        bbd.dropKBOtbl();
        bbd.createTemporaryKBOtbl();

        if (mlbList != null && !mlbList.isEmpty()) {
            logger.info("Inserting MLB schedule data");
            bbd.insertTdyMLB(mlbList);
        } else {
            logger.info("No MLB schedule data to insert");
        }

        if (kboList != null && !kboList.isEmpty()) {
            logger.info("Inserting KBO schedule data");
            bbd.insertTdyKBO(kboList);
        } else {
            logger.info("No KBO schedule data to insert");
        }
    }
}
