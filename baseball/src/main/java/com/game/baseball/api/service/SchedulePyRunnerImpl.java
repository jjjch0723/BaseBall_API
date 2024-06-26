  package com.game.baseball.api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game.baseball.api.config.Paths;
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

    @Override
    public void pyRunner() {
        String tdy = gd.getTodaydate();
        String tmr = gd.getTomorrowdate();

        String mlbPyPath = Paths.MLB_PY_PATH;
        String kboPyPath = Paths.KBO_PY_PATH;

        logger.info("Running MLB schedule script at path: {}", mlbPyPath);
        rpi.pyRunner(mlbPyPath);
        logger.info("MLB schedule script completed");

        logger.info("Running KBO schedule script at path: {}", kboPyPath);
        rpi.pyRunner(kboPyPath);
        logger.info("KBO schedule script completed");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error("Thread sleep interrupted", e);
        }

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
