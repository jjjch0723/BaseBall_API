package com.game.baseball.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.game.baseball.api.config.FilePathsUtil;
import com.game.baseball.api.dao.BaseBallDaoImpl;
import com.game.baseball.api.jsonFileManager.readManager.readJSONImpl;
import com.game.baseball.api.runPy.runPyfileImpl;

@Service
public class GptPyRunnerImpl implements PyRunner {

    private static final Logger logger = LoggerFactory.getLogger(GptPyRunnerImpl.class);

    @Autowired
    private BaseBallDaoImpl bbd;

    @Autowired
    private readJSONImpl rji;

    @Autowired
    private runPyfileImpl rpi;

    @Autowired
    private FilePathsUtil filePathsUtil;

    @Override
    @Transactional
    public void pyRunner() {
        try {
            executePythonScript();
            processGptResults();
        } catch (Exception e) {
            logger.error("Error occurred during GPT batch processing", e);
        }
    }

    private void executePythonScript() {
        String gptPyPath = filePathsUtil.getGptExecptPyPath();

        try {
            logger.info("Running GPT exepect script using runPyfileImpl at path: {}", gptPyPath);
            rpi.pyRunner(gptPyPath);  
            logger.info("GPT exepect script executed successfully via runPyfileImpl.");
        } catch (Exception e) {
            logger.error("Failed to run GPT exepect script using runPyfileImpl", e);
        }

        // 추가적으로 직접 시스템 명령을 통해 실행하는 부분 (실행 결과 로그에 남기기)
        try {
            logger.info("Running GPT exepect script at path: {}", gptPyPath);

            // 파이썬 스크립트 실행
            String command = "python3 " + gptPyPath; // 실제 파이썬 스크립트 경로로 수정
            Process process = Runtime.getRuntime().exec(command);

            // 표준 출력 읽기
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 파이썬에서 출력된 내용을 로그로 기록
                    logger.info("Python Script Output: {}", line);
                }
            }

            // 프로세스 종료 대기 및 종료 코드 확인
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Python script executed successfully.");
            } else {
                logger.error("Python script exited with error code: {}", exitCode);
            }

        } catch (Exception e) {
            logger.error("Failed to run GPT exepect script", e);
        }
    }

    @Transactional
    private void processGptResults() {
        List<Map<String, Object>> elist = rji.readGPTExecpet();

        logger.info("Dropping and recreating GPT exepect table");
        bbd.dropGPTAnlytbl();
        bbd.createTemporaryGPTAnly();

        if (elist != null && !elist.isEmpty()) {
            logger.info("Inserting GPT Exepect result data");
            bbd.insertTdyExepect(elist);
        } else {
            logger.info("No GPT Exepect data to insert");
        }

        logger.info("Moving GPT Exepect data to main table");
        bbd.moveTempGPTexepectToMainTable();
    }
}
