package com.game.baseball.api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.game.baseball.api.config.FilePathsUtil;
import com.game.baseball.api.dao.BaseBallDaoImpl;
import com.game.baseball.api.jsonFileManager.getDay;
import com.game.baseball.api.jsonFileManager.readManager.readJSONImpl;
import com.game.baseball.api.runPy.runPyfileImpl;

@Service
public class ResultPyRunnerImpl implements PyRunner {

    private static final Logger logger = LoggerFactory.getLogger(ResultPyRunnerImpl.class);

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
    @Transactional
    public void pyRunner() {
        String tdy = gd.getTodaydate();
        String ysd = gd.getYesterdaydate();

        String mlbPyPath = filePathsUtil.getMlbResultPyPath();
        String kboPyPath = filePathsUtil.getKboResultPyPath();

        logger.info("Running MLB result script at path: {}", mlbPyPath);
        rpi.pyRunner(mlbPyPath);
        logger.info("MLB result script completed");

        logger.info("Running KBO result script at path: {}", kboPyPath);
        rpi.pyRunner(kboPyPath);
        logger.info("KBO result script completed");

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            logger.error("Thread sleep interrupted", e);
        }

        List<Map<String, Object>> mlbList = rji.readMLBrsltfile(tdy);
        List<Map<String, Object>> kboList = rji.readKBOrsltfile(ysd);

        logger.info("Dropping and recreating MLB result table");
        bbd.dropMLBResulttbl();
        bbd.createTemporaryMLBresulttbl();

        logger.info("Dropping and recreating KBO result table");
        bbd.dropKBOResulttbl();
        bbd.createTemporaryKBOresulttbl();

        if (mlbList != null && !mlbList.isEmpty()) {
            logger.info("Inserting MLB result data");
            bbd.insertResultMLB(mlbList);
        } else {
            logger.info("No MLB result data to insert");
        }

        if (kboList != null && !kboList.isEmpty()) {
            logger.info("Inserting KBO result data");
            bbd.insertResultKBO(kboList);
        } else {
            logger.info("No KBO result data to insert");
        }

        logger.info("Moving MLB result data to main table");
        bbd.moveTempMLBResultToMainTable();
        logger.info("MLB result data moved to main table");

        logger.info("Moving KBO result data to main table");
        bbd.moveTempKBOResultToMainTable();
        logger.info("KBO result data moved to main table");
    }
}
